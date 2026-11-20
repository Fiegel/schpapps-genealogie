package schpapps.genealogie.infrastructure.testprofile;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * Indique au profil des tests d'intégration d'utiliser Postgresql.
 */
public class PostgresTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "it";
    }
}
