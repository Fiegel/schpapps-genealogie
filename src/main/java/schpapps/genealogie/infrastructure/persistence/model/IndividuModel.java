package schpapps.genealogie.infrastructure.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "INDIVIDU")
public class IndividuModel {

    @Column(name = "INDI_ID")
    @Id
    private String id;

    @Column(name = "INDI_NOM")
    private String nom;

    @Column(name = "INDI_PRENOM")
    private String prenom;

    @Column(name = "INDI_SEXE")
    private String sexe;

    @Column(name = "INDI_DATE_NAISSANCE")
    private LocalDate dateNaissance;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(final String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(final String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(final String sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(final LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}
