package schpapps.genealogie.infrastructure.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import schpapps.genealogie.domain.ports.inbound.CreerIndividuUseCase;
import schpapps.genealogie.domain.ports.inbound.TraiterUtilisateurCreeUseCase;
import schpapps.genealogie.domain.ports.outbound.IndividuRepository;
import schpapps.genealogie.domain.ports.outbound.UtilisateurRepository;
import schpapps.genealogie.domain.service.CreerIndividuService;
import schpapps.genealogie.domain.service.TraiterUtilisateurCreeService;

/**
 * Configuration / déclaration des scénarios.
 */
public class UseCaseConfiguration {

    /**
     * Ajoute le scénario de création des individus.
     *
     * @return Le scénario de création des individus.
     */
    @Produces
    @ApplicationScoped
    public CreerIndividuUseCase creerIndividuUseCase(final IndividuRepository individuRepository) {
        return new CreerIndividuService(individuRepository);
    }

    /**
     * Ajoute le scénario d'enregistrement des utilisateurs.
     *
     * @return Le scénario d'enregistrement des utilisateurs.
     */
    @Produces
    @ApplicationScoped
    public TraiterUtilisateurCreeUseCase enregistrerUtilisateurUseCase(final UtilisateurRepository utilisateurRepository) {
        return new TraiterUtilisateurCreeService(utilisateurRepository);
    }
}
