package schpapps.genealogie.domain.events;

/**
 * Event représentant la création d'un utilisateur.
 *
 * @param id L'identifiant technique de l'utilisateur.
 * @param nom Le nom.
 * @param prenom Le prénom.
 */
public record UtilisateurCreeEvent(String id, String nom, String prenom) {
}
