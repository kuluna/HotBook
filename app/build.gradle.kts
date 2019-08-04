plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services") apply false
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "jp.kuluna.hotbook"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 11
        versionName = "1.0.10"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    dataBinding.isEnabled = true
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.41")
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime:2.0.0")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("com.google.android.material:material:1.0.0")
    implementation("androidx.exifinterface:exifinterface:1.0.0")
    implementation("androidx.preference:preference:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("com.google.firebase:firebase-core:17.0.1")
    implementation("com.google.firebase:firebase-perf:18.0.1")
    implementation("com.squareup.moshi:moshi:1.8.0")
    implementation("com.github.bumptech.glide:glide:4.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.6.1")
    implementation("com.squareup.retrofit2:retrofit:2.6.1")

    kapt("com.github.bumptech.glide:compiler:4.9.0")
    kapt("androidx.lifecycle:lifecycle-compiler:2.0.0")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

apply(plugin = "com.google.gms.google-services")
