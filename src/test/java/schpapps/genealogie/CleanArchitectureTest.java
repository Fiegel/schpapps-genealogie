package schpapps.genealogie;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Tests pour garantir le respect des règles de Clean Architecture.
 */
class CleanArchitectureTest {

    private static final String ROOT_PACKAGE = "schpapps.genealogie";

    @Test
    @DisplayName("Le domaine métier (hors tests) ne doit jamais dépendre de l'infrastructure")
    void le_domain_doit_etre_independant_hors_tests() {
        final ArchRule regleDomainIndependant = classes()
                .that().resideInAPackage("..schpapps.genealogie.domain..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage("..schpapps.genealogie.domain..",
                        "java..",
                        "lombok..");

        regleDomainIndependant.check(new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(ROOT_PACKAGE));
    }

    @Test
    @DisplayName("Les tests du domaine métier ne doivent jamais dépendre de l'infrastructure")
    void les_tests_du_domain_doivent_etre_independants() {
        final ArchRule regleDomainIndependant = classes()
                .that().resideInAPackage("..schpapps.genealogie.domain..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage("..schpapps.genealogie.domain..",
                        "..schpapps.genealogie.provider..",
                        "java..",
                        "lombok..",
                        "org.junit..",
                        "org.assertj..");

        regleDomainIndependant.check(new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.ONLY_INCLUDE_TESTS)
                .importPackages(ROOT_PACKAGE));
    }

    @Test
    @DisplayName("Respect strict des couches de la Clean Architecture")
    void respect_des_couches_clean_architecture() {
        final ArchRule regleArchitecture = layeredArchitecture()
                .consideringAllDependencies()
                .layer("Domain").definedBy("..schpapps.genealogie.domain..")
                .layer("Infrastructure").definedBy("..schpapps.genealogie.infrastructure..")
                .whereLayer("Domain").mayOnlyBeAccessedByLayers("Infrastructure")
                .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer();

        regleArchitecture.check(new ClassFileImporter().importPackages(ROOT_PACKAGE));
    }
}
