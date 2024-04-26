plugins {
  java
  checkstyle
  alias(libs.plugins.jib)
  alias(libs.plugins.protobuf)
  // jhipster-needle-gradle-plugin
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

checkstyle {
  toolVersion = libs.versions.checkstyle.get()
}

// jhipster-needle-gradle-project-extension-plugin-configuration

repositories {
  mavenCentral()
  // jhipster-needle-gradle-repositories
}

group = "tech.jhipster.lite"
version = "0.0.1-SNAPSHOT"

// jhipster-needle-profile-activation

dependencies {
  implementation(libs.checkstyle)
  // jhipster-needle-gradle-implementation-dependencies
  // jhipster-needle-gradle-compile-dependencies
  // jhipster-needle-gradle-runtime-dependencies
  // jhipster-needle-gradle-test-dependencies
}
