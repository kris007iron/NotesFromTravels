import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.travel_notes_mobile"
    compileSdk = 35

    defaultConfig {
        val secretPropertiesFile = rootProject.file("secret.properties")
        val secretProperties = Properties()
        if (secretPropertiesFile.exists()) {
            secretProperties.load(secretPropertiesFile.inputStream())
        }
        buildConfigField("String", "MAPS_API_KEY", "\"${secretProperties.getProperty("MAPS_API_KEY")}\"")
        manifestPlaceholders["MAPS_API_KEY"] = secretProperties.getProperty("MAPS_API_KEY")
        applicationId = "com.example.travel_notes_mobile"
        android.buildFeatures.buildConfig = true
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildFeatures {
            viewBinding = true
            dataBinding = true
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.google.android.material:material:1.9.0")
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.places)
    implementation(libs.play.services.maps)
    implementation (libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.gson.converter)
    implementation(libs.material.v190)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}