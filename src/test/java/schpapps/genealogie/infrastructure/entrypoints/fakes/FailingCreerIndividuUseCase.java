package schpapps.genealogie.infrastructure.entrypoints.fakes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.ports.inbound.CreerIndividuUseCase;
import schpapps.genealogie.domain.ports.inbound.commande.CreerIndividuCommande;

/**
 * Implémentation de test du scénario de création d'un individu qui échoue.
 */
@Alternative
@ApplicationScoped
public class FailingCreerIndividuUseCase implements CreerIndividuUseCase {

    @Override
    public Individu executer(final CreerIndividuCommande commande) {
        throw new RuntimeException("Erreur serveur inattendue ou crash BDD");
    }
}
