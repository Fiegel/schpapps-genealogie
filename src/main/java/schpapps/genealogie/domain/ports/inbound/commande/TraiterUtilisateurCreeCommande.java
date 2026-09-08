package schpapps.genealogie.domain.ports.inbound.commande;

/**
 * La commande de traitement de la création d'un utilisateur.
 *
 * @param id L'identifiant technique de l'utilisateur.
 * @param nom Le nom.
 * @param prenom Le prénom.
 */
public record TraiterUtilisateurCreeCommande(String id, String nom, String prenom) {
}
