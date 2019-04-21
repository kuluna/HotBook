buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.0")
        classpath("com.google.firebase:firebase-plugins:1.2.0")
        classpath("com.google.gms:google-services:4.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.30")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task<Delete>("clean") {}
