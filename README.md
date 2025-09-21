# Findit Android App

A modern Android application for searching and browsing products using the MercadoLibre API. Built with Jetpack Compose, Clean Architecture, and modern Android development practices.

## 🚀 Features

- **Product Search**: Search for products with real-time suggestions and autocomplete
- **Product Details**: View detailed product information including images, prices, and descriptions
- **Search History**: Keep track of previous searches for quick access
- **Recently Viewed**: Quick access to recently viewed products
- **Offline Support**: Cached data for offline browsing and improved performance
- **Modern UI**: Beautiful Material Design 3 interface with Jetpack Compose

## 🏗️ Architecture

This app follows **Clean Architecture** principles with clear separation of concerns:

### Presentation Layer
- **Jetpack Compose** for modern declarative UI
- **MVVM Pattern** with ViewModels managing UI state
- **StateFlow** for reactive state management
- **Navigation Compose** for type-safe navigation

### Domain Layer
- **Use Cases** encapsulating business logic
- **Repository Interfaces** defining data contracts
- **Domain Models** representing business entities

### Data Layer
- **Repository Implementations** coordinating data sources
- **Remote Data Source** using Retrofit for API calls
- **Local Data Source** using Room for offline storage
- **Data Mappers** converting between DTOs and domain models

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin** - Modern programming language for Android
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - Latest design system
- **AndroidX** - Android extension libraries

### Architecture Components
- **Hilt** - Dependency injection framework
- **Room** - Local database with SQLite abstraction
- **Navigation Compose** - Type-safe navigation
- **ViewModel** - UI-related data holder

### Networking
- **Retrofit** - HTTP client for REST API calls
- **OkHttp** - HTTP client implementation
- **Moshi** - JSON serialization/deserialization
- **Coil** - Image loading library

### Testing
- **JUnit** - Unit testing framework
- **MockK** - Mocking framework for Kotlin
- **Truth** - Fluent assertions
- **Turbine** - Flow testing utilities

## 📱 Screens

### Home Screen
- Search bar with autocomplete suggestions
- Recent search history
- Recently viewed products
- Quick access to search functionality

### Search Screen
- Search history management
- Autocomplete suggestions
- Search result previews

### Product Search Results
- Paginated product listings
- Infinite scroll support
- Product cards with thumbnails
- Search result filtering

### Product Detail Screen
- Detailed product information
- High-resolution images
- Price and availability
- Product specifications

## 🔧 Setup

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK API 24+ (Android 7.0)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Findit
   ```

2. **Configure API credentials**
   
   Create a `local.properties` file in the root directory:
   ```properties
   ML_ACCESS_TOKEN=your_mercadolibre_access_token
   ML_REFRESH_TOKEN=your_mercadolibre_refresh_token
   ML_CLIENT_ID=your_mercadolibre_client_id
   ML_CLIENT_SECRET=your_mercadolibre_client_secret
   ```

3. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

## 📦 Dependencies

### Core Libraries
- **AndroidX Core KTX** - Kotlin extensions for Android core
- **Lifecycle Runtime KTX** - Lifecycle-aware components
- **Activity Compose** - Compose integration for Activities

### UI Framework
- **Jetpack Compose BOM** - Ensures compatible Compose versions
- **Compose UI** - Core Compose UI components
- **Material 3** - Material Design 3 components
- **Navigation Compose** - Navigation component for Compose

### Dependency Injection
- **Hilt Android** - Dependency injection framework
- **Hilt Navigation Compose** - Hilt integration with Navigation

### Networking
- **Retrofit** - HTTP client for REST API calls
- **Moshi** - JSON serialization/deserialization
- **OkHttp** - HTTP client implementation

### Local Storage
- **Room Runtime** - SQLite database abstraction
- **Room KTX** - Kotlin extensions for Room

### Image Loading
- **Coil Compose** - Image loading for Compose
- **Coil Network OkHttp** - Network integration

## 🏛️ Project Structure

```
app/src/main/java/com/products/app/
├── core/                    # Core utilities and constants
│   ├── AppResult.kt        # Result wrapper for operations
│   ├── Constants.kt        # App-wide constants
│   └── NetworkErrorHandler.kt # Error handling utilities
├── data/                    # Data layer
│   ├── local/              # Local data sources
│   │   ├── dao/            # Room DAOs
│   │   ├── database/       # Room database
│   │   └── entity/         # Room entities
│   ├── mapper/             # Data mappers
│   ├── remote/             # Remote data sources
│   │   ├── dto/            # Data transfer objects
│   │   └── ProductsApi.kt   # API interfaces
│   └── repository/         # Repository implementations
├── di/                     # Dependency injection modules
├── domain/                 # Domain layer
│   ├── model/              # Domain models
│   ├── repository/         # Repository interfaces
│   └── usecase/            # Use cases
└── presentation/           # Presentation layer
    ├── common/             # Shared UI components
    ├── home/               # Home screen
    ├── productDetail/      # Product detail screen
    ├── productSearch/      # Product search screen
    └── search/             # Search screen
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions, please open an issue in the GitHub repository or contact the development team.

---

**Built with ❤️ using modern Android development practices**