val projectArend = gradle.includedBuild("Arend")

plugins {
    java
}

group = "org.arend.io"
version = "0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Wrapper> {
    gradleVersion = "6.5"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.arend:api")
    testImplementation("junit", "junit", "4.12")
}
