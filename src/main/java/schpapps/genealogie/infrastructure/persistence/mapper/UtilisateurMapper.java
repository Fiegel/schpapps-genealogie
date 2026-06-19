package schpapps.genealogie.infrastructure.persistence.mapper;

import schpapps.genealogie.domain.entite.Utilisateur;
import schpapps.genealogie.infrastructure.persistence.model.UtilisateurModel;

/**
 * Le mapper entre {@link UtilisateurModel} et {@link Utilisateur}.
 */
public class UtilisateurMapper {

    /**
     * Convertit un {@link Utilisateur} en un {@link UtilisateurModel}.
     *
     * @param utilisateur L'entité du domain à convertir.
     * @return Le modèle équivalent.
     */
    public static UtilisateurModel toModel(final Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }

        final UtilisateurModel utilisateurModel = new UtilisateurModel();
        utilisateurModel.setId(utilisateur.id);
        utilisateurModel.setNom(utilisateur.nom);
        utilisateurModel.setPrenom(utilisateur.prenom);

        return utilisateurModel;
    }

    /**
     * Convertit un {@link UtilisateurModel} en un {@link Utilisateur}.
     *
     * @param utilisateurModel Le modèle à convertir.
     * @return L'entité du domain équivalente.
     */
    public static Utilisateur toDomain(final UtilisateurModel utilisateurModel) {
        if (utilisateurModel == null) {
            return null;
        }

        return new Utilisateur(utilisateurModel.getId(),
                utilisateurModel.getNom(),
                utilisateurModel.getPrenom());
    }
}
