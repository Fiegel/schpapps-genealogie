package schpapps.genealogie.domain.service;

import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.ports.inbound.CreerIndividuScenario;
import schpapps.genealogie.domain.ports.inbound.commande.CreerIndividuCommande;
import schpapps.genealogie.domain.ports.outbound.IndividuRepository;
import schpapps.genealogie.domain.valueobject.Sexe;

import static java.lang.System.Logger.Level.INFO;

/**
 * Le service de création des individus.
 */
public class CreerIndividuService implements CreerIndividuScenario {

    private static final System.Logger LOGGER = System.getLogger(CreerIndividuService.class.getName());

    private final IndividuRepository individuRepository;

    /**
     * Constructeur valué.
     *
     * @param individuRepository Le repository (port) des individus.
     */
    public CreerIndividuService(final IndividuRepository individuRepository) {
        this.individuRepository = individuRepository;
    }

    @Override
    public Individu executer(final CreerIndividuCommande commande) {
        final Individu individuToSave = Individu.generer(commande.nom(),
                commande.prenom(),
                Sexe.getByName(commande.sexe()),
                commande.dateNaissance());

        individuRepository.save(individuToSave);

        LOGGER.log(INFO, "Individu créé = {0}", individuToSave.id);

        return individuToSave;
    }
}
