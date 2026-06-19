package schpapps.genealogie.infrastructure.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import schpapps.genealogie.domain.valueobject.Sexe;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

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
}
