package tech.jhipster.lite.generator.server.springboot.broker.kafka.domain;

import static org.mockito.Mockito.when;
import static tech.jhipster.lite.TestFileUtils.tmpDirForTest;
import static tech.jhipster.lite.module.infrastructure.secondary.JHipsterModulesAssertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.JHipsterModulesFixture;
import tech.jhipster.lite.module.domain.docker.DockerImageVersion;
import tech.jhipster.lite.module.domain.docker.DockerImages;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;

@UnitTest
@ExtendWith(MockitoExtension.class)
class KafkaModuleFactoryTest {

  @Mock
  private DockerImages dockerImages;

  @InjectMocks
  private KafkaModuleFactory factory;

  @Test
  void shouldBuildKafkaModuleInit() {
    when(dockerImages.get("apache/kafka")).thenReturn(new DockerImageVersion("apache/kafka", "1.0.0"));

    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
      .basePackage("com.jhipster.test")
      .projectBaseName("myapp")
      .put("kafkaClusterId", "my-cluster")
      .build();

    JHipsterModule module = factory.buildModuleInit(properties);

    assertThatModuleWithFiles(
      module,
      pomFile(),
      new ModuleFile(
        "src/main/resources/generator/server/springboot/core/test/IntegrationTest.java.mustache",
        "src/test/java/com/jhipster/test/IntegrationTest.java"
      ),
      readmeFile()
    )
      .hasFile("pom.xml")
      .containing("<artifactId>kafka-clients</artifactId>")
      .containing("<artifactId>kafka</artifactId>")
      .and()
      .hasFile("src/main/docker/kafka.yml")
      .containing("CLUSTER_ID: 'my-cluster'")
      .and()
      .hasFile("src/main/resources/config/application.yml")
      .containing(
        """
        kafka:
          producer:
            '[key.serializer]': org.apache.kafka.common.serialization.StringSerializer
            '[value.serializer]': org.apache.kafka.common.serialization.StringSerializer
          bootstrap-servers: localhost:9092
          consumer:
            '[value.deserializer]': org.apache.kafka.common.serialization.StringDeserializer
            '[auto.offset.reset]': earliest
            '[key.deserializer]': org.apache.kafka.common.serialization.StringDeserializer
            '[group.id]': myapp
          polling:
            timeout: 10000
        """
      )
      .and()
      .hasFile("src/test/resources/config/application-test.yml")
      .containing(
        """
        kafka:
          bootstrap-servers: localhost:9092
        """
      )
      .and()
      .hasFile("src/test/java/com/jhipster/test/KafkaTestContainerExtension.java")
      .and()
      .hasFile("src/test/java/com/jhipster/test/IntegrationTest.java")
      .containing("@ExtendWith(KafkaTestContainerExtension.class)")
      .and()
      .hasFile("src/main/java/com/jhipster/test/wire/kafka/infrastructure/config/KafkaProperties.java")
      .and()
      .hasFile("src/test/java/com/jhipster/test/wire/kafka/infrastructure/config/KafkaPropertiesTest.java")
      .and()
      .hasFile("src/main/java/com/jhipster/test/wire/kafka/infrastructure/config/KafkaConfiguration.java")
      .and()
      .hasPrefixedFiles("documentation", "apache-kafka.md")
      .hasFile("README.md")
      .containing("[Apache Kafka](documentation/apache-kafka.md)")
      .containing(
        """
        ```bash
        docker compose -f src/main/docker/kafka.yml up -d
        ```
        """
      );
  }

  @Test
  void shouldBuildKafkaModuleSampleProducerConsumer() {
    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
      .basePackage("com.jhipster.test")
      .projectBaseName("myapp")
      .build();

    JHipsterModule module = factory.buildModuleSampleProducerConsumer(properties);

    var sampleProducerPath = "sample/infrastructure/secondary/kafka/producer";
    var sampleConsumerPath = "sample/infrastructure/primary/kafka/consumer";

    assertThatModuleWithFiles(module, pomFile())
      .hasFile("src/main/resources/config/application.yml")
      .containing(
        """
        kafka:
          topic:
            sample: queue.myapp.sample
        """
      )
      .and()
      .hasPrefixedFiles(
        "src/main/java/com/jhipster/test",
        sampleProducerPath + "/SampleProducer.java",
        sampleConsumerPath + "/AbstractConsumer.java",
        sampleConsumerPath + "/SampleConsumer.java"
      )
      .hasPrefixedFiles(
        "src/test/java/com/jhipster/test",
        sampleProducerPath + "/SampleProducerTest.java",
        sampleProducerPath + "/SampleProducerIT.java",
        sampleConsumerPath + "/SampleConsumerTest.java",
        sampleConsumerPath + "/SampleConsumerIT.java"
      );
  }

  @Test
  void shouldBuildKafkaModuleAkhq() {
    when(dockerImages.get("tchiotludo/akhq")).thenReturn(new DockerImageVersion("tchiotludo/akhq", "1.0.0"));

    JHipsterModuleProperties properties = JHipsterModulesFixture.propertiesBuilder(tmpDirForTest())
      .basePackage("com.jhipster.test")
      .projectBaseName("myapp")
      .build();

    JHipsterModule module = factory.buildModuleAkhq(properties);

    assertThatModuleWithFiles(module, pomFile(), readmeFile())
      .hasFile("src/main/docker/akhq.yml")
      .and()
      .hasFile("README.md")
      .containing(
        """
        ```bash
        docker compose -f src/main/docker/akhq.yml up -d
        ```
        """
      );
  }
}
