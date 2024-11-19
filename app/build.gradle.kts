plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.google.devtools.ksp")
    id("kotlin-parcelize")

    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.tiuho22bangkit.gizi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tiuho22bangkit.gizi"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        buildConfigField("String", "BASE_URL", "\"https://newsapi.org/v2/\"")
        buildConfigField("String", "API_KEY", "\"a2c7c8f11d164471a4b66dc1696b5186\"")
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
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "11"
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // tflite
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite)    // tflite core (saran dari chatGPT) :v

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Glide
    implementation(libs.glide)

    ksp(libs.ksp)

    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // splash screen
    implementation(libs.androidx.core.splashscreen)

    // gambar bulat
    implementation(libs.circleimageview)
}