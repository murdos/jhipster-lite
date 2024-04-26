package tech.jhipster.lite.generator.server.springboot.mvc.sample.feature.domain;

import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.assertThatModuleWithFiles;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.pomFile;

import org.junit.jupiter.api.Test;
import tech.jhipster.lite.TestFileUtils;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
class SampleFeatureModuleFactoryTest {

  private static final SampleFeatureModuleFactory factory = new SampleFeatureModuleFactory();

  @Test
  void shouldBuildModule() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(TestFileUtils.tmpDirForTest())
      .basePackage("com.jhipster.test")
      .build();

    JHipsterModule module = factory.buildModule(properties);

    assertThatModuleWithFiles(module, pomFile())
      .hasFile("pom.xml")
      .and()
      .hasFiles("documentation/sample.md")
      .hasPrefixedFiles("src/main/java/com/jhipster/test/sample", "package-info.java")
      .hasPrefixedFiles(
        "src/main/java/com/jhipster/test/sample/application",
        "BeersApplicationService.java",
        "BeerIdAccessChecker.java",
        "BeerResource.java",
        "BeersAccessesConfiguration.java",
        "BeerToCreateAccessChecker.java"
      )
      .hasPrefixedFiles(
        "src/main/java/com/jhipster/test/sample/domain/beer",
        "Beer.java",
        "BeerName.java",
        "Beers.java",
        "BeersCreator.java",
        "BeerSellingState.java",
        "BeersRemover.java",
        "BeersRepository.java",
        "BeerToCreate.java",
        "UnknownBeerException.java"
      )
      .hasPrefixedFiles("src/main/java/com/jhipster/test/sample/domain/order", "BeerOrder.java", "BeerOrderLine.java", "OrderedBeer.java")
      .hasPrefixedFiles("src/main/java/com/jhipster/test/sample/domain", "Amount.java", "BeerId.java")
      .hasPrefixedFiles(
        "src/main/java/com/jhipster/test/sample/infrastructure/primary/beer",
        "BeersResource.java",
        "RestBeer.java",
        "RestBeers.java",
        "RestBeerToCreate.java"
      )
      .hasFiles("src/main/java/com/jhipster/test/sample/infrastructure/secondary/InMemoryBeersRepository.java")
      .hasPrefixedFiles(
        "src/test/java/com/jhipster/test/sample/application",
        "BeerIdAccessCheckerTest.java",
        "BeerToCreateAccessCheckerTest.java"
      )
      .hasPrefixedFiles(
        "src/test/java/com/jhipster/test/sample/domain/beer",
        "BeersFixture.java",
        "BeersRemoverTest.java",
        "BeersTest.java"
      )
      .hasPrefixedFiles("src/test/java/com/jhipster/test/sample/domain/order", "BeerOrderFixture.java", "BeerOrderTest.java")
      .hasPrefixedFiles("src/test/java/com/jhipster/test/sample/domain", "AmountTest.java", "BeersIdentityFixture.java")
      .hasPrefixedFiles(
        "src/test/java/com/jhipster/test/sample/infrastructure/primary/beer",
        "BeersSteps.java",
        "RestBeersTest.java",
        "RestBeerTest.java",
        "RestBeerToCreateTest.java"
      )
      .hasFiles("src/test/java/com/jhipster/test/sample/infrastructure/secondary/InMemoryBeersResetter.java")
      .hasFiles("src/test/java/com/jhipster/test/HttpSteps.java")
      .hasFiles("src/test/features/beers-catalog.feature");
  }
}
