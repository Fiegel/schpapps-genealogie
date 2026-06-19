package schpapps.genealogie.domain.ports.inbound;

import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.ports.inbound.commande.CreerIndividuCommande;

/**
 * Scénario de la création d'un individu.
 */
public interface CreerIndividuScenario {

    /**
     * Exécute la création d'un individu.
     *
     * @param commande La commande contenant les données.
     * @return L'individu créé.
     */
    Individu executer(final CreerIndividuCommande commande);

}
