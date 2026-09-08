package schpapps.genealogie.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitaires de l'énumération Sexe.
 */
public class SexeTest {

    @Test
    void devrait_couvrir_les_methodes_synthetiques_enum() {
        assertThat(Sexe.valueOf("HOMME")).isEqualTo(Sexe.HOMME);
        assertThat(Sexe.values()).containsExactlyInAnyOrder(Sexe.FEMME, Sexe.HOMME, Sexe.INCONNU);
    }

    @Test
    void devrait_retourner_sexe_approprie() {
        assertThat(Sexe.getByName("HOMME")).isEqualTo(Sexe.HOMME);
        assertThat(Sexe.getByName("HELICOPTERE")).isEqualTo(Sexe.INCONNU);
        assertThat(Sexe.getByName(null)).isEqualTo(Sexe.INCONNU);
    }
}
