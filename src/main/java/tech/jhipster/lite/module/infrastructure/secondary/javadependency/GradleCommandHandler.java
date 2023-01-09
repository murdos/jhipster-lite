package tech.jhipster.lite.module.infrastructure.secondary.javadependency;

import org.apache.commons.lang3.NotImplementedException;
import tech.jhipster.lite.module.domain.Indentation;
import tech.jhipster.lite.module.domain.javabuild.command.AddBuildPluginManagement;
import tech.jhipster.lite.module.domain.javabuild.command.AddDirectJavaBuildPlugin;
import tech.jhipster.lite.module.domain.javabuild.command.AddDirectJavaDependency;
import tech.jhipster.lite.module.domain.javabuild.command.AddJavaDependencyManagement;
import tech.jhipster.lite.module.domain.javabuild.command.RemoveDirectJavaDependency;
import tech.jhipster.lite.module.domain.javabuild.command.RemoveJavaDependencyManagement;
import tech.jhipster.lite.module.domain.javabuild.command.SetVersion;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;

class GradleCommandHandler implements JavaDependenciesCommandHandler {

  GradleCommandHandler(Indentation indentation, JHipsterProjectFolder projectFolder) {}

  @Override
  public void handle(SetVersion command) {
    throw new NotImplementedException("Not yet implemented");
  }

  @Override
  public void handle(AddDirectJavaDependency command) {
    throw new NotImplementedException("Not yet implemented");
  }

  @Override
  public void handle(RemoveDirectJavaDependency command) {
    throw new NotImplementedException("Not yet implemented");
  }

  @Override
  public void handle(RemoveJavaDependencyManagement command) {
    throw new NotImplementedException("Not yet implemented");
  }

  @Override
  public void handle(AddJavaDependencyManagement command) {
    throw new NotImplementedException("Not yet implemented");
  }

  @Override
  public void handle(AddDirectJavaBuildPlugin command) {
    throw new NotImplementedException("Not yet implemented");
  }

  @Override
  public void handle(AddBuildPluginManagement command) {
    throw new NotImplementedException("Not yet implemented");
  }
}
