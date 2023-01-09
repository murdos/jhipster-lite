package tech.jhipster.lite.module.infrastructure.secondary.javadependency;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tech.jhipster.lite.module.domain.JHipsterModulesFixture.javaDependenciesCommands;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.Indentation;
import tech.jhipster.lite.module.domain.javabuild.command.JavaBuildCommands;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;

@UnitTest
class FileSystemJavaBuildCommandsHandlerTest {

  private static final FileSystemJavaBuildCommandsHandler handler = new FileSystemJavaBuildCommandsHandler();

  @Test
  void shouldNotTryToHandleEmptyCommands() {
    assertThatCode(() ->
        handler.handle(Indentation.DEFAULT, new JHipsterProjectFolder("src/test/resources/projects/empty"), new JavaBuildCommands(null))
      )
      .doesNotThrowAnyException();
  }

  @Test
  void shouldNotHandleCommandsOnProjectWithoutMavenOrGradle() {
    assertThatThrownBy(() ->
        handler.handle(Indentation.DEFAULT, new JHipsterProjectFolder("src/test/resources/projects/empty"), javaDependenciesCommands())
      )
      .isExactlyInstanceOf(MissingJavaBuildConfigurationException.class);
  }
}
