import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

val keystorePropsFile = rootProject.file("release.properties")
val keystoreProps = Properties()

val hasValidSigningProps = keystorePropsFile.exists().also { exists ->
    if (exists) {
        FileInputStream(keystorePropsFile).use { keystoreProps.load(it) }
    }
}.let {
    listOf("storeFile", "storePassword", "keyAlias", "keyPassword").all { key ->
        keystoreProps[key] != null
    }
}

val isCiBuild = System.getenv("CI") == "true"

val releaseStoreFile = if (isCiBuild) {
    rootProject.file("release.keystore")
} else {
    rootProject.file(keystoreProps["storeFile"] as String)
}

val releaseStorePassword = if (isCiBuild) {
    System.getenv("KEYSTORE_PASSWORD")
} else {
    keystoreProps["storePassword"] as String
}

val releaseKeyAlias = if (isCiBuild) {
    System.getenv("KEY_ALIAS")
} else {
    keystoreProps["keyAlias"] as String
}

val releaseKeyPassword = if (isCiBuild) {
    System.getenv("KEY_PASSWORD")
} else {
    keystoreProps["keyPassword"] as String
}

android {
    namespace = "com.sdcgroup.speedmeter"
    compileSdk = 36

    lint {
        checkReleaseBuilds = false
    }

    signingConfigs {
        if (hasValidSigningProps || isCiBuild) {
            create("release") {
                storeFile = releaseStoreFile

                if (releaseStorePassword != null) {
                    storePassword = releaseStorePassword
                }

                if (releaseKeyAlias != null) {
                    keyAlias = releaseKeyAlias
                }

                if (releaseKeyPassword != null) {
                    keyPassword = releaseKeyPassword
                }
            }
        }
    }

    defaultConfig {
        applicationId = "com.sdcgroup.speedmeter"
        minSdk = 26
        targetSdk = 36
        versionCode = 12
        versionName = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes {
        release {
            if (hasValidSigningProps || isCiBuild) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable        = true
            isMinifyEnabled     = false
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures {
        compose = true
        buildConfig   = true
    }

    packaging {
        resources {
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            resources.excludes.add("META-INF/kotlinx_coroutines_core.version")
            resources.excludes.add("META-INF/*.version")
            resources.excludes.add("META-INF/proguard/**")
            resources.excludes.add("META-INF/*.kotlin_module")
            resources.excludes.add("kotlin/**")
            resources.excludes.add("DebugProbesKt.bin")
        }
    }
    bundle {
        language { enableSplit = true }
        density { enableSplit = true }
        abi { enableSplit = true }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
            "-opt-in=androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi"
        )
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.runtime.saveable)
    implementation(libs.collection.ktx)
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
