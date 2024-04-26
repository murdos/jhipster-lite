package tech.jhipster.lite.generator.server.javatool.jacoco.domain;

import static tech.jhipster.lite.module.domain.JHipsterModule.*;
import static tech.jhipster.lite.module.domain.mavenplugin.MavenBuildPhase.*;

import java.util.regex.Pattern;
import tech.jhipster.lite.module.domain.JHipsterModule;
import tech.jhipster.lite.module.domain.gradleplugin.GradleMainBuildPlugin;
import tech.jhipster.lite.module.domain.javabuild.ArtifactId;
import tech.jhipster.lite.module.domain.javabuild.GroupId;
import tech.jhipster.lite.module.domain.javabuild.VersionSlug;
import tech.jhipster.lite.module.domain.mavenplugin.MavenPlugin;
import tech.jhipster.lite.module.domain.mavenplugin.MavenPlugin.MavenPluginOptionalBuilder;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.module.domain.replacement.ElementReplacer;
import tech.jhipster.lite.module.domain.replacement.RegexReplacer;
import tech.jhipster.lite.shared.error.domain.Assert;

public class JacocoModuleFactory {

  private static final String JACOCO = "jacoco";
  private static final GroupId JACOCO_GROUP = groupId("org.jacoco");
  private static final ArtifactId JACOCO_ARTIFACT_ID = artifactId("jacoco-maven-plugin");
  private static final VersionSlug JACOCO_VERSION = versionSlug(JACOCO);
  private static final Pattern BUILD_GRADLE_TASK_TEST_NEEDLE = Pattern.compile(
    """
    tasks.test {
      filter {
        includeTestsMatching("**Test*")
        excludeTestsMatching("**IT*")
        excludeTestsMatching("**CucumberTest*")
      }
      useJUnitPlatform()
    }
    """,
    Pattern.LITERAL
  );
  private static final ElementReplacer EXISTING_BUILD_GRADLE_TASK_TEST_NEEDLE = new RegexReplacer(
    (contentBeforeReplacement, replacement) -> BUILD_GRADLE_TASK_TEST_NEEDLE.matcher(contentBeforeReplacement).find(),
    BUILD_GRADLE_TASK_TEST_NEEDLE
  );
  private static final String ADD_JACOCO_BUILD_GRADLE_TASK_TEST =
    """
    tasks.test {
      filter {
        includeTestsMatching("**Test*")
        excludeTestsMatching("**IT*")
        excludeTestsMatching("**CucumberTest*")
      }
      useJUnitPlatform()
      finalizedBy("jacocoTestReport")
    }
    """;
  private static final String ADD_JACOCO_WITH_MIN_COVERAGE_CHECK_BUILD_GRADLE_TASK_TEST =
    """
    tasks.test {
      filter {
        includeTestsMatching("**Test*")
        excludeTestsMatching("**IT*")
        excludeTestsMatching("**CucumberTest*")
      }
      useJUnitPlatform()
      finalizedBy("jacocoTestCoverageVerification")
    }
    """;

  public JHipsterModule buildJacocoModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return moduleBuilder(properties)
      .mavenPlugins()
        .plugin(mavenJacocoPlugin())
        .pluginManagement(mavenJacocoPluginManagement())
        .and()
      .gradlePlugins()
        .plugin(gradleJacocoPlugin())
        .and()
      .optionalReplacements()
        .in(path("build.gradle.kts"))
        .add(EXISTING_BUILD_GRADLE_TASK_TEST_NEEDLE, ADD_JACOCO_BUILD_GRADLE_TASK_TEST)
        .and()
      .and()
      .build();
    //@formatter:on
  }

  public JHipsterModule buildJacocoWithMinCoverageCheckModule(JHipsterModuleProperties properties) {
    Assert.notNull("properties", properties);

    //@formatter:off
    return moduleBuilder(properties)
      .mavenPlugins()
        .plugin(mavenJacocoPlugin())
        .pluginManagement(mavenJacocoWithMinCoverageCheckPluginManagement())
        .and()
      .gradlePlugins()
        .plugin(gradleJacocoWithMinCoverageCheckPlugin())
        .and()
      .optionalReplacements()
        .in(path("build.gradle.kts"))
        .add(EXISTING_BUILD_GRADLE_TASK_TEST_NEEDLE, ADD_JACOCO_WITH_MIN_COVERAGE_CHECK_BUILD_GRADLE_TASK_TEST)
        .and()
      .and()
      .build();
    //@formatter:on
  }

  private static MavenPlugin mavenJacocoPlugin() {
    return mavenPlugin().groupId(JACOCO_GROUP).artifactId(JACOCO_ARTIFACT_ID).build();
  }

  private static MavenPlugin mavenJacocoPluginManagement() {
    return commonMavenJacocoPluginManagement().build();
  }

  private static MavenPlugin mavenJacocoWithMinCoverageCheckPluginManagement() {
    return commonMavenJacocoPluginManagement()
      .addExecution(
        pluginExecution()
          .goals("check")
          .id("check")
          .configuration(
            """
              <dataFile>target/jacoco/allTest.exec</dataFile>
              <rules>
                <rule>
                  <element>CLASS</element>
                  <limits>
                    <limit>
                      <counter>LINE</counter>
                      <value>COVEREDRATIO</value>
                      <minimum>1.00</minimum>
                    </limit>
                    <limit>
                      <counter>BRANCH</counter>
                      <value>COVEREDRATIO</value>
                      <minimum>1.00</minimum>
                    </limit>
                  </limits>
                </rule>
              </rules>
            """
          )
      )
      .build();
  }

  private static MavenPluginOptionalBuilder commonMavenJacocoPluginManagement() {
    return mavenPlugin()
      .groupId(JACOCO_GROUP)
      .artifactId(JACOCO_ARTIFACT_ID)
      .versionSlug(JACOCO_VERSION)
      .addExecution(pluginExecution().goals("prepare-agent").id("pre-unit-tests"))
      .addExecution(pluginExecution().goals("report").id("post-unit-test").phase(TEST))
      .addExecution(pluginExecution().goals("prepare-agent-integration").id("pre-integration-tests"))
      .addExecution(pluginExecution().goals("report-integration").id("post-integration-tests").phase(POST_INTEGRATION_TEST))
      .addExecution(
        pluginExecution()
          .goals("merge")
          .id("merge")
          .phase(VERIFY)
          .configuration(
            """
              <fileSets>
              <fileSet implementation="org.apache.maven.shared.model.fileset.FileSet">
                <directory>${project.basedir}</directory>
                <includes>
                  <include>**/*.exec</include>
                </includes>
              </fileSet>
            </fileSets>
            <destFile>target/jacoco/allTest.exec</destFile>
            """
          )
      )
      .addExecution(
        pluginExecution()
          .goals("report")
          .id("post-merge-report")
          .phase(VERIFY)
          .configuration(
            """
              <dataFile>target/jacoco/allTest.exec</dataFile>
              <outputDirectory>target/jacoco/</outputDirectory>
            """
          )
      );
  }

  private static GradleMainBuildPlugin gradleJacocoPlugin() {
    return gradleCorePlugin()
      .id(JACOCO)
      .toolVersionSlug(JACOCO)
      .configuration(
        """
        jacoco {
          toolVersion = libs.versions.jacoco.get()
        }

        tasks.jacocoTestReport {
          dependsOn("test", "integrationTest")
          reports {
            xml.required.set(true)
            html.required.set(true)
          }
          executionData.setFrom(fileTree(buildDir).include("**/jacoco/test.exec", "**/jacoco/integrationTest.exec"))
        }
        """
      )
      .build();
  }

  private static GradleMainBuildPlugin gradleJacocoWithMinCoverageCheckPlugin() {
    return gradleCorePlugin()
      .id(JACOCO)
      .toolVersionSlug(JACOCO)
      .configuration(
        """
        jacoco {
          toolVersion = libs.versions.jacoco.get()
        }

        tasks.jacocoTestReport {
          dependsOn("test", "integrationTest")
          reports {
            xml.required.set(true)
            html.required.set(true)
          }
          executionData.setFrom(fileTree(buildDir).include("**/jacoco/test.exec", "**/jacoco/integrationTest.exec"))
        }

        tasks.jacocoTestCoverageVerification {
          dependsOn("jacocoTestReport")
          violationRules {

              rule {
                  element = "CLASS"

                  limit {
                      counter = "LINE"
                      value = "COVEREDRATIO"
                      minimum = "1.00".toBigDecimal()
                  }

                  limit {
                      counter = "BRANCH"
                      value = "COVEREDRATIO"
                      minimum = "1.00".toBigDecimal()
                  }
              }
          }
          executionData.setFrom(fileTree(buildDir).include("**/jacoco/test.exec", "**/jacoco/integrationTest.exec"))
        }
        """
      )
      .build();
  }
}
