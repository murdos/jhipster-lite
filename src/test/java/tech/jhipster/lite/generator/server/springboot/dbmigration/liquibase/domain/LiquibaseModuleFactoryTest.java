package tech.jhipster.lite.generator.server.springboot.dbmigration.liquibase.domain;

import static tech.jhipster.lite.TestFileUtils.*;
import static tech.jhipster.lite.module.domain.properties.SpringConfigurationFormat.*;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class LiquibaseModuleFactoryTest {

  private static final LiquibaseModuleFactory factory = new LiquibaseModuleFactory();

  @Test
  void shouldBuildModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
      .basePackage("com.jhipster.test")
      .build();

    JHipsterModule module = factory.buildModule(properties);

    assertThatModuleWithFiles(module, pomFile(), logbackFile(), testLogbackFile())
      .hasFile("pom.xml")
      .containing(
        """
            <dependency>
              <groupId>org.liquibase</groupId>
              <artifactId>liquibase-core</artifactId>
              <version>${liquibase.version}</version>
            </dependency>
        """
      )
      .and()
      .hasPrefixedFiles("src/main/resources/config/liquibase", "master.xml", "changelog/0000000000_example.xml")
      .hasPrefixedFiles(
        "src/main/java/com/jhipster/test/wire/liquibase/infrastructure/secondary",
        "AsyncSpringLiquibase.java",
        "LiquibaseConfiguration.java",
        "SpringLiquibaseUtil.java"
      )
      .hasPrefixedFiles(
        "src/test/java/com/jhipster/test/wire/liquibase/infrastructure/secondary",
        "AsyncSpringLiquibaseTest.java",
        "LiquibaseConfigurationIT.java",
        "SpringLiquibaseUtilTest.java"
      )
      .hasFile("src/test/resources/logback.xml")
      .containing("<logger name=\"liquibase\" level=\"WARN\" />")
      .containing("<logger name=\"LiquibaseSchemaResolver\" level=\"INFO\" />")
      .containing("<logger name=\"com.zaxxer.hikari\" level=\"WARN\" />")
      .and()
      .hasFile("src/main/resources/logback-spring.xml")
      .containing("<logger name=\"liquibase\" level=\"WARN\" />")
      .containing("<logger name=\"LiquibaseSchemaResolver\" level=\"INFO\" />")
      .containing("<logger name=\"com.zaxxer.hikari\" level=\"WARN\" />")
      .and()
      .hasFile("src/main/resources/config/application.yml")
      .containing(
        """
        spring:
          liquibase:
            change-log: classpath:config/liquibase/master.xml
        """
      );
  }

  @Test
  void shouldBuildModuleWithYamlSpringConfigurationFormat() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
      .basePackage("com.jhipster.test")
      .springConfigurationFormat(YAML)
      .build();

    JHipsterModule module = factory.buildModule(properties);

    assertThatModuleWithFiles(module, pomFile(), logbackFile(), testLogbackFile())
      .hasFile("src/test/java/com/jhipster/test/wire/liquibase/infrastructure/secondary/SpringLiquibaseUtilTest.java")
      .containing("YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();")
      .notContaining("Properties properties = new Properties();");
  }

  @Test
  void shouldBuildModuleWithPropertiesSpringConfigurationFormat() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
      .basePackage("com.jhipster.test")
      .springConfigurationFormat(PROPERTIES)
      .build();

    JHipsterModule module = factory.buildModule(properties);

    assertThatModuleWithFiles(module, pomFile(), logbackFile(), testLogbackFile())
      .hasFile("src/test/java/com/jhipster/test/wire/liquibase/infrastructure/secondary/SpringLiquibaseUtilTest.java")
      .containing("Properties properties = new Properties();")
      .notContaining("YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();");
  }
}
