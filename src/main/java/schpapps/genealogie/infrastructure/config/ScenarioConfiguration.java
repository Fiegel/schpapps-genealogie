package schpapps.genealogie.infrastructure.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import schpapps.genealogie.domain.ports.inbound.CreerIndividuScenario;
import schpapps.genealogie.domain.ports.inbound.EnregistrerUtilisateurScenario;
import schpapps.genealogie.domain.ports.outbound.IndividuRepository;
import schpapps.genealogie.domain.ports.outbound.UtilisateurRepository;
import schpapps.genealogie.domain.service.CreerIndividuService;
import schpapps.genealogie.domain.service.EnregistrerUtilisateurService;

/**
 * Configuration / déclaration des scénarios.
 */
public class ScenarioConfiguration {

    /**
     * Ajoute le scénario de création des individus.
     *
     * @return Le scénario de création des individus.
     */
    @Produces
    @ApplicationScoped
    public CreerIndividuScenario creerIndividuScenario(final IndividuRepository individuRepository) {
        return new CreerIndividuService(individuRepository);
    }

    /**
     * Ajoute le scénario d'enregistrement des utilisateurs.
     *
     * @return Le scénario d'enregistrement des utilisateurs.
     */
    @Produces
    @ApplicationScoped
    public EnregistrerUtilisateurScenario enregistrerUtilisateurScenario(final UtilisateurRepository utilisateurRepository) {
        return new EnregistrerUtilisateurService(utilisateurRepository);
    }
}
