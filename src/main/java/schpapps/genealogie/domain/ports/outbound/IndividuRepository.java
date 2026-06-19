package schpapps.genealogie.domain.ports.outbound;

import schpapps.genealogie.domain.entite.Individu;

import java.util.Optional;

/**
 * Le port des individus.
 */
public interface IndividuRepository {

    /**
     * Enregistre un individu.
     *
     * @param individu L'individu à enregistrer.
     */
    void save(final Individu individu);

    /**
     * Recherche un individu à partir de son identifiant technique.
     *
     * @param id L'identifiant technique de l'individu recherché.
     * @return L'individu trouvé.
     */
    Optional<Individu> getById(final String id);
}
