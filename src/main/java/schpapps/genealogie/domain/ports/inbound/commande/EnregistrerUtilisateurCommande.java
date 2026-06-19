package schpapps.genealogie.domain.ports.inbound.commande;

/**
 * La commande de l'enregistrement d'un utilisateur.
 *
 * @param id L'identifiant technique.
 * @param nom Le nom.
 * @param prenom Le prénom.
 */
public record EnregistrerUtilisateurCommande(String id,
        String nom,
        String prenom) {
}
