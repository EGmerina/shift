
# Sellers and Transactions API

RESTful API сервис для управления продавцами и их финансовыми транзакциями. Приложение предоставляет возможности для создания и просмотра данных о продавцах, проведения транзакций (покупок), а также сбора базовой аналитики.

## 🛠 Технологии и зависимости

Проект написан на Java и использует фреймворк Spring Boot. Основной инструмент для сборки — **Gradle**.

* **Spring Boot Web** — создание REST API.
* **Spring Data JPA / Hibernate** — взаимодействие с базой данных.
* **PostgreSQL** — основная реляционная база данных.
* **H2 Database** — легковесная база данных в памяти (in-memory) для запуска интеграционных тестов.
* **Docker Compose** — автоматическое поднятие окружения (базы данных) при запуске приложения.
* **MapStruct** — автоматическая генерация мапперов для преобразования Entity в DTO и обратно.
* **Lombok** — сокращение шаблонного кода (геттеры, сеттеры, конструкторы).

##  Инструкция по сборке и запуску

### Требования
Для запуска проекта на вашем компьютере должны быть установлены:
1. **Java** (версия 17 или выше).
2. **Docker** (для автоматического запуска PostgreSQL).

### Шаг 1. Сборка проекта
Сборка осуществляется через Gradle wrapper. Выполните команду в корневой папке проекта:

Для Windows:
```bash
gradlew.bat clean build
```
Для macOS / Linux:
```bash
./gradlew clean build
```

### Шаг 2. Запуск приложения
Приложение использует встроенную поддержку `spring-boot-docker-compose`. При запуске Spring сам найдет файл `docker-compose.yaml` и поднимет контейнер с PostgreSQL.

Вы можете запустить проект напрямую через Gradle:
```bash
./gradlew bootRun
```
Или запустить скомпилированный `.jar` файл:
```bash
java -jar build/libs/sellers-and-transactions-0.0.1-SNAPSHOT.jar
```
*(Имя jar-файла может отличаться в зависимости от версии в `build.gradle`)*.

По умолчанию сервер запускается на порту `8080`.

---

##  Примеры использования API

Ниже приведены примеры основных запросов к API.

### 1. Создание нового продавца
**Запрос (POST `/api/sellers`)**
```bash
curl -X POST http://localhost:8080/api/sellers \
-H "Content-Type: application/json" \
-d '{
  "name": "Магазин Электроники",
  "contactInfo": "electro@shift.ru"
}'
```

**Успешный ответ (201 Created)**
```json
{
  "id": 1,
  "name": "Магазин Электроники",
  "contactInfo": "electro@shift.ru",
  "registrationDate": "2026-04-09T20:06:58.106"
}
```

### 2. Создание новой транзакции
**Запрос (POST `/api/transactions`)**
```bash
curl -X POST http://localhost:8080/api/transactions \
-H "Content-Type: application/json" \
-d '{
  "sellerId": 1,
  "amount": 25000.50,
  "paymentType": "CARD"
}'
```

**Успешный ответ (201 Created)**
```json
{
  "id": 3,
  "seller": {
    "id": 1,
    "name": "Магазин Электроники",
    "contactInfo": "electro@shift.ru",
    "registrationDate": "2026-04-09T20:06:58.106"
  },
  "amount": 25000.50,
  "paymentType": "CARD",
  "transactionDate": "2026-04-09T20:06:58.122"
}
```

### 3. Аналитика: Поиск продавцов с суммой транзакций меньше указанной
**Запрос (GET `/api/analytics/transactions-less-than?amount=50000`)**
```bash
curl -X GET "http://localhost:8080/api/analytics/transactions-less-than?amount=50000"
```

**Успешный ответ (200 OK)**
```json
[
  {
    "id": 1,
    "name": "Магазин Электроники",
    "contactInfo": "electro@shift.ru",
    "registrationDate": "2026-04-09T20:06:58.106"
  }
]
```