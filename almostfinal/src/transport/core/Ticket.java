package transport.core;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Ticket extends TitreTransport implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;  // Gestion des versions de sérialisation

    // ✅ Constructeur par défaut avec le prix standard
    public Ticket() {
        super();
        this.prix = 50.0; // Prix par défaut pour un ticket simple
    }

    // ✅ Constructeur avec un prix personnalisé
    public Ticket(double prix) {
        super();
        this.prix = prix;
    }

    @Override
    public boolean estValide(LocalDate date) throws TitreNonValideException {
        if (!date.equals(dateAchat)) {
            throw new TitreNonValideException("Ticket non valide pour cette date.");
        }
        return true;
    }
    // Dans Ticket.java
    @Override
    public String toString() {
        return String.format("Ticket [ID: %s, Prix: %s, Date: %s]",
                getId(), getPrix(), getDateAchat());
    }
}
