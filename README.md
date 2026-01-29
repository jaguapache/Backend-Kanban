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

## Configuración de la aplicación

En el archivo `src/main/resources/application.properties` se configura la conexión a PostgreSQL. Un ejemplo básico es:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/crud_kanban_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
``

La API se expone, en estos endpoints:

- `POST /api/kanban/task` → crea una tarea nueva.
- `GET /api/kanban/tasks` → devuelve la lista de tareas.
- `PUT /api/kanban/task/{id}` → actualiza una tarea existente.
- `DELETE /api/kanban/task/{id}` → elimina una tarea.
- `GET /api/kanban/getByStatus/{status}` → obtiene tareas filtradas por estado (`TODO`, `DOING`, `DONE`).

## Ejecución del proyecto

1. Asegúrate de tener **Java 17** (o la versión requerida en `pom.xml`).
2. Compila y ejecuta la aplicación:

```bash
mvn spring-boot:run
```

La aplicación se levantará normalmente en `http://localhost:8080`.
