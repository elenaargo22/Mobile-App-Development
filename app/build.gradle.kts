plugins {
    alias(libs.plugins.android.application)
<<<<<<< HEAD
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.lab12"
=======
}

android {
    namespace = "com.example.lab9"
>>>>>>> 28f35cdad50f472f125f8dda9f63a355e96a5d97
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
<<<<<<< HEAD
        applicationId = "com.example.lab12"
=======
        applicationId = "com.example.lab9"
>>>>>>> 28f35cdad50f472f125f8dda9f63a355e96a5d97
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
<<<<<<< HEAD
            optimization {
                enable = false
            }
=======
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
>>>>>>> 28f35cdad50f472f125f8dda9f63a355e96a5d97
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
<<<<<<< HEAD
    buildFeatures {
        compose = true
=======

    buildFeatures{
        viewBinding = true
>>>>>>> 28f35cdad50f472f125f8dda9f63a355e96a5d97
    }
}

dependencies {
<<<<<<< HEAD
    implementation(libs.androidx.ui)
    implementation("org.osmdroid:osmdroid-android:6.1.20")
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
=======
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
>>>>>>> 28f35cdad50f472f125f8dda9f63a355e96a5d97
}