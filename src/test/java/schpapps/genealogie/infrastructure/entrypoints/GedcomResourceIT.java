package schpapps.genealogie.infrastructure.entrypoints;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Tests d'intégration des endpoints REST sur les fichiers GEDCOM.
 */
@QuarkusTest
class GedcomResourceIT {

    @Test
    void succes_importerGedcom() {
        final File tempFile = createTempGedcomFile();

        given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", tempFile, "text/plain")
                .when()
                .post("/api/gedcom/import")
                .then()
                .statusCode(200)
                .body(containsString("Fichier reçu"));
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

    private File createTempGedcomFile() {
        try {
            final File file = File.createTempFile("test", ".ged");
            file.deleteOnExit();

            return file;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
