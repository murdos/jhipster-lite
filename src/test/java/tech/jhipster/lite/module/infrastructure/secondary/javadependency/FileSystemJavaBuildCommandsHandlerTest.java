package tech.jhipster.lite.module.infrastructure.secondary.javadependency;

import static org.assertj.core.api.Assertions.*;
import static tech.jhipster.lite.TestFileUtils.*;
import static tech.jhipster.lite.module.domain.JHipsterModulesFixture.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.Indentation;
import tech.jhipster.lite.module.domain.file.TemplateRenderer;
import tech.jhipster.lite.module.domain.javabuild.command.JavaBuildCommands;
import tech.jhipster.lite.module.domain.javabuild.command.SetVersion;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;
import tech.jhipster.lite.module.infrastructure.secondary.FileSystemProjectFiles;
import tech.jhipster.lite.module.infrastructure.secondary.javabuild.FileSystemProjectJavaBuildToolRepository;

@UnitTest
class FileSystemJavaBuildCommandsHandlerTest {

  private static final FileSystemJavaBuildCommandsHandler handler = new FileSystemJavaBuildCommandsHandler(
    new FileSystemProjectJavaBuildToolRepository(),
    new FileSystemProjectFiles(),
    TemplateRenderer.NOOP
  );

  @Test
  void shouldNotTryToHandleEmptyCommands() {
    assertThatCode(
      () -> handler.handle(Indentation.DEFAULT, projectFrom("src/test/resources/projects/empty"), new JavaBuildCommands(null))
    ).doesNotThrowAnyException();
  }

  @Test
  void shouldNotHandleCommandsOnProjectWithoutMavenOrGradle() {
    assertThatThrownBy(
      () -> handler.handle(Indentation.DEFAULT, projectFrom("src/test/resources/projects/empty"), javaDependenciesCommands())
    ).isExactlyInstanceOf(MissingJavaBuildConfigurationException.class);
  }

  @Test
  void shouldHandleCommandsWithMavenOnProjectWithPom() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-maven");
    handler.handle(Indentation.DEFAULT, projectFolder, new JavaBuildCommands(List.of(new SetVersion(springBootVersion()))));
    assertThat(content(projectFolder.filePath("pom.xml"))).contains("<spring-boot.version>1.2.3</spring-boot.version>");
  }

  @Test
  void shouldHandleCommandsWithGradleOnProjectWithBuildGradle() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");
    handler.handle(Indentation.DEFAULT, projectFolder, new JavaBuildCommands(List.of(new SetVersion(springBootVersion()))));
    assertThat(content(projectFolder.filePath("gradle/libs.versions.toml"))).contains("spring-boot = \"1.2.3\"");
  }
}
