package transport.core;
import java.io.Serial;
import java.io.Serializable;

public class MoyenTransport implements Suspendable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String identifiant;
    private final String ligne;
    private boolean suspendu;

    public MoyenTransport(String identifiant, String ligne) {
        if (identifiant == null || ligne == null) {
            throw new IllegalArgumentException("L'identifiant et la ligne ne peuvent pas être null");
        }
        this.identifiant = identifiant;
        this.ligne = ligne;
        this.suspendu = false;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getLigne() {
        return ligne;
    }

    @Override
    public String toString() {
        return identifiant + " - Ligne " + ligne + (suspendu ? " (Suspendu)" : "");
    }

    @Override
    public void suspendre() {
        this.suspendu = true;
    }

    @Override
    public void reactiver() {
        this.suspendu = false;
    }

    @Override
    public boolean estSuspendu() {
        return suspendu;
    }

    @Override
    public String getEtat() {
        return suspendu ? "suspendu" : "actif";
    }

    // Méthodes equals et hashCode pour une bonne pratique
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoyenTransport)) return false;
        MoyenTransport that = (MoyenTransport) o;
        return identifiant.equals(that.identifiant) && ligne.equals(that.ligne);
    }

    @Override
    public int hashCode() {
        return 31 * identifiant.hashCode() + ligne.hashCode();
    }
}