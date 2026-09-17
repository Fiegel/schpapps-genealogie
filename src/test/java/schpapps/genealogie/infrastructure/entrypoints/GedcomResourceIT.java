package schpapps.genealogie.infrastructure.entrypoints;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.InformationComplementaire;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.RapportDiagnostic;

import java.io.File;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Tests d'intégration des endpoints REST sur les fichiers GEDCOM.
 */
@QuarkusTest
class GedcomResourceIT {

    @Test
    void succes_importerGedcom() {
        final File tempFile = createValidGedcomFile();

        given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", tempFile, "text/plain")
                .when()
                .post("/api/gedcom/import")
                .then()
                .statusCode(200)
                .body("filename", equalTo(tempFile.getName()))
                .body("dateUpload", notNullValue())

                .body("header", notNullValue())
                .body("header.informationComplementaireList", hasSize(1))
                .body("header.informationComplementaireList[0].lineNumber", equalTo(2))
                .body("header.informationComplementaireList[0].tag", equalTo("CHAR"))
                .body("header.informationComplementaireList[0].valeur", equalTo("UTF-8"))
                .body("header.informationComplementaireList[0].sousInformationList", emptyCollectionOf(InformationComplementaire.class))

                .body("individuList", hasSize(1))
                .body("individuList[0].id", equalTo("@I1@"))
                .body("individuList[0].informationComplementaireList", hasSize(2))
                .body("individuList[0].informationComplementaireList[0].lineNumber", equalTo(4))
                .body("individuList[0].informationComplementaireList[0].tag", equalTo("NAME"))
                .body("individuList[0].informationComplementaireList[0].valeur", equalTo("Jean /DUPONT/"))
                .body("individuList[0].informationComplementaireList[0].sousInformationList", emptyCollectionOf(InformationComplementaire.class))
                .body("individuList[0].informationComplementaireList[1].lineNumber", equalTo(5))
                .body("individuList[0].informationComplementaireList[1].tag", equalTo("FAMC"))
                .body("individuList[0].informationComplementaireList[1].valeur", equalTo("@F1@"))
                .body("individuList[0].informationComplementaireList[1].sousInformationList", emptyCollectionOf(InformationComplementaire.class))

                .body("familleList", hasSize(1))
                .body("familleList[0].id", equalTo("@F1@"))
                .body("familleList[0].informationComplementaireList", hasSize(1))
                .body("familleList[0].informationComplementaireList[0].lineNumber", equalTo(7))
                .body("familleList[0].informationComplementaireList[0].tag", equalTo("CHIL"))
                .body("familleList[0].informationComplementaireList[0].valeur", equalTo("@I1@"))
                .body("familleList[0].informationComplementaireList[0].sousInformationList", emptyCollectionOf(InformationComplementaire.class))

                .body("rapportDiagnosticList", emptyCollectionOf(RapportDiagnostic.class));
    }

    @Test
    void echec_importerGedcom_retour_400_fichier_manquant() {
        given()
                .contentType(ContentType.MULTIPART)
                .when()
                .post("/api/gedcom/import")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("message", containsString("fichier GEDCOM est requis"))
                .body("timestamp", notNullValue());
    }

    @Test
    void echec_importerGedcom_retour_400_extension_invalide() {
        given()
                .multiPart("file", "mon_arbre.txt", "1 NAME Jean /Dupont/".getBytes(), "text/plain")
                .when()
                .post("/api/gedcom/import")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("fichier non supporté"))
                .body("timestamp", notNullValue());
    }

    @Test
    void echec_importerGedcom_retour_400_structure_invalide() {
        final File tempFile = createInvalidGedcomFile();

        given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", tempFile, "text/plain")
                .when()
                .post("/api/gedcom/import")
                .then()
                .statusCode(400)
                .body("$", hasSize(2))

                .body("[0].lineNumber", equalTo(1))
                .body("[0].severite", equalTo("SEVERE"))
                .body("[0].message", containsString("En-tête invalide"))

                .body("[1].lineNumber", equalTo(5))
                .body("[1].severite", equalTo("SEVERE"))
                .body("[1].message", containsString("Trailer invalide"));
    }

    private File createInvalidGedcomFile() {
        try {
            final File file = File.createTempFile("test", ".ged");
            file.deleteOnExit();

            final String content = """
                    1 HEAD
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    1 NAME Jean /DUPONT/
                    1 TRLR
                    """;

            Files.writeString(file.toPath(), content);
            return file;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File createValidGedcomFile() {
        try {
            final File file = File.createTempFile("test", ".ged");
            file.deleteOnExit();

            final String content = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    1 NAME Jean /DUPONT/
                    1 FAMC @F1@
                    0 @F1@ FAM
                    1 CHIL @I1@
                    0 TRLR
                    """;

            Files.writeString(file.toPath(), content);
            return file;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
