# AI Resume Analyzer & Job Matcher

Java Full Stack portfolio project.

## Stack
- Java 17
- Spring Boot 3.3.5
- Maven
- Spring Web
- Spring Data JPA
- H2 Database
- Apache PDFBox
- React + Vite

## Run Backend
Open a terminal in `backend`:

```powershell
mvn spring-boot:run
```

Backend: http://localhost:8080

## Run Frontend
Open another terminal in `frontend`:

```powershell
npm install
npm run dev
```

Frontend: http://localhost:5173

## First version
This version intentionally uses a demo keyword-matching analysis instead of a paid AI API. Real AI integration can be added after the complete local workflow works.

## Workflow
PDF/text resume -> Spring Boot -> PDFBox text extraction -> keyword matching -> H2 -> React results.
