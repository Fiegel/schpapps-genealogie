package schpapps.genealogie.infrastructure.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import schpapps.genealogie.domain.ports.inbound.TraiterUtilisateurCreeUseCase;
import schpapps.genealogie.domain.ports.inbound.commande.TraiterUtilisateurCreeCommande;
import schpapps.genealogie.infrastructure.kafka.event.UtilisateurCreeEvent;

/**
 * Le consumer des événements Kafka liés aux utilisateurs.
 */
@ApplicationScoped
public class UtilisateurEventConsumer {

    private final TraiterUtilisateurCreeUseCase traiterUtilisateurCreeUseCase;

    /**
     * Constructeur valué.
     *
     * @param traiterUtilisateurCreeUseCase Le scénario de traitement des créations d'utilisateurs.
     */
    public UtilisateurEventConsumer(final TraiterUtilisateurCreeUseCase traiterUtilisateurCreeUseCase) {
        this.traiterUtilisateurCreeUseCase = traiterUtilisateurCreeUseCase;
    }

    /**
     * Consomme et applique la récéption d'un événement de création d'un utilisateur dans l'AS propriétaire.
     *
     * @param event L'événement de création d'un utilisateur.
     */
    @Incoming("utilisateurs-events")
    @Transactional
    public void consommer(final UtilisateurCreeEvent event) {
        var commande = new TraiterUtilisateurCreeCommande(event.id(), event.nom(), event.prenom());

        traiterUtilisateurCreeUseCase.executer(commande);
    }
}
