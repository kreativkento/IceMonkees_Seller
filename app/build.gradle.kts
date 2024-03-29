plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // added plugins  -- Kento
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.icemonkees_seller"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.icemonkees_seller"
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

    // added extensions -- Kento
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-analytics-impl:18.0.4")
    implementation("com.android.support:support-annotations:28.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // added implementations -- Kento
        // firebase
        implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
        implementation("com.google.firebase:firebase-auth:22.3.1")
        implementation("com.google.firebase:firebase-database-ktx:20.3.1")
        implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
        implementation("com.google.firebase:firebase-analytics")
        // media
        implementation("androidx.media3:media3-common:1.3.0")
        // recycler view
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        // firestore
        implementation("com.google.firebase:firebase-firestore:24.11.0")
        implementation("com.google.firebase:firebase-firestore-ktx:24.11.0")
        // glide
        implementation("com.github.bumptech.glide:glide:4.13.0")
        // navigation
        implementation ("com.google.android.material:material:1.11.0")
        // kapt (Kotlin Annotation Processing Tool)
        kapt("com.github.bumptech.glide:compiler:4.13.0")

}