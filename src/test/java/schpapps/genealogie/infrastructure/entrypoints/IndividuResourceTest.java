package schpapps.genealogie.infrastructure.entrypoints;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.ports.inbound.CreerIndividuUseCase;
import schpapps.genealogie.domain.ports.inbound.commande.CreerIndividuCommande;
import schpapps.genealogie.domain.valueobject.Sexe;
import schpapps.genealogie.infrastructure.entrypoints.dto.CreerIndividuRequest;
import schpapps.genealogie.infrastructure.entrypoints.dto.CreerIndividuResponse;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitaires des endpoints REST sur les individus.
 */
@ExtendWith(MockitoExtension.class)
class IndividuResourceTest {

    @Mock
    private CreerIndividuUseCase creerIndividuUseCase;

    @InjectMocks
    private IndividuResource individuResource;

    @Test
    void succes_creerIndividu_retour_201() {
        // Given
        final LocalDate dateNaissanceProvided = LocalDate.of(1990, 1, 1);
        final CreerIndividuRequest requestProvided = new CreerIndividuRequest("Dupont", "Jean", "HOMME", dateNaissanceProvided);

        final Individu individuProvided = new Individu("indi-123", "Dupont", "Jean", Sexe.HOMME, dateNaissanceProvided);
        when(creerIndividuUseCase.executer(any(CreerIndividuCommande.class)))
                .thenReturn(individuProvided);

        // When
        try (final Response responseActual = individuResource.creerIndividu(requestProvided)) {
            // Then
            assertThat(responseActual.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
            assertThat(responseActual.getEntity()).isInstanceOf(CreerIndividuResponse.class);

            final ArgumentCaptor<CreerIndividuCommande> argumentCaptor = ArgumentCaptor.forClass(CreerIndividuCommande.class);
            verify(creerIndividuUseCase).executer(argumentCaptor.capture());

            final CreerIndividuCommande commandeActual = argumentCaptor.getValue();
            assertThat(commandeActual.nom()).isEqualTo("Dupont");
            assertThat(commandeActual.prenom()).isEqualTo("Jean");
            assertThat(commandeActual.sexe()).isEqualTo("HOMME");
            assertThat(commandeActual.dateNaissance()).isEqualTo(dateNaissanceProvided);
        }
    }

    @Test
    @SuppressWarnings("resource")
    void echec_creerIndividu_use_case_exception() {
        // Given
        final LocalDate dateNaissanceProvided = LocalDate.of(1990, 1, 1);
        final CreerIndividuRequest requestProvided = new CreerIndividuRequest("Dupont", "Jean", "HOMME", dateNaissanceProvided);

        when(creerIndividuUseCase.executer(any()))
                .thenThrow(new IllegalArgumentException("Données individu invalides"));

        // When - Then
        assertThatThrownBy(() -> individuResource.creerIndividu(requestProvided))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Données individu invalides");
    }
}
