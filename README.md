# FileVersionControl

A full-stack application featuring API endpoints for committing and restoring files

## Overview

The project provides API endpoints to handle commit and restore operations, as well as a frontend application
for convenient interaction with the API.

### Features

- Commit the files in a directory
- Restore a previous commit to a specified location

### Tech Stack

- Frontend: Angular
- Backend: Spring Boot

## Getting Started

### Prerequisites

- Java JDK 20
- Apache Maven

### Installation

```bash
git clone https://github.com/lotoyawillis/FileVersionControl.git
cd FileVersionControl
npm install
cd src
cd main
cd frontend
cd angular-app
ng build --configuration production
cd ..
cd ..
cd ..
cd ..
mvn clean install
mvn spring-boot:run
```