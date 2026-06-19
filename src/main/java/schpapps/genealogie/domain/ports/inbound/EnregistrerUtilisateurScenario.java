package schpapps.genealogie.domain.ports.inbound;

import schpapps.genealogie.domain.ports.inbound.commande.EnregistrerUtilisateurCommande;

/**
 * Scénario de l'enregistrement d'un utilisateur.
 */
public interface EnregistrerUtilisateurScenario {

    /**
     * Exécute l'enregistrement d'un utilisateur.
     *
     * @param commande La commande contenant les données.
     */
    void executer(final EnregistrerUtilisateurCommande commande);

}
