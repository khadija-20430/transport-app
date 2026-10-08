package transport.core;

import java.io.Serial;
import java.io.Serializable;

public class Station implements Suspendable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String nom;
    private boolean suspendu;
    private final boolean accessible;

    public Station(String nom, boolean accessible) {
        this.nom = nom;
        this.accessible = accessible;
        this.suspendu = false;
    }

    // Getters
    public String getNom() { return nom; }
    public boolean isAccessible() { return accessible; }

    // Suspendable implementation
    @Override public void suspendre() { this.suspendu = true; }
    @Override public void reactiver() { this.suspendu = false; }
    @Override public boolean estSuspendu() { return suspendu; }
    @Override public String getEtat() { return suspendu ? "suspendue" : "active"; }

    @Override
    public String toString() {
        return nom + (accessible ? " [Accessible]" : "") + (suspendu ? " (Suspendue)" : "");
    }
}