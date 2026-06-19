package schpapps.genealogie.domain.ports.outbound;

import schpapps.genealogie.domain.entite.Utilisateur;

import java.util.Optional;

/**
 * Le port des utilisateurs.
 */
public interface UtilisateurRepository {

    /**
     * Enregistre un utilisateur.
     *
     * @param utilisateur L'utilisateur à enregistrer.
     */
    void save(final Utilisateur utilisateur);

    /**
     * Recherche un utilisateur à partir de son identifiant technique.
     *
     * @param id L'identifiant technique de l'utilisateur recherché.
     * @return L'utilisateur trouvé.
     */
    Optional<Utilisateur> getById(final String id);
}
