import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.kapt)
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

        val props = Properties()
        val localPropsFile = rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            props.load(localPropsFile.inputStream())
        }
        val mlToken = props.getProperty("ML_ACCESS_TOKEN") ?: ""
        val mlRefreshToken = props.getProperty("ML_REFRESH_TOKEN") ?: ""
        val mlClientId = props.getProperty("ML_CLIENT_ID") ?: ""
        val mlClientSecret = props.getProperty("ML_CLIENT_SECRET") ?: ""

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.material.components)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Dependency Injection with Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.moshi)
    implementation(libs.converter.scalars)
    implementation(libs.okhttp.logging.interceptor)
    
    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)
    
}