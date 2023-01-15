package tech.jhipster.lite.module.infrastructure.secondary.javadependency.gradle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tech.jhipster.lite.module.domain.JHipsterModulesFixture.springBootVersion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.Indentation;
import tech.jhipster.lite.module.domain.javabuild.command.SetVersion;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyVersion;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;

@UnitTest
class GradleCommandHandlerTest {

  @Test
  void shouldHandleInvalidTomlVersionCatalog() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-unreadable");

    assertThatThrownBy(() -> new GradleCommandHandler(Indentation.DEFAULT, projectFolder))
      .isExactlyInstanceOf(InvalidTomlVersionCatalogException.class);
  }

  @Nested
  @DisplayName("Set dependency version")
  class HandleSetVersion {

    @Test
    void shouldAddVersionToMissingTomlVersionCatalogAnd() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder).handle(new SetVersion(springBootVersion()));

      assertThat(versionCatalogContent(projectFolder))
        .contains("[versions]")
        .contains("spring-boot = \"1.2.3\"");
    }

    @Test
    void shouldAddVersionToExistingTomlVersionCatalog() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder).handle(new SetVersion(springBootVersion()));

      assertThat(versionCatalogContent(projectFolder))
        .contains("[versions]")
        .contains("spring-boot = \"1.2.3\"");
    }

    @Test
    void shouldUpdateExistingProperty() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(Indentation.DEFAULT, projectFolder);
      gradleCommandHandler.handle(new SetVersion(new JavaDependencyVersion("jjwt", "0.12.0")));

      gradleCommandHandler.handle(new SetVersion(new JavaDependencyVersion("jjwt", "0.13.0")));

      assertThat(versionCatalogContent(projectFolder))
        .contains("[versions]")
        .contains("jjwt = \"0.13.0\"");
    }
  }

  private static JHipsterProjectFolder projectFrom(String sourceProject) {
    Path folder = Paths.get(TestFileUtils.tmpDirForTest());

    try {
      Files.createDirectories(folder);
    } catch (IOException e) {
      throw new AssertionError(e);
    }

    for (String buildFile : List.of("build.gradle.kts", "settings.gradle.kts")) {
      Path buildConfigPath = folder.resolve(buildFile);
      try {
        Files.copy(Paths.get(sourceProject), buildConfigPath);
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
    Path sourceTomlVersionCatalogPath = Paths.get(sourceProject).resolve("gradle").resolve("libs.versions.toml");
    if (Files.exists(sourceTomlVersionCatalogPath)) {
      try {
        Path gradleFolder = folder.resolve("gradle");
        Files.createDirectories(gradleFolder);
        Files.copy(sourceTomlVersionCatalogPath, gradleFolder.resolve("libs.versions.toml"));
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }

    return new JHipsterProjectFolder(folder.toString());
  }

  private static String buildGradleContent(JHipsterProjectFolder projectFolder) {
    return content(Paths.get(projectFolder.get()).resolve("build.gradle.kts"));
  }

  private static String versionCatalogContent(JHipsterProjectFolder projectFolder) {
    return content(Paths.get(projectFolder.get()).resolve("gradle/libs.versions.toml"));
  }

  private static String content(Path path) {
    try {
      return Files.readString(path);
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }
}
