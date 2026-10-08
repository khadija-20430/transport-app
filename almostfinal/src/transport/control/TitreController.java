package transport.control;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import transport.core.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class TitreController {

    @FXML private ToggleGroup titreTypeGroup;
    @FXML private RadioButton radioTicket;
    @FXML private RadioButton radioCarte;

    @FXML private GridPane carteForm;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private CheckBox handicapCheck;
    @FXML private CheckBox employeCheck;
    @FXML private TextField matriculeField;
    @FXML private ComboBox<TypeFonction> fonctionCombo;
    @FXML private Label resultLabel;

    @FXML private ListView<String> listViewTitres;
    @FXML private ToggleGroup paiementGroup;
    @FXML private RadioButton radioCash;
    @FXML private RadioButton radioDahabia;
    @FXML private RadioButton radioBaridiMob;
    @FXML private Button closeButton;

    private GestionTitre gestionTitre;

    @FXML
    public void initialize() {
        // Initialisation de GestionTitre
        gestionTitre = new GestionTitre();

        // Création et association du ToggleGroup pour les boutons radio
        ToggleGroup titreTypeGroup = new ToggleGroup();
        radioTicket.setToggleGroup(titreTypeGroup);
        radioCarte.setToggleGroup(titreTypeGroup);

        // Configuration initiale
        carteForm.setVisible(false);
        fonctionCombo.getItems().setAll(TypeFonction.values());
        paiementGroup = new ToggleGroup();
        radioCash.setToggleGroup(paiementGroup);
        radioDahabia.setToggleGroup(paiementGroup);
        radioBaridiMob.setToggleGroup(paiementGroup);
        // Gestion de la visibilité du formulaire carte
        titreTypeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isCarteSelected = newVal == radioCarte;
            carteForm.setVisible(isCarteSelected);
            carteForm.setManaged(isCarteSelected);
        });

        // Activation/désactivation des champs employé
        employeCheck.selectedProperty().addListener((obs, oldVal, isEmploye) -> {
            matriculeField.setDisable(!isEmploye);
            fonctionCombo.setDisable(!isEmploye);
        });

        // Charger la liste des titres au démarrage
        afficherListeTitres();
    }

    @FXML
    private void handleAcheter() {
        try {
            if (radioTicket.isSelected()) {
                acheterTicket();
            } else {
                acheterCartePersonnelle();
            }
            afficherListeTitres(); // Mettre à jour la liste des titres après achat
        } catch (Exception e) {
            showError("Erreur : " + e.getMessage());
        }
    }
    @FXML
    private void handleClose() {
        // Récupérer la fenêtre (stage) courante et la fermer
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void acheterTicket() {
        try {
            // Création et ajout du ticket via GestionTitre
            gestionTitre.ajouterTicket();
            resultLabel.setText("Ticket acheté avec succès !");
        } catch (Exception e) {
            showError("Erreur lors de l'achat du ticket : " + e.getMessage());
        }
    }
    private ModePaiement getModePaiementSelectionne() {
        RadioButton selectedRadio = (RadioButton) paiementGroup.getSelectedToggle();
        if (selectedRadio == null) {
            throw new IllegalArgumentException("Veuillez sélectionner un mode de paiement.");
        }

        return switch (selectedRadio.getText()) {
            case "Espèces" -> ModePaiement.ESPECE;
            case "Dahabia" -> ModePaiement.DAHABIA;
            case "BaridiMob" -> ModePaiement.BARIDIMOB;
            default -> throw new IllegalArgumentException("Mode de paiement inconnu.");
        };
    }
    private void acheterCartePersonnelle() {
        try {
            // Validation des champs obligatoires
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || dateNaissancePicker.getValue() == null) {
                throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
            }

            // Récupération du mode de paiement sélectionné
            ModePaiement paiement = getModePaiementSelectionne();
            if (paiement == null) {
                throw new IllegalArgumentException("Veuillez choisir un mode de paiement.");
            }

            // Création de la personne
            Personne personne;
            if (employeCheck.isSelected()) {
                if (matriculeField.getText().isEmpty() || fonctionCombo.getValue() == null) {
                    throw new IllegalArgumentException("Matricule et fonction sont obligatoires pour un employé.");
                }
                personne = new Employe(
                        nomField.getText(),
                        prenomField.getText(),
                        dateNaissancePicker.getValue(),
                        handicapCheck.isSelected(),
                        matriculeField.getText(),
                        fonctionCombo.getValue()
                );
            } else {
                personne = new Usager(
                        nomField.getText(),
                        prenomField.getText(),
                        dateNaissancePicker.getValue(),
                        handicapCheck.isSelected()
                );
            }

            // Création de la carte via GestionTitre
            gestionTitre.ajouterCarte(personne, paiement);
            resultLabel.setText("Carte personnelle créée avec succès !");
        } catch (Exception e) {
            showError("Erreur lors de l'achat de la carte : " + e.getMessage());
        }
    }

    @FXML
    private void handleReset() {
        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(null);
        handicapCheck.setSelected(false);
        employeCheck.setSelected(false);
        matriculeField.clear();
        fonctionCombo.getSelectionModel().clearSelection();
        resultLabel.setText("");
    }

    @FXML
    private void afficherListeTitres() {
        try {
            // Récupération des titres depuis GestionTitre
            List<TitreTransport> titres = gestionTitre.getTitres();
            ObservableList<String> items = FXCollections.observableArrayList();

            for (TitreTransport titre : titres) {
                String type = (titre instanceof CartePersonnelle) ? "Carte" : "Ticket";
                items.add(type + " - ID: " + titre.getId() + " - Prix: " + titre.getPrix() + " DA - Date: " + titre.getDateAchat());
            }

            listViewTitres.setItems(items);
            resultLabel.setText("Liste des titres chargée.");
        } catch (Exception e) {
        }
    }


    @FXML
    private void handleAfficherListetitre() throws Exception {
        loadFXML("DisplayListeTitres.fxml");
    }

    private void loadFXML(String fxml) {
        try {
            // Vérification du chemin du fichier FXML
            String resourcePath = "/transport/ui/" + fxml;
            URL fxmlLocation = getClass().getResource(resourcePath);

            if (fxmlLocation == null) {
            }

            // Chargement du fichier FXML
            Parent root = FXMLLoader.load(fxmlLocation);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
