import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import kotlin.toString

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
}


// Load local.properties, for using in local, But on github actions we will directly use env variables
val localProperties = Properties()
val localPropsFile = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    localProperties.load(localPropsFile.inputStream())
}

buildkonfig {
    packageName = "app.linksnapkmp"

    // Expose object name and make it public to make available in other modules
    exposeObjectWithName = "BuildKonfig"
    defaultConfigs {
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "BASE_URL",
            "http://192.168.1.6:8080/"
        )
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "WEB_CLIENT_ID",
            System.getenv("LINKSNAP_WEB_CLIENT_ID")
                ?: localProperties["LINKSNAP_WEB_CLIENT_ID"].toString()
        )
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "WEB_CLIENT_SECRET",
            System.getenv("LINKSNAP_WEB_CLIENT_SECRET")
                ?: localProperties["LINKSNAP_WEB_CLIENT_SECRET"].toString()
        )
    }
}


kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    androidLibrary {
        namespace = "app.linksnapkmp.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {

        commonMain.dependencies {
            api(projects.core)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)


            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)

            // Settings
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.serialization)

            // Koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)


            // Navigation
            implementation(libs.navigation.compose)

            // Image Loading
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // Icons
            implementation(libs.compose.materialIconsCore)
            implementation(libs.compose.materialIconsExtended)

            // KMAuth
            api(libs.kmauth.google)
            implementation(libs.kmauth.google.compose)
        }

        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.activity.compose)
            api(libs.koin.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}