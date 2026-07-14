# Quiz Assessment Platform Backend

A Spring Boot backend application for a Quiz Assessment Platform.

---

## 1. Local Setup

### Prerequisites
- Java 17
- Maven 3.x
- MySQL Database running locally on port `3306`

### Database Setup
1. Log in to your local MySQL instance.
2. Create the database named `quizdb`:
   ```sql
   CREATE DATABASE quizdb;
   ```

### Configuration
Local configurations are located in `src/main/resources/application.properties`. By default, it expects:
- **Host**: `localhost:3306`
- **Database**: `quizdb`
- **Username**: `root`
- **Password**: `Sairam@123`

To run locally with different settings, you can override them via environment variables:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `APP_JWT_SECRET`

### Execution
Run the Spring Boot application using Maven:
```powershell
.\mvnw.cmd spring-boot:run
```

---

## 2. How to Deploy on Render

This project contains a Render Blueprint configuration (`render.yaml`) that spins up a private MySQL container (with persistent storage) and the Spring Boot application built from the `Dockerfile`.

### Step-by-Step Deployment Guide

1. **Commit and Push**:
   Ensure all changes are pushed to your remote repository on GitHub/GitLab:
   ```bash
   git add -A
   git commit -m "Prepare for Render deployment"
   git push origin <your-branch-name>
   ```

2. **Access Render**:
   - Log in to your [Render Dashboard](https://dashboard.render.com).

3. **Deploy the Blueprint**:
   - Click **New** in the top-right corner and select **Blueprint**.
   - Connect your GitHub/GitLab account (if not already done).
   - Select your repository.
   - Choose the branch you want to deploy (e.g., `manohar`).

4. **Verify Properties**:
   - Render will automatically parse the `render.yaml` file.
   - It will automatically set up:
     - **`quiz-mysql`**: A private MySQL instance.
     - **`mysql-data`**: A 1GB persistent volume mounted to `/var/lib/mysql` to preserve database records across restarts.
     - **`quiz-platform-backend`**: The web service container running the Spring Boot app.
     - **`APP_JWT_SECRET`**: Automatically generated as a secure, random cryptokey.
   - Click **Apply** to deploy the services.

5. **Access Live API**:
   - Once the build succeeds, Render will provide a public URL for your web service (e.g., `https://quiz-platform-backend.onrender.com`).
   - The H2 console is disabled in production settings (MySQL database profile).

---

## 3. Database Persistence (Volumes)
The database uses a persistent disk volume configured via `render.yaml`. If the MySQL service is restarted or rebuilt:
- The persistent disk mounted at `/var/lib/mysql` holds the database data.
- Seeding data in `data.sql` is run when the database is empty or reinitialized.
