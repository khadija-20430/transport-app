package transport.core;

import java.time.LocalDate;
import java.util.Objects;

public class ReclamationTechnique extends Reclamation {
    private final TypeProblemeTechnique typeProbleme;
    private final NiveauGravite gravite;

    private final Suspendable cible;  // Station ou MoyenTransport (peut être null pour Paiement/Service)


    public ReclamationTechnique(Personne personne, Suspendable cible,
                                String description, LocalDate date,
                                TypeProblemeTechnique typeProbleme,
                                NiveauGravite gravite) {
        super(personne, TypeReclamation.TECHNIQUE, description, date);
        this.cible = Objects.requireNonNull(cible, "La cible ne peut pas être null");
        this.typeProbleme = Objects.requireNonNull(typeProbleme);
        this.gravite = Objects.requireNonNull(gravite);

    }

    // Getters
    public TypeProblemeTechnique getTypeProbleme() {
        return typeProbleme;
    }

    public NiveauGravite getGravite() {
        return gravite;
    }




    public Suspendable getCible() {
        return cible;
    }

    public String getTypeCible() {
        return (cible instanceof Station) ? "Station" : "MoyenTransport";
    }

    @Override
    public String toString() {
        return String.format(
                "Réclamation Technique #%d\n" +
                        "Date: %s | Type: %s\n" +
                        "Cible: %s (%s)\n" +
                        "Problème: %s | Gravité: %s\n" +

                        "Soumis par: %s",
                getNumero(),
                getDate(),
                getType(),
                cible,
                getTypeCible(),
                typeProbleme,
                gravite,
                getPersonne()
        );
    }


}