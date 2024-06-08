package tech.jhipster.lite.generator.server.springboot.dbmigration.flyway.infrastructure.primary;

import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.DATABASE_MIGRATION;
import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.JPA_PERSISTENCE;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.dbmigration.flyway.application.FlywayApplicationService;
import tech.jhipster.lite.module.domain.resource.*;

@Configuration
class FlywayModuleConfiguration {

  private static final String API_DOC_GROUP = "Spring Boot - Database Migration";

  @Bean
  JHipsterModuleResource flywayInitializationModule(FlywayApplicationService flyway) {
    return JHipsterModuleResource.builder()
      .slug(FLYWAY)
      .propertiesDefinition(
        JHipsterModulePropertiesDefinition.builder().addBasePackage().addIndentation().addSpringConfigurationFormat().build()
      )
      .apiDoc(API_DOC_GROUP, "Add Flyway")
      .organization(JHipsterModuleOrganization.builder().feature(DATABASE_MIGRATION).addDependency(JPA_PERSISTENCE).build())
      .tags(flywayTags())
      .factory(flyway::buildInitializationModule);
  }

  @Bean
  JHipsterModuleResource flywayMysqlModule(FlywayApplicationService flyway) {
    return JHipsterModuleResource.builder()
      .slug(FLYWAY_MYSQL)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().addIndentation().build())
      .apiDoc(API_DOC_GROUP, "Add Flyway MySQL")
      .organization(JHipsterModuleOrganization.builder().addDependency(FLYWAY).addDependency(MYSQL).build())
      .tags(flywayTags())
      .factory(flyway::buildMysqlDependencyModule);
  }

  @Bean
  JHipsterModuleResource flywayMariaDBModule(FlywayApplicationService flyway) {
    return JHipsterModuleResource.builder()
      .slug(FLYWAY_MARIADB)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().addIndentation().build())
      .apiDoc(API_DOC_GROUP, "Add Flyway MariaDB")
      .organization(JHipsterModuleOrganization.builder().addDependency(FLYWAY).addDependency(MARIADB).build())
      .tags(flywayTags())
      .factory(flyway::buildMysqlDependencyModule);
  }

  @Bean
  JHipsterModuleResource flywayPostgresModule(FlywayApplicationService flyway) {
    return JHipsterModuleResource.builder()
      .slug(FLYWAY_POSTGRESQL)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().addIndentation().build())
      .apiDoc(API_DOC_GROUP, "Add Flyway PostgreSQL")
      .organization(JHipsterModuleOrganization.builder().addDependency(FLYWAY).addDependency(POSTGRESQL).build())
      .tags(flywayTags())
      .factory(flyway::buildPostgresqlDependencyModule);
  }

  @Bean
  JHipsterModuleResource flywayMsSqlServerModule(FlywayApplicationService flyway) {
    return JHipsterModuleResource.builder()
      .slug(FLYWAY_MSSQL)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().addIndentation().build())
      .apiDoc(API_DOC_GROUP, "Add Flyway PostgreSQL")
      .organization(JHipsterModuleOrganization.builder().addDependency(FLYWAY).addDependency(MSSQL).build())
      .tags(flywayTags())
      .factory(flyway::buildMsSqlServerDependencyModule);
  }

  private String[] flywayTags() {
    return new String[] { "server", "spring", "spring-boot", "database", "migration" };
  }
}
