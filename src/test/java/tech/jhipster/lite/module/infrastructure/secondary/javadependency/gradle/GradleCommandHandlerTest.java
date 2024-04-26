package tech.jhipster.lite.module.infrastructure.secondary.javadependency.gradle;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.*;
import static tech.jhipster.lite.TestFileUtils.*;
import static tech.jhipster.lite.module.domain.JHipsterModule.*;
import static tech.jhipster.lite.module.domain.JHipsterModulesFixture.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import tech.jhipster.lite.UnitTest;
import tech.jhipster.lite.module.domain.Indentation;
import tech.jhipster.lite.module.domain.buildproperties.BuildProperty;
import tech.jhipster.lite.module.domain.buildproperties.PropertyKey;
import tech.jhipster.lite.module.domain.buildproperties.PropertyValue;
import tech.jhipster.lite.module.domain.file.TemplateRenderer;
import tech.jhipster.lite.module.domain.gradleplugin.GradlePlugin;
import tech.jhipster.lite.module.domain.javabuild.command.AddDirectJavaDependency;
import tech.jhipster.lite.module.domain.javabuild.command.AddDirectMavenPlugin;
import tech.jhipster.lite.module.domain.javabuild.command.AddGradleConfiguration;
import tech.jhipster.lite.module.domain.javabuild.command.AddGradlePlugin;
import tech.jhipster.lite.module.domain.javabuild.command.AddJavaBuildProfile;
import tech.jhipster.lite.module.domain.javabuild.command.AddJavaDependencyManagement;
import tech.jhipster.lite.module.domain.javabuild.command.AddMavenBuildExtension;
import tech.jhipster.lite.module.domain.javabuild.command.AddMavenPluginManagement;
import tech.jhipster.lite.module.domain.javabuild.command.RemoveDirectJavaDependency;
import tech.jhipster.lite.module.domain.javabuild.command.RemoveJavaDependencyManagement;
import tech.jhipster.lite.module.domain.javabuild.command.SetBuildProperty;
import tech.jhipster.lite.module.domain.javabuild.command.SetVersion;
import tech.jhipster.lite.module.domain.javabuildprofile.BuildProfileActivation;
import tech.jhipster.lite.module.domain.javabuildprofile.BuildProfileId;
import tech.jhipster.lite.module.domain.javadependency.JavaDependency;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyScope;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyType;
import tech.jhipster.lite.module.domain.javadependency.JavaDependencyVersion;
import tech.jhipster.lite.module.domain.properties.JHipsterProjectFolder;
import tech.jhipster.lite.module.infrastructure.secondary.FileSystemProjectFiles;

@UnitTest
class GradleCommandHandlerTest {

  private static final FileSystemProjectFiles filesReader = new FileSystemProjectFiles();
  private final TemplateRenderer templateRenderer = TemplateRenderer.NOOP;

  @Test
  void shouldHandleInvalidTomlVersionCatalog() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-unreadable");

    assertThatThrownBy(
      () -> new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer)
    ).isExactlyInstanceOf(InvalidTomlVersionCatalogException.class);
  }

  @Nested
  class HandleSetVersion {

    @Test
    void shouldAddVersionToMissingTomlVersionCatalogAnd() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new SetVersion(springBootVersion())
      );

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [versions]
        \tspring-boot = "1.2.3"
        """
      );
    }

    @Test
    void shouldAddVersionToExistingTomlVersionCatalog() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new SetVersion(springBootVersion())
      );

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [versions]
        \tspring-boot = "1.2.3"
        """
      );
    }

    @Test
    void shouldUpdateExistingProperty() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      gradleCommandHandler.handle(new SetVersion(new JavaDependencyVersion("jjwt", "0.12.0")));

      gradleCommandHandler.handle(new SetVersion(new JavaDependencyVersion("jjwt", "0.13.0")));

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [versions]
        \tjjwt = "0.13.0"
        """
      );
    }
  }

  @Nested
  class HandleSetBuildProperty {

    @Nested
    class WithoutProfile {

      @Test
      void shouldAddPropertiesToBuildGradleFileWithoutProperties() {
        JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

        new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
          new SetBuildProperty(new BuildProperty(new PropertyKey("spring-profiles-active"), new PropertyValue("local")))
        );

        assertThat(buildGradleContent(projectFolder)).contains(
          """
          val springProfilesActive by extra("local")
          // jhipster-needle-gradle-properties
          """
        );
      }

      @Test
      void shouldAddPropertiesToBuildGradleFileWithProperties() {
        JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-properties");

        new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
          new SetBuildProperty(springProfilesActiveProperty())
        );

        assertThat(buildGradleContent(projectFolder)).contains(
          """
          val javaVersion by extra("21")
          val springProfilesActive by extra("local")
          // jhipster-needle-gradle-properties
          """
        );
      }

      @Test
      void shouldUpdateExistingProperty() {
        JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-properties");
        GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
          Indentation.DEFAULT,
          projectFolder,
          filesReader,
          templateRenderer
        );
        gradleCommandHandler.handle(new SetBuildProperty(springProfilesActiveProperty()));

        gradleCommandHandler.handle(
          new SetBuildProperty(new BuildProperty(new PropertyKey("spring.profiles.active"), new PropertyValue("dev")))
        );

        assertThat(buildGradleContent(projectFolder))
          .contains(
            """
            val springProfilesActive by extra("dev")
            // jhipster-needle-gradle-properties
            """
          )
          .doesNotContain(
            """
            val springProfilesActive by extra("local")\
            """
          );
      }

      @Test
      void shouldNotUpdateExistingPropertyWithSameValue() {
        JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-properties");
        GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
          Indentation.DEFAULT,
          projectFolder,
          filesReader,
          templateRenderer
        );
        gradleCommandHandler.handle(new SetBuildProperty(springProfilesActiveProperty()));

        gradleCommandHandler.handle(new SetBuildProperty(springProfilesActiveProperty()));

        assertThat(buildGradleContent(projectFolder)).contains(
          """
          val springProfilesActive by extra("local")
          // jhipster-needle-gradle-properties
          """
        );
      }
    }

    @Nested
    class WithProfile {

      @Test
      void shouldNotAddPropertiesToGradleWithoutBuildGradleProfileFile() {
        JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

        assertThatThrownBy(
          () ->
            new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
              new SetBuildProperty(springProfilesActiveProperty(), localBuildProfile())
            )
        ).isExactlyInstanceOf(MissingGradleProfileException.class);
      }

      @Test
      void shouldAddPropertiesToBuildGradleProfileFileWithoutProperties() {
        JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");

        new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
          new SetBuildProperty(springProfilesActiveProperty(), localBuildProfile())
        );

        assertThat(scriptPluginContent(projectFolder, localBuildProfile())).contains(
          """
          val springProfilesActive by extra("local")
          // jhipster-needle-gradle-properties
          """
        );
      }

      @Test
      void shouldAddPropertiesToBuildGradleProfileFileWithProperties() {
        JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile-and-properties");

        new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
          new SetBuildProperty(springProfilesActiveProperty(), localBuildProfile())
        );

        assertThat(scriptPluginContent(projectFolder, localBuildProfile())).contains(
          """
          val javaVersion by extra("21")
          val springProfilesActive by extra("local")
          // jhipster-needle-gradle-properties
          """
        );
      }

      @Test
      void shouldUpdateExistingProfileProperty() {
        JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile-and-properties");
        GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
          Indentation.DEFAULT,
          projectFolder,
          filesReader,
          templateRenderer
        );
        gradleCommandHandler.handle(new SetBuildProperty(springProfilesActiveProperty(), localBuildProfile()));

        gradleCommandHandler.handle(
          new SetBuildProperty(new BuildProperty(new PropertyKey("spring.profiles.active"), new PropertyValue("dev")), localBuildProfile())
        );

        assertThat(scriptPluginContent(projectFolder, localBuildProfile()))
          .contains(
            """
            val springProfilesActive by extra("dev")
            // jhipster-needle-gradle-properties
            """
          )
          .doesNotContain(
            """
            val springProfilesActive by extra("local")\
            """
          );
      }

      @Test
      void shouldNotUpdateExistingProfilePropertyWithSameValue() {
        JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile-and-properties");
        GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
          Indentation.DEFAULT,
          projectFolder,
          filesReader,
          templateRenderer
        );
        gradleCommandHandler.handle(new SetBuildProperty(springProfilesActiveProperty(), localBuildProfile()));

        gradleCommandHandler.handle(new SetBuildProperty(springProfilesActiveProperty(), localBuildProfile()));

        assertThat(scriptPluginContent(projectFolder, localBuildProfile())).contains(
          """
          val springProfilesActive by extra("local")
          // jhipster-needle-gradle-properties
          """
        );
      }
    }
  }

  @Nested
  class HandleAddJavaBuildProfile {

    @Test
    void shouldEnablePrecompiledScriptPluginsToBuildGradleFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddJavaBuildProfile(buildProfileId("local"))
      );

      assertFileExists(projectFolder, "buildSrc/build.gradle.kts", "The file build.gradle.kts should exist at %s");
    }

    @Test
    void shouldInjectTomlVersionCatalogLibsIntoBuildGradleFileWithProfiles() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddJavaBuildProfile(buildProfileId("local"))
      );

      assertFileExists(projectFolder, "buildSrc/settings.gradle.kts", "The file settings.gradle.kts should exist at %s");
    }

    @Test
    void shouldAddProfileWithIdOnlyToBuildGradleFileWithoutProfiles() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddJavaBuildProfile(buildProfileId("local"))
      );

      assertThat(buildGradleContent(projectFolder)).contains(
        """
        if (profiles.contains("local")) {
          apply(plugin = "profile-local")
        }
        // jhipster-needle-profile-activation\
        """
      );
      assertFileExists(
        projectFolder,
        "buildSrc/src/main/kotlin/profile-local.gradle.kts",
        "The file profile-local.gradle.kts should exist at %s"
      );
    }

    @Test
    void shouldAddProfileWithIdOnlyToBuildGradleFileWithProfiles() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );

      gradleCommandHandler.handle(new AddJavaBuildProfile(buildProfileId("local")));
      gradleCommandHandler.handle(new AddJavaBuildProfile(buildProfileId("dev")));

      assertThat(buildGradleContent(projectFolder)).contains(
        """
        if (profiles.contains("local")) {
          apply(plugin = "profile-local")
        }
        if (profiles.contains("dev")) {
          apply(plugin = "profile-dev")
        }
        // jhipster-needle-profile-activation\
        """
      );
      assertFileExists(
        projectFolder,
        "buildSrc/src/main/kotlin/profile-local.gradle.kts",
        "The file profile-local.gradle.kts should exist at %s"
      );
    }

    @Test
    void shouldNotDuplicateExistingProfile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );

      gradleCommandHandler.handle(new AddJavaBuildProfile(buildProfileId("local")));
      gradleCommandHandler.handle(new AddJavaBuildProfile(buildProfileId("local")));

      assertThat(buildGradleContent(projectFolder)).contains(
        """
        val profiles = (project.findProperty("profiles") as String? ?: "")
          .split(",")
          .map { it.trim() }
          .filter { it.isNotEmpty() }
        if (profiles.contains("local")) {
          apply(plugin = "profile-local")
        }
        // jhipster-needle-profile-activation\
        """
      );
      assertFileExists(
        projectFolder,
        "buildSrc/src/main/kotlin/profile-local.gradle.kts",
        "The file profile-local.gradle.kts should exist at %s"
      );
    }

    @ParameterizedTest
    @MethodSource("provideBuildProfileActivations")
    void shouldNotAddDefaultActivationToBuildGradleFile(BuildProfileActivation profileActivation) {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddJavaBuildProfile(buildProfileId("local"), profileActivation)
      );

      assertThat(buildGradleContent(projectFolder)).contains(
        """
        if (profiles.contains("local")) {
          apply(plugin = "profile-local")
        }
        // jhipster-needle-profile-activation\
        """
      );
    }

    private static Stream<BuildProfileActivation> provideBuildProfileActivations() {
      return Stream.of(BuildProfileActivation.builder().build(), BuildProfileActivation.builder().activeByDefault(false).build());
    }

    @Test
    void shouldAddDefaultActivationWithActivationByDefaultTrueToBuildGradleFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddJavaBuildProfile(buildProfileId("local"), BuildProfileActivation.builder().activeByDefault().build())
      );

      assertThat(buildGradleContent(projectFolder)).contains(
        """
        if (profiles.isEmpty() || profiles.contains("local")) {
          apply(plugin = "profile-local")
        }
        // jhipster-needle-profile-activation\
        """
      );
    }

    @Test
    void shouldNotDuplicateExistingProfileWithDifferentActivation() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );

      gradleCommandHandler.handle(
        new AddJavaBuildProfile(buildProfileId("local"), BuildProfileActivation.builder().activeByDefault(false).build())
      );
      gradleCommandHandler.handle(
        new AddJavaBuildProfile(buildProfileId("local"), BuildProfileActivation.builder().activeByDefault(true).build())
      );

      assertThat(buildGradleContent(projectFolder)).contains(
        """
        val profiles = (project.findProperty("profiles") as String? ?: "")
          .split(",")
          .map { it.trim() }
          .filter { it.isNotEmpty() }
        if (profiles.contains("local")) {
          apply(plugin = "profile-local")
        }
        // jhipster-needle-profile-activation\
        """
      );
      assertFileExists(
        projectFolder,
        "buildSrc/src/main/kotlin/profile-local.gradle.kts",
        "The file profile-local.gradle.kts should exist at %s"
      );
    }
  }

  @Nested
  class HandleAddDirectJavaDependency {

    private final JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

    @Test
    void shouldAddEntryInLibrariesSectionToExistingTomlVersionCatalog() {
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddDirectJavaDependency(springBootStarterWebDependency())
      );

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [libraries.spring-boot-starter-web]
        \t\tname = "spring-boot-starter-web"
        \t\tgroup = "org.springframework.boot"
        """
      );
    }

    @Test
    void shouldUpdateEntryInLibrariesSectionToExistingTomlVersionCatalog() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      JavaDependency initialDependency = javaDependency().groupId("org.springframework.boot").artifactId("spring-boot-starter-web").build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(initialDependency));

      JavaDependency updatedDependency = javaDependency().groupId("org.spring.boot").artifactId("spring-boot-starter-web").build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(updatedDependency));

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [libraries.spring-boot-starter-web]
        \t\tname = "spring-boot-starter-web"
        \t\tgroup = "org.spring.boot"
        """
      );
    }

    @Test
    void shouldIncludeVersionRefInLibrariesSectionOfTomlVersionCatalog() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );

      JavaDependency dependency = javaDependency()
        .groupId("org.spring.boot")
        .artifactId("spring-boot-starter-web")
        .versionSlug("spring-boot")
        .build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependency));

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [libraries.spring-boot-starter-web.version]
        \t\t\tref = "spring-boot"
        """
      );
    }

    @Test
    void shouldUseValidLibraryAlias() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );

      JavaDependency dependency = javaDependency().groupId("com.zaxxer").artifactId("HikariCP").build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependency));

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [libraries.hikariCP]
        \t\tname = "HikariCP"
        \t\tgroup = "com.zaxxer"
        """
      );
      assertThat(buildGradleContent(projectFolder)).contains("implementation(libs.hikariCP)");
    }

    @EnumSource(value = JavaDependencyScope.class, mode = EXCLUDE, names = { "TEST", "RUNTIME", "PROVIDED", "IMPORT" })
    @ParameterizedTest
    void shouldAddImplementationDependencyInBuildGradleFileForScope(JavaDependencyScope scope) {
      JavaDependency dependency = javaDependency().groupId("org.spring.boot").artifactId("spring-boot-starter-web").scope(scope).build();
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddDirectJavaDependency(dependency)
      );

      assertThat(buildGradleContent(projectFolder)).contains("implementation(libs.spring.boot.starter.web)");
    }

    @Test
    void shouldAddRuntimeOnlyDependencyInBuildGradleFileForRuntimeScope() {
      JavaDependency dependency = javaDependency()
        .groupId("org.spring.boot")
        .artifactId("spring-boot-starter-web")
        .scope(JavaDependencyScope.RUNTIME)
        .build();
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddDirectJavaDependency(dependency)
      );

      assertThat(buildGradleContent(projectFolder)).contains("runtimeOnly(libs.spring.boot.starter.web)");
    }

    @Test
    void shouldAddCompileOnlyDependencyInBuildGradleFileForProvidedScope() {
      JavaDependency dependency = javaDependency()
        .groupId("org.spring.boot")
        .artifactId("spring-boot-starter-web")
        .scope(JavaDependencyScope.PROVIDED)
        .build();
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddDirectJavaDependency(dependency)
      );

      assertThat(buildGradleContent(projectFolder)).contains("compileOnly(libs.spring.boot.starter.web)");
    }

    @Test
    void shouldAddTestImplementationDependencyInBuildGradleFileForTestScope() {
      JavaDependency dependency = javaDependency()
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter-engine")
        .scope(JavaDependencyScope.TEST)
        .build();
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddDirectJavaDependency(dependency)
      );

      assertThat(buildGradleContent(projectFolder)).contains("testImplementation(libs.junit.jupiter.engine)");
    }

    @Test
    void shouldAddDependencyExclusionsInBuildGradleFile() {
      JavaDependency dependency = javaDependency()
        .groupId("org.springframework.boot")
        .artifactId("spring-boot-starter-web")
        .addExclusion(groupId("org.springframework.boot"), artifactId("spring-boot-starter-tomcat"))
        .addExclusion(groupId("org.springframework.boot"), artifactId("spring-boot-starter-json"))
        .build();
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddDirectJavaDependency(dependency)
      );

      assertThat(buildGradleContent(projectFolder)).contains(
        """
          implementation(libs.spring.boot.starter.web) {
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-json")
          }
        """
      );
    }

    @Test
    void shouldAddDependenciesInOrder() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );

      JavaDependency dependencyCompile = javaDependency()
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter-engine")
        .scope(JavaDependencyScope.PROVIDED)
        .build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependencyCompile));

      JavaDependency dependencyRuntime = javaDependency()
        .groupId("org.springframework.boot")
        .artifactId("spring-boot-starter-web")
        .scope(JavaDependencyScope.RUNTIME)
        .build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependencyRuntime));

      JavaDependency dependencyTestImplementation = javaDependency()
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter-engine")
        .scope(JavaDependencyScope.TEST)
        .build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependencyTestImplementation));

      JavaDependency dependencyImplementation = javaDependency().groupId("com.google.guava").artifactId("guava").build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependencyImplementation));

      assertThat(buildGradleContent(projectFolder)).contains(
        """
        dependencies {
          implementation(libs.guava)
          // jhipster-needle-gradle-implementation-dependencies
          compileOnly(libs.junit.jupiter.engine)
          // jhipster-needle-gradle-compile-dependencies
          runtimeOnly(libs.spring.boot.starter.web)
          // jhipster-needle-gradle-runtime-dependencies
          testImplementation(libs.junit.jupiter.engine)
          // jhipster-needle-gradle-test-dependencies
        }
        """
      );
    }

    @Test
    void shouldAddDependencyToBuildGradleProfileFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");
      JavaDependency dependency = javaDependency()
        .groupId("org.spring.boot")
        .artifactId("spring-boot-starter-web")
        .scope(JavaDependencyScope.RUNTIME)
        .build();

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddDirectJavaDependency(dependency, localBuildProfile())
      );

      assertThat(scriptPluginContent(projectFolder, localBuildProfile())).contains(
        """
        runtimeOnly(libs.findLibrary("spring.boot.starter.web").get())\
        """
      );
    }
  }

  @Nested
  class HandleRemoveDirectJavaDependency {

    private final JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

    @Test
    void shouldRemoveEntryInLibrariesSection() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      gradleCommandHandler.handle(new AddDirectJavaDependency(defaultVersionDependency()));

      gradleCommandHandler.handle(new RemoveDirectJavaDependency(defaultVersionDependency().id()));

      assertThat(versionCatalogContent(projectFolder))
        .doesNotContain("[libraries.spring-boot-starter]")
        .doesNotContain(
          """
          \t\tname = "spring-boot-starter"
          \t\tgroup = "org.springframework.boot"
          """
        );
    }

    @Test
    void shouldRemoveDependencyInBuildGradleFile() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      gradleCommandHandler.handle(new AddDirectJavaDependency(defaultVersionDependency()));

      gradleCommandHandler.handle(new RemoveDirectJavaDependency(defaultVersionDependency().id()));

      assertThat(buildGradleContent(projectFolder)).doesNotContain("implementation(libs.spring.boot.starter)");
    }

    @Test
    void shouldRemoveDependencyWithExclusionInBuildGradleFile() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependencyWithVersionAndExclusion()));

      gradleCommandHandler.handle(new RemoveDirectJavaDependency(dependencyWithVersionAndExclusion().id()));

      assertThat(buildGradleContent(projectFolder)).doesNotContain("implementation(libs.jjwt.jackson)");
    }

    @Test
    void shouldRemoveTestDependencyInBuildGradleFile() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      JavaDependency dependency = javaDependency()
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter-engine")
        .scope(JavaDependencyScope.TEST)
        .build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependency));

      gradleCommandHandler.handle(new RemoveDirectJavaDependency(dependency.id()));

      assertThat(buildGradleContent(projectFolder)).doesNotContain("testImplementation(libs.junit.jupiter.engine)");
    }

    @Test
    void shouldRemoveRuntimeDependencyInBuildGradleFile() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      JavaDependency dependency = javaDependency()
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter-engine")
        .scope(JavaDependencyScope.RUNTIME)
        .build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependency));

      gradleCommandHandler.handle(new RemoveDirectJavaDependency(dependency.id()));

      assertThat(buildGradleContent(projectFolder)).doesNotContain("runtimeOnly(libs.junit.jupiter.engine)");
    }

    @Test
    void shouldRemoveProvidedDependencyInBuildGradleFile() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      JavaDependency dependency = javaDependency()
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter-engine")
        .scope(JavaDependencyScope.PROVIDED)
        .build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependency));

      gradleCommandHandler.handle(new RemoveDirectJavaDependency(dependency.id()));

      assertThat(buildGradleContent(projectFolder)).doesNotContain("compileOnly(libs.junit.jupiter.engine)");
    }

    @Test
    void shouldRemoveRuntimeDependencyInBuildGradleProfileFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      JavaDependency dependency = javaDependency()
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter-engine")
        .scope(JavaDependencyScope.RUNTIME)
        .build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependency, localBuildProfile()));

      gradleCommandHandler.handle(new RemoveDirectJavaDependency(dependency.id(), localBuildProfile()));

      assertThat(scriptPluginContent(projectFolder, localBuildProfile())).doesNotContain(
        """
        runtimeOnly(libs.findLibrary("junit.jupiter.engine").get())
        """
      );
    }

    @Test
    void shouldNotRemoveEntryInLibrariesSectionWhenDependencyNotFoundInBuildGradleProfileFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      JavaDependency dependency = javaDependency()
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter-engine")
        .scope(JavaDependencyScope.RUNTIME)
        .build();
      gradleCommandHandler.handle(new AddDirectJavaDependency(dependency));

      gradleCommandHandler.handle(new RemoveDirectJavaDependency(dependency.id(), localBuildProfile()));

      assertThat(versionCatalogContent(projectFolder))
        .contains("[libraries.junit-jupiter-engine]")
        .contains(
          """
          \t\tname = "junit-jupiter-engine"
          \t\tgroup = "org.junit.jupiter"
          """
        );
    }
  }

  @Nested
  class HandleAddJavaDependencyManagement {

    private final JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

    @Test
    void shouldAddEntryInLibrariesSectionToExistingTomlVersionCatalog() {
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddJavaDependencyManagement(springBootDependencyManagement())
      );

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [libraries.spring-boot-dependencies]
        \t\tname = "spring-boot-dependencies"
        \t\tgroup = "org.springframework.boot"
        """
      );

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [libraries.spring-boot-dependencies.version]
        \t\t\tref = "spring-boot"
        """
      );
    }

    @Test
    void shouldAddImplementationDependencyInBuildGradleFileForScope() {
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddJavaDependencyManagement(springBootDependencyManagement())
      );

      assertThat(buildGradleContent(projectFolder)).contains("implementation(platform(libs.spring.boot.dependencies))");
    }

    @Test
    void shouldAddDependencyExclusionsInBuildGradleFile() {
      JavaDependency dependency = javaDependency()
        .groupId("org.springframework.boot")
        .artifactId("spring-boot-starter-web")
        .scope(JavaDependencyScope.IMPORT)
        .addExclusion(groupId("org.springframework.boot"), artifactId("spring-boot-starter-tomcat"))
        .addExclusion(groupId("org.springframework.boot"), artifactId("spring-boot-starter-json"))
        .build();
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddJavaDependencyManagement(dependency)
      );

      assertThat(buildGradleContent(projectFolder)).contains(
        """
          implementation(platform(libs.spring.boot.starter.web)) {
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-json")
          }
        """
      );
    }

    @Test
    void shouldAddImplementationDependencyInBuildGradleProfileFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        new AddJavaDependencyManagement(springBootDependencyManagement(), localBuildProfile())
      );

      assertThat(scriptPluginContent(projectFolder, localBuildProfile())).contains(
        """
        implementation(platform(libs.findLibrary("spring.boot.dependencies").get()))\
        """
      );
    }
  }

  @Nested
  class HandleRemoveJavaDependencyManagement {

    private final JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

    @Test
    void shouldRemoveEntryInLibrariesSection() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      gradleCommandHandler.handle(new AddJavaDependencyManagement(springBootDependencyManagement()));

      gradleCommandHandler.handle(new RemoveJavaDependencyManagement(springBootDependencyManagement().id()));

      assertThat(versionCatalogContent(projectFolder))
        .doesNotContain("[libraries.spring-boot-dependencies]")
        .doesNotContain(
          """
          \t\tname = "spring-boot-dependencies"
          \t\tgroup = "org.springframework.boot"
          """
        );
    }

    @Test
    void shouldRemoveDependencyInBuildGradleFile() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      gradleCommandHandler.handle(new AddJavaDependencyManagement(springBootDependencyManagement()));

      gradleCommandHandler.handle(new RemoveJavaDependencyManagement(springBootDependencyManagement().id()));

      assertThat(buildGradleContent(projectFolder)).doesNotContain("implementation(platform(libs.spring.boot.dependencies))");
    }

    @Test
    void shouldRemoveDependencyWithExclusionInBuildGradleFile() {
      var dependency = javaDependency()
        .groupId("org.springframework.boot")
        .artifactId("spring-boot-dependencies")
        .addExclusion(jsonWebTokenDependencyId())
        .addExclusion(springBootDependencyId())
        .scope(JavaDependencyScope.IMPORT)
        .type(JavaDependencyType.POM)
        .build();
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      gradleCommandHandler.handle(new AddJavaDependencyManagement(dependency));
      assertThat(buildGradleContent(projectFolder)).contains("implementation(platform(libs.spring.boot.dependencies))");

      gradleCommandHandler.handle(new RemoveDirectJavaDependency(dependency.id()));

      assertThat(buildGradleContent(projectFolder)).doesNotContain("implementation(platform(libs.spring.boot.dependencies))");
    }

    @Test
    void shouldRemoveDependencyInBuildGradleProfileFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      gradleCommandHandler.handle(new AddJavaDependencyManagement(springBootDependencyManagement(), localBuildProfile()));

      gradleCommandHandler.handle(new RemoveJavaDependencyManagement(springBootDependencyManagement().id(), localBuildProfile()));

      assertThat(buildGradleContent(projectFolder)).doesNotContain(
        """
        implementation(platform(libs.findLibrary("spring.boot.dependencies").get()))\
        """
      );
    }

    @Test
    void shouldNotRemoveEntryInLibrariesSectionWhenDependencyManagerNotFoundInBuildGradleProfileFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      gradleCommandHandler.handle(new AddJavaDependencyManagement(springBootDependencyManagement()));

      gradleCommandHandler.handle(new RemoveJavaDependencyManagement(springBootDependencyManagement().id(), localBuildProfile()));

      assertThat(versionCatalogContent(projectFolder))
        .contains("[libraries.spring-boot-dependencies]")
        .contains(
          """
          \t\tname = "spring-boot-dependencies"
          \t\tgroup = "org.springframework.boot"
          """
        );
    }
  }

  @Nested
  class HandleAddGradlePlugin {

    private final JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

    @Test
    void shouldDeclareAndConfigurePluginInBuildGradleFile() {
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        AddGradlePlugin.builder().plugin(checkstyleGradlePlugin()).build()
      );

      assertThat(buildGradleContent(projectFolder))
        .contains(
          """
          plugins {
            java
            checkstyle
            // jhipster-needle-gradle-plugins
          }
          """
        )
        .contains(
          """

          checkstyle {
            toolVersion = libs.versions.checkstyle.get()
          }

          // jhipster-needle-gradle-plugins-configurations
          """
        );
    }

    @Test
    void shouldApplyVersionCatalogReferenceConvention() {
      GradlePlugin plugin = gradleCommunityPlugin().id("org.springframework.boot").pluginSlug("spring-boot").build();
      AddGradlePlugin build = AddGradlePlugin.builder().plugin(plugin).build();
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(build);

      assertThat(versionCatalogContent(projectFolder)).contains("[plugins.spring-boot]");
      // "-" is transformed to "." in the plugin slug
      assertThat(buildGradleContent(projectFolder)).contains("alias(libs.plugins.spring.boot)");
    }

    @Test
    void shouldAddToolVersion() {
      AddGradlePlugin build = AddGradlePlugin.builder().plugin(checkstyleGradlePlugin()).toolVersion(checkstyleToolVersion()).build();
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(build);

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        checkstyle = "8.42.1"
        """
      );
    }

    @Test
    void shouldAddPluginVersion() {
      AddGradlePlugin build = AddGradlePlugin.builder().plugin(checkstyleGradlePlugin()).pluginVersion(checkstyleToolVersion()).build();
      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(build);

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        checkstyle = "8.42.1"
        """
      );
    }

    @Test
    void shouldIgnoreAlreadyDeclaredPluginInBuildGradleFile() {
      GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(
        Indentation.DEFAULT,
        projectFolder,
        filesReader,
        templateRenderer
      );
      AddGradlePlugin command = AddGradlePlugin.builder().plugin(checkstyleGradlePlugin()).build();
      gradleCommandHandler.handle(command);

      gradleCommandHandler.handle(command);

      assertThat(buildGradleContent(projectFolder))
        .contains(
          """
          plugins {
            java
            checkstyle
            // jhipster-needle-gradle-plugins
          }
          """
        )
        .contains(
          """
          java {
            toolchain {
              languageVersion = JavaLanguageVersion.of(21)
            }
          }


          checkstyle {
            toolVersion = libs.versions.checkstyle.get()
          }

          // jhipster-needle-gradle-plugins-configurations
          """
        );
    }

    @Test
    void shouldDeclareAndConfigureCommunityPluginInBuildGradleProfileFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        AddGradlePlugin.builder().plugin(gitPropertiesGradleProfilePlugin()).buildProfile(localBuildProfile()).build()
      );

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [libraries.gradle-git-properties]
        \t\tname = "gradle-git-properties"
        \t\tgroup = "com.gorylenko.gradle-git-properties"
        """
      );
      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [libraries.gradle-git-properties.version]
        \t\t\tref = "git-properties"
        """
      );
      assertThat(pluginBuildGradleContent(projectFolder)).contains("implementation(libs.gradle.git.properties)");
      assertThat(scriptPluginContent(projectFolder, localBuildProfile()))
        .contains(
          """
          plugins {
            java
            id("com.gorylenko.gradle-git-properties")
            // jhipster-needle-gradle-plugins
          }
          """
        )
        .contains(
          """

          gitProperties {
            failOnNoGitDirectory = false
            keys = listOf("git.branch", "git.commit.id.abbrev", "git.commit.id.describe", "git.build.version")
          }

          // jhipster-needle-gradle-plugins-configurations
          """
        );
    }

    @Test
    void shouldDeclareCommunityPluginWithDifferentGroupIdAndPluginIdInBuildGradleProfileFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        AddGradlePlugin.builder().plugin(dockerGradlePluginDependency()).buildProfile(localBuildProfile()).build()
      );

      assertThat(versionCatalogContent(projectFolder)).contains(
        """
        [libraries.gradle-docker-plugin]
        \t\tname = "gradle-docker-plugin"
        \t\tgroup = "com.bmuschko"
        """
      );
      assertThat(pluginBuildGradleContent(projectFolder)).contains("implementation(libs.gradle.docker.plugin");
      assertThat(scriptPluginContent(projectFolder, localBuildProfile())).contains(
        """
        plugins {
          java
          id("com.bmuschko.docker-remote-api")
          // jhipster-needle-gradle-plugins
        }
        """
      );
    }

    @Test
    void shouldDeclareAndConfigureCorePluginInBuildGradleProfileFile() {
      JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/gradle-with-local-profile");

      new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
        AddGradlePlugin.builder().plugin(checkstyleGradleProfilePlugin()).buildProfile(localBuildProfile()).build()
      );

      assertThat(scriptPluginContent(projectFolder, localBuildProfile()))
        .contains(
          """
          plugins {
            java
            checkstyle
            // jhipster-needle-gradle-plugins
          }
          """
        )
        .contains(
          """
          checkstyle {
            toolVersion = libs.versions.checkstyle.get()
          }
          """
        );
    }
  }

  @Test
  void addMavenBuildExtensionShouldNotBeHandled() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty");

    GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer);
    AddMavenBuildExtension command = new AddMavenBuildExtension(mavenBuildExtensionWithSlug());
    assertThatCode(() -> gradleCommandHandler.handle(command)).doesNotThrowAnyException();
  }

  @Test
  void addAddDirectMavenPluginShouldNotBeHandled() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty");

    GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer);
    AddDirectMavenPlugin command = AddDirectMavenPlugin.builder().plugin(mavenEnforcerPlugin()).build();
    assertThatCode(() -> gradleCommandHandler.handle(command)).doesNotThrowAnyException();
  }

  @Test
  void addAddBuildPluginManagementShouldNotBeHandled() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty");

    GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer);
    AddMavenPluginManagement command = AddMavenPluginManagement.builder().plugin(mavenEnforcerPlugin()).build();
    assertThatCode(() -> gradleCommandHandler.handle(command)).doesNotThrowAnyException();
  }

  @Test
  void shouldAddGradleFreeConfigurationBlock() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");

    new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer).handle(
      new AddGradleConfiguration(
        """
        tasks.build {
          dependsOn("processResources")
        }

        tasks.processResources {
          filesMatching("**/*.yml") {
            filter { it.replace("@spring.profiles.active@", springProfilesActive) }
          }
          filesMatching("**/*.properties") {
            filter { it.replace("@spring.profiles.active@", springProfilesActive) }
          }
        }
        """
      )
    );

    assertThat(buildGradleContent(projectFolder)).contains(
      """
      tasks.build {
        dependsOn("processResources")
      }

      tasks.processResources {
        filesMatching("**/*.yml") {
          filter { it.replace("@spring.profiles.active@", springProfilesActive) }
        }
        filesMatching("**/*.properties") {
          filter { it.replace("@spring.profiles.active@", springProfilesActive) }
        }
      }

      // jhipster-needle-gradle-free-configuration-blocks\
      """
    );
  }

  @Test
  void shouldNotDuplicateExistingGradleFreeConfigurationBlock() {
    JHipsterProjectFolder projectFolder = projectFrom("src/test/resources/projects/empty-gradle");
    GradleCommandHandler gradleCommandHandler = new GradleCommandHandler(Indentation.DEFAULT, projectFolder, filesReader, templateRenderer);
    gradleCommandHandler.handle(
      new AddGradleConfiguration(
        """
        tasks.build {
          dependsOn("processResources")
        }
        """
      )
    );

    gradleCommandHandler.handle(
      new AddGradleConfiguration(
        """
        tasks.build {
          dependsOn("processResources")
        }
        """
      )
    );

    assertThat(buildGradleContent(projectFolder)).contains(
      """

      tasks.build {
        dependsOn("processResources")
      }

      // jhipster-needle-gradle-free-configuration-blocks\
      """
    );
  }

  private static String buildGradleContent(JHipsterProjectFolder projectFolder) {
    return content(Paths.get(projectFolder.get()).resolve("build.gradle.kts"));
  }

  private static String versionCatalogContent(JHipsterProjectFolder projectFolder) {
    return contentNormalizingNewLines(Paths.get(projectFolder.get()).resolve("gradle/libs.versions.toml"));
  }

  private static String pluginBuildGradleContent(JHipsterProjectFolder projectFolder) {
    return content(Paths.get(projectFolder.get()).resolve("buildSrc/build.gradle.kts"));
  }

  private static String scriptPluginContent(JHipsterProjectFolder projectFolder, BuildProfileId buildProfileId) {
    return content(Paths.get(projectFolder.get()).resolve("buildSrc/src/main/kotlin/profile-%s.gradle.kts".formatted(buildProfileId)));
  }

  private static void assertFileExists(JHipsterProjectFolder projectFolder, String other, String description) {
    Path profileGradlePath = Paths.get(projectFolder.get()).resolve(other);
    assertThat(Files.exists(profileGradlePath)).as(description, profileGradlePath.toString()).isTrue();
  }
}
