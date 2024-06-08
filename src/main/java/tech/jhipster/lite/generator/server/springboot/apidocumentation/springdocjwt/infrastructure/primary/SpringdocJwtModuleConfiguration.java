package tech.jhipster.lite.generator.server.springboot.apidocumentation.springdocjwt.infrastructure.primary;

import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.AUTHENTICATION_SPRINGDOC;
import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.SPRINGDOC;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.SPRINGDOC_JWT;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.SPRING_BOOT_JWT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.apidocumentation.springdocjwt.application.SpringdocJwtApplicationService;
import tech.jhipster.lite.module.domain.resource.*;

@Configuration
class SpringdocJwtModuleConfiguration {

  @Bean
  JHipsterModuleResource springdocJwtModule(SpringdocJwtApplicationService springdocJwt) {
    return JHipsterModuleResource.builder()
      .slug(SPRINGDOC_JWT)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addBasePackage().addIndentation().build())
      .apiDoc("Spring Boot - API Documentation", "Add JWT authentication for springdoc")
      .organization(
        JHipsterModuleOrganization.builder()
          .feature(AUTHENTICATION_SPRINGDOC)
          .addDependency(SPRINGDOC)
          .addDependency(SPRING_BOOT_JWT)
          .build()
      )
      .tags("server", "swagger", "springdoc")
      .factory(springdocJwt::buildModule);
  }
}
