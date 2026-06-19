package schpapps.genealogie.infrastructure.entrypoints.dto;

import schpapps.genealogie.domain.entite.Individu;

import java.time.LocalDate;

/**
 * Response DTO de la création d'un individu.
 *
 * @param id L'identifiant technique de l'individu.
 * @param nom Le nom.
 * @param prenom Le prénom.
 * @param sexe Le sexe
 * @param dateNaissance La date de naissance.
 */
public record CreerIndividuResponse(String id,
        String nom,
        String prenom,
        String sexe,
        LocalDate dateNaissance) {

    /**
     * Convertit un individu en la réponse de sa demande de création.
     *
     * @param individu L'individu créé.
     * @return Les données de la réponse.
     */
    public static CreerIndividuResponse from(final Individu individu) {
        return new CreerIndividuResponse(individu.id,
                individu.nom,
                individu.prenom,
                individu.sexe.name(),
                individu.dateNaissance);
    }
}
