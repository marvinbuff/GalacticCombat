import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.50"

  // json serialization
  id("kotlinx-serialization") version "1.3.50"

  // FXGL
  id("application")

  // Java FX
  id("org.openjfx.javafxplugin") version "0.0.8"
}


version = "SNAPSHOT"
group = "labs"

application {
  mainClassName = "galacticCombat.GalacticCombatAppKt"
}

repositories {
  mavenCentral()
  jcenter()
  maven("https://nexus.gluonhq.com/nexus/content/repositories/releases")
    maven("http://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
  api(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-RC")
  testImplementation("junit:junit:4.12")

  // For Json Serialization
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.11.1")
  compile("org.json:json:20190722")

  // FXGL
  compile("com.github.almasb:fxgl:11.7") // dev-SNAPSHOT
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

javafx {
  version = "12.0.2"
  modules = mutableListOf("javafx.controls", "javafx.fxml")
}