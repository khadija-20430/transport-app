package transport.control;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import transport.core.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GestionReclamation {
    private final TransportController transportController;
    final ServiceReclamation serviceReclamation;

    public GestionReclamation(TransportController transportController) {
        this.transportController = Objects.requireNonNull(transportController,
                "Le contrôleur de transport ne peut pas être null");
        this.serviceReclamation = new ServiceReclamation();
        chargerReclamations();
    }

    private void chargerReclamations() {
        Objects.requireNonNull(transportController.getReclamations(),
                        "La liste des réclamations ne peut pas être null")
                .forEach(serviceReclamation::soumettre);
    }

    // ==================== METHODES DE SOUMISSION ====================
    public void soumettreReclamationTechnique(Personne personne, Suspendable cible,
                                              String description, LocalDate date,
                                              TypeProblemeTechnique typeProbleme,
                                              NiveauGravite gravite) {
        Objects.requireNonNull(cible, "La cible ne peut pas être null pour une réclamation technique");

        // Sauvegarde de l'état de suspension avant la nouvelle réclamation
        boolean etaitDejaSuspendu = cible.estSuspendu();

        Reclamation reclamation = new ReclamationTechnique(
                Objects.requireNonNull(personne),
                cible,
                Objects.requireNonNull(description),
                Objects.requireNonNull(date),
                Objects.requireNonNull(typeProbleme),
                Objects.requireNonNull(gravite)
        );

        soumettreReclamation(reclamation);

        // Vérification de suspension après soumission
        serviceReclamation.verifierSuspensionTechnique(cible);

        // Afficher alerte seulement si nouvellement suspendu
        if (cible.estSuspendu() && !etaitDejaSuspendu) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Suspension automatique");
                alert.setHeaderText(null);
                alert.setContentText("La cible " + cible + " a été suspendue automatiquement");
                alert.showAndWait();
            });
        }
    }

    public void soumettreReclamationPaiement(Personne personne,
                                             String description, LocalDate date,
                                             double montant, ModePaiement modePaiement) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        Reclamation reclamation = new ReclamationPaiement(
                Objects.requireNonNull(personne),
                Objects.requireNonNull(description),
                Objects.requireNonNull(date),
                montant,
                Objects.requireNonNull(modePaiement)
        );
        soumettreReclamation(reclamation);
    }

    public void soumettreReclamationService(Personne personne,
                                            String description, LocalDate date,
                                            TypeIncident typeIncident) {
        Reclamation reclamation = new ReclamationService(
                Objects.requireNonNull(personne),
                Objects.requireNonNull(description),
                Objects.requireNonNull(date),
                Objects.requireNonNull(typeIncident)
        );
        soumettreReclamation(reclamation);
    }

    public void soumettreReclamation(Reclamation reclamation) {
        Objects.requireNonNull(reclamation, "La réclamation ne peut pas être null");
        serviceReclamation.soumettre(reclamation);
        transportController.ajouterReclamation(reclamation);
        transportController.sauvegarder();
    }

    // ==================== METHODES DE GESTION ====================
    public void resoudreReclamation(Reclamation reclamation) {
        Objects.requireNonNull(reclamation, "La réclamation ne peut pas être null");
        if (reclamation.estResolue()) {
            throw new IllegalStateException("La réclamation est déjà résolue");
        }

        // Sauvegarde de l'état avant résolution
        boolean etaitSuspendu = false;
        Suspendable cible;

        if (reclamation instanceof ReclamationTechnique) {
            cible = ((ReclamationTechnique) reclamation).getCible();
            etaitSuspendu = cible.estSuspendu();
        } else {
            cible = null;
        }

        serviceReclamation.resoudre(reclamation);

        // Vérification de réactivation seulement pour les réclamations techniques
        if (cible != null) {
            serviceReclamation.verifierSuspensionTechnique(cible);

            // Afficher message si réactivé
            if (etaitSuspendu && !cible.estSuspendu()) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Réactivation automatique");
                    alert.setHeaderText(null);
                    alert.setContentText("La cible " + cible + " a été réactivée");
                    alert.showAndWait();
                });
            }
        }

        transportController.sauvegarder();
    }


    public List<Reclamation> getReclamationsParType(TypeReclamation type) {
        return serviceReclamation.getReclamationsParType(
                Objects.requireNonNull(type));
    }


    public List<Reclamation> getAllReclamations() {
        return serviceReclamation.getAllReclamations();
    }


}