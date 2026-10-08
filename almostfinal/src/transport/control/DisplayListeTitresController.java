package transport.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import transport.core.*;

public class DisplayListeTitresController {
    @FXML private TableView<TitreWrapper> titresTable;
    @FXML private TableColumn<TitreWrapper, String> colId;
    @FXML private TableColumn<TitreWrapper, String> colDate;
    @FXML private TableColumn<TitreWrapper, String> colPrix;
    @FXML private TableColumn<TitreWrapper, String> colType;
    @FXML private TableColumn<TitreWrapper, String> colDetails;
    @FXML private TableColumn<TitreWrapper, String> colValidite;

    public static class TitreWrapper {
        private final TitreTransport titre;

        public TitreWrapper(TitreTransport titre) {
            this.titre = titre;
        }

        public String getId() { return titre.getId(); }
        public String getDateAchat() { return titre.getDateAchat().toString(); }
        public String getPrix() { return String.format("%.2f DA", titre.getPrix()); }
        public String getType() {
            return (titre instanceof CartePersonnelle) ? "Carte" : "Ticket";
        }
        public String getDetails() {
            if (titre instanceof CartePersonnelle) {
                CartePersonnelle c = (CartePersonnelle) titre;
                return c.getPersonne().getNom() + " " + c.getPersonne().getPrenom();
            }
            return "Ticket simple";
        }
        public String getValidite() {
            try {
                return titre.estValide(java.time.LocalDate.now()) ? "Valide" : "Expiré";
            } catch (TitreNonValideException e) {
                return "Invalide";
            }
        }
    }

    @FXML
    public void initialize() {
        // Configurer les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateAchat"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDetails.setCellValueFactory(new PropertyValueFactory<>("details"));
        colValidite.setCellValueFactory(new PropertyValueFactory<>("validite"));

        // Charger les données
        ObservableList<TitreWrapper> data = FXCollections.observableArrayList();
        for (TitreTransport t : new TransportController().getTitres()) {
            data.add(new TitreWrapper(t));
        }
        titresTable.setItems(data);
    }

    @FXML
    private void retour() {
        titresTable.getScene().getWindow().hide();
    }
}