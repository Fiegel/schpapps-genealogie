package schpapps.genealogie.infrastructure.kafka;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.narayana.jta.QuarkusTransactionException;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import schpapps.genealogie.domain.ports.outbound.UtilisateurRepository;
import schpapps.genealogie.infrastructure.kafka.event.UtilisateurCreeEvent;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Tests d'intégration de la consommation d'événements de création d'utilisateurs.
 */
@QuarkusTest
class UtilisateurEventConsumerIT {

    @Inject
    @Connector("smallrye-in-memory")
    InMemoryConnector connector;

    @Inject
    UtilisateurRepository utilisateurRepository;

    InMemorySource<UtilisateurCreeEvent> utilisateurCreeEventSource;

    @BeforeEach
    void setUp() {
        utilisateurCreeEventSource = connector.source("utilisateurs-events");
    }

    @Test
    void succes_traitement_event_utilisateur_cree() {
        // Given
        final String utilisateurIdProvided = "util-" + UUID.randomUUID();
        final UtilisateurCreeEvent event = new UtilisateurCreeEvent(utilisateurIdProvided, "Doe", "John");

        // When
        utilisateurCreeEventSource.send(event);

        // Then
        await().atMost(Duration.ofSeconds(5))
                .ignoreExceptionsInstanceOf(QuarkusTransactionException.class)
                .untilAsserted(() -> {
                    QuarkusTransaction.requiringNew().run(() -> {
                        var utilisateurActual = utilisateurRepository.getById(utilisateurIdProvided);
                        assertThat(utilisateurActual).isPresent();
                        assertThat(utilisateurActual.get().prenom).isEqualTo("John");
                        assertThat(utilisateurActual.get().nom).isEqualTo("Doe");
                    });
                });
    }
}
