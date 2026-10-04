# TaskCodeCompiler

## About

TaskCodeCompiler is a backend application for running code in isolated Docker containers.

I made this project to learn how RabbitMQ works between two Spring applications and how asynchronous code execution can be organized.

The application supports:
- Python
- C
- C++

## How it works

1. A user registers and logs in.
2. The API creates a task and sends it to RabbitMQ.
3. The consumer receives the task and runs the code in a Docker container.
4. The consumer sends the execution result back through RabbitMQ.
5. The API saves the result in PostgreSQL.
6. The user can request the task status and result.

## Stack

Java 21, Spring Boot, Spring Security, Spring Data JPA, PostgreSQL,
RabbitMQ, Liquibase, Docker, Gradle.

## Main features

- Registration and authentication with session tokens
- Asynchronous code execution through RabbitMQ
- Python, C and C++ execution
- Isolated Docker containers
- Execution timeout and output limit
- CPU, memory and network restrictions for containers
- Task and result persistence in PostgreSQL
- Task ownership checks
- Docker Compose for running the application

## Run locally

Requirements:
- Java 21
- Docker
- Docker Compose
- Make (optional)

Using Make:

```bash
  make up
```

Or with Docker Compose:
```bash
    docker compose up --build
```

## Configuration

The main configuration can be changed through environment variables.

| Variable | Default | Description |
| --- | --- | --- |
| `PORT` | `8090` | API server port |
| `DB` | `backend` | PostgreSQL database name |
| `DB_USERNAME` | `postgres` | PostgreSQL username |
| `DB_PASSWORD` | `postgres` | PostgreSQL password |
| `DB_URL` | `jdbc:postgresql://localhost:5434/backend` | PostgreSQL connection URL |
| `RABBIT_USER` | `compiler` | RabbitMQ username |
| `RABBIT_PASSWORD` | `compiler` | RabbitMQ password |
| `BROKER_ADDRESS` | `localhost` | RabbitMQ host |
| `APP_DOCKERFILE_PATH` | `./code-compiler-consumer/dockerCompiler` | Directory containing the compiler Dockerfile |
| `APP_DOCKER_HOST` | `tcp://localhost:2375` | Docker daemon address |

When the application is started with Docker Compose, service addresses are configured automatically.

Code execution settings are configured in `compiler.yaml`:

- maximum output size: `2000` bytes
- execution timeout: `30` seconds
- compiler image build timeout: `180` seconds
- consumer concurrency: `2`, maximum `4`

API RabbitMQ listener concurrency is `5`, maximum `10`.
Session lifetime is `10` minutes and expired sessions are cleaned every hour.

## API

All task endpoints require authentication with a bearer token.

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/register` | Register a new user |
| `POST` | `/login` | Login and receive a bearer token |
| `POST` | `/task/{compiler}` | Submit source code for execution |
| `GET` | `/status/{task_id}` | Get task execution status |
| `GET` | `/result/{task_id}` | Get task execution result |

Supported values for `{compiler}` are `py`, `c` and `c++`.

Example authorization header:

```text
Authorization: Bearer <token>
```

## Notes

Submitted code is executed in temporary Docker containers with network
access disabled and CPU, memory, execution time and output limits.

<i>This is a learning and portfolio project. The execution environment is not
intended to be a production-grade sandbox for running untrusted code.</i>
