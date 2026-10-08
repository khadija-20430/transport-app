package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Reclamation implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int compteur = 1;

    private final int numero;
    private final LocalDate date;
    private final Personne personne;
    private final TypeReclamation type;
    private final String description;
    private boolean resolue;

    public Reclamation(Personne personne, TypeReclamation type,
                       String description, LocalDate date) {
        this.personne = Objects.requireNonNull(personne, "La personne ne peut pas être null");
        this.type = Objects.requireNonNull(type, "Le type ne peut pas être null");
        this.description = Objects.requireNonNull(description, "La description ne peut pas être null");
        this.date = Objects.requireNonNull(date, "La date ne peut pas être null");
        this.numero = compteur++;
        this.resolue = false;
    }

    // ==================== GETTERS ====================
    public int getNumero() { return numero; }
    public LocalDate getDate() { return date; }
    public Personne getPersonne() { return personne; }
    public TypeReclamation getType() { return type; }
    public String getDescription() { return description; }
    public boolean estResolue() { return resolue; }

    // ==================== MÉTHODES MÉTIER ====================
    public void resoudre() {
        this.resolue = true;
    }
    // ==================== AFFICHAGE ====================
    @Override
    public String toString() {
        return String.format(
                "Réclamation #%d\n" +
                        "• Date: %s\n" +
                        "• Type: %s\n" +
                        "• Description: %s\n" +
                        "• Statut: %s\n" +
                        "• Soumise par: %s",
                numero,
                date,
                type,

                description,
                resolue ? "Résolue" : "En cours",
                personne
        );
    }
}