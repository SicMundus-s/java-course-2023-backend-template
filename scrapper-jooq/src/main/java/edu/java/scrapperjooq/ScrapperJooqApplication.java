package edu.java.scrapperjooq;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Property;
import org.jooq.meta.jaxb.Target;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrapperJooqApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrapperJooqApplication.class, args);
        try {
            generateJooqClasses();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateJooqClasses() throws Exception {
        String targetPackage = "edu.java.scrapper.domain.jooq";
        String targetDirectory = "scrapper/src/main/java";

        Generate options = new Generate()
            .withGeneratedAnnotation(true)
            .withGeneratedAnnotationDate(false)
            .withNullableAnnotation(true)
            .withNullableAnnotationType("org.jetbrains.annotations.Nullable")
            .withNonnullAnnotation(true)
            .withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
            .withJpaAnnotations(false)
            .withValidationAnnotations(true)
            .withSpringAnnotations(true)
            .withConstructorPropertiesAnnotation(true)
            .withConstructorPropertiesAnnotationOnPojos(true)
            .withConstructorPropertiesAnnotationOnRecords(true)
            .withFluentSetters(false)
            .withDaos(false)
            .withPojos(true);

        Configuration configuration = new Configuration()
            .withGenerator(new Generator()
                .withGenerate(options)
                .withDatabase(new Database()
                    .withName("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")
                    .withProperties(
                        new Property().withKey("rootPath").withValue("migrations"),
                        new Property().withKey("scripts").withValue("master.xml")
                    ))
                .withTarget(new Target()
                    .withPackageName(targetPackage)
                    .withDirectory(targetDirectory)));

        GenerationTool.generate(configuration);
    }

}
