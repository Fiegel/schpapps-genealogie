package schpapps.genealogie.infrastructure.entrypoints;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import schpapps.genealogie.domain.valueobject.Sexe;
import schpapps.genealogie.infrastructure.testprofile.PostgresTestProfile;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests d'intégration des endpoints REST sur les individus.
 */
@QuarkusTest
@TestProfile(PostgresTestProfile.class)
class IndividuResourceIT {

    @Test
    void succes_creerIndividu_retour_201() {
        // Given
        final String payloadJsonProvided = """
                {
                    "prenom": "Jérémy",
                    "nom": "Fiegel",
                    "sexe": "HOMME",
                    "dateNaissance": "1984-11-09"
                }
                """;

        // When - Then
        given().contentType(ContentType.JSON)
                .body(payloadJsonProvided)
                .when()
                .post("/api/individus")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nom", equalTo("Fiegel"))
                .body("prenom", equalTo("Jérémy"))
                .body("sexe", equalTo(Sexe.HOMME.name()))
                .body("dateNaissance", equalTo(LocalDate.of(1984, 11, 9).toString()));
    }

    @Test
    void echec_creerIndividu_retour_400_nom_vide() {
        // Given
        final String payloadJsonProvided = """
                {
                    "prenom": "Jérémy",
                    "nom": "",
                    "sexe": "HOMME",
                    "dateNaissance": "1984-11-09"
                }
                """;

        // When - Then
        given().contentType(ContentType.JSON)
                .body(payloadJsonProvided)
                .when()
                .post("/api/individus")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("nom ne peut pas être vide"))
                .body("timestamp", notNullValue());
    }

    @Test
    void echec_creerIndividu_retour_400_date_naissance_dans_le_futur() {
        // Given
        final String payloadJsonProvided = """
                {
                    "prenom": "Jérémy",
                    "nom": "Fiegel",
                    "sexe": "HOMME",
                    "dateNaissance": "2099-01-01"
                }
                """;

        // When - Then
        given().contentType(ContentType.JSON)
                .body(payloadJsonProvided)
                .when()
                .post("/api/individus")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("date de naissance ne peut pas être dans le futur"))
                .body("timestamp", notNullValue());
    }
}
