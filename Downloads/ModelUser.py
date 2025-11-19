from .entities.User import User
from werkzeug.security import generate_password_hash

class ModelUser:

    @classmethod
    def login(cls, db, user):
        try:
            cursor = db.connection.cursor()
            sql = """
                SELECT id_usuario, nombre_completo, email, password_hash, rol, activo
                FROM Usuario
                WHERE email = %s
            """
            cursor.execute(sql, (user.email,))
            row = cursor.fetchone()
            if row:
                user_data = User(*row[:5])  # id_usuario, nombre_completo, email, password_hash, rol
                if User.check_password(row[3], user.password) and row[5] == 1:
                    return user_data
            return None
        except Exception as ex:
            print("ERROR ModelUser.login:", ex)
            return None

    @classmethod
    def get_by_id(cls, db, id):
        try:
            cursor = db.connection.cursor()
            sql = """
                SELECT id_usuario, nombre_completo, email, password_hash, rol, activo
                FROM Usuario
                WHERE id_usuario = %s
            """
            cursor.execute(sql, (id,))
            row = cursor.fetchone()
            if row:
                return User(*row[:5])
            return None
        except Exception as ex:
            print("ERROR ModelUser.get_by_id:", ex)
            return None

    @classmethod
    def get_all_users(cls, db):
        try:
            cursor = db.connection.cursor()
            sql = """
                SELECT 
                    u.id_usuario,
                    u.nombre_completo,
                    u.email,
                    u.rol,
                    u.activo
                FROM Usuario u
                ORDER BY u.nombre_completo;
            """
            cursor.execute(sql)
            rows = cursor.fetchall()
            users = []
            for row in rows:
                user = {
                    'id_usuario': row[0],
                    'nombre_completo': row[1],
                    'email': row[2],
                    'rol': row[3],
                    'activo': row[4],
                }
                users.append(user)
            return users
        except Exception as ex:
            print("ERROR ModelUser.get_all_users:", ex)
            return []

    @classmethod
    def create_user(cls, db, nombre_completo, email, password, rol='Empleado',
                    telefono=None, chofer_data=None):
        """
        Crea:
          1) Empleado
          2) (opcional) Chofer si rol == 'Chofer'
          3) Usuario (login), enlazado con id_empleado
        """
        try:
            cursor = db.connection.cursor()

            # Mapear rol lógico del sistema al rol de la tabla Empleado
            map_rol_empleado = {
                'Empleado': 'Ventanilla',
                'Admin': 'Admin',
                'Chofer': 'Chofer',
                'Mecanico': 'Mecanico',
                'Cliente': 'Cliente'
            }
            rol_empleado = map_rol_empleado.get(rol, 'Ventanilla')

            if rol == 'Cliente':
                cursor.execute("""INSERT INTO Cliente(nombre, correo, telefono, tipo) VALUES(%s, %s, %s, %s)""",
                                    (nombre_completo, email, telefono, 'Particular'))
            else:
                # 1) Insertar en EMPLEADO
                sql_emp = """
                    INSERT INTO Empleado (nombre, correo, telefono, rol, activo)
                    VALUES (%s, %s, %s, %s, 1)
                """

                cursor.execute(sql_emp, (nombre_completo, email, telefono, rol_empleado))

                # 2) Si es CHOFER, insertar en CHOFER
                if rol == 'Chofer':
                    if chofer_data is None or not chofer_data.get('licencia'):
                        raise ValueError("La licencia es obligatoria para registrar un chofer.")

                    sql_ch = """
                        INSERT INTO Chofer (
                            nombre, telefono, correo,
                            rfc, curp, nss,
                            direccion, fecha_ingreso,
                            activo,
                            licencia, licencia_tipo, licencia_expira,
                            anios_experiencia, notas
                        )
                        VALUES (
                            %s, %s, %s,
                            %s, %s, %s,
                            %s, %s,
                            1,
                            %s, %s, %s,
                            %s, %s
                        )
                    """
                    cursor.execute(sql_ch, (
                        nombre_completo,
                        telefono,
                        email,
                        chofer_data.get('rfc'),
                        chofer_data.get('curp'),
                        chofer_data.get('nss'),
                        chofer_data.get('direccion'),
                        chofer_data.get('fecha_ingreso'),
                        chofer_data.get('licencia'),
                        chofer_data.get('licencia_tipo'),
                        chofer_data.get('licencia_expira'),
                        chofer_data.get('anios_experiencia', 0),
                        chofer_data.get('notas')
                    ))

            # 3) Insertar en USUARIO (para login), enlazando id_empleado
            password_hash = generate_password_hash(password)
            sql_usr = """
                INSERT INTO Usuario (nombre_completo, email, password_hash, rol, activo)
                VALUES (%s, %s, %s, %s, 1)
            """
            cursor.execute(sql_usr, (nombre_completo, email, password_hash, rol))

            db.connection.commit()
            return True

        except Exception as ex:
            print("ERROR ModelUser.create_user:", ex)
            db.connection.rollback()
            return False

    @classmethod
    def update_user(cls, db, id_usuario, nombre_completo, email, rol, activo):
        """Actualizar información de un usuario"""
        try:
            
            cursor = db.connection.cursor()
            sql = """
                UPDATE Usuario
                SET nombre_completo = %s,
                    email = %s,
                    rol = %s,
                    activo = %s
                WHERE id_usuario = %s
            """
            cursor.execute(sql, (nombre_completo, email, rol, activo, id_usuario))
            db.connection.commit()
            return True
        except Exception as ex:
            print("ERROR ModelUser.update_user:", ex)
            db.connection.rollback()
            return False

    @classmethod
    def toggle_user_status(cls, db, id_usuario):
        """Activar/Desactivar un usuario"""
        try:
            cursor = db.connection.cursor()
            sql = "UPDATE Usuario SET activo = NOT activo WHERE id_usuario = %s"
            cursor.execute(sql, (id_usuario,))
            db.connection.commit()
            return True
        except Exception as ex:
            print("ERROR ModelUser.toggle_user_status:", ex)
            db.connection.rollback()
            return False

    @classmethod
    def change_password(cls, db, id_usuario, new_password):
        """Cambiar la contraseña de un usuario"""
        try:
            cursor = db.connection.cursor()
            password_hash = generate_password_hash(new_password)
            sql = "UPDATE Usuario SET password_hash = %s WHERE id_usuario = %s"
            cursor.execute(sql, (password_hash, id_usuario))
            db.connection.commit()
            return True
        except Exception as ex:
            print("ERROR ModelUser.change_password:", ex)
            db.connection.rollback()
            return False

    @classmethod
    def update_user_full(cls, db, id_usuario,
                         nombre_completo, email, rol, activo,
                         telefono_empleado=None,
                         chofer_data=None):
        """
        Actualiza:
          - Usuario
          - Empleado vinculado
          - (Opcional) Chofer, si el rol es 'Chofer'

        chofer_data es un diccionario con claves:
          rfc, curp, nss, direccion, fecha_ingreso,
          licencia, licencia_tipo, licencia_expira,
          anios_experiencia, notas
        """
        try:
            conn = db.connection
            cursor = conn.cursor()

            cursor.execute("""
                SELECT email, rol
                FROM Usuario
                WHERE id_usuario = %s
            """, (id_usuario,))
            row = cursor.fetchone()
            if not row:
                raise ValueError("Usuario no encontrado")

            email_anterior = row[0] 
            rol_anterior = row[1] 

            # 3) Actualizar tabla Usuario
            cursor.execute("""
                UPDATE Usuario
                SET nombre_completo = %s,
                    email           = %s,
                    rol             = %s,
                    activo          = %s
                WHERE id_usuario   = %s
            """, (nombre_completo, email, rol, activo, id_usuario))

            if rol == 'Cliente':

                cursor.execute("""SELECT * from Cliente
                                    WHERE correo = %s""",
                                    (email_anterior,))
                
                fila = cursor.fetchone()

                if fila:
                    cursor.execute("""UPDATE Cliente
                            SET nombre = %s,
                            correo = %s,
                            telefono = %s, 
                            tipo = %s
                            WHERE correo = %s
                            """, (nombre_completo, email, telefono_empleado, 'Particular', email_anterior))
                else:
                    cursor.execute("""INSERT INTO Cliente (nombre, correo, telefono, tipo)
                                    VALUES (%s, %s, %s, %s)
                            """, (nombre_completo, email, telefono_empleado, 'Particular'))
                
                if rol_anterior == 'Ventanilla':
                    cursor.execute("""DELETE FROM Empleado WHERE correo = %s""",
                                        (email_anterior,))
                elif rol_anterior == 'Chofer':
                    cursor.execute("""DELETE FROM Chofer WHERE correo = %s""",
                                        (email_anterior,))
            else:

                if rol_anterior == 'Cliente':
                    cursor.execute("""DELETE FROM Cliente WHERE correo = %s""",
                                        (email_anterior,))
                
                # 4) Actualizar tabla Empleado (nombre, correo, telefono, rol, activo)
                map_rol_empleado = {
                    'Empleado': 'Ventanilla',
                    'Admin': 'Admin',
                    'Chofer': 'Chofer',
                    'Mecanico': 'Mecanico',
                }
                rol_empleado = map_rol_empleado.get(rol, 'Ventanilla')

                cursor.execute("""
                    UPDATE Empleado
                    SET nombre  = %s,
                        correo  = %s,
                        telefono= %s,
                        rol     = %s,
                        activo  = %s
                    WHERE correo = %s
                """, (nombre_completo, email, telefono_empleado, rol_empleado, activo, email_anterior))

                # 5) Manejo de tabla Chofer según el rol
                if rol == 'Chofer':
                    # si no hay chofer_data, usamos dict vacío
                    if chofer_data is None:
                        chofer_data = {}

                    # verificar si ya existe registro en Chofer para ese empleado
                    cursor.execute("""
                        SELECT id_chofer
                        FROM Chofer
                        WHERE correo = %s
                    """, (email_anterior,))
                    row_ch = cursor.fetchone()

                    if row_ch:
                        # UPDATE existente
                        cursor.execute("""
                            UPDATE Chofer
                            SET nombre            = %s,
                                telefono          = %s,
                                correo            = %s,
                                rfc               = %s,
                                curp              = %s,
                                nss               = %s,
                                direccion         = %s,
                                fecha_ingreso     = %s,
                                licencia          = %s,
                                licencia_tipo     = %s,
                                licencia_expira   = %s,
                                anios_experiencia = %s,
                                notas             = %s,
                                activo            = %s
                            WHERE correo = %s
                        """, (
                            nombre_completo,
                            telefono_empleado,
                            email,
                            chofer_data.get('rfc'),
                            chofer_data.get('curp'),
                            chofer_data.get('nss'),
                            chofer_data.get('direccion'),
                            chofer_data.get('fecha_ingreso'),
                            chofer_data.get('licencia'),
                            chofer_data.get('licencia_tipo'),
                            chofer_data.get('licencia_expira'),
                            chofer_data.get('anios_experiencia', 0),
                            chofer_data.get('notas'),
                            1 if activo else 0,
                            email_anterior
                        ))
                    else:
                        # INSERT nuevo chofer
                        cursor.execute("""
                            INSERT INTO Chofer (
                                nombre, telefono, correo,
                                rfc, curp, nss,
                                direccion, fecha_ingreso,
                                activo,
                                licencia, licencia_tipo, licencia_expira,
                                anios_experiencia, notas
                            )
                            VALUES (
                                %s, %s, %s,
                                %s, %s, %s,
                                %s, %s,
                                %s,
                                %s, %s, %s,
                                %s, %s
                            )
                        """, (
                            nombre_completo,
                            telefono_empleado,
                            email,
                            chofer_data.get('rfc'),
                            chofer_data.get('curp'),
                            chofer_data.get('nss'),
                            chofer_data.get('direccion'),
                            chofer_data.get('fecha_ingreso'),
                            1 if activo else 0,
                            chofer_data.get('licencia'),
                            chofer_data.get('licencia_tipo'),
                            chofer_data.get('licencia_expira'),
                            chofer_data.get('anios_experiencia', 0),
                            chofer_data.get('notas')
                        ))

                else:
                    # Si ya no es chofer, opcionalmente lo marcamos inactivo en Chofer
                    cursor.execute("""
                        UPDATE Chofer
                        SET activo = 0
                        WHERE correo = %s
                    """, (email_anterior,))

            conn.commit()
            return True

        except Exception as ex:
            print("ERROR ModelUser.update_user_full:", ex)
            try:
                db.connection.rollback()
            except:
                pass
            return False

