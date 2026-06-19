package schpapps.genealogie.domain.entite;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import schpapps.genealogie.domain.valueobject.Sexe;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Un individu.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Individu {

    private static final String PREFIXE_ID = "indi-";

    @EqualsAndHashCode.Include
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
}
