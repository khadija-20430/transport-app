package transport.core;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

// Classe abstraite TitreTransport
public abstract class TitreTransport implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;  // Identifiant de version pour la sérialisation

    public String id;
    public LocalDate dateAchat;
    public double prix;
    public boolean estValide;

    public TitreTransport() {
        this.dateAchat = LocalDate.now();
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public double getPrix() {
        return prix;
    }

    public abstract boolean estValide(LocalDate date) throws TitreNonValideException;
}
