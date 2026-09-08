package schpapps.genealogie.infrastructure.persistence.adapter;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import schpapps.genealogie.domain.entite.Utilisateur;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de {@link UtilisateurAdapter}.
 */
@QuarkusTest
@TestTransaction
class UtilisateurAdapterTest {

    @Inject
    private UtilisateurAdapter utilisateurAdapter;

    @Test
    void succes_save() {
        // Given
        final Utilisateur utilisateurProvided = new Utilisateur("util-1", "Doe", "John");

        // When
        utilisateurAdapter.save(utilisateurProvided);

        // Then
        final var utilisateurOptionalActual = utilisateurAdapter.getById(utilisateurProvided.id);
        assertThat(utilisateurOptionalActual).isPresent();

        final var utilisateurActual = utilisateurOptionalActual.get();
        assertThat(utilisateurActual).isNotNull();
        assertThat(utilisateurActual.id).isEqualTo(utilisateurProvided.id);
        assertThat(utilisateurActual.nom).isEqualTo(utilisateurProvided.nom);
        assertThat(utilisateurActual.prenom).isEqualTo(utilisateurProvided.prenom);
    }
}
