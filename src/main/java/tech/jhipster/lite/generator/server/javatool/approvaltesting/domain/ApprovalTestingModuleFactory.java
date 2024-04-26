package tech.jhipster.lite.generator.server.javatool.approvaltesting.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;

import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.shared.error.domain.Assert;

public class ApprovalTestingModuleFactory {

  public JHipsterModule build(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return moduleBuilder(properties)
      .javaDependencies()
        .addTestDependency(groupId("com.approvaltests"), artifactId("approvaltests"), versionSlug("approvaltests"))
        .and()
      .build();
    //@formatter:on
  }
}
