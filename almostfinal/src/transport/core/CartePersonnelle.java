package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class CartePersonnelle extends TitreTransport implements Serializable {
    private static final long serialVersionUID = 1L;  // Identifiant de version pour la sérialisation

    public static int cpt = 0;
    public Personne personne;
    public String type;
    public ModePaiement modePaiement;
    public CartePersonnelle(Personne personne, ModePaiement modePaiement) throws ReductionImpossibleException {
        super();
        this.personne = personne;
        this.type = TypeCarte();
        this.prix = Prix();
        this.modePaiement = modePaiement;
    }

    public String TypeCarte() throws ReductionImpossibleException {
        boolean estEmploye = personne instanceof Employe;
        boolean estHandicape = personne.getHandicape();
        int age = LocalDate.now().getYear() - personne.getDateNaissance().getYear();

        if (estHandicape) return "SOLIDARITE";
        if (estEmploye) return "PARTENAIRE";
        if (age < 25) return "JUNIOR";
        if (age > 65) return "SENIOR";

        throw new ReductionImpossibleException("Vous n'avez droit à aucune réduction.");
    }

    public double Prix() {
        switch (type) {
            case "JUNIOR": return 5000 - 5000 * 0.3;
            case "SENIOR": return 5000 - 5000 * 0.25;
            case "SOLIDARITE": return 5000 * 0.5;
            case "PARTENAIRE": return 5000 - 5000 * 0.6;
            default: return 5000;
        }
    }

    @Override
    public boolean estValide(LocalDate date) throws TitreNonValideException {
        if (date.isBefore(dateAchat) || date.isAfter(dateAchat.plusYears(1))) {
            throw new TitreNonValideException("Carte personnelle non valide.");
        }
        return true;
    }

    public String getType() {
        return type;
    }
    public ModePaiement getModePaiement() {
        return modePaiement;
    }
    public Personne getPersonne() {
        return personne;
    }
}
