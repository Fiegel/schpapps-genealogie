package schpapps.genealogie.infrastructure.kafka.event;

/**
 * Event représentant la création d'un utilisateur.
 *
 * @param id L'identifiant technique de l'utilisateur.
 * @param nom Le nom.
 * @param prenom Le prénom.
 */
public record UtilisateurCreeEvent(String id, String nom, String prenom) {
}
