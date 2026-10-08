package transport.core;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Personne implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;  // Ajoutez cette ligne pour la gestion des versions de sérialisation

    protected String nom;
    protected String prenom;
    protected LocalDate dateNaissance;
    protected boolean handicape;

    public Personne(String nom, String prenom, LocalDate dateNaissance, boolean handicape) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.handicape = handicape;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public boolean getHandicape() {
        return handicape;
    }
    public abstract String getType();
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateStr = (dateNaissance != null) ? dateNaissance.format(formatter) : "date inconnue";
        return nom + " " + prenom + " (Né(e) le : " + dateStr + ") " + (handicape ? "Handicapé" : "Non handicapé");
    }


}
