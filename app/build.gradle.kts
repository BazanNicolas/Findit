import java.util.Properties

/**
 * Build configuration for the Products Android Application module.
 * 
 * This file configures the Android app module with all necessary plugins,
 * dependencies, and build settings for the Products application.
 */

plugins {
    alias(libs.plugins.android.application)      // Android application plugin
    alias(libs.plugins.kotlin.android)          // Kotlin support for Android
    alias(libs.plugins.kotlin.compose)          // Compose compiler optimizations
    alias(libs.plugins.dagger.hilt)             // Dependency injection with Hilt
    alias(libs.plugins.kotlin.kapt)            // Annotation processing for Hilt
}

android {
    namespace = "com.products.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.products.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Load MercadoLibre API credentials from local.properties file
        // These credentials are used for API authentication and should be kept secure
        val props = Properties()
        val localPropsFile = rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            props.load(localPropsFile.inputStream())
        }
        val mlToken = props.getProperty("ML_ACCESS_TOKEN") ?: ""
        val mlRefreshToken = props.getProperty("ML_REFRESH_TOKEN") ?: ""
        val mlClientId = props.getProperty("ML_CLIENT_ID") ?: ""
        val mlClientSecret = props.getProperty("ML_CLIENT_SECRET") ?: ""

        // Expose API credentials as BuildConfig fields for runtime access
        buildConfigField("String", "ML_ACCESS_TOKEN", "\"$mlToken\"")
        buildConfigField("String", "ML_REFRESH_TOKEN", "\"$mlRefreshToken\"")
        buildConfigField("String", "ML_CLIENT_ID", "\"$mlClientId\"")
        buildConfigField("String", "ML_CLIENT_SECRET", "\"$mlClientSecret\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    // Java compilation settings
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    // Kotlin compilation settings
    kotlinOptions {
        jvmTarget = "11"
    }

    // Enable build features
    buildFeatures {
        compose = true      // Enable Jetpack Compose
        buildConfig = true  // Enable BuildConfig generation for API credentials
    }
}

dependencies {
    // AndroidX Core Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Jetpack Compose UI Framework
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material.components)
    
    // Testing Dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.arch.core.testing)
    
    // Android Instrumentation Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    
    // Debug Tools
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Dependency Injection with Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Networking with Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.moshi)
    implementation(libs.converter.scalars)
    implementation(libs.okhttp.logging.interceptor)
    
    // JSON Serialization with Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)
    
    // Image Loading with Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    
    // Local Database with Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
}