package tech.jhipster.lite;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.util.List;
import org.springframework.modulith.core.*;

public class HexagonalNamedInterfacesDetectionStrategy implements NamedInterfacesDetectionStrategy {

  @Override
  public NamedInterfaces getModuleNamedInterfaces(JavaPackage moduleBasePackage) {
    if (moduleBasePackage.getAnnotation(SharedKernel.class).isPresent()) {
      return NamedInterfaces.forOpen(moduleBasePackage);
    }

    //    moduleBasePackage
    //      .getDirectSubPackages()
    //      .stream()
    //      .filter(subPackage -> subPackage.getLocalName().equals("infrastructure"))
    //      .flatMap(subPackage -> subPackage.getDirectSubPackages().stream())
    //      .filter(subPackage -> subPackage.getLocalName().equals("primary"))
    //      .map(HexagonalNamedInterfacesDetectionStrategy::getTopLevelClassesRecursive)
    //      .map(classes -> NamedInterface.of(moduleBasePackage.getLocalName() + "::infrastructure::primary", classes))
    //      .var;
    return NamedInterfacesDetectionStrategy.explicitlyAnnotated().getModuleNamedInterfaces(moduleBasePackage);
  }

  private static List<? extends Class<?>> getTopLevelClassesRecursive(JavaPackage subPackage) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    try {
      return ClassPath.from(loader).getTopLevelClassesRecursive(subPackage.getName()).stream().map(ClassPath.ClassInfo::load).toList();
    } catch (IOException exception) {
      throw new IllegalStateException(exception);
    }
  }
}
