package schpapps.genealogie.domain.ports.outbound;

import lombok.Getter;
import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.valueobject.Sexe;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implémentation du repository des Individus pour les tests du domaine.
 */
public class InMemoryIndividuRepository implements IndividuRepository {

    @Getter
    private final Map<String, Individu> individuMap;

    public InMemoryIndividuRepository() {
        individuMap = new ConcurrentHashMap<>();

        getTestData().forEach(individu -> individuMap.put(individu.id, individu));
    }

    @Override
    public void save(final Individu individu) {
        individuMap.put(individu.id, individu);
    }

    @Override
    public Optional<Individu> getById(final String id) {
        return Optional.ofNullable(individuMap.get(id));
    }

    private Set<Individu> getTestData() {
        final Individu jeremyFiegel = Individu.generer("Fiegel", "Jérémy", Sexe.HOMME, LocalDate.of(1984, 11, 9));
        final Individu gertrudePetit = Individu.generer("Petit", "Gertrude", Sexe.FEMME, LocalDate.of(1982, 4, 2));

        return Set.of(jeremyFiegel, gertrudePetit);
    }
}
