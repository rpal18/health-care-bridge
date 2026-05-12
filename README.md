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

# 🔥 Key Features

## 🤖 AI Emergency Triage

- Image + text-based emergency analysis
- Detects severity level:
  - LOW
  - MEDIUM
  - HIGH

- Suggests required medical resources:
  - ICU Bed
  - Oxygen
  - Ventilator
  - Blood
  - Trauma Support
  - etc.

### AI Stack

- Llama 4 Scout (Hosted on Groq)
- Spring AI Integration

---

## 📍 Geospatial Emergency Routing

Uses:

- PostgreSQL
- PostGIS

to perform:

- nearest facility search
- distance calculation
- spatial querying
- emergency resource mapping

### Example

Find nearest hospitals having:
- ICU beds
- oxygen
- blood availability
- ventilators

within minimum distance from the user location.

---

## ⚡ Spatial Query Optimization

To improve performance for nearby organization discovery, a spatial index was created on the location column.

### Example

```sql
CREATE INDEX idx_facility_location
ON facility
USING GIST(location);
```

This significantly improves:
- nearest facility lookup
- geospatial filtering
- distance-based searches

especially for large-scale datasets.

---

# 🩸 Blood Resource Support

Supports:

### Blood Groups

- A+
- A-
- B+
- B-
- AB+
- AB-
- O+
- O-

### Blood Components

- Platelets
- Plasma
- RBC
- Whole Blood
- Cryoprecipitate
- etc.

---

## 📲 Telegram Bot Integration

Integrated Telegram Bot for real-time emergency reporting.

### Workflow

1. User shares live GPS location
2. User uploads:
   - emergency image OR
   - emergency description
3. AI analyzes emergency
4. System returns:
   - severity level
   - clinical reasoning
   - required resources
   - nearest available facilities

---

# 🏥 Facility Management System

### Supported Facility Types

- Hospital
- Blood Bank
- Trauma Center
- Clinic
- NGO
- Govt Body

### Features

- Facility onboarding workflow
- Approval/rejection system
- Facility admin assignment
- Resource inventory management
- Soft delete & restore
- Status tracking

---

# 🔐 Authentication & Authorization

Implemented using:
- Spring Security
- JWT Authentication

### Roles

- USER
- ADMIN
- ORG_ADMIN

---

# 🛠️ Tech Stack

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring AI

## Database

- PostgreSQL
- PostGIS

## AI

- Llama 4 Scout
- Groq API

## Other Tools

- Telegram Bot API
- Swagger UI
- JUnit 5
- Mockito
- ModelMapper

---

# 🧠 System Architecture

The system consists of:

- AI Triage Engine
- Emergency Routing Engine
- Facility Resource Management
- Telegram Bot Layer
- JWT Authentication Layer
- Spatial Query Engine

---

# 🗂️ Database Design

Core Entities:

- Users
- Facilities
- Resources
- Requested Facilities
- Admins
- Patients
- Donors

Geospatial data is stored using:

```sql
geography(Point, 4326)
```

for accurate location-based querying.

---

# 📡 API Documentation

Swagger UI integrated for API testing and documentation.

### Example APIs

- Authentication APIs
- Facility APIs
- Resource APIs
- AI Analysis APIs
- Emergency Routing APIs

---

# 📮 Postman Collection

API collection for testing all endpoints is available inside the repository.

## Includes

- Authentication APIs
- Facility APIs
- Resource APIs
- AI Analysis APIs
- Emergency Routing APIs

### Import into Postman

1. Open Postman
2. Click Import
3. Select:

```text
postman/LifeLink-API.postman_collection.json
```

4. Configure environment variables if needed
5. Start testing APIs

---

# 🧪 Testing

Implemented unit testing for:
- Service Layer
- Business Logic

Using:
- JUnit 5
- Mockito

---

# 📸 Emergency Workflow

## Step 1 – User Shares Location

User shares live GPS location through Telegram Bot.

## Step 2 – Upload Emergency Evidence

User uploads:
- accident image OR
- text description

## Step 3 – AI Triage

AI model analyzes:
- injury severity
- trauma indicators
- required medical resources

## Step 4 – Spatial Resource Search

PostGIS finds nearest facilities with required resources.

## Step 5 – Emergency Assistance Response

User receives:
- severity level
- medical reasoning
- required resources
- nearest healthcare facilities

---

# 🚀 Future Improvements

## Planned Enhancements

### 🐳 Docker Containerization

- Spring Boot container
- PostgreSQL + PostGIS container

### ☁️ Cloud Deployment

Planned deployment on cloud infrastructure.

### 🔐 Advanced Security

Implementation of:
- secure secret management
- encryption best practices
- hardened JWT security
- secure API communication

### 🌐 Frontend Development

Frontend application planned for future versions.

---

# 📌 Project Highlights

- AI + Geospatial System Design
- Real-world emergency use case
- Telegram Bot integration
- PostGIS spatial querying
- AI-powered medical triage
- Scalable backend architecture
- Role-based access management

---

# ▶️ How to Run

## Prerequisites

- Java 21
- Maven
- PostgreSQL
- PostGIS Extension

---

## Clone Repository

```bash
git clone <your-repository-url>
cd lifelink
```

---

## Configure Database

Create PostgreSQL database and enable PostGIS:

```sql
CREATE EXTENSION postgis;
```

---

## Configure Environment Variables

```properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

groq.api.key=

jwt.secret=

telegram.bot.token=
```

---

## Run Application

```bash
mvn spring-boot:run
```

---

# 📂 Project Structure

```text
LifeLink/
│
├── src/
├── postman/
├── screenshots/
├── docs/
├── README.md
└── pom.xml
```

---

# 👨‍💻 Author

Rohit Pal

Backend Developer | Java & Spring Boot Enthusiast  
Focused on building scalable AI-powered backend systems.

---

# ⭐ Motivation

LifeLink was built with the goal of improving emergency response accessibility using AI and geospatial technologies, enabling faster medical assistance during critical situations.
