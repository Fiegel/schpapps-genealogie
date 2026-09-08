package schpapps.genealogie.domain.service;

import schpapps.genealogie.domain.entite.Utilisateur;
import schpapps.genealogie.domain.ports.inbound.TraiterUtilisateurCreeUseCase;
import schpapps.genealogie.domain.ports.inbound.commande.TraiterUtilisateurCreeCommande;
import schpapps.genealogie.domain.ports.outbound.UtilisateurRepository;

import static java.lang.System.Logger.Level.INFO;

/**
 * Le service de traitement des events de création d'utilisateurs.
 */
public class TraiterUtilisateurCreeService implements TraiterUtilisateurCreeUseCase {

    private static final System.Logger LOGGER = System.getLogger(TraiterUtilisateurCreeService.class.getName());

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur valué.
     *
     * @param utilisateurRepository Le repository (port) des utilisateurs.
     */
    public TraiterUtilisateurCreeService(final UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void executer(final TraiterUtilisateurCreeCommande commande) {
        final Utilisateur utilisateurToSave = new Utilisateur(commande.id(),
                commande.nom(),
                commande.prenom());

        utilisateurRepository.save(utilisateurToSave);

        LOGGER.log(INFO, "Utilisateur créé = {0}", utilisateurToSave.id);
    }
}
