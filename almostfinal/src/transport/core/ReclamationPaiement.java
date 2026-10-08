package transport.core;

import java.time.LocalDate;

public class ReclamationPaiement extends Reclamation {
    private final double somme;
    private final ModePaiement modePaiement;

    public ReclamationPaiement(Personne personne, String description,
                               LocalDate date, double somme,
                               ModePaiement modePaiement) {
        super(personne, TypeReclamation.PAIEMENT,  description, date);
        this.somme = somme;
        this.modePaiement = modePaiement;
    }

    // Getters
    public double getSomme() { return somme; }
    public ModePaiement getModePaiement() { return modePaiement; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | %.2f %s ",
                somme, modePaiement);
    }
}