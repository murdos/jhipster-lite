package tech.jhipster.lite.generator.client.tools.cypress.infrastructure.primary;

import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.CLIENT_CORE;
import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.FRONT_BROWSER_TEST;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.CYPRESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.client.tools.cypress.application.CypressApplicationService;
import tech.jhipster.lite.module.domain.resource.*;

@Configuration
class CypressModuleConfiguration {

  @Bean
  JHipsterModuleResource cypressModule(CypressApplicationService cypress) {
    return JHipsterModuleResource.builder()
      .slug(CYPRESS)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addIndentation().build())
      .apiDoc("E2e", "Add Cypress")
      .organization(JHipsterModuleOrganization.builder().feature(FRONT_BROWSER_TEST).addDependency(CLIENT_CORE).build())
      .tags("client", "test", "cypress", "e2e")
      .factory(cypress::buildModule);
  }
}
