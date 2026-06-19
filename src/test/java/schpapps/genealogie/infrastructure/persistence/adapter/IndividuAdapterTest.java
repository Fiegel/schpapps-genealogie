package schpapps.genealogie.infrastructure.persistence.adapter;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.valueobject.Sexe;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de {@link IndividuAdapter}.
 */
@QuarkusTest
@TestTransaction
class IndividuAdapterTest {

    @Inject
    private IndividuAdapter individuAdapter;

    @Test
    void succes_save() {
        // Given
        final Individu individuProvided = Individu.generer("Fiegel", "Jérémy", Sexe.HOMME, LocalDate.of(1984, 11, 9));

        // When
        individuAdapter.save(individuProvided);

        // Then
        assertThat(individuProvided.id).isNotBlank();

        final Optional<Individu> individuOptionalActual = individuAdapter.getById(individuProvided.id);
        assertThat(individuOptionalActual).isPresent();

        final Individu individuActual = individuOptionalActual.get();
        assertThat(individuActual).isNotNull();
        assertThat(individuActual.id).isEqualTo(individuProvided.id);
        assertThat(individuActual.nom).isEqualTo(individuProvided.nom);
        assertThat(individuActual.prenom).isEqualTo(individuProvided.prenom);
        assertThat(individuActual.sexe).isEqualTo(individuProvided.sexe);
        assertThat(individuActual.dateNaissance).isEqualTo(individuProvided.dateNaissance);
    }
}
