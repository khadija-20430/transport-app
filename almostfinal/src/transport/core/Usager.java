package transport.core;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Usager extends Personne implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static int cpt = 1;
    public int num;

    public Usager(String nom, String prenom, LocalDate dateNaissance, boolean handicape) {
        super(nom, prenom, dateNaissance, handicape);
        this.num = cpt++;
    }
    @Override
    public String getType() {
        return "Usager";
    }
    public int getNum() {
        return num;
    }
}
