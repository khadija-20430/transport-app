package transport.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import transport.core.*;
import java.time.LocalDate;

public class ReclamationController {
    @FXML private ComboBox<TypeReclamation> typeComboBox;
    @FXML private ComboBox<Personne> personneComboBox;
    @FXML private ComboBox<Suspendable> cibleComboBox;
    @FXML private TextArea descriptionTextArea;
    @FXML private TableView<Reclamation> reclamationTableView;
    @FXML private TableColumn<Reclamation, Boolean> resolueColumn;
    @FXML private ComboBox<String> filtreComboBox;
    @FXML private Label messageLabel;
    @FXML private VBox techDetails;
    @FXML private VBox serviceDetails;
    @FXML private VBox paymentDetails;

    // Champs pour les réclamations techniques
    @FXML private ComboBox<TypeProblemeTechnique> typeProblemeCombo;
    @FXML private ComboBox<NiveauGravite> graviteCombo;

    // Champs pour les réclamations de service
    @FXML private ComboBox<TypeIncident> typeIncidentCombo;

    // Champs pour les réclamations de paiement
    @FXML private TextField montantField;
    @FXML private ComboBox<ModePaiement> modePaiementCombo;

    private GestionReclamation gestionReclamation;

    @FXML
    public void initialize() {
        TransportController transportController = new TransportController();
        gestionReclamation = new GestionReclamation(transportController);

        // Configuration des ComboBox
        typeComboBox.setItems(FXCollections.observableArrayList(TypeReclamation.values()));
        personneComboBox.setItems(FXCollections.observableArrayList(transportController.getPersonnes()));

        // Initialisation de la ComboBox cible
        Suspendable[] toutesCibles = new Suspendable[
                transportController.getStations().length + transportController.getMoyensDeTransport().length
                ];

        System.arraycopy(transportController.getStations(), 0, toutesCibles, 0, transportController.getStations().length);
        System.arraycopy(transportController.getMoyensDeTransport(), 0, toutesCibles,
                transportController.getStations().length, transportController.getMoyensDeTransport().length);

        cibleComboBox.setItems(FXCollections.observableArrayList(toutesCibles));

        // Listener pour changer les sections visibles
        typeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isTechnique = newVal == TypeReclamation.TECHNIQUE;
            techDetails.setVisible(isTechnique);
            techDetails.setManaged(isTechnique);

            boolean isService = newVal == TypeReclamation.SERVICE;
            serviceDetails.setVisible(isService);
            serviceDetails.setManaged(isService);

            boolean isPaiement = newVal == TypeReclamation.PAIEMENT;
            paymentDetails.setVisible(isPaiement);
            paymentDetails.setManaged(isPaiement);

            cibleComboBox.setDisable(!isTechnique);
        });

        filtreComboBox.setItems(FXCollections.observableArrayList(
                "Toutes", "Technique", "Service", "Paiement", "Non résolues"
        ));
        filtreComboBox.getSelectionModel().selectFirst();

        // Configuration de la TableView
        resolueColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().estResolue()));

        // Configuration spéciale de la colonne Cible
        TableColumn<Reclamation, String> cibleColumn = (TableColumn<Reclamation, String>) reclamationTableView.getColumns().get(4);
        cibleColumn.setCellValueFactory(cellData -> {
            Reclamation reclamation = cellData.getValue();
            if (reclamation instanceof ReclamationTechnique) {
                Suspendable cible = ((ReclamationTechnique) reclamation).getCible();
                return new SimpleStringProperty(cible != null ? cible.toString() : "N/A");
            }
            return new SimpleStringProperty("N/A");
        });

        rafraichirTableView();
    }
    private void rafraichirTableView() {
        reclamationTableView.setItems(FXCollections.observableArrayList(
                gestionReclamation.getAllReclamations()
        ));
    }

    @FXML
    private void handleSoumettre() {
        try {
            TypeReclamation type = typeComboBox.getValue();
            Personne personne = personneComboBox.getValue();
            String description = descriptionTextArea.getText();

            // Validation des champs obligatoires
            if (type == null || personne == null || description.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            LocalDate date = LocalDate.now();

            switch (type) {
                case TECHNIQUE:
                    TypeProblemeTechnique typeProbleme = typeProblemeCombo.getValue();
                    NiveauGravite gravite = graviteCombo.getValue();
                    Suspendable cible = cibleComboBox.getValue();

                    if (typeProbleme == null || gravite == null || cible == null) {
                        showAlert("Erreur", "Veuillez remplir tous les détails techniques.");
                        return;
                    }

                    gestionReclamation.soumettreReclamationTechnique(
                            personne,
                            cible,
                            description,
                            date,
                            typeProbleme,
                            gravite
                    );
                    break;

                case SERVICE:
                    TypeIncident typeIncident = typeIncidentCombo.getValue();

                    if (typeIncident == null) {
                        showAlert("Erreur", "Veuillez sélectionner un type d'incident.");
                        return;
                    }

                    gestionReclamation.soumettreReclamationService(
                            personne,
                            description,
                            date,
                            typeIncident
                    );
                    break;

                case PAIEMENT:
                    ModePaiement modePaiement = modePaiementCombo.getValue();
                    String montantText = montantField.getText();

                    if (modePaiement == null || montantText.isEmpty()) {
                        showAlert("Erreur", "Veuillez remplir tous les détails de paiement.");
                        return;
                    }

                    try {
                        double montant = Double.parseDouble(montantText);
                        gestionReclamation.soumettreReclamationPaiement(
                                personne,
                                description,
                                date,
                                montant,
                                modePaiement
                        );
                    } catch (NumberFormatException e) {
                        showAlert("Erreur", "Le montant doit être un nombre valide.");
                        return;
                    }
                    break;
            }

            rafraichirTableView();
            reinitialiserFormulaire();
            showAlert("Succès", "Réclamation soumise avec succès.");

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFiltrer() {
        String filtre = filtreComboBox.getValue();
        if (filtre == null || filtre.equals("Toutes")) {
            rafraichirTableView();
            return;
        }

        switch (filtre) {
            case "Technique":
                reclamationTableView.setItems(FXCollections.observableArrayList(
                        gestionReclamation.getReclamationsParType(TypeReclamation.TECHNIQUE)
                ));
                break;
            case "Service":
                reclamationTableView.setItems(FXCollections.observableArrayList(
                        gestionReclamation.getReclamationsParType(TypeReclamation.SERVICE)
                ));
                break;
            case "Paiement":
                reclamationTableView.setItems(FXCollections.observableArrayList(
                        gestionReclamation.getReclamationsParType(TypeReclamation.PAIEMENT)
                ));
                break;
            case "Non résolues":
                reclamationTableView.setItems(FXCollections.observableArrayList(
                        gestionReclamation.getAllReclamations().stream()
                                .filter(r -> !r.estResolue())
                                .toList()
                ));
                break;
        }
    }

    @FXML
    private void handleAfficherTout() {
        rafraichirTableView();
    }

    @FXML
    private void handleResoudre() {
        Reclamation selected = reclamationTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Veuillez sélectionner une réclamation.");
            return;
        }

        if (selected.estResolue()) {
            messageLabel.setText("Cette réclamation est déjà résolue.");
            return;
        }

        gestionReclamation.resoudreReclamation(selected);

        if (selected instanceof ReclamationTechnique) {
            Suspendable cible = ((ReclamationTechnique) selected).getCible();
            gestionReclamation.serviceReclamation.verifierSuspensionTechnique(cible);
        }

        reclamationTableView.refresh();
        messageLabel.setText("Réclamation marquée comme résolue.");
    }

    private void reinitialiserFormulaire() {
        typeComboBox.setValue(null);
        personneComboBox.setValue(null);
        cibleComboBox.setValue(null);
        descriptionTextArea.clear();
        typeProblemeCombo.setValue(null);
        graviteCombo.setValue(null);
        typeIncidentCombo.setValue(null);
        montantField.clear();
        modePaiementCombo.setValue(null);

        techDetails.setVisible(false);
        techDetails.setManaged(false);
        serviceDetails.setVisible(false);
        serviceDetails.setManaged(false);
        paymentDetails.setVisible(false);
        paymentDetails.setManaged(false);
        cibleComboBox.setDisable(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}