package schpapps.genealogie.domain.entite;

import java.util.Objects;

/**
 * Un utilisateur de l'application.
 */
public class Utilisateur {

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

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Utilisateur that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Utilisateur {"
                + "id='" + id + '\''
                + ", nom='" + nom + '\''
                + ", prenom='" + prenom + '\''
                + '}';
    }
}
