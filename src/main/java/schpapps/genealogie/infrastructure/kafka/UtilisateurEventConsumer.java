package schpapps.genealogie.infrastructure.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import schpapps.genealogie.domain.events.UtilisateurCreeEvent;
import schpapps.genealogie.domain.ports.inbound.TraiterUtilisateurCreeScenario;
import schpapps.genealogie.domain.ports.inbound.commande.TraiterUtilisateurCreeCommande;

/**
 * Le consumer des événements Kafka liés aux utilisateurs.
 */
@ApplicationScoped
public class UtilisateurEventConsumer {

    private final TraiterUtilisateurCreeScenario traiterUtilisateurCreeScenario;

    /**
     * Constructeur valué.
     *
     * @param traiterUtilisateurCreeScenario Le scénario de traitement des créations d'utilisateurs.
     */
    public UtilisateurEventConsumer(final TraiterUtilisateurCreeScenario traiterUtilisateurCreeScenario) {
        this.traiterUtilisateurCreeScenario = traiterUtilisateurCreeScenario;
    }

    /**
     * Consomme et applique la récéption d'un événement de création d'un utilisateur dans l'AS propriétaire.
     *
     * @param event L'événement de création d'un utilisateur.
     */
    @Incoming("utilisateurs-events")
    public void consommer(final UtilisateurCreeEvent event) {
        var commande = new TraiterUtilisateurCreeCommande(event.id(), event.nom(), event.prenom());

        traiterUtilisateurCreeScenario.executer(commande);
    }
}
