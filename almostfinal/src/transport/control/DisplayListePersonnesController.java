package transport.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import transport.core.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class DisplayListePersonnesController {
    @FXML private TableView<PersonneWrapper> personnesTable;

    public static class PersonneWrapper {
        private final Personne personne;

        public PersonneWrapper(Personne personne) {
            this.personne = personne;
        }

        public String getType() { return personne.getType(); }
        public String getNom() { return personne.getNom(); }
        public String getPrenom() { return personne.getPrenom(); }
        public String getDateNaissance() { return personne.getDateNaissance().toString(); }
        public String getHandicap() { return personne.getHandicape() ? "Oui" : "Non"; }
        public String getDetails() {
            if (personne instanceof Employe) {
                Employe e = (Employe) personne;
                return "Matricule: " + e.getMatricule() + ", Fonction: " + e.getTypeFonction();
            }
            return "/";
        }
    }

    @FXML
    public void initialize() {
        // Charger les données
        ObservableList<PersonneWrapper> data = FXCollections.observableArrayList();

        // Ajouter les usagers
        for (Usager u : TransportController.getUsagers()) {
            data.add(new PersonneWrapper(u));
        }

        // Ajouter les employés
        for (Employe e : TransportController.getEmployes()) {
            data.add(new PersonneWrapper(e));
        }

        personnesTable.setItems(data);
    }

    @FXML
    private void retour() {
        personnesTable.getScene().getWindow().hide();
    }
}