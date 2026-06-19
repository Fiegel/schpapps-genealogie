package schpapps.genealogie.infrastructure.entrypoints.dto;

import java.time.LocalDate;

/**
 * La requête DTO de la création d'un individu.
 *
 * @param nom Le nom à donner.
 * @param prenom Le prénom.
 * @param sexe Le sexe.
 * @param dateNaissance La date de naissance.
 */
public record CreerIndividuRequest(String nom,
        String prenom,
        String sexe,
        LocalDate dateNaissance) {
}
