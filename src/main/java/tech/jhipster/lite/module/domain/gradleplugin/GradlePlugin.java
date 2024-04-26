package tech.jhipster.lite.module.domain.gradleplugin;

import java.util.Optional;

public sealed interface GradlePlugin permits GradleMainBuildPlugin, GradleProfilePlugin {
  GradlePluginId id();
  Optional<GradlePluginConfiguration> configuration();
}
