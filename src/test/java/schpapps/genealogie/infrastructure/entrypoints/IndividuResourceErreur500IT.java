package schpapps.genealogie.infrastructure.entrypoints;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import schpapps.genealogie.infrastructure.entrypoints.fakes.Erreur500TestProfile;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

/**
 * Test d'intégration des endpoints REST sur les individus, simulant une erreur 500 inattendue.
 */
@QuarkusTest
@TestProfile(Erreur500TestProfile.class)
class IndividuResourceErreur500IT {

    @Test
    void erreur_500_sur_exception_imprevue() {
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
                .statusCode(500)
                .body("status", equalTo(500))
                .body("error", equalTo("Internal Server Error"))
                .body("message", containsString("erreur inattendue"))
                .body("timestamp", notNullValue());
    }
}
