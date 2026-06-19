package schpapps.genealogie.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import schpapps.genealogie.domain.events.UtilisateurCreeEvent;
import schpapps.genealogie.domain.ports.inbound.EnregistrerUtilisateurScenario;
import schpapps.genealogie.domain.ports.inbound.commande.EnregistrerUtilisateurCommande;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.System.Logger.Level.ERROR;

/**
 * Le consumer des événements Kafka liés aux utilisateurs.
 */
@ApplicationScoped
public class UtilisateurEventConsumer {

    private static final System.Logger LOGGER = System.getLogger(UtilisateurEventConsumer.class.getName());

    private final EnregistrerUtilisateurScenario enregistrerUtilisateurScenario;

    private final ObjectMapper objectMapper;

    /**
     * Constructeur valué.
     *
     * @param enregistrerUtilisateurScenario Le scénario d'enregistrement d'un utilisateur.
     * @param objectMapper Le converter des JSON.
     */
    public UtilisateurEventConsumer(final EnregistrerUtilisateurScenario enregistrerUtilisateurScenario,
            final ObjectMapper objectMapper) {
        this.enregistrerUtilisateurScenario = enregistrerUtilisateurScenario;
        this.objectMapper = objectMapper;
    }

    /**
     * Consomme et applique la récéption d'un événement de création d'un utilisateur dans l'AS propriétaire.
     *
     * @param json L'événement de création d'un utilisateur en JSON.
     */
    @Incoming("utilisateurs-events")
    public void consommer(final String json) {
        try {
            final UtilisateurCreeEvent event = objectMapper.readValue(json, UtilisateurCreeEvent.class);

            var command = new EnregistrerUtilisateurCommande(event.id(), event.nom(), event.prenom());

            List<UtilisateurCreeEvent> eventList = Collections.singletonList(event);

            final UtilisateurCreeEvent[] array = eventList.toArray(new UtilisateurCreeEvent[0]);

            Arrays.parallelSort(new int[]{1, 2, 3, 4});

            enregistrerUtilisateurScenario.executer(command);
        } catch (JsonProcessingException e) {
            LOGGER.log(ERROR, "Problème détecté dans le mapping JSON vers UtilisateurCreeEvent");
        }
    }
}
