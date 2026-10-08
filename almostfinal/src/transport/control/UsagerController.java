package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import transport.core.*;

import java.util.List;
import java.util.Objects;

public class UsagerController {

    private TransportController controller = new TransportController();

    @FXML
    private TextField nomField, prenomField, matriculeField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private CheckBox handicapeBox;
    @FXML
    private RadioButton usagerRadio, employeRadio;
    @FXML
    private ComboBox<TypeFonction> fonctionCombo;
    @FXML
    private Label messageLabel;
    @FXML
    private ListView<String> listViewPersonnes;

    @FXML
    public void initialize() {
        // Groupe de boutons radio
        ToggleGroup group = new ToggleGroup();
        usagerRadio.setToggleGroup(group);
        employeRadio.setToggleGroup(group);

        // Gestion de la visibilité des champs spécifiques aux employés
        usagerRadio.setOnAction(e -> updateVisibility(false));
        employeRadio.setOnAction(e -> updateVisibility(true));

        // Remplissage de la ComboBox avec les fonctions possibles
        fonctionCombo.setItems(FXCollections.observableArrayList(TypeFonction.values()));
    }

    private void updateVisibility(boolean employeSelected) {
        matriculeField.setVisible(employeSelected);
        fonctionCombo.setVisible(employeSelected);
    }

    @FXML
    private void handleAddPersonne() {
        try {
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || datePicker.getValue() == null) {
                messageLabel.setText("Veuillez remplir tous les champs.");
                return;
            }

            if (employeRadio.isSelected()) {
                if (matriculeField.getText().isEmpty() || fonctionCombo.getValue() == null) {
                    messageLabel.setText("Veuillez remplir les champs Matricule et Fonction.");
                    return;
                }

                // Création de l'employé
                Employe employe = new Employe(
                        nomField.getText(),
                        prenomField.getText(),
                        datePicker.getValue(),
                        handicapeBox.isSelected(),
                        matriculeField.getText(),
                        fonctionCombo.getValue()  // Utilisation de l'énumération
                );
                controller.ajouterEmploye(employe);
                messageLabel.setText("Employé ajouté avec succès !");
            } else {
                // Création de l'usager
                Usager usager = new Usager(
                        nomField.getText(),
                        prenomField.getText(),
                        datePicker.getValue(),
                        handicapeBox.isSelected()
                );
                controller.ajouterUsager(usager);
                messageLabel.setText("Usager ajouté avec succès !");
            }

            clearForm();
            handleAfficherListe();

        } catch (Exception e) {
            messageLabel.setText("Erreur lors de l'ajout de la personne.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAfficherListe() {
        try {
            List<Personne> personnes = controller.getPersonnes();
            ObservableList<String> items = FXCollections.observableArrayList();

            for (Personne p : personnes) {
                if (p instanceof Usager) {
                    Usager u = (Usager) p;
                    items.add("[Usager] " + u.getNom() + " " + u.getPrenom() +
                            " - Né(e) le : " + u.getDateNaissance() +
                            (u.getHandicape() ? " (Handicapé)" : ""));
                } else if (p instanceof Employe) {
                    Employe e = (Employe) p;
                    items.add("[Employé] " + e.getNom() + " " + e.getPrenom() +
                            " - Matricule : " + e.getMatricule() +
                            " - Fonction : " + e.getTypeFonction());
                }
            }

            listViewPersonnes.setItems(items);
            messageLabel.setText("Liste des personnes chargée.");

        } catch (Exception e) {
            messageLabel.setText("Erreur lors du chargement de la liste.");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        datePicker.setValue(null);
        handicapeBox.setSelected(false);
        matriculeField.clear();
        fonctionCombo.setValue(null);
    }

    @FXML
    private void handleAfficherListePersonnes() throws Exception {
        loadFXML("DisplayListePersonnes.fxml");
    }

    private void loadFXML(String fxml) throws Exception {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/transport/ui/" + fxml)));
        stage.setScene(new Scene(root));
        stage.show();
    }


}
