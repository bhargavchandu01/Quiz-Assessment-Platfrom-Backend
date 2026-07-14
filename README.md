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
To configure the application locally, create a `.env` file in the root directory. You can copy the template from `.env.example`:
```bash
cp .env.example .env
```

Open the `.env` file and set the properties to match your local setup:
- `SPRING_DATASOURCE_URL`: JDBC URL for your local MySQL database.
- `SPRING_DATASOURCE_USERNAME`: Your MySQL username.
- `SPRING_DATASOURCE_PASSWORD`: Your MySQL password.
- `ALLOWED_CORS_ORIGINS`: Comma-separated list of allowed frontend origins (e.g. `http://localhost:5173,https://quiz-assessment-frontend.vercel.app`).
- `APP_JWT_SECRET`: A secure Base64-encoded signing key for JWT tokens.

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

---

## 3. How to Deploy on Railway

Railway automatically detects the `Dockerfile` in the root of the project to build the Spring Boot application container.

### Step-by-Step Deployment Guide

1. **Create Database Service**:
   - Create a **MySQL** database service in your Railway project dashboard.
   - Railway will automatically provision the database and inject connection variables: `MYSQLHOST`, `MYSQLPORT`, `MYSQLUSER`, `MYSQLPASSWORD`, and `MYSQLDATABASE`.

2. **Deploy Application**:
   - Connect your GitHub repository to a new Railway Service.
   - Railway will build and deploy the container using the root `Dockerfile`.

3. **Configure Environment Variables**:
   - In the **Variables** tab of your Spring Boot service on Railway, add:
     - `ALLOWED_CORS_ORIGINS`: Set this to your production frontend URL (e.g. `https://quiz-assessment-frontend.vercel.app`).
     - `APP_JWT_SECRET`: A secure Base64-encoded secret key for signing tokens.

---

## 4. Database Persistence (Volumes)
The database uses a persistent disk volume configured via `render.yaml` on Render, or automated disk mounting on Railway. If the MySQL service is restarted or rebuilt:
- The persistent disk mounted at `/var/lib/mysql` holds the database data.
- Seeding data in `data.sql` is run when the database is empty or reinitialized.

