package tech.jhipster.lite.generator.server.springboot.mvc.sample.cassandrapersistence.infrastructure.primary;

import static tech.jhipster.lite.generator.slug.domain.JHLiteFeatureSlug.SAMPLE_PERSISTENCE;
import static tech.jhipster.lite.generator.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.mvc.sample.cassandrapersistence.application.SampleCassandraPersistenceApplicationService;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleOrganization;
import tech.jhipster.lite.module.domain.resource.JHipsterModulePropertiesDefinition;
import tech.jhipster.lite.module.domain.resource.JHipsterModuleResource;

@Configuration
class SampleCassandraPersistenceModuleConfiguration {

  @Bean
  JHipsterModuleResource sampleCassandraPersistenceModule(
    SampleCassandraPersistenceApplicationService sampleCassandraPersistenceApplicationService
  ) {
    return JHipsterModuleResource.builder()
      .slug(SAMPLE_CASSANDRA_PERSISTENCE)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().build())
      .apiDoc("Spring Boot - MVC", "Add Cassandra persistence for sample feature")
      .organization(
        JHipsterModuleOrganization.builder()
          .feature(SAMPLE_PERSISTENCE)
          .addDependency(SAMPLE_FEATURE)
          .addDependency(CASSANDRA_MIGRATION)
          .build()
      )
      .tags("server")
      .factory(sampleCassandraPersistenceApplicationService::buildModule);
  }
}
