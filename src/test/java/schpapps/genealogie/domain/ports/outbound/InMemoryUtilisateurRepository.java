package schpapps.genealogie.domain.ports.outbound;

import lombok.Getter;
import schpapps.genealogie.domain.entite.Utilisateur;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implémentation du repository des Utilisateurs pour les tests du domaine.
 */
public class InMemoryUtilisateurRepository implements UtilisateurRepository {

    @Getter
    private final Map<String, Utilisateur> utilisateurMap;

    public InMemoryUtilisateurRepository() {
        utilisateurMap = new ConcurrentHashMap<>();

        getTestData().forEach(utilisateur -> utilisateurMap.put(utilisateur.id, utilisateur));
    }

    @Override
    public void save(final Utilisateur utilisateur) {
        utilisateurMap.put(utilisateur.id, utilisateur);
    }

    @Override
    public Optional<Utilisateur> getById(final String id) {
        return Optional.ofNullable(utilisateurMap.get(id));
    }

    private Set<Utilisateur> getTestData() {
        final Utilisateur jeremyFiegel = new Utilisateur("util-" + UUID.randomUUID(), "Fiegel", "Jérémy");
        final Utilisateur jeanPetit = new Utilisateur("util-" + UUID.randomUUID(), "Petit", "Jean");

        return Set.of(jeremyFiegel, jeanPetit);
    }
}
