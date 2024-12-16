// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.1")  // Use the correct version for your project
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")  // Use the correct Kotlin version

        // Hilt Plugin
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.49")  // Add the Hilt Gradle Plugin
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    // Make sure Hilt is applied globally if you're using plugin management
    // Hilt plugin should be declared here for consistency
}
