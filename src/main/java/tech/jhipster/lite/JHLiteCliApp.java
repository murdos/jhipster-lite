package tech.jhipster.lite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import picocli.CommandLine;
import tech.jhipster.lite.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;

@ExcludeFromGeneratedCodeCoverage(reason = "Not testing logs")
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class JHLiteCliApp implements CommandLineRunner, ExitCodeGenerator {

  private static final Logger log = LoggerFactory.getLogger(JHLiteCliApp.class);
  private final CommandLine.IFactory factory;
  private final JHLiteCommand command;
  private int exitCode;

  public JHLiteCliApp(CommandLine.IFactory factory, JHLiteCommand command) {
    this.factory = factory;
    this.command = command;
  }

  @Override
  public void run(String... args) throws Exception {
    exitCode = new CommandLine(command, factory).execute(args);
  }

  @Override
  public int getExitCode() {
    return exitCode;
  }

  public static void main(String[] args) {
    ConfigurableApplicationContext context = new SpringApplicationBuilder(JHLiteCliApp.class)
      .bannerMode(Banner.Mode.OFF)
      .web(WebApplicationType.NONE)
      .run(args);
    System.exit(SpringApplication.exit(context));
  }
}
