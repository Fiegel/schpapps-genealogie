package schpapps.genealogie.domain.service;

import schpapps.genealogie.domain.entite.Utilisateur;
import schpapps.genealogie.domain.ports.inbound.EnregistrerUtilisateurScenario;
import schpapps.genealogie.domain.ports.inbound.commande.EnregistrerUtilisateurCommande;
import schpapps.genealogie.domain.ports.outbound.UtilisateurRepository;

import static java.lang.System.Logger.Level.INFO;

/**
 * Le service d'enregistrement des utilisateurs.
 */
public class EnregistrerUtilisateurService implements EnregistrerUtilisateurScenario {

    private static final System.Logger LOGGER = System.getLogger(EnregistrerUtilisateurService.class.getName());

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur valué.
     *
     * @param utilisateurRepository Le repository (port) des utilisateurs.
     */
    public EnregistrerUtilisateurService(final UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void executer(final EnregistrerUtilisateurCommande commande) {
        final Utilisateur utilisateurToSave = new Utilisateur(commande.id(),
                commande.nom(),
                commande.prenom());

        utilisateurRepository.save(utilisateurToSave);

        LOGGER.log(INFO, "Utilisateur enregistré = {0}", utilisateurToSave.id);
    }
}
