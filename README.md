# Backend Kanban - VIEWNEXT

Este proyecto es el **backend** de una aplicación Kanban de práctica para VIEWNEXT, desarrollado con **Spring Boot**. Expone una API REST para gestionar las tareas de un tablero Kanban, permitiendo crear, listar, actualizar y eliminar tareas.

## Características principales

- Gestión de tareas (modelo `Task`).
- Estados de tarea: `TODO`, `DOING`, `DONE`.
- Prioridades de tarea: `Baja`, `Media`, `Alta`.
- Arquitectura típica de Spring Boot: capas de controlador, servicio y repositorio.

## Base de datos

El backend está pensado para trabajar con **PostgreSQL**.

### Creación de la tabla `tasks`

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

## Configuración de la aplicación

En el archivo `src/main/resources/application.properties` se configura la conexión a PostgreSQL. Un ejemplo básico es:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/crud_kanban_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

La API se expone, en estos endpoints:

- `POST /api/kanban/task` → crea una tarea nueva.
- `GET /api/kanban/tasks` → devuelve la lista de tareas.
- `PUT /api/kanban/task/{id}` → actualiza una tarea existente.
- `DELETE /api/kanban/task/{id}` → elimina una tarea.
- `GET /api/kanban/getByStatus/{status}` → obtiene tareas filtradas por estado (`TODO`, `DOING`, `DONE`).

### Colección Postman

Puedes importar la siguiente colección en Postman ("Import" → "Raw text") para tener todas las operaciones CRUD preparadas:

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
                    { "key": "Content-Type", "value": "application/json" }
                ],
                "url": {
                    "raw": "http://localhost:8080/api/kanban/task",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "8080",
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
                "url": {
                    "raw": "http://localhost:8080/api/kanban/tasks",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "8080",
                    "path": ["api", "kanban", "tasks"]
                }
            }
        },
        {
            "name": "Actualizar tarea",
            "request": {
                "method": "PUT",
                "header": [
                    { "key": "Content-Type", "value": "application/json" }
                ],
                "url": {
                    "raw": "http://localhost:8080/api/kanban/task/1",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "8080",
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
                "url": {
                    "raw": "http://localhost:8080/api/kanban/task/1",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "8080",
                    "path": ["api", "kanban", "task", "1"]
                }
            }
        },
        {
            "name": "Tareas por estado",
            "request": {
                "method": "GET",
                "url": {
                    "raw": "http://localhost:8080/api/kanban/getByStatus/TODO",
                    "protocol": "http",
                    "host": ["localhost"],
                    "port": "8080",
                    "path": ["api", "kanban", "getByStatus", "TODO"]
                }
            }
        }
    ]
}
```

## Ejecución del proyecto

1. Asegúrate de tener **Java 17** (o la versión requerida en `pom.xml`).
2. Compila y ejecuta la aplicación:

```bash
mvn spring-boot:run
```

La aplicación se levantará normalmente en `http://localhost:8080`.
