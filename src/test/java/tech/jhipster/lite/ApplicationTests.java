package tech.jhipster.lite;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ApplicationTests {

  @Test
  void writeDocumentationSnippets() {
    var modules = ApplicationModules.of(JHLiteApp.class).verify();

    modules.forEach(System.out::println);
  }
}
