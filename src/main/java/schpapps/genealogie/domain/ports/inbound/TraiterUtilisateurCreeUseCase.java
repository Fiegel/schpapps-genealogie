package schpapps.genealogie.domain.ports.inbound;

import schpapps.genealogie.domain.ports.inbound.commande.TraiterUtilisateurCreeCommande;

/**
 * Scénario de traitement d'un événement de création d'un utilisateur.
 */
public interface TraiterUtilisateurCreeUseCase {

    /**
     * Exécute le traitement d'un événement de création d'un utilisateur.
     *
     * @param commande La commande de traitement de la création d'un utilisateur.
     */
    void executer(final TraiterUtilisateurCreeCommande commande);

}
