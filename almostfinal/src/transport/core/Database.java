package transport.core;

import java.io.*;
import java.util.*;

public class Database {

    private static final String USAGER_FILE = "usager.dat";
    private static final String EMPLOYE_FILE = "employe.dat";  // Nouveau fichier pour employés
    private static final String TITRE_FILE = "titre.dat";
    private static final String RECLAMATION_FILE = "reclamation.dat";

    // --- Usagers ---
    public static void sauvegarderUsagers(List<Usager> usagers) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USAGER_FILE))) {
            oos.writeObject(usagers);
        }
    }

    public static List<Usager> chargerUsagers() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USAGER_FILE))) {
            return (List<Usager>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // --- Employés ---
    public static void sauvegarderEmployes(List<Employe> employes) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMPLOYE_FILE))) {
            oos.writeObject(employes);
        }
    }

    public static List<Employe> chargerEmployes() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EMPLOYE_FILE))) {
            return (List<Employe>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // --- Titres ---
    public static void sauvegarderTitres(List<TitreTransport> titres) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TITRE_FILE))) {
            oos.writeObject(titres);
        }
    }

    public static List<TitreTransport> chargerTitres() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TITRE_FILE))) {
            return (List<TitreTransport>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // --- Réclamations ---
    public static void sauvegarderReclamations(List<Reclamation> reclamations) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RECLAMATION_FILE))) {
            oos.writeObject(reclamations);
        }
    }

    public static List<Reclamation> chargerReclamations() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RECLAMATION_FILE))) {
            return (List<Reclamation>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // --- Méthodes d'affichage ---
    public static void afficherUsagers() {
        try {
            List<Usager> usagers = chargerUsagers();
            System.out.println("Liste des usagers:");
            for (Usager usager : usagers) {
                System.out.println(usager);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la lecture des usagers : " + e.getMessage());
        }
    }


    public static void afficherTitres() {
        try {
            List<TitreTransport> titres = chargerTitres();
            System.out.println("Liste des titres de transport:");
            for (TitreTransport titre : titres) {
                System.out.println(titre);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la lecture des titres : " + e.getMessage());
        }
    }
}
