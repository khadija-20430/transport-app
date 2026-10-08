package transport.core;
import java.time.*;


public class Employe extends Personne {
    public TypeFonction typeFonction;
    public String matricule;
    public Employe(String nom, String prenom, LocalDate dateNaissance, boolean handicape, String matricule, TypeFonction typeFonction) {
        super(nom, prenom, dateNaissance, handicape);
        this.matricule = matricule;
        this.typeFonction = typeFonction;
    }
    public String getMatricule() {
        return matricule;
    }

    public TypeFonction getTypeFonction() {
        return typeFonction;
    }
    @Override
    public String getType() {
        return "Employé";
    }
}

