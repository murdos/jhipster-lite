package tech.jhipster.lite;

import static tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import tech.jhipster.lite.module.application.JHipsterModulesApplicationService;
import tech.jhipster.lite.module.domain.JHipsterModuleSlug;
import tech.jhipster.lite.module.domain.JHipsterModulesToApply;
import tech.jhipster.lite.module.domain.properties.JHipsterModuleProperties;
import tech.jhipster.lite.project.application.ProjectsApplicationService;
import tech.jhipster.lite.project.domain.ProjectPath;
import tech.jhipster.lite.project.domain.history.ModuleParameters;
import tech.jhipster.lite.project.domain.history.ProjectHistory;

@Component
@CommandLine.Command(name = "generate")
public class JHLiteCommand implements Callable<Integer> {

  private final JHipsterModulesApplicationService modules;
  private final ProjectsApplicationService projects;

  public static final String PROJECT_BASE_NAME_PARAMETER = "baseName";
  public static final String SERVER_PORT_PARAMETER = "serverPort";
  public static final String SPRING_CONFIGURATION_FORMAT = "springConfigurationFormat";

  @Option(names = "--root-package", description = "Root package name")
  private String rootPackageName;

  @Option(names = "--indentation", description = "Indentation size")
  private Integer indentSize;

  @Option(names = "--project-name", description = "Project name")
  private String projectName;

  @Option(names = "--commit", description = "Commit changes", defaultValue = "true", negatable = true)
  private Boolean commit;

  @Parameters(description = "Modules to generates")
  private String[] slugs = {};

  public JHLiteCommand(JHipsterModulesApplicationService modules, ProjectsApplicationService projects) {
    this.modules = modules;
    this.projects = projects;
  }

  public Integer call() throws Exception {
    JHipsterModuleProperties properties = new JHipsterModuleProperties(".", commit, parameters());
    JHipsterModulesToApply modulesToApply = new JHipsterModulesToApply(Stream.of(slugs).map(JHipsterModuleSlug::new).toList(), properties);
    modules.apply(modulesToApply);

    return 0;
  }

  private Map<String, Object> parameters() {
    ProjectHistory history = projects.getHistory(new ProjectPath("."));
    return history.latestProperties().merge(new ModuleParameters(parametersFromOptions())).get();
  }

  private Map<String, Object> parametersFromOptions() {
    Map<String, Object> parameters = new HashMap<>();
    if (rootPackageName != null) {
      parameters.put(BASE_PACKAGE_PARAMETER, rootPackageName);
    }
    if (projectName != null) {
      parameters.put(PROJECT_NAME_PARAMETER, projectName);
    }
    if (indentSize != null) {
      parameters.put(INDENTATION_PARAMETER, indentSize);
    }
    return parameters;
  }
}
