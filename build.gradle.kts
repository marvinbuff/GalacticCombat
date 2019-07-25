import io.morethan.jmhreport.gradle.task.JmhReportTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.41"
  id("me.champeau.gradle.jmh") version "0.5.0-rc-1"
  id("com.github.ben-manes.versions") version "0.21.0"
  id("io.morethan.jmhreport") version "0.9.0"
}

version = "SNAPSHOT"
group = "labs"

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  api(kotlin("stdlib"))
  api("it.unimi.dsi:fastutil:8.2.3")
  api("org.pcollections:pcollections:3.0.3")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-RC")
  testImplementation("junit:junit:4.12")

//  "jmhImplementation"("org.openjdk.jmh:jmh-core:1.21")
//  "jmhImplementation"("org.openjdk.jmh:jmh-generator-annprocess:1.21")
}

jmh {
  fork = 2
  jvmArgs = listOf("-Djmh.separateClasspathJAR=true")
  resultFormat = "JSON"
}

jmhReport {
  jmhResultPath = "build/reports/jmh/results.json"
  jmhReportOutput = "build/reports/jmh"
}

tasks {
  withType<JmhReportTask> {
    dependsOn("jmh")
  }

  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"

      freeCompilerArgs = freeCompilerArgs + listOf(
        "-Xuse-experimental=kotlin.Experimental"
      )
    }
  }
}

tasks.create<Zip>("zip") {
  description = "Archives sources in a zip file"
  group = "Archive"

  from("src")
  archiveName = "basic-demo-1.0.zip"
}