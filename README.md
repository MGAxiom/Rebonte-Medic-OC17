# Rebonnte - Pharmaceutical Inventory Management

Rebonnte is a modern Android application designed to help pharmacy staff and healthcare professionals manage their medicine inventory efficiently. This project was developed as part of the OpenClassrooms Android Developer program (Project 17), focusing on implementing a robust, scalable, and testable architecture using the latest Android technologies.

## 🌟 Key Features

*   **Secure Authentication**: Integrated Firebase Auth with support for Google Sign-In and traditional credentials.
*   **Medicine Catalog**: Comprehensive list of medicines with detailed information including descriptions, images, and categories.
*   **Aisle Organization**: Medicines are organized by physical location (aisles), making them easy to find in a real-world pharmacy setting.
*   **Advanced Search & Filtering**: Quickly find medicines by name or sort them based on various criteria.
*   **Personalized Profile**: User profile management including preferences.
*   **Dark Mode Support**: Fully responsive UI that adapts to system theme settings or manual overrides.
*   **Real-time Synchronization**: Powered by Cloud Firestore to ensure data is always up-to-date across devices.

## 🛠 Tech Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
*   **Dependency Injection**: [Koin](https://insert-koin.io/)
*   **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
*   **Networking & Backend**: [Firebase](https://firebase.google.com/) (Auth, Firestore, Storage)
*   **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
*   **Data Persistence**: [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
*   **Serialization**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)

## 🏗 Architecture

The project follows **Clean Architecture** principles combined with **MVVM (Model-View-ViewModel)** pattern:

*   **Data Layer**: Handles data sourcing from Firebase and local preferences.
*   **Domain Layer**: Contains business logic, use cases, and plain models (Usecase-driven).
*   **UI Layer**: State-driven UI using Jetpack Compose and ViewModels to manage UI state.

We emphasize **Unidirectional Data Flow (UDF)** to ensure the UI is a predictable reflection of the underlying state.

## 🧪 Quality & Testing

Quality is at the heart of Rebonnte. We use a comprehensive testing suite to ensure stability:

*   **Unit Tests**: Testing business logic and ViewModels using `JUnit`, `MockK`, and `Turbine` for Coroutines/Flows.
*   **Instrumentation Tests**: UI testing with `Espresso` and `Compose Test Library`.
*   **Code Coverage**: Integrated `Jacoco` to monitor test coverage.
*   **CI/CD**: Automatic analysis via `SonarCloud` to maintain high code quality standards.

## 🚀 Getting Started

### Prerequisites

*   Android Studio Ladybug (or newer)
*   JDK 17
*   A Firebase project (see below)

### Setup

1.  Clone the repository.
2.  Create a project in the [Firebase Console](https://console.firebase.google.com/).
3.  Add an Android app to your Firebase project with the package name `com.openclassrooms.rebonnte`.
4.  Download the `google-services.json` file and place it in the `app/` directory.
5.  Enable Email/Password and Google Sign-In in the Firebase Auth settings.
6.  Sync Gradle and run the project!

---

*This project is part of the OpenClassrooms Android Developer curriculum.*
