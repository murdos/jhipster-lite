package tech.jhipster.lite.module.domain.npm;

import tech.jhipster.lite.shared.error.domain.Assert;

public record NpmPackageVersion(String version) {
  public NpmPackageVersion {
    Assert.notBlank("version", version);
  }

  public String get() {
    return version();
  }

  public String majorVersion() {
    return version().split("\\.")[0];
  }
}
