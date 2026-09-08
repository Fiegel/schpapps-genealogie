package schpapps.genealogie.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import schpapps.genealogie.domain.ports.InMemoryUtilisateurRepository;
import schpapps.genealogie.domain.ports.inbound.commande.TraiterUtilisateurCreeCommande;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * Tests unitaires du service de traitement des events de création d'utilisateurs.
 */
class TraiterUtilisateurCreeServiceTest {

    private InMemoryUtilisateurRepository utilisateurRepository;

    private TraiterUtilisateurCreeService traiterUtilisateurCreeService;

    @BeforeEach
    void setUp() {
        this.utilisateurRepository = new InMemoryUtilisateurRepository();
        this.traiterUtilisateurCreeService = new TraiterUtilisateurCreeService(utilisateurRepository);
    }

    @Test
    void succes_quand_tout_est_valide() {
        // Given
        final String idProvided = "123";
        final String nomProvided = "Doe";
        final String prenomProvided = "John";

        final TraiterUtilisateurCreeCommande commandeProvided = new TraiterUtilisateurCreeCommande(idProvided, nomProvided, prenomProvided);

        // When
        traiterUtilisateurCreeService.executer(commandeProvided);

        // Then
        assertThat(utilisateurRepository.getById(idProvided)).isPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("schpapps.genealogie.provider.TestProviders#blankStrings")
    void echec_quand_id_est_blank(final String idInvalideProvided) {
        // When
        assertThatThrownBy(() -> new TraiterUtilisateurCreeCommande(idInvalideProvided, "Doe", "John"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("identifiant technique ");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("schpapps.genealogie.provider.TestProviders#blankStrings")
    void echec_quand_nom_est_blank(final String nomInvalideProvided) {
        // When
        assertThatThrownBy(() -> new TraiterUtilisateurCreeCommande("123", nomInvalideProvided, "John"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(" nom ");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("schpapps.genealogie.provider.TestProviders#blankStrings")
    void echec_quand_prenom_est_blank(final String prenomInvalideProvided) {
        // When
        assertThatThrownBy(() -> new TraiterUtilisateurCreeCommande("123", "Doe", prenomInvalideProvided))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(" prénom ");
    }
}
