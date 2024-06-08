package tech.jhipster.lite.generator.server.springboot.mvc.sample.feature.infrastructure.primary;

import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.CUCUMBER_AUTHENTICATION;
import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.SPRINGDOC;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.mvc.sample.feature.application.SampleFeatureApplicationService;
import tech.jhipster.lite.module.domain.resource.*;

@Configuration
class SampleFeatureModuleConfiguration {

  @Bean
  JHipsterModuleResource sampleFeatureModule(SampleFeatureApplicationService sample) {
    return JHipsterModuleResource.builder()
      .slug(SAMPLE_FEATURE)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().addIndentation().addProjectBaseName().build())
      .apiDoc("Spring Boot - MVC", "Add sample context with some APIs")
      .organization(
        JHipsterModuleOrganization.builder()
          .addDependency(CUCUMBER_AUTHENTICATION)
          .addDependency(SPRINGDOC)
          .addDependency(JAVA_BASE)
          .addDependency(KIPE_EXPRESSION)
          .addDependency(KIPE_AUTHORIZATION)
          .build()
      )
      .tags("server")
      .factory(sample::buildModule);
  }
}
