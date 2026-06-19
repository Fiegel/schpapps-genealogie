package schpapps.genealogie.infrastructure.persistence.mapper;

import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.valueobject.Sexe;
import schpapps.genealogie.infrastructure.persistence.model.IndividuModel;

/**
 * Le mapper entre {@link IndividuModel} et {@link Individu}.
 */
public class IndividuMapper {

    /**
     * Convertit un {@link Individu} en un {@link IndividuModel}.
     *
     * @param individu L'entité du domain à convertir.
     * @return Le modèle équivalent.
     */
    public static IndividuModel toModel(final Individu individu) {
        if(individu == null) {
            return null;
        }

        final IndividuModel individuModel = new IndividuModel();
        individuModel.setId(individu.id);
        individuModel.setNom(individu.nom);
        individuModel.setPrenom(individu.prenom);
        individuModel.setSexe(individu.sexe.name());
        individuModel.setDateNaissance(individu.dateNaissance);

        return individuModel;
    }

    /**
     * Convertit un {@link IndividuModel} en un {@link Individu}.
     *
     * @param individuModel Le modèle à convertir.
     * @return L'entité du domain équivalente.
     */
    public static Individu toDomain(final IndividuModel individuModel) {
        if(individuModel == null) {
            return null;
        }

        return new Individu(individuModel.getId(),
                individuModel.getNom(),
                individuModel.getPrenom(),
                Sexe.getByName(individuModel.getSexe()),
                individuModel.getDateNaissance());
    }
}
