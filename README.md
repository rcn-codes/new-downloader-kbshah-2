Multi-Platform Social Downloader (MVI)
🎓 Learning Purpose
Note: This project was developed strictly for educational and learning purposes. The goal was to master the MVI (Model-View-Intent) architecture, asynchronous networking with Ktor, and dependency injection using Koin. This is a portfolio piece designed to demonstrate clean code practices and modern Android development workflows.

🚀 Overview
A high-performance social media downloader built with Jetpack Compose. The application allows users to fetch media from various platforms while maintaining a predictable state and a smooth user experience through reactive programming.

🛠 Tech Stack & Architecture
This project implements the MVI (Model-View-Intent) architectural pattern to ensure a unidirectional data flow and easy debugging.

UI: Jetpack Compose (Declarative UI)

Networking: Ktor Client (Asynchronous requests & streaming)

Dependency Injection: Koin (Lightweight & pragmatic DI)

Concurrency: Kotlin Coroutines & Flow

Architecture: MVI Pattern (State, Intent, Effect)

Language: 100% Kotlin

✨ Key Features
Reactive State Management: Uses MVI to handle complex download states (Idle, Downloading, Success, Error).

Real-time Progress: Leverages Ktor’s onDownload hooks to provide live progress bar updates.

Dependency Decoupling: Koin manages the lifecycle of the Ktor client and ViewModels.

Asynchronous Processing: Efficient background threading to ensure the UI remains responsive during heavy downloads.

📂 Project Structure
data/: Ktor API implementation and Repository logic.

di/: Koin modules for network and viewmodel injection.

ui/: Compose screens, components, and themes.

mvi/: State, Intent, and ViewModel definitions.

⚖️ Disclaimer
This app is intended for personal use and learning. Users should respect the terms of service of the respective social media platforms. I do not encourage the downloading of copyrighted content without permission.

How to use this in your GitHub:
Repository Title: Social-Downloader-MVI-Ktor

Short Description (About Section): > "A practice Android project building a Social Media Downloader using Jetpack Compose, Koin, Ktor, and MVI architecture. Focused on clean code and reactive state management."

Topics/Tags: kotlin, android, jetpack-compose, mvi-architecture, ktor-client, koin-di.
