# Java Quiz Application with NIO

## Description
This project is a **Java-based Quiz Application** that uses **Java NIO (Non-blocking I/O)** to implement a server-client architecture. The application allows multiple clients to connect to the server, participate in quizzes, and receive real-time feedback. It demonstrates efficient network programming and interactive quiz functionality.

---

## Features
- **Server-Side**:
  - Handles multiple clients concurrently using non-blocking I/O.
  - Manages quiz questions and client scores.
  - Broadcasts real-time updates to connected clients.

- **Client-Side**:
  - Connects to the server to participate in the quiz.
  - Displays quiz questions and collects answers from users.
  - Receives and displays feedback and scores in real-time.

- **Quiz Functionality**:
  - Supports multiple-choice and true/false questions.
  - Maintains user scores throughout the session.
  - Provides a leaderboard for all participants.

---

## Requirements
- **Java Version**: 11 or higher
- **Dependencies**: 
  - None (Standard Java libraries only)

---

## Setup Instructions

### Server
1. Navigate to the `server` directory:
    ```bash
    cd server
    ```

2. Compile the server code:
    ```bash
    javac QuizServer.java
    ```

3. Run the server:
    ```bash
    java Server
    ```

### Client
1. Navigate to the `client` directory:
    ```bash
    cd client
    ```

2. Compile the client code:
    ```bash
    javac Client.java
    ```

3. Run the client:
    ```bash
    java QuizClient
    ```

---

## Usage

1. **Start the Server**: Run the server application to listen for incoming client connections.
2. **Connect Clients**: Run one or more client applications to join the quiz.
3. **Participate in the Quiz**:
    - Clients will receive quiz questions from the server.
    - Submit answers through the client interface.
    - View feedback and live scores after each question.

---

## File Structure
