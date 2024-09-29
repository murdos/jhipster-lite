package tech.jhipster.lite;

import java.util.stream.Stream;
import org.springframework.modulith.core.ApplicationModuleDetectionStrategy;
import org.springframework.modulith.core.JavaPackage;

public class HexagonalModuleDetectionStrategy implements ApplicationModuleDetectionStrategy {

  @Override
  public Stream<JavaPackage> getModuleBasePackages(JavaPackage basePackage) {
    var sharedKernels = basePackage.getSubPackagesAnnotatedWith(SharedKernel.class);
    var businessContexts = basePackage.getSubPackagesAnnotatedWith(BusinessContext.class);
    return Stream.concat(sharedKernels, businessContexts);
  }
}
