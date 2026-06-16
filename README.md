# LifeLink – AI-Powered HealthCareBridge

## 🚑 Overview

LifeLink is an AI-powered emergency response backend system designed to help users quickly discover the nearest healthcare facilities and emergency resources during critical situations.

The system combines:

- AI-based emergency triage
- Geospatial search using PostGIS
- Telegram Bot integration
- Role-based healthcare management
- Spring Boot backend architecture

A user can upload an accident image or describe the emergency, and the system automatically:

1. Analyzes the emergency using AI
2. Detects required medical resources
3. Finds the nearest capable healthcare facilities
4. Returns prioritized emergency assistance

---

## 🔗 Live Demo

**Swagger UI:** [http://35.171.59.235/swagger-ui/index.html](http://35.171.59.235/swagger-ui/index.html)

---

## 📲 Try It via Telegram Bot

Experience the AI emergency triage feature directly from Telegram — no signup required.

**Bot:** [@ResQBridge_bot](https://t.me/ResQBridge_bot)

### Steps

1. Open Telegram and search for `@ResQBridge_bot`
2. Open the bot and send `/start`
3. Send your **live location**
4. Send a **photo** of the emergency situation or **describe it in text**
5. The AI will analyze the emergency and return:
   - Severity level
   - Clinical reasoning
   - Required medical resources
   - Nearest available healthcare facilities

---

## 🔥 Key Features

### 🤖 AI Emergency Triage

- Image + text-based emergency analysis
- Detects severity level: LOW / MEDIUM / HIGH
- Suggests required medical resources (ICU Bed, Oxygen, Ventilator, Blood, Trauma Support, etc.)

**AI Stack:**
- Llama 4 Scout (hosted on Groq)
- Spring AI Integration

---

### 📍 Geospatial Emergency Routing

Uses PostgreSQL + PostGIS to perform:

- Nearest facility search
- Distance calculation
- Spatial querying
- Emergency resource mapping

**Example:** Find nearest hospitals with ICU beds, oxygen, blood availability, and ventilators within minimum distance from the user location.

---

### ⚡ Spatial Query Optimization

A spatial index is created on the location column for improved performance:

```sql
CREATE INDEX idx_facility_location
ON facility
USING GIST(location);
```

This significantly improves nearest facility lookup and distance-based searches, especially for large-scale datasets.

---

### 🩸 Blood Resource Support

**Blood Groups:** A+, A-, B+, B-, AB+, AB-, O+, O-

**Blood Components:** Platelets, Plasma, RBC, Whole Blood, Cryoprecipitate, etc.

---

### 🏥 Facility Management System

**Supported Facility Types:** Hospital, Blood Bank, Trauma Center, Clinic, NGO, Govt Body

**Features:**
- Facility onboarding workflow
- Approval/rejection system
- Facility admin assignment
- Resource inventory management
- Soft delete & restore
- Status tracking

---

### 🔐 Authentication & Authorization

Implemented using Spring Security + JWT Authentication.

**Roles:** USER, ADMIN, ORG_ADMIN

---

## 🛠️ Tech Stack

| Category | Technologies |
|---|---|
| Backend | Java 21, Spring Boot, Spring Security, Spring Data JPA, Spring AI |
| Database | PostgreSQL, PostGIS |
| AI | Llama 4 Scout, Groq API |
| Messaging | Telegram Bot API |
| DevOps | Docker, Docker Compose, AWS EC2, GitHub Actions CI/CD |
| Testing | JUnit 5, Mockito |
| Docs | Swagger UI |
| Other | ModelMapper, MapStruct |

---

## 🧠 System Architecture

- AI Triage Engine
- Emergency Routing Engine
- Facility Resource Management
- Telegram Bot Layer
- JWT Authentication Layer
- Spatial Query Engine

---

## 🗂️ Database Design

Core Entities: Users, Facilities, Resources, Requested Facilities, Admins, Patients, Donors

Geospatial data stored using:

```sql
geography(Point, 4326)
```

---

## 🚀 Deployment

- Containerized with Docker and Docker Compose
- Deployed on AWS EC2 (Ubuntu)
- CI/CD pipeline via GitHub Actions
  - CI: build and test on every push to `main`
  - CD: manual deploy to EC2 via SSH

---

## 🧪 Testing

Unit testing implemented for service layer and business logic using JUnit 5 and Mockito.

CI pipeline runs tests on every push with a PostGIS service container spun up on the GitHub Actions runner.

---

## ▶️ How to Run Locally

### Prerequisites

- Java 21
- Maven
- Docker & Docker Compose

### Clone Repository

```bash
git clone <your-repository-url>
cd HeathCareBridge
```

### Configure Environment Variables

Create a `.env` file in the project root (refer to `.env.example`):

```properties
DB_URL=jdbc:postgresql://localhost:5432/healthcarebridge
DB_USERNAME=
DB_PASSWORD=
DB_NAME=

GROQ_API_KEY=
JWT_SECRET=
TELEGRAM_BOT_TOKEN=
TELEGRAM_BOT_USERNAME=
ADMIN_EMAIL=
ADMIN_PASSWORD=
```

### Run with Docker Compose

```bash
docker compose up -d
```

Access Swagger UI at: `http://localhost/swagger-ui/index.html`

---

## 📡 API Documentation

Swagger UI: [http://35.171.59.235/swagger-ui/index.html](http://35.171.59.235/swagger-ui/index.html)

Available API groups:
- Authentication APIs
- Facility APIs
- Resource APIs
- AI Analysis APIs
- Emergency Routing APIs

---

## 📮 Postman Collection

```text
postman/LifeLink-API.postman_collection.json
```

Import into Postman → configure environment variables → start testing.

---

## 📂 Project Structure

```text
LifeLink/
│
├── .github/workflows/    # CI/CD pipeline
├── src/                  # Application source
├── postman/              # Postman collection
├── screenshots/          # Project screenshots
├── docs/                 # Documentation
├── docker-compose.yml
├── Dockerfile
├── .env.example
└── README.md
```

---

## 👨‍💻 Author

**Rohit Pal**

Backend Developer — Java & Spring Boot  
[LinkedIn](www.linkedin.com/in/rohit-pal-a41a6229a) | [GitHub]([https://github.com/rpal18](https://github.com/rpal18))

---

## ⭐ Motivation

LifeLink was built to improve emergency response accessibility using AI and geospatial technologies, enabling faster medical assistance during critical situations.
