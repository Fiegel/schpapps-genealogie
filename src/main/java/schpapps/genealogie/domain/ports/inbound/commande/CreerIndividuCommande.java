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
}
