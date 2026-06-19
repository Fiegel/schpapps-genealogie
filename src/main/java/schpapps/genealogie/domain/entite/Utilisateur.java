package schpapps.genealogie.domain.entite;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Un utilisateur de l'application.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Utilisateur {

    @EqualsAndHashCode.Include
    public final String id;

    public final String nom;

    public final String prenom;

    /**
     * Constructeur valué.
     *
     * @param id L'identifiant technique.
     * @param nom Le nom.
     * @param prenom Le prénom.
     */
    public Utilisateur(final String id, final String nom, final String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }
}
