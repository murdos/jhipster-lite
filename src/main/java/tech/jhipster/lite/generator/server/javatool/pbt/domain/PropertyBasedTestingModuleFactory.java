package tech.jhipster.lite.generator.server.javatool.pbt.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class PropertyBasedTestingModuleFactory {

  public JHipsterModule build(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return moduleBuilder(properties)
      .documentation(documentationTitle("Property Based Testing"), from("server/javatool/pbt/property-based-testing.md"))
      .javaDependencies()
        .addTestDependency(groupId("net.jqwik"), artifactId("jqwik"), versionSlug("jqwik"))
        .and()
      .build();
    //@formatter:on
  }
}
