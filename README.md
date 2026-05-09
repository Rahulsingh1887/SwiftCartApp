SwiftCart 🛒

SwiftCart is a full-stack e-commerce web application built using Spring Boot, React.js, and MySQL.
The project provides a modern online shopping experience with features like product browsing, cart management, order placement, authentication, and admin product management.

🚀 Features

👤 User Features
User Registration & Login
JWT Authentication & Authorization
Browse Products
Search & Filter Products
Add to Cart
Update Cart Quantity
Place Orders
View Order History
Responsive UI

🛠️ Admin Features

Add Products
Update Products
Delete Products
Manage Inventory
View Orders

🏗️ Tech Stack

Frontend
React.js
Axios
React Router
CSS / Bootstrap
Backend
Spring Boot
Spring Security
JWT Authentication
REST APIs
Hibernate / JPA
Database
MySQL

Project Structure
SwiftCart/
frontend/          # React Frontend
backend/           # Spring Boot Backend
README.md


⚙️ Installation & Setup

1️⃣ Clone Repository
git clone https://github.com/Rahulsingh1887/SwiftCart.git

2️⃣ Backend Setup
Navigate to backend folder
cd backend
Configure MySQL Database

Update application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/swiftcart
spring.datasource.username=root
spring.datasource.password=yourpassword
Run Backend
mvn spring-boot:run

Backend runs on: http://localhost:8080

3️⃣ Frontend Setup
Navigate to frontend folder
cd frontend
Install Dependencies
npm install
Start Frontend
npm start

Frontend runs on:

http://localhost:3000
🔐 Authentication

SwiftCart uses JWT (JSON Web Token) based authentication for secure login and protected APIs.

🌟 Future Improvements
Payment Gateway Integration
Wishlist Feature
Product Reviews & Ratings
Email Notifications
Deployment on AWS
🤝 Contributing

Contributions are welcome.
Feel free to fork the repository and submit pull requests.
