package tech.jhipster.lite.module.infrastructure.secondary;

import static org.assertj.core.api.Assertions.*;
import static tech.jhipster.lite.TestFileUtils.*;
import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;

@UnitTest
class YamlFileSpringPropertiesHandlerTest {

  public static final Path EXISTING_SPRING_CONFIGURATION = Paths.get(
    "src/test/resources/projects/project-with-spring-application-yaml/application.yml"
  );

  @Test
  void shouldCreateUnknownFile() {
    Path yamlFile = Paths.get(TestFileUtils.tmpDirForTest(), "src/main/resources/application.yml");
    YamlFileSpringPropertiesHandler handler = new YamlFileSpringPropertiesHandler(yamlFile);

    handler.set(propertyKey("springdoc.swagger-ui.operationsSorter"), propertyValue("alpha"));

    assertThat(content(yamlFile))
      .contains(
        """
        springdoc:
          swagger-ui:
            operationsSorter: alpha
        """
      );
  }

  @Test
  void shouldAppendPropertyToFileWithProperties() {
    Path yamlFile = Paths.get(TestFileUtils.tmpDirForTest(), "src/main/resources/application.yml");
    loadDefaultProperties(EXISTING_SPRING_CONFIGURATION, yamlFile);
    YamlFileSpringPropertiesHandler handler = new YamlFileSpringPropertiesHandler(yamlFile);

    handler.set(propertyKey("springdoc.swagger-ui.operationsSorter"), propertyValue("alpha"));

    assertThat(content(yamlFile))
      .contains(
        """
        spring:
          application:
            name: JHLite
        logging:
          level:
            tech.jhipster.lite: INFO
        springdoc:
          swagger-ui:
            operationsSorter: alpha
        """
      );
  }

  @Test
  void shouldReplaceExistingProperty() {
    Path yamlFile = Paths.get(TestFileUtils.tmpDirForTest(), "src/main/resources/application.yml");
    loadDefaultProperties(EXISTING_SPRING_CONFIGURATION, yamlFile);
    YamlFileSpringPropertiesHandler handler = new YamlFileSpringPropertiesHandler(yamlFile);

    handler.set(propertyKey("spring.application.name"), propertyValue("alpha"));

    assertThat(content(yamlFile))
      .contains(
        """
        spring:
          application:
            name: alpha
        """
      )
      .doesNotContain(
        """
        spring:
          application:
            name: JHLite
        """
      );
  }

  @Test
  void shouldHandleEscapedKeyWithDot() {
    Path yamlFile = Paths.get(TestFileUtils.tmpDirForTest(), "src/main/resources/application.yml");
    YamlFileSpringPropertiesHandler handler = new YamlFileSpringPropertiesHandler(yamlFile);

    handler.set(
      propertyKey("kafka.consumer.'[key.deserializer]'"),
      propertyValue("org.apache.kafka.common.serialization.StringDeserializer")
    );

    assertThat(content(yamlFile))
      .contains(
        """
        kafka:
          consumer:
            '[key.deserializer]': org.apache.kafka.common.serialization.StringDeserializer
        """
      );
  }
}
