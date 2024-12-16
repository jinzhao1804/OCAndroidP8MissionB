plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")


}

android {
    namespace = "com.example.p8vitesse"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.p8vitesse"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation ("com.github.bumptech.glide:glide:4.12.0")  // Use the latest version
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("com.google.android.material:material:1.12.0")


    // ViewModel & LiveData
    implementation ("androidx.fragment:fragment-ktx:1.8.5")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7") // Add this line if not present
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7") // Optional, if you need LiveData


    // Room dependencies
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.49") // Align Hilt version
    kapt("com.google.dagger:hilt-compiler:2.49") // Align Hilt version

    // ViewPager2
    implementation("com.google.android.material:material:1.12.0") // TabLayout and Material UI
    implementation("androidx.recyclerview:recyclerview:1.3.2") // RecyclerView for lists
    implementation("androidx.viewpager2:viewpager2:1.1.0")     // ViewPager2 for tabs

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
