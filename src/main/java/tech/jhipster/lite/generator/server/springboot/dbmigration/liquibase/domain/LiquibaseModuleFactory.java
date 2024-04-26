package tech.jhipster.lite.generator.server.springboot.dbmigration.liquibase.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;
import static tech.jhipster.lite.module.domain.JHipsterModule.from;
import static tech.jhipster.lite.module.domain.properties.SpringConfigurationFormat.*;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.LogLevel;
import tech.jhipster.lite.module.domain.file.JHipsterSource;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class LiquibaseModuleFactory {

  private static final JHipsterSource SOURCE = from("server/springboot/dbmigration/liquibase");

  private static final String LIQUIBASE_SECONDARY = "wire/liquibase/infrastructure/secondary";

  private static final String LIQUIBASE = "liquibase";

  public JHipsterModule buildModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    String packagePath = properties.packagePath();

    //@formatter:off
    return moduleBuilder(properties)
      .context()
        .put("yamlSpringConfigurationFormat", properties.springConfigurationFormat() == YAML)
        .put("propertiesSpringConfigurationFormat", properties.springConfigurationFormat() == PROPERTIES)
      .and()
      .javaDependencies()
        .addDependency(groupId("org.liquibase"), artifactId("liquibase-core"), versionSlug(LIQUIBASE))
        .and()
      .springMainProperties()
        .set(propertyKey("spring.liquibase.change-log"), propertyValue("classpath:config/liquibase/master.xml"))
        .and()
      .files()
        .add(SOURCE.file("resources/master.xml"), to("src/main/resources/config/liquibase/master.xml"))
        .add(SOURCE.file("resources/0000000000_example.xml"), to("src/main/resources/config/liquibase/changelog/0000000000_example.xml"))
        .batch(SOURCE.append("main"), toSrcMainJava().append(packagePath).append(LIQUIBASE_SECONDARY))
          .addTemplate("AsyncSpringLiquibase.java")
          .addTemplate("LiquibaseConfiguration.java")
          .addTemplate("SpringLiquibaseUtil.java")
          .and()
        .batch(SOURCE.append("test"), toSrcTestJava().append(packagePath).append(LIQUIBASE_SECONDARY))
          .addTemplate("AsyncSpringLiquibaseTest.java")
          .addTemplate("LiquibaseConfigurationIT.java")
          .addTemplate("SpringLiquibaseUtilTest.java")
          .and()
        .and()
      .springMainLogger(LIQUIBASE, LogLevel.WARN)
      .springMainLogger("LiquibaseSchemaResolver", LogLevel.INFO)
      .springMainLogger("com.zaxxer.hikari", LogLevel.WARN)
      .springTestLogger(LIQUIBASE, LogLevel.WARN)
      .springTestLogger("LiquibaseSchemaResolver", LogLevel.INFO)
      .springTestLogger("com.zaxxer.hikari", LogLevel.WARN)
      .build();
    //@formatter:on
  }
}
