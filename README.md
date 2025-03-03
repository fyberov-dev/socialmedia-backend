
# iti0302-2024-project

![Static Badge](https://img.shields.io/badge/version-0.0.1--SNAPSHOT-brightgreen)

## Social Media

## Project introduction

Our social media platform is a modern community-driven application designed for users to engage in meaningful conversations, share posts, and participate in multiple group discussions, resembling features from platforms like Reddit.

### Key Features:
- Dynamic Chat System: Users can join and create multiple chats to discuss specific topics. Chats are managed by a creator and can have multiple participants. 
- Interactive Feed: Users can publish posts containing content, which other users can engage with through comments. 
- Role Management: The platform allows assigning specific roles to users, supporting administrative and moderation functionalities. 
- User-Centric Design: Each user has a personal profile with unique attributes, such as username, email, and secure password storage. 
- Community Engagement: Posts and comments form the backbone of community interactions, with timestamps for tracking the flow of discussions. 
- Scalable Structure: The database design ensures scalability, with many-to-many relationships (e.g., users and roles, users and chats) for flexible growth.


---
Backend:

* Spring Boot
* JPA
* PostgreSQL
* Liquibase
* Mockito

Frontend:

* Vue
* Vite
* Pinia
* Tailwind CSS
* DaisyUI
* Axios

## Pre requirement

- JDK
- GRADLE
- DOCKER

## Getting started

Clone the project to the localhost

```shell
git clone git@github.com:fyberov-dev/socialmedia-backend.git
```

### Startup using .jar file

Open the terminal and write next command to build the application:

```shell
./gradlew clean build
```

After successful build write next command to start the application:

```shell
java -jar ./build/libs/iti0302-project-[VERSION].jar
```

*change the '[VERSION]' to the current version of the application

### Startup using Docker

Write down next command to launch the project:

```shell
docker compose up --build
```
