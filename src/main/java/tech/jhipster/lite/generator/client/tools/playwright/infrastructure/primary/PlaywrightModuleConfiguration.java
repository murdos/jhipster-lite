package tech.jhipster.lite.generator.client.tools.playwright.infrastructure.primary;

import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.CLIENT_CORE;
import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.FRONT_BROWSER_TEST;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.PLAYWRIGHT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.client.tools.playwright.application.PlaywrightApplicationService;
import tech.jhipster.lite.module.domain.resource.*;

@Configuration
class PlaywrightModuleConfiguration {

  @Bean
  JHipsterModuleResource playwrightModule(PlaywrightApplicationService playwright) {
    return JHipsterModuleResource.builder()
      .slug(PLAYWRIGHT)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().build())
      .apiDoc("E2e", "Add Playwright")
      .organization(JHipsterModuleOrganization.builder().feature(FRONT_BROWSER_TEST).addDependency(CLIENT_CORE).build())
      .tags("client", "test", "playwright", "e2e")
      .factory(playwright::buildModule);
  }
}
