# Patient Management Microservices Project

This repository contains a microservices-based Patient Management system implemented using Java Spring Boot, AWS CDK (Cloud Development Kit), Docker, and LocalStack for local AWS service emulation.

---

## Overview

This project demonstrates a distributed system for patient management including:

- Authentication  
- Patient records  
- Billing  
- Analytics  
- API gateway  
- **Inter-service communication using gRPC**

It integrates with AWS services like RDS, ECS, Kafka, and Secrets Manager — all emulated locally with LocalStack to facilitate development without requiring real AWS accounts.

---

## Architecture

- **Microservices:** Separate Spring Boot services for authentication, patient management, billing, analytics, and an API gateway.  
- **Databases:** MySQL databases per service.  
- **Messaging:** Apache Kafka for event streaming.  
- **Communication:** gRPC for efficient, strongly-typed inter-service communication.  
- **Infrastructure:** AWS CDK used to define cloud infrastructure as code.  
- **Local AWS Emulation:** LocalStack to mock AWS services locally.  
- **Containerization:** Docker for containerizing services, Kafka, Zookeeper, MySQL, and LocalStack.

---
