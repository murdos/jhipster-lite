package tech.jhipster.lite.module.infrastructure.secondary.javadependency;

import tech.jhipster.lite.error.domain.ErrorKey;

enum JavaDependencyErrorKey implements ErrorKey {
  MALFORMED_ADDITIONAL_INFORMATION("malformed-java-dependency-additional-information"),
  INVALID_POM("invalid-pom-file"),
  MISSING_BUILD_CONFIGURATION("missing-java-build-configuration-files");

  private final String key;

  JavaDependencyErrorKey(String key) {
    this.key = key;
  }

  @Override
  public String get() {
    return key;
  }
}
