package tech.jhipster.lite.generator.server.springboot.mvc.sample.mongopersistence.infrastructure.primary;

import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.SAMPLE_PERSISTENCE;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.mvc.sample.mongopersistence.application.SampleMongoDBPersistenceApplicationService;
import tech.jhipster.lite.module.domain.resource.*;

@Configuration
class SampleMongoDBPersistenceModuleConfiguration {

  @Bean
  JHipsterModuleResource sampleMongoDBModule(SampleMongoDBPersistenceApplicationService sampleMongoDBPersistence) {
    return JHipsterModuleResource.builder()
      .slug(SAMPLE_MONGODB_PERSISTENCE)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().build())
      .apiDoc("Spring Boot - MVC", "Add MongoDB persistence for sample feature")
      .organization(
        JHipsterModuleOrganization.builder().feature(SAMPLE_PERSISTENCE).addDependency(SAMPLE_FEATURE).addDependency(MONGOCK).build()
      )
      .tags("server")
      .factory(sampleMongoDBPersistence::buildModule);
  }
}
