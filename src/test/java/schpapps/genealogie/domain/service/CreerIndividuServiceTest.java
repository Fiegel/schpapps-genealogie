package schpapps.genealogie.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.ports.InMemoryIndividuRepository;
import schpapps.genealogie.domain.ports.inbound.commande.CreerIndividuCommande;
import schpapps.genealogie.domain.valueobject.Sexe;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * Tests unitaires du service de création des individus.
 */
class CreerIndividuServiceTest {

    private InMemoryIndividuRepository individuRepository;

    private CreerIndividuService creerIndividuService;

    @BeforeEach
    void setUp() {
        this.individuRepository = new InMemoryIndividuRepository();
        this.creerIndividuService = new CreerIndividuService(individuRepository);
    }

    @Test
    void succes_quand_tout_est_valide() {
        // Given
        final String nomProvided = "Doe";
        final String prenomProvided = "John";
        final String sexeProvided = "HOMME";
        final LocalDate dateNaissanceProvided = LocalDate.of(1999, 1, 10);

        final CreerIndividuCommande commandeProvided = new CreerIndividuCommande(nomProvided, prenomProvided, sexeProvided, dateNaissanceProvided);

        // When
        final Individu individuResponseActual = creerIndividuService.executer(commandeProvided);

        // Then
        assertThat(individuResponseActual).isNotNull();
        assertThat(individuResponseActual.id).isNotBlank();
        assertThat(individuResponseActual.nom).isEqualTo(nomProvided);
        assertThat(individuResponseActual.prenom).isEqualTo(prenomProvided);
        assertThat(individuResponseActual.sexe.name()).isEqualTo(sexeProvided);
        assertThat(individuResponseActual.dateNaissance).isEqualTo(dateNaissanceProvided);

        assertThat(individuRepository.getById(individuResponseActual.id)).isPresent();
    }

    @Test
    void succes_avec_champs_optionnels() {
        // Given
        final String nomProvided = "Doe";
        final String prenomProvided = "John";
        final String sexeProvided = null;
        final LocalDate dateNaissanceProvided = null;

        final CreerIndividuCommande commandeProvided = new CreerIndividuCommande(nomProvided, prenomProvided, sexeProvided, dateNaissanceProvided);

        // When
        final Individu individuResponseActual = creerIndividuService.executer(commandeProvided);

        // Then
        assertThat(individuResponseActual).isNotNull();
        assertThat(individuResponseActual.id).isNotBlank();
        assertThat(individuResponseActual.nom).isEqualTo(nomProvided);
        assertThat(individuResponseActual.prenom).isEqualTo(prenomProvided);
        assertThat(individuResponseActual.sexe).isEqualTo(Sexe.INCONNU);
        assertThat(individuResponseActual.dateNaissance).isNull();

        assertThat(individuRepository.getById(individuResponseActual.id)).isPresent();
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("schpapps.genealogie.provider.TestProviders#blankStrings")
    void echec_quand_nom_est_blank(final String nomInvalideProvided) {
        // When
        assertThatThrownBy(() -> new CreerIndividuCommande(nomInvalideProvided, "John", "HOMME", LocalDate.of(1999, 1, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(" nom ");
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("schpapps.genealogie.provider.TestProviders#blankStrings")
    void echec_quand_prenom_est_blank(final String prenomInvalideProvided) {
        // When
        assertThatThrownBy(() -> new CreerIndividuCommande("Doe", prenomInvalideProvided, "HOMME", LocalDate.of(1999, 1, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(" prénom ");
    }

    @ParameterizedTest
    @MethodSource("schpapps.genealogie.provider.TestProviders#futureLocalDates")
    void echec_quand_dateNaissance_dans_le_futur(final LocalDate dateNaissanceInvalideProvided) {
        // When
        assertThatThrownBy(() -> new CreerIndividuCommande("Doe", "John", "HOMME", dateNaissanceInvalideProvided))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(" date de naissance ");
    }
}
