package transport.control;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import transport.core.Station;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainMenuController {
    private TransportController transportController;

    @FXML private VBox alertBox;
    @FXML private Label suspensionLabel;

    public MainMenuController() {
        this.transportController = new TransportController();
    }

    @FXML
    public void initialize() {
        checkSuspensions();
    }

    @FXML
    private void handleAddUsager() throws Exception {
        loadFXML("UsagerView.fxml");
    }

    @FXML
    private void handleAddTitre() throws Exception {
        loadFXML("TitreView.fxml");
    }

    @FXML
    private void handleCheckTitre() throws Exception {
        loadFXML("TitreValideVerifView.fxml");
    }

    @FXML
    private void handleViewReclamations() throws Exception {
        loadFXML("ReclamationView.fxml");
    }

    protected void loadFXML(String fxmlFile) {
        try {
            // Utilisez getResource avec le chemin absolu depuis la racine src
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/ui/" + fxmlFile));

            // Vérification supplémentaire
            URL url = getClass().getResource("/transport/ui/" + fxmlFile);
            if (url == null) {
                throw new RuntimeException("Fichier FXML introuvable: " + fxmlFile +
                        "\nChemin recherché: /transport/ui/" + fxmlFile +
                        "\nChemin absolu du projet: " + new File("").getAbsolutePath());
            }

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement de l'interface: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void checkSuspensions() {
        // Vérifier au démarrage s'il y a des stations suspendues
        boolean hasSuspended = false;
        StringBuilder sb = new StringBuilder("Stations suspendues:\n");

        for (Station station : transportController.getStations()) {
            if (station.estSuspendu()) {
                sb.append("- ").append(station.getNom()).append("\n");
                hasSuspended = true;
            }
        }

        if (hasSuspended) {

        } else {
            alertBox.setVisible(false);
        }
    }
}