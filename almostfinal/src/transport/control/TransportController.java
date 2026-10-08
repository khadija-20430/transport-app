package transport.control;

import transport.core.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

class TransportController {
    private static List<Personne> personnes;
    private List<TitreTransport> titres;
    private List<Reclamation> reclamations;
    private ServiceReclamation serviceReclamation; // Ajout du ServiceReclamation

    public TransportController() {
        try {
            List<Usager> usagers = Database.chargerUsagers();
            List<Employe> employes = Database.chargerEmployes();
            personnes = new ArrayList<>();

            if (usagers != null) personnes.addAll(usagers);
            if (employes != null) personnes.addAll(employes);

            titres = Database.chargerTitres();
            reclamations = Database.chargerReclamations();

            // Initialisation du ServiceReclamation
            serviceReclamation = new ServiceReclamation();
            reclamations.forEach(serviceReclamation::soumettre);

        } catch (Exception e) {
            personnes = new ArrayList<>();
            titres = new ArrayList<>();
            reclamations = new ArrayList<>();
            serviceReclamation = new ServiceReclamation();
            System.out.println("Erreur de chargement des données : " + e.getMessage());
        }
    }



    public List<Personne> getPersonnes() {
        return new ArrayList<>(personnes);
    }

    public void ajouterUsager(Usager usager) {
        personnes.add(usager);
        sauvegarder();
    }

    public void ajouterEmploye(Employe employe) {
        personnes.add(employe);
        sauvegarder();
    }

    public static List<Usager> getUsagers() {
        List<Usager> usagers = new ArrayList<>();
        for (Personne p : personnes) {
            if (p instanceof Usager) {
                usagers.add((Usager) p);
            }
        }
        return usagers;
    }

    public static List<Employe> getEmployes() {
        List<Employe> employes = new ArrayList<>();
        for (Personne p : personnes) {
            if (p instanceof Employe) {
                employes.add((Employe) p);
            }
        }
        return employes;
    }

    public List<TitreTransport> getTitres() {
        titres.sort(Comparator.comparing(TitreTransport::getDateAchat).reversed());
        return new ArrayList<>(titres);
    }

    public void ajouterReclamation(Reclamation reclamation) {
        reclamations.add(reclamation);
        sauvegarder();
    }

    public List<Reclamation> getReclamations() {
        return new ArrayList<>(reclamations);
    }


    void sauvegarder() {
        try {
            // Séparer les personnes en usagers et employés avant la sauvegarde
            List<Usager> usagers = new ArrayList<>();
            List<Employe> employes = new ArrayList<>();

            for (Personne p : personnes) {
                if (p instanceof Usager) {
                    usagers.add((Usager) p);
                } else if (p instanceof Employe) {
                    employes.add((Employe) p);
                }
            }

            // Sauvegarder les données
            Database.sauvegarderUsagers(usagers);
            Database.sauvegarderEmployes(employes);
            Database.sauvegarderTitres(titres);
            Database.sauvegarderReclamations(reclamations);
            System.out.println("Données sauvegardées avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des données : " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Méthode pour récupérer les stations fictives
    public Station[] getStations() {
        return new Station[] {
                new Station("Station Université", false),
                new Station("Station Gare Centrale", true),
                new Station("Station Centre-Ville", false),
                new Station("Station Parc", false),
                new Station("Station Hôpital", true),
                new Station("Station Marché", false),
                new Station("Station Musée", false),
                new Station("Station Stade", true),
                new Station("Station Aéroport", false),
                new Station("Station Plage", false)
        };
    }

    // Méthode pour récupérer les moyens de transport fictifs
    public MoyenTransport[] getMoyensDeTransport() {
        return new MoyenTransport[] {
                new MoyenTransport("Bus", "Ligne 1"),
                new MoyenTransport("Tramway", "Ligne A"),
                new MoyenTransport("Métro", "Ligne 2"),
                new MoyenTransport("Taxi", "Ligne C"),
                new MoyenTransport("Train", "Ligne R"),
                new MoyenTransport("Vélo", "Ligne V"),
                new MoyenTransport("Navette", "Ligne S"),
                new MoyenTransport("Ferry", "Ligne M"),
                new MoyenTransport("Téléphérique", "Ligne T"),
                new MoyenTransport("Covoiturage", "Ligne C2")
        };
    }
}
