# Tablero Kanban - Frontend y Backend

Este repositorio contiene tanto el **frontend** (tablero Kanban en React + Vite) como el **backend** (API REST en Spring Boot) de una aplicación Kanban de práctica para VIEWNEXT.

## Frontend - Tablero Kanban (React + Vite)

Aplicación de tablero **Kanban** práctico para la formación, desarrollada usando [React](https://react.dev/) + [Vite](https://vite.dev/) + [TypeScript](https://www.typescriptlang.org/) y estilos con [Tailwind CSS](https://tailwindcss.com/) (v4), con **autenticación JWT** y **gestión de roles** (`READ_ONLY` / `WRITE`) integrada con el backend.

### Ecosistema principal

- [React](https://react.dev/)
- [Vite](https://vite.dev/)
- [TypeScript](https://www.typescriptlang.org/)
- [Tailwind CSS](https://tailwindcss.com/)
- [@tailwindcss/vite](https://tailwindcss.com/docs/installation/framework-guides#vite)
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc)
- [Font Awesome React](https://github.com/FortAwesome/react-fontawesome)
- [@dnd-kit/core](https://github.com/clauderic/dnd-kit) y [@dnd-kit/sortable](https://docs.dndkit.com/presets/sortable)
- [react-modal](https://github.com/reactjs/react-modal)
- [ESLint](https://eslint.org/)
- [Vitest](https://vitest.dev/)
- [Testing Library](https://testing-library.com/docs/react-testing-library/intro/)

### Instalación (frontend)

Se recomienda usar [Node.js](https://nodejs.org/) versión `>=20` y [npm](https://www.npmjs.com/get-npm) versión `>=10`.

#### Preparar ambiente de desarrollo

Clona el repo en la carpeta deseada e instala dependencias con los siguientes comandos:

```bash
# crea una carpeta
mkdir <project-folder-name>

# clona el repositorio
git clone git@github.com:jaguapache/Tablero-Kanban.git <project-folder-name>

# entra en la carpeta
cd <project-folder-name>

# instala dependencias (carpeta del frontend)
npm install
```

#### Ejecutar ambiente de desarrollo

```bash
npm run dev
```

Levanta el servidor de desarrollo de Vite con hot reload (por defecto en `http://localhost:5173`).

### Funcionalidades principales (frontend)

- **Autenticación y gestión de sesión**
  - Pantalla de **login** con email y contraseña.
  - Consumo del endpoint `/api/users/login` y almacenamiento del **token JWT** en `localStorage`.
  - Decodificación del JWT en el frontend para obtener `scope` y `user_id`.
  - Cierre de sesión manual (botón "Cerrar sesión") y automático cuando el backend responde `401`.

- **Gestión de roles y permisos (READ_ONLY / WRITE)**
  - El `scope` del JWT (`users.read` / `users.read users.write`) se traduce en un flag `writeScope` en el frontend.
  - Usuarios con **solo lectura** (`READ_ONLY`) pueden ver el tablero y los usuarios, pero:
    - No pueden arrastrar tarjetas (Drag & Drop deshabilitado).
    - No ven menús de edición/eliminación de tareas o usuarios.
  - Usuarios con rol **WRITE** tienen permisos completos de escritura en tareas y usuarios (según reglas del backend).

- **Tablero Kanban con Drag & Drop**
  - Tres columnas: `TODO`, `DOING` y `DONE`.
  - Reordenación de tarjetas dentro de la misma columna y movimiento entre columnas, implementado con `@dnd-kit/core` y `@dnd-kit/sortable`.
  - Las tareas se cargan y persisten contra la API REST del backend (`/api/kanban/tasks`, `/api/kanban/task/...`).
  - El Drag & Drop solo está activo cuando el usuario tiene `writeScope`.

- **Gestión de usuarios y perfil**
  - Listado de usuarios consumiendo `/api/users/getAllUsers` o `/api/users/getUsersActivated`.
  - Alta de usuarios desde el frontend (registro) usando `/api/users/register`.
  - Edición y desactivación de usuarios mediante modales (`react-modal`) conectados a `/api/users/updateUser/{id}`.
  - Detalle de usuario con información básica y **datos meteorológicos** (consumo del endpoint `/api/users/getWeather/{id}` expuesto por el backend).
  - Vista simplificada de **"Mi perfil"** para usuarios con rol `READ_ONLY`, usando modales en lugar de navegación de página completa.

- **Contadores por columna**
  - Sección de contadores que muestra el número de tareas en cada columna.
  - Los contadores se actualizan automáticamente cuando se mueve, se crea o se elimina una tarea.

### Build de producción (frontend)

```bash
npm run build
```

Genera el build optimizado de la aplicación en la carpeta `dist/`.

Para previsualizar el build generado:

```bash
npm run preview
```

Levanta un servidor local que sirve el contenido de `dist/` para realizar pruebas de producción.

### Tests y calidad de código (frontend)

#### Lint

```bash
npm run lint
```

Ejecuta ESLint sobre el proyecto para revisar el estilo y encontrar problemas comunes en el código TypeScript/React.

### Scripts disponibles (frontend)

Resumen de los scripts definidos en `package.json` del frontend:

- `npm run dev` → servidor de desarrollo con Vite
- `npm run build` → build de producción (`dist/`)
- `npm run preview` → previsualizar el build
- `npm run lint` → ejecutar ESLint
- `npm test` → ejecutar tests con Vitest

---

## Backend Kanban - VIEWNEXT (Spring Boot)

Este proyecto es el **backend** de una aplicación Kanban de práctica para VIEWNEXT, desarrollado con **Spring Boot**. Expone una API REST para gestionar las tareas de un tablero Kanban, permitiendo crear, listar, actualizar y eliminar tareas.

### Características principales (backend)

- Gestión de tareas (modelo `Task`).
- Estados de tarea: `TODO`, `DOING`, `DONE`.
- Prioridades de tarea: `Baja`, `Media`, `Alta`.
- Arquitectura típica de Spring Boot: capas de controlador, servicio y repositorio.
- Gestión de usuarios con roles `READ_ONLY` y `WRITE`.
- Autenticación por email/contraseña usando JWT (Spring Security Resource Server).
- Protección de endpoints de tareas y usuarios según rol/scope.

### Base de datos

El backend está pensado para trabajar con **PostgreSQL**.

#### Creación de la tabla `tasks`

```sql
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,   
    priority VARCHAR(20) NOT NULL  
);

ALTER TABLE tasks
ADD CONSTRAINT chk_tasks_status
CHECK (status IN ('TODO', 'DOING', 'DONE'));

ALTER TABLE tasks
ADD CONSTRAINT chk_tasks_priority
CHECK (priority IN ('Baja', 'Media', 'Alta'));
```

Los contraints son las restricciones para que los campos **status** y **priority** admitan solo los valores **TODO, DOING y DONE** y **Baja, Media y Alta** respectivamente.

#### Operaciones SQL básicas sobre `tasks`

```sql
-- Añadir una tarea
INSERT INTO tasks (title, status, priority)
VALUES ('Tarea de prueba', 'TODO', 'Media');

-- Editar (actualizar) una tarea por id
UPDATE tasks
SET title = 'Tarea actualizada',
    status = 'DOING',
    priority = 'Alta'
WHERE id = 1;

-- Eliminar una tarea por id
DELETE FROM tasks
WHERE id = 1;

-- Obtener todas las tareas
SELECT * FROM tasks;

-- Obtener tareas filtradas por estado (TODO, DOING, DONE)
SELECT id, title, status, priority
FROM tasks
WHERE status = 'TODO';
```

#### Creación de la tabla `users`

```sql
CREATE TABLE users (
  id         BIGSERIAL PRIMARY KEY,
  name       VARCHAR(100) NOT NULL,
  lastname   VARCHAR(100) NOT NULL,
  email      VARCHAR(255) NOT NULL UNIQUE,
  password   VARCHAR(255) NOT NULL,
  enabled    BOOLEAN NOT NULL DEFAULT TRUE,
  ubication  INTEGER NOT NULL CHECK (ubication BETWEEN 10000 AND 99999)
);
```

#### Tabla de roles y relación usuarios-roles

```sql
CREATE TABLE roles (
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_users_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_users_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);
```

#### Operaciones SQL básicas sobre `users`

```sql
-- Añadir un usuario
INSERT INTO users (name, lastname, email, ubication)
VALUES ('Alejandro', 'Guapache', 'prueba@hotmail.com', 28050);

-- Editar (actualizar) un usuario por id
UPDATE users
SET name = 'Felipe',
    email = 'prueba@gmail.com',
    ubication = 28020
WHERE id = 1;

-- Eliminar un usuario por id
DELETE FROM users
WHERE id = 1;

-- Obtener todos los usuarios
SELECT * FROM users;
```

### Configuración de la aplicación (backend)

En el archivo `src/main/resources/application.properties` se configura la conexión a PostgreSQL. Un ejemplo básico es:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/DB_NAME
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
server.port=9000
```

#### Configuración de la API de OpenWeather

Para obtener información meteorológica asociada a cada usuario (usando su campo `ubication` como código postal), el backend consume la API pública de **OpenWeather** mediante **Spring WebClient**.

La API key se configura de forma externa usando variables de entorno y propiedades de Spring Boot:

1. En `application.properties`:

    ```properties
    openweather.api.key=${OPENWEATHER_API_KEY}
    ```

2. Crea un fichero en la raiz llamada .env

    ```bash
    OPENWEATHER_API_KEY="YOU_API_KEY"
    ```

    De esta forma **la clave no se guarda en el repositorio**, solo en tu entorno local.

### Endpoints de la API (backend)

La API se expone, en estos endpoints:

#### Tareas (TaskController)

- `POST /api/kanban/task` → crea una tarea nueva.
- `GET /api/kanban/tasks` → devuelve la lista de tareas.
- `PUT /api/kanban/task/{id}` → actualiza una tarea existente.
- `DELETE /api/kanban/task/{id}` → elimina una tarea.
- `GET /api/kanban/getByStatus/{status}` → obtiene tareas filtradas por estado (`TODO`, `DOING`, `DONE`).

#### Usuarios (UserController)

- `POST /api/users/register` → registro de usuario de aplicación. Crea un usuario con rol `READ_ONLY`. Si el email ya existe y el usuario está deshabilitado (`enabled = false`), lo reactiva (pone `enabled = true`, actualiza datos y contraseña) devolviendo `201`. Si el email ya está registrado y el usuario está habilitado, devuelve `400`.
- `POST /api/users/login` → login por email/contraseña. Devuelve un token JWT con campos `access_token`, `token_type` (Bearer) y `scope` (`users.read` o `users.read users.write`) si las credenciales son correctas y el usuario está habilitado. Si el usuario no existe, está deshabilitado o la contraseña es incorrecta, devuelve `401` con el mensaje `Usuario o contraseña incorrectos`.
- `POST /api/users/login` → login por email/contraseña. Devuelve un token JWT con campos `access_token`, `token_type` (Bearer) y `scope` (`users.read` o `users.read users.write`) si las credenciales son correctas y el usuario está habilitado. Si el usuario no existe o está deshabilitado (`enabled = false`), devuelve `404` con el mensaje `Usuario no encontrado`. Si la contraseña es incorrecta, devuelve `401` con el mensaje `Usuario o contraseña incorrectos`.
- `POST /api/users/createUser` → crea un usuario nuevo (uso más administrativo, similar a `register` pero permitiendo marcar el campo `admin` para asignar también el rol `WRITE`).
- `GET /api/users/getAllUsers` → devuelve la lista de usuarios (sin filtrar por `enabled`).
- `GET /api/users/getUsersActivated` → devuelve solo los usuarios con `enabled = true`.
- `GET /api/users/getUser/{id}` → devuelve un usuario concreto por `id`.
- `PUT /api/users/updateUser/{id}` → actualiza un usuario existente. Los usuarios con rol `READ_ONLY` solo pueden modificar sus propios datos; los usuarios con rol `WRITE` pueden modificar cualquier usuario.
- `DELETE /api/users/deleteUser/{id}` → elimina un usuario.
- `GET /api/users/getWeather/{id}` → obtiene la información meteorológica (temp, tempMin, tempMax) asociada al usuario mediante OpenWeather.

### Seguridad y JWT (backend)

- El backend usa Spring Security como **Resource Server** para validar tokens JWT.
- El login (`/api/users/login`) genera un JWT que incluye:
  - `sub` → email del usuario.
  - `user_id` → identificador del usuario en base de datos.
  - `scope` → `users.read` o `users.read users.write` según los roles del usuario.
- Los scopes del token se mapean a authorities `SCOPE_users.read` y `SCOPE_users.write`.
- Reglas principales:
  - Endpoints de login y registro (`/api/users/login`, `/api/users/register`) están abiertos.
  - Endpoints de gestión de tareas (`/api/kanban/**`) y operaciones de escritura sobre usuarios (`POST/PUT/DELETE /api/users/**`) requieren un token válido con scope `users.write`.
  - Los usuarios con solo `READ_ONLY` (scope `users.read`) pueden consultar y actualizar únicamente su propio usuario mediante `/api/users/updateUser/{id}`.

#### Ejemplo de login y uso del token en Postman

1. Petición `POST http://localhost:9000/api/users/login` con body JSON:

    ```json
    {
      "email": "usuario@example.com",
      "password": "tu_password"
    }
    ```

2. Copia el valor de `access_token` de la respuesta.
3. En las siguientes peticiones protegidas, añade el header:

    ```text
    Authorization: Bearer <access_token>
    ```

### Colección Postman (backend)

Puedes importar la siguiente colección en Postman ("Import" → "Raw text") para tener todas las operaciones CRUD preparadas:

### API Tareas

```json
{
    "info": {
        "name": "Kanban Backend - VIEWNEXT",
        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    },
    "item": [
        {
            "name": "Crear tarea",
            "request": {
                "method": "POST",
                "header": [
              { "key": "Content-Type", "value": "application/json" },
              { "key": "Authorization", "value": "Bearer {{token}}" }
                ],
                "url": {
                    "raw": "http://localhost:9000/api/kanban/task",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "9000",
                    "path": ["api", "kanban", "task"]
                },
                "body": {
                    "mode": "raw",
                    "raw": "{\n  \"title\": \"Tarea de prueba\",\n  \"status\": \"TODO\",\n  \"priority\": \"Media\"\n}"
                }
            }
        },
        {
            "name": "Listar tareas",
            "request": {
                "method": "GET",
            "header": [
              { "key": "Authorization", "value": "Bearer {{token}}" }
            ],
            "url": {
              "raw": "http://localhost:9000/api/kanban/tasks",
              "protocol": "http",
              "host": ["localhost"],
              "port": "9000",
              "path": ["api", "kanban", "tasks"]
            }
            }
        },
        {
            "name": "Actualizar tarea",
            "request": {
                "method": "PUT",
                "header": [
              { "key": "Content-Type", "value": "application/json" },
              { "key": "Authorization", "value": "Bearer {{token}}" }
                ],
                "url": {
                    "raw": "http://localhost:9000/api/kanban/task/1",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "9000",
                    "path": ["api", "kanban", "task", "1"]
                },
                "body": {
                    "mode": "raw",
                    "raw": "{\n  \"title\": \"Tarea actualizada\",\n  \"status\": \"DOING\",\n  \"priority\": \"Alta\"\n}"
                }
            }
        },
        {
            "name": "Eliminar tarea",
            "request": {
                "method": "DELETE",
            "header": [
              { "key": "Authorization", "value": "Bearer {{token}}" }
            ],
            "url": {
              "raw": "http://localhost:9000/api/kanban/task/1",
              "protocol": "http",
              "host": ["localhost"],
              "port": "9000",
              "path": ["api", "kanban", "task", "1"]
            }
            }
        },
        {
            "name": "Tareas por estado",
            "request": {
                "method": "GET",
            "header": [
              { "key": "Authorization", "value": "Bearer {{token}}" }
            ],
            "url": {
              "raw": "http://localhost:9000/api/kanban/getByStatus/TODO",
              "protocol": "http",
              "host": ["localhost"],
              "port": "9000",
              "path": ["api", "kanban", "getByStatus", "TODO"]
            }
            }
        }
    ]
}
```

### API Usuarios

```json
{
  "info": {
    "name": "Kanban Users Backend - VIEWNEXT",
    "schema": "<https://schema.getpostman.com/json/collection/v2.1.0/collection.json>"
  },
  "item": [
    {
      "name": "Registro usuario",
      "request": {
        "method": "POST",
        "header": [
          { "key": "Content-Type", "value": "application/json" }
        ],
        "url": {
          "raw": "http://localhost:9000/api/users/register",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9000",
          "path": ["api", "users", "register"]
        },
        "body": {
          "mode": "raw",
          "raw": "{\n  \"name\": \"Alejandro\",\n  \"lastname\": \"Guapache\",\n  \"email\": \"prueba@hotmail.com\",\n  \"password\": \"mi_password\",\n  \"ubication\": 28050\n}"
        }
      }
    },
    {
      "name": "Login",
      "request": {
        "method": "POST",
        "header": [
          { "key": "Content-Type", "value": "application/json" }
        ],
        "url": {
          "raw": "http://localhost:9000/api/users/login",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9000",
          "path": ["api", "users", "login"]
        },
        "body": {
          "mode": "raw",
          "raw": "{\n  \"email\": \"prueba@hotmail.com\",\n  \"password\": \"mi_password\"\n}"
        }
      }
    },
    {
      "name": "Crear usuario (admin)",
      "request": {
        "method": "POST",
        "header": [
          { "key": "Content-Type", "value": "application/json" },
          { "key": "Authorization", "value": "Bearer {{token}}" }
        ],
        "url": {
          "raw": "http://localhost:9000/api/users/createUser",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9000",
          "path": ["api", "users", "createUser"]
        },
        "body": {
          "mode": "raw",
          "raw": "{\n  \"name\": \"Admin\",\n  \"lastname\": \"User\",\n  \"email\": \"admin@example.com\",\n  \"password\": \"mi_password\",\n  \"ubication\": 28050,\n  \"admin\": true\n}"
        }
      }
    },
    {
      "name": "Listar usuarios",
      "request": {
        "method": "GET",
        "header": [
          { "key": "Authorization", "value": "Bearer {{token}}" }
        ],
        "url": {
          "raw": "http://localhost:9000/api/users/getAllUsers",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9000",
          "path": ["api", "users", "getAllUsers"]
        }
      }
    },
    {
      "name": "Listar usuarios activados",
      "request": {
        "method": "GET",
        "header": [
          { "key": "Authorization", "value": "Bearer {{token}}" }
        ],
        "url": {
          "raw": "http://localhost:9000/api/users/getUsersActivated",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9000",
          "path": ["api", "users", "getUsersActivated"]
        }
      }
    },
    {
      "name": "Obtener usuario por id",
      "request": {
        "method": "GET",
        "header": [
          { "key": "Authorization", "value": "Bearer {{token}}" }
        ],
        "url": {
          "raw": "http://localhost:9000/api/users/getUser/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9000",
          "path": ["api", "users", "getUser", "1"]
        }
      }
    },
    {
      "name": "Actualizar usuario",
      "request": {
        "method": "PUT",
        "header": [
          { "key": "Content-Type", "value": "application/json" },
          { "key": "Authorization", "value": "Bearer {{token}}" }
        ],
        "url": {
          "raw": "http://localhost:9000/api/users/updateUser/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9000",
          "path": ["api", "users", "updateUser", "1"]
        },
        "body": {
          "mode": "raw",
          "raw": "{\n  \"name\": \"Felipe\",\n  \"lastname\": \"Guapache\",\n  \"email\": \"prueba@gmail.com\",\n  \"ubication\": 28020\n}"
        }
      }
    },
    {
      "name": "Eliminar usuario",
      "request": {
        "method": "DELETE",
        "header": [
          { "key": "Authorization", "value": "Bearer {{token}}" }
        ],
        "url": {
          "raw": "http://localhost:9000/api/users/deleteUser/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9000",
          "path": ["api", "users", "deleteUser", "1"]
        }
      }
    },
    {
      "name": "Clima por usuario",
      "request": {
        "method": "GET",
        "header": [
          { "key": "Authorization", "value": "Bearer {{token}}" }
        ],
        "url": {
          "raw": "http://localhost:9000/api/users/getWeather/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "9000",
          "path": ["api", "users", "getWeather", "1"]
        }
      }
    }
  ]
}
```

### Swagger API

`https://app.swaggerhub.com/apis/viewnext-694/kanban-api/1.0`

### Ejecución del proyecto (backend)

1. Asegúrate de tener **Java 17** (o la versión requerida en `pom.xml`).
2. Compila y ejecuta la aplicación:

```bash
mvn spring-boot:run
```

La aplicación se levantará normalmente en `http://localhost:9000`.
