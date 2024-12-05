// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath (libs.androidx.navigation.safe.args.gradle.plugin)
        //         classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.1")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}