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
    gradleVersion = "6.8.1"
}

task("generateCode") {
    group = "build"
    tasks["compileJava"].dependsOn(this)
    doLast {
        //language=JAVA
        val code = """
            package org.arend.io;
            import org.jetbrains.annotations.NotNull;
            import java.nio.file.*;
            public class Generated {
              public static final @NotNull String ROOT = "${projectDir.parent.replace("\\", "\\\\")}";
              public static final @NotNull Path ROOT_PATH = Paths.get(ROOT);
            }
        """.trimIndent()
        file("src/main/java/org/arend/io/Generated.java").writeText(code)
    }
}

repositories {
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    val arendVersion = "v1.5.0"
    implementation("com.github.JetBrains.Arend:api:$arendVersion")
}
