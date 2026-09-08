package schpapps.genealogie.domain.ports.inbound.commande;

import java.time.LocalDate;

/**
 * La commande de la création d'un individu.
 *
 * @param nom Le nom.
 * @param prenom Le prénom.
 * @param sexe Le sexe.
 * @param dateNaissance La date de naissance.
 */
public record CreerIndividuCommande(String nom,
        String prenom,
        String sexe,
        LocalDate dateNaissance) {

    /**
     * Constructeur avec validation des données.
     *
     * @param nom Le nom.
     * @param prenom Le prénom.
     * @param sexe Le sexe.
     * @param dateNaissance La date de naissance.
     */
    public CreerIndividuCommande {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }

        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }

        if (dateNaissance != null && dateNaissance.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur.");
        }
    }
}
