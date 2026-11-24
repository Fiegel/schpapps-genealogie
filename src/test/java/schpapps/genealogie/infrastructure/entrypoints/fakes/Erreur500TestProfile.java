package schpapps.genealogie.infrastructure.entrypoints.fakes;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Set;

/**
 * Indique au profil des tests d'intégration d'utiliser un UseCase qui échoue pour simuler une erreur 500.
 */
public class Erreur500TestProfile implements QuarkusTestProfile {

    @Override
    public Set<Class<?>> getEnabledAlternatives() {
        return Set.of(FailingCreerIndividuUseCase.class);
    }
}
