package transport.control;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import transport.core.*;
import java.time.LocalDate;

public class VerificationTitreController {
    @FXML private TextField idTitreField;
    @FXML private DatePicker dateVerificationPicker;
    @FXML private VBox resultBox;
    @FXML private Text typeTitreText;
    @FXML private Text dateAchatText;
    @FXML private Text validiteText;

    private TransportController transportController;

    public VerificationTitreController() {
        this.transportController = new TransportController();
    }

    @FXML
    public void initialize() {
        dateVerificationPicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleVerifier() {
        try {
            String idTitre = idTitreField.getText();
            LocalDate dateVerification = dateVerificationPicker.getValue();

            if (idTitre.isEmpty() || dateVerification == null) {
                showAlert("Erreur", "Veuillez saisir l'ID du titre et la date de vérification.");
                return;
            }

            TitreTransport titre = transportController.getTitres().stream()
                    .filter(t -> t.getId().equals(idTitre))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Aucun titre trouvé avec cet ID"));

            // Afficher les détails du titre
            resultBox.setVisible(true);
            typeTitreText.setText("Type: " + (titre instanceof CartePersonnelle ? "Carte Personnelle" : "Ticket Simple"));
            dateAchatText.setText("Acheté le: " + titre.getDateAchat());

            // Vérifier la validité
            try {
                boolean estValide = titre.estValide(dateVerification);
                validiteText.setStyle("-fx-fill: " + (estValide ? "#27ae60" : "#e74c3c"));
                validiteText.setText(estValide ? "VALIDE" : "EXPIRÉ");
            } catch (TitreNonValideException e) {
                validiteText.setStyle("-fx-fill: #e74c3c");
                validiteText.setText("INVALIDE: " + e.getMessage());
            }

        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        idTitreField.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}