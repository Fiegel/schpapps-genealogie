package schpapps.genealogie.domain.ports.inbound.commande;

/**
 * La commande de traitement de la création d'un utilisateur.
 *
 * @param id L'identifiant technique de l'utilisateur.
 * @param nom Le nom.
 * @param prenom Le prénom.
 */
public record TraiterUtilisateurCreeCommande(String id, String nom, String prenom) {

    /**
     * Constructeur avec validation des données.
     *
     * @param id L'identifiant technique de l'utilisateur.
     * @param nom Le nom.
     * @param prenom Le prénom.
     */
    public TraiterUtilisateurCreeCommande {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'identifiant technique ne peut pas être vide.");
        }

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }

        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
    }
}
