-- =========================================================
-- Quiz Platform - Seed Data
-- Password for all users: "password123" (BCrypt encoded)
-- =========================================================

-- Insert Roles via Users (roles stored as enum in User entity)
-- Admin User
INSERT INTO users (id, username, email, password, role, created_at)
VALUES (1, 'admin', 'admin@quizplatform.com',
        '$2a$10$VAX79z6MRu8WoyLvE3KVKewy0xgTOnZ/Js6.fc8OhuqYprvMIXzyy',
        'ADMIN', CURRENT_TIMESTAMP);

-- Teacher Users
INSERT INTO users (id, username, email, password, role, created_at)
VALUES (2, 'teacher1', 'teacher1@quizplatform.com',
        '$2a$10$VAX79z6MRu8WoyLvE3KVKewy0xgTOnZ/Js6.fc8OhuqYprvMIXzyy',
        'TEACHER', CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, created_at)
VALUES (3, 'teacher2', 'teacher2@quizplatform.com',
        '$2a$10$VAX79z6MRu8WoyLvE3KVKewy0xgTOnZ/Js6.fc8OhuqYprvMIXzyy',
        'TEACHER', CURRENT_TIMESTAMP);

-- Student Users
INSERT INTO users (id, username, email, password, role, created_at)
VALUES (4, 'student1', 'student1@quizplatform.com',
        '$2a$10$VAX79z6MRu8WoyLvE3KVKewy0xgTOnZ/Js6.fc8OhuqYprvMIXzyy',
        'STUDENT', CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, created_at)
VALUES (5, 'student2', 'student2@quizplatform.com',
        '$2a$10$VAX79z6MRu8WoyLvE3KVKewy0xgTOnZ/Js6.fc8OhuqYprvMIXzyy',
        'STUDENT', CURRENT_TIMESTAMP);

-- =========================================================
-- Quizzes
-- =========================================================
INSERT INTO quizzes (id, title, description, time_limit_minutes, passing_score, is_active, created_by_id, created_at)
VALUES (1, 'Java Fundamentals', 'Test your knowledge of core Java concepts', 30, 60, TRUE, 2, CURRENT_TIMESTAMP);

INSERT INTO quizzes (id, title, description, time_limit_minutes, passing_score, is_active, created_by_id, created_at)
VALUES (2, 'Spring Boot Basics', 'Assessment on Spring Boot framework fundamentals', 45, 70, TRUE, 2, CURRENT_TIMESTAMP);

INSERT INTO quizzes (id, title, description, time_limit_minutes, passing_score, is_active, created_by_id, created_at)
VALUES (3, 'Data Structures', 'Quiz on common data structures and algorithms', 60, 75, TRUE, 3, CURRENT_TIMESTAMP);

-- =========================================================
-- Questions for Quiz 1 (Java Fundamentals)
-- =========================================================
INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (1, 1, 'Which of the following is NOT a primitive data type in Java?',
        'int', 'String', 'boolean', 'char', 'B', 5, 1);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (2, 1, 'What is the default value of an int variable in Java?',
        '0', 'null', '1', 'undefined', 'A', 5, 2);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (3, 1, 'Which keyword is used to inherit a class in Java?',
        'implements', 'extends', 'inherits', 'super', 'B', 5, 3);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (4, 1, 'What does JVM stand for?',
        'Java Virtual Machine', 'Java Variable Method', 'Java Version Manager', 'Java Verified Module', 'A', 5, 4);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (5, 1, 'Which collection does NOT allow duplicate elements?',
        'ArrayList', 'LinkedList', 'HashSet', 'Vector', 'C', 5, 5);

-- =========================================================
-- Questions for Quiz 2 (Spring Boot Basics)
-- =========================================================
INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (6, 2, 'What annotation is used to mark a class as a Spring REST controller?',
        '@Controller', '@RestController', '@Service', '@Repository', 'B', 5, 1);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (7, 2, 'Which annotation enables auto-configuration in Spring Boot?',
        '@SpringBootApplication', '@EnableAutoConfig', '@AutoConfigure', '@SpringConfig', 'A', 5, 2);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (8, 2, 'What is the default port for a Spring Boot application?',
        '3000', '8080', '9090', '4040', 'B', 5, 3);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (9, 2, 'Which annotation injects dependencies in Spring?',
        '@Inject', '@Autowired', '@Resource', 'All of the above', 'D', 5, 4);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (10, 2, 'Which file is used for Spring Boot application configuration?',
        'config.xml', 'settings.json', 'application.properties', 'bootstrap.yaml only', 'C', 5, 5);

-- =========================================================
-- Questions for Quiz 3 (Data Structures)
-- =========================================================
INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (11, 3, 'What is the time complexity of binary search?',
        'O(n)', 'O(n²)', 'O(log n)', 'O(1)', 'C', 5, 1);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (12, 3, 'Which data structure uses LIFO order?',
        'Queue', 'Stack', 'LinkedList', 'Tree', 'B', 5, 2);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (13, 3, 'What is the worst case time complexity of QuickSort?',
        'O(n log n)', 'O(n)', 'O(n²)', 'O(log n)', 'C', 5, 3);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (14, 3, 'In a binary tree, a node with no children is called?',
        'Root', 'Parent', 'Leaf', 'Branch', 'C', 5, 4);

INSERT INTO questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks, question_order)
VALUES (15, 3, 'Which data structure is used for implementing BFS?',
        'Stack', 'Queue', 'Tree', 'Graph', 'B', 5, 5);

-- =========================================================
-- Reset sequences so new inserts don't collide with seeded IDs
-- =========================================================
ALTER TABLE users           AUTO_INCREMENT = 100;
ALTER TABLE quizzes         AUTO_INCREMENT = 100;
ALTER TABLE questions       AUTO_INCREMENT = 100;
ALTER TABLE results         AUTO_INCREMENT = 100;
ALTER TABLE student_answers AUTO_INCREMENT = 100;
