import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.secrets.gradle)
}
val keyProperties = Properties().apply {
    load(File(rootDir, "key.properties").inputStream())
}


android {
    namespace = "com.giovanna.amatucci.foodbook"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.giovanna.amatucci.foodbook"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "BASE_URL", "\"platform.fatsecret.com\"")
        buildConfigField("String", "TOKEN_URL", "\"oauth.fatsecret.com\"")
        buildConfigField(
            "String", "DB_PASSPHRASE", "\"${keyProperties.getProperty("DB_PASSPHRASE")}\""
        )
        buildConfigField(
            "String",
            "FATSECRET_CLIENT_ID",
            "\"${keyProperties.getProperty("FATSECRET_CLIENT_ID")}\""
        )
        buildConfigField(
            "String",
            "FATSECRET_CLIENT_SECRET",
            "\"${keyProperties.getProperty("FATSECRET_CLIENT_SECRET")}\""
        )
    }

    buildTypes {
        release {
            applicationIdSuffix = ".release"
            versionNameSuffix = "-release"
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "DEBUG_MODE", "false")
            buildConfigField("Long", "REQUEST_TIMEOUT", "20_000L")
            buildConfigField("Long", "CONNECT_TIMEOUT", "15_000L")
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            buildConfigField("Boolean", "DEBUG_MODE", "true")
            buildConfigField("Long", "REQUEST_TIMEOUT", "30_000L")
            buildConfigField("Long", "CONNECT_TIMEOUT", "20_000L")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // ---------- 🧱 Core & Kotlin ----------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // ---------- 🧠 State, Lifecycle & ViewModel ----------
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // ---------- 💉 Dependency Injection (Koin) ----------
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // ---------- 🎨 Jetpack Compose UI ----------
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // ---------- 🗺️ Navigation ----------
    implementation(libs.androidx.navigation.compose)

    // ---------- 🌐 Networking (Ktor & Serialization) ----------
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.cio)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)

    // ---------- 🖼️ Image Loading ----------
    implementation(libs.coil.compose)
    implementation(libs.androidx.adapters)

    // ---------- 🧪 Testing ----------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.koin.test)

    // --- Logging (Timber) ---
    implementation(libs.timber)

    // --- Room ---
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // --- SQLCipher ---
    implementation(libs.sqlcipher)
    implementation(libs.androidx.sqlite)

    // --- Paging ---
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

}