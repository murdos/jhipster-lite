package tech.jhipster.lite.generator.server.springboot.springcloud.consul.infrastructure.primary;

import static tech.jhipster.lite.shared.slug.domain.JHLiteFeatureSlug.SERVICE_DISCOVERY;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.CONSUL;
import static tech.jhipster.lite.shared.slug.domain.JHLiteModuleSlug.SPRING_BOOT_ACTUATOR;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.lite.generator.server.springboot.springcloud.consul.application.ConsulApplicationService;
import tech.jhipster.lite.module.domain.resource.*;

@Configuration
class ConsulModuleConfiguration {

  @Bean
  JHipsterModuleResource consulModule(ConsulApplicationService consulApplicationService) {
    return JHipsterModuleResource.builder()
      .slug(CONSUL)
      .propertiesDefinition(JHipsterModulePropertiesDefinition.builder().addProjectBaseName().build())
      .apiDoc("Spring Boot - Spring Cloud", "Add Spring Cloud Consul config and discovery")
      .organization(JHipsterModuleOrganization.builder().feature(SERVICE_DISCOVERY).addDependency(SPRING_BOOT_ACTUATOR).build())
      .tags("server", "spring", "spring-boot", "cloud")
      .factory(consulApplicationService::buildModule);
  }
}
