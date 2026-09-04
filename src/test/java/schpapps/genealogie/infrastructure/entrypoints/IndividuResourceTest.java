package schpapps.genealogie.infrastructure.entrypoints;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.ports.inbound.CreerIndividuScenario;
import schpapps.genealogie.domain.ports.inbound.commande.CreerIndividuCommande;
import schpapps.genealogie.domain.valueobject.Sexe;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;

@QuarkusTest
class IndividuResourceTest {

    @InjectMock
    private CreerIndividuScenario creerIndividuScenario;

    @Test
    void succes_creerIndividu_retour_201() {
        // Given
        final Individu individuProvided = Individu.generer("Fiegel", "Jérémy", Sexe.HOMME, LocalDate.of(1984, 11, 9));

        when(creerIndividuScenario.executer(Mockito.any(CreerIndividuCommande.class)))
                .thenReturn(individuProvided);

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
                .body("id", equalTo(individuProvided.id))
                .body("nom", equalTo("Fiegel"))
                .body("prenom", equalTo("Jérémy"))
                .body("sexe", equalTo(Sexe.HOMME.name()))
                .body("dateNaissance", equalTo(LocalDate.of(1984, 11, 9).toString()));
    }
}
