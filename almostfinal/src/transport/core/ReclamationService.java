package transport.core;

import java.time.LocalDate;

public class ReclamationService extends Reclamation {
    private final TypeIncident typeIncident;

    public ReclamationService(Personne personne, String description,
                              LocalDate date, TypeIncident typeIncident) {
        super(personne, TypeReclamation.SERVICE,  description, date);
        this.typeIncident = typeIncident;
    }

    // Getters
    public TypeIncident getTypeIncident() { return typeIncident; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | %s ",
                typeIncident);
    }
}