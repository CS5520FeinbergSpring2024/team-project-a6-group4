buildscript {
//    repositories {
//        google()
//        mavenCentral()
//        // Other repositories if needed
//    }
    dependencies {
        classpath(libs.google.services )
        //classpath 'com.android.tools.build:gradle:8.2.1'
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
}
//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        // Other repositories if needed
//    }
//}
//allprojects {
//    repositories {
//        // Other repositories
//        maven { url("https://jitpack.io") }
//    }
//}
