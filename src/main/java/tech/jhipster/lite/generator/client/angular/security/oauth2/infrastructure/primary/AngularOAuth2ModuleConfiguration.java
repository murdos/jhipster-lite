package tech.jhipster.lite.generator.client.angular.security.oauth2.infrastructure.primary;

import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.ANGULAR_AUTHENTICATION;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.ANGULAR_CORE;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.ANGULAR_OAUTH_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.client.angular.security.oauth2.application.AngularOauth2ApplicationService;
import tech.jhipster.lite.module.domain.resource.*;

@Configuration
class AngularOAuth2ModuleConfiguration {

  @Bean
  JHipsterModuleResource angularOAuth2Module(AngularOauth2ApplicationService angularOAuth2) {
    return JHipsterModuleResource.builder()
      .slug(ANGULAR_OAUTH_2)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addIndentation().build())
      .apiDoc("Angular", "Add OAuth2 authentication")
      .organization(JHipsterModuleOrganization.builder().feature(ANGULAR_AUTHENTICATION).addDependency(ANGULAR_CORE).build())
      .tags("client", "angular")
      .factory(angularOAuth2::buildModule);
  }
}
