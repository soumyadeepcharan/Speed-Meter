buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.21")
  }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
