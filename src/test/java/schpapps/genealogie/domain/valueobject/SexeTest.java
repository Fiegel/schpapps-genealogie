package schpapps.genealogie.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitaires de l'énumération Sexe.
 */
class SexeTest {

    @Test
    void devrait_retourner_sexe_approprie() {
        assertThat(Sexe.getByName("HOMME")).isEqualTo(Sexe.HOMME);
        assertThat(Sexe.getByName("HELICOPTERE")).isEqualTo(Sexe.INCONNU);
        assertThat(Sexe.getByName(null)).isEqualTo(Sexe.INCONNU);
    }
}
