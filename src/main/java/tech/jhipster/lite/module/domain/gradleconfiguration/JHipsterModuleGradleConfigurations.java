package tech.jhipster.lite.module.domain.gradleconfiguration;

import java.util.ArrayList;
import java.util.Collection;
import tech.jhipster.lite.module.domain.JHipsterModule.JHipsterModuleBuilder;
import tech.jhipster.lite.module.domain.javabuild.command.AddGradleConfiguration;
import tech.jhipster.lite.module.domain.javabuild.command.JavaBuildCommands;
import tech.jhipster.lite.shared.error.domain.Assert;

public final class JHipsterModuleGradleConfigurations {

  private final Collection<GradleConfiguration> configurations;

  public JHipsterModuleGradleConfigurations(JHipsterModuleGradleConfigurationBuilder builder) {
    Assert.notNull("configurations", builder.configurations);

    this.configurations = builder.configurations;
  }

  public static JHipsterModuleGradleConfigurationBuilder builder(JHipsterModuleBuilder module) {
    return new JHipsterModuleGradleConfigurationBuilder(module);
  }

  public JavaBuildCommands buildChanges() {
    return new JavaBuildCommands(configurations.stream().map(configuration -> new AddGradleConfiguration(configuration.get())).toList());
  }

  public static final class JHipsterModuleGradleConfigurationBuilder {

    private final JHipsterModuleBuilder module;
    private final Collection<GradleConfiguration> configurations = new ArrayList<>();

    private JHipsterModuleGradleConfigurationBuilder(JHipsterModuleBuilder module) {
      Assert.notNull("module", module);

      this.module = module;
    }

    public JHipsterModuleGradleConfigurationBuilder configuration(String configuration) {
      configurations.add(new GradleConfiguration(configuration));

      return this;
    }

    public JHipsterModuleBuilder and() {
      return module;
    }

    public JHipsterModuleGradleConfigurations build() {
      return new JHipsterModuleGradleConfigurations(this);
    }
  }
}
