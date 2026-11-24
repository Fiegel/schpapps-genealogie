package schpapps.genealogie.domain.entite;

import schpapps.genealogie.domain.valueobject.Sexe;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Un individu.
 */
public class Individu {

    private static final String PREFIXE_ID = "indi-";

    public final String id;

    public final String nom;

    public final String prenom;

    public final Sexe sexe;

    public final LocalDate dateNaissance;

    /**
     * Constructeur valué.
     *
     * @param id L'identifiant technique.
     * @param nom Le nom de naissance.
     * @param prenom Le prénom.
     * @param sexe Le sexe.
     * @param dateNaissance La date de naissance.
     */
    public Individu(final String id,
            final String nom,
            final String prenom,
            final Sexe sexe,
            final LocalDate dateNaissance) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
    }

    /**
     * Génère un nouvel individu (génère aussi son identifiant technique indi-).
     *
     * @param nom Le nom.
     * @param prenom Le prénom.
     * @param sexe Le sexe.
     * @param dateNaissance La date de naissance.
     * @return L'individu correspondant.
     */
    public static Individu generer(final String nom,
            final String prenom,
            final Sexe sexe,
            final LocalDate dateNaissance) {
        final String idGenere = PREFIXE_ID + UUID.randomUUID().toString();

        return new Individu(idGenere, nom, prenom, sexe, dateNaissance);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Individu individu)) return false;
        return Objects.equals(id, individu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Individu {"
                + "id='" + id + '\''
                + ", nom='" + nom + '\''
                + ", prenom='" + prenom + '\''
                + ", sexe=" + sexe
                + ", dateNaissance=" + dateNaissance
                + '}';
    }
}
