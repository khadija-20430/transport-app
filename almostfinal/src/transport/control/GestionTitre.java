package transport.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transport.core.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestionTitre {
    private ObservableList<TitreTransport> titres;

    public GestionTitre() {
        Database database = new Database();
        chargerTitres();
    }

    // Ajouter une carte avec calcul du prix
    public void ajouterCarte(Personne personne, ModePaiement paiement) throws ReductionImpossibleException {
        if (titres == null) {
            titres = FXCollections.observableArrayList();
        }

        try {
            CartePersonnelle nouvelleCarte = new CartePersonnelle(personne, paiement);
            titres.add(nouvelleCarte);
            sauvegarderTitres();
            System.out.println("Carte ajoutée : " + nouvelleCarte.getType() + ", paiement: " + paiement);
        } catch (ReductionImpossibleException e) {
            System.err.println("Erreur lors de la création de la carte : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de l'ajout de la carte : " + e.getMessage());
        }
    }


    public void ajouterTicket() {
        if (titres == null) {
            titres = FXCollections.observableArrayList();
        }

        double prix = calculerPrixTicket();
        Ticket nouveauTicket = new Ticket(prix);
        titres.add(nouveauTicket);
        sauvegarderTitres();
    }

    // Récupérer la liste des titres sous forme ObservableList pour UI
    public ObservableList<TitreTransport> getTitres() {
        if (titres == null) {
            titres = FXCollections.observableArrayList();
        }
        return titres;
    }



    // Prix fixe pour un ticket simple
    private double calculerPrixTicket() {
        return 50.0;
    }


    private void chargerTitres() {
        try {
            List<TitreTransport> titresCharges = Database.chargerTitres();
            // Transformer la liste classique en ObservableList
            this.titres = titresCharges != null ?
                    FXCollections.observableArrayList(titresCharges) :
                    FXCollections.observableArrayList();
        } catch (IOException | ClassNotFoundException e) {
            this.titres = FXCollections.observableArrayList();
            System.err.println("Erreur lors du chargement des titres: " + e.getMessage());
        }
    }

    private void sauvegarderTitres() {
        try {
            // Convertir ObservableList en ArrayList classique
            List<TitreTransport> titresAEnregistrer = new ArrayList<>(titres);
            Database.sauvegarderTitres(titresAEnregistrer);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des titres: " + e.getMessage());
        }
    }

}
