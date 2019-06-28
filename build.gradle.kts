import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.40"
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-M1")
  testImplementation("junit:junit:4.12")
}

allprojects {
  version = "1.0-SNAPSHOT"

  repositories {
    mavenCentral()
    jcenter()
  }

  tasks {
    withType<KotlinCompile> {

      kotlinOptions {
        jvmTarget = "1.8"

        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xuse-experimental=kotlin.Experimental"
        )
      }
    }
  }
}

tasks.create<Zip>("zip") {
  description = "Archives sources in a zip file"
  group = "Archive"

  from("src")
  archiveName = "basic-demo-1.0.zip"
}