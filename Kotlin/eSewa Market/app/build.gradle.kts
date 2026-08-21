import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"

    id("com.google.gms.google-services")

    alias(libs.plugins.ksp)
    alias(libs.plugins.google.firebase.crashlytics)

    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.example.eSewaMarket"
    compileSdk = 36

    packaging {
        resources {
            excludes.add("/META-INF/INDEX.LIST")
            excludes.add("/META-INF/DEPENDENCIES")
            excludes.add("/META-INF/io.netty.versions.properties")
        }
    }

    defaultConfig {
        applicationId = "com.example.eSewaMarket"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures{
            viewBinding = true
            compose = true
        }
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.googleid)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.appdistribution.gradle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.google.android.flexbox:flexbox:3.0.0")

    implementation("androidx.core:core-splashscreen:1.0.0")

    implementation("androidx.viewpager2:viewpager2:1.1.0")

    implementation("androidx.paging:paging-runtime-ktx:3.3.6")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    implementation(platform("com.google.firebase:firebase-bom:34.16.0"))
    implementation("com.google.firebase:firebase-analytics")

    val room_version = "3.0.1"
    implementation("androidx.room3:room3-runtime:$room_version")
    ksp("androidx.room3:room3-compiler:$room_version")

    //Expandable textview third part library
    implementation("at.blogc:expandabletextview:1.0.5")

    implementation(platform("androidx.compose:compose-bom:2026.06.00"))

    implementation("androidx.activity:activity-compose:1.13.0")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    debugImplementation("androidx.compose.ui:ui-tooling")

    // Coil setup
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

    // To use constraintlayout in compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.2")
}