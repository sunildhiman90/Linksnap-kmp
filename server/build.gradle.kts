plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)

    alias(libs.plugins.kotlinSerialization)
}

group = "app.linksnap"
version = "1.0.0"
application {
    mainClass = "app.linksnap.ApplicationKt"
}

dependencies {
    api(projects.core)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.serverAuth)
    implementation(libs.ktor.serverAuthJwt)
    implementation(libs.ktor.serverCors)
    implementation(libs.ktor.serialization.json)

    // Database
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)

    // Required for Automatic Migration of db
    implementation(libs.exposed.migration.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.mysql.connector)
    implementation(libs.hikari)

    implementation(libs.jsoup)
    implementation(libs.google.genai)

    // DI
    implementation(libs.koin.core)

    //implementation(libs.mysql.connector.java.v840)

    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}