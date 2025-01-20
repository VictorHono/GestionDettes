import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GestionClients {
    private static final String FICHIER_CLIENTS = "Clients.txt";
    private List<Client> clients;

    public GestionClients() {
        clients = new ArrayList<>();
        chargerClients();
    }

    private void chargerClients() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_CLIENTS))) {
            clients = (List<Client>) ois.readObject();
        } catch (FileNotFoundException e) {
            clients = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors du chargement des clients: " + e.getMessage());
        }
    }

    private void sauvegarderClients() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_CLIENTS))) {
            oos.writeObject(clients);
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des clients: " + e.getMessage());
        }
    }

    public String genererCode() {
        return String.format("C%04d", clients.size() + 1);
    }

    public void ajouterClient(String nom, int age) {
        if (!validerNom(nom) || !validerAge(age)) {
            return;
        }
        String code = genererCode();
        Client client = new Client(code, nom, age);
        clients.add(client);
        sauvegarderClients();
        System.out.println("\nNouveau client ajouté avec succès:");
        System.out.println(client);
    }

    private boolean validerNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            System.out.println("Le nom ne peut pas être vide");
            return false;
        }
        if (nom.length() < 2) {
            System.out.println("Le nom doit contenir au moins 2 caractères");
            return false;
        }
        return true;
    }

    private boolean validerAge(int age) {
        if (age < 18) {
            System.out.println("Le client doit avoir au moins 18 ans");
            return false;
        }
        if (age > 120) {
            System.out.println("L'âge semble invalide");
            return false;
        }
        return true;
    }

    public void afficherClients() {
        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistré.");
            return;
        }
        System.out.println("\nListe des clients:");
        for (Client client : clients) {
            System.out.println(client);
        }
    }

    public void afficherClientsTriesParNom() {
        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistré.");
            return;
        }
        System.out.println("\nListe des clients (triés par nom):");
        clients.stream()
              .sorted(Comparator.comparing(Client::getNom))
              .forEach(System.out::println);
    }

    public void afficherClientsTriesParSolde() {
        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistré.");
            return;
        }
        System.out.println("\nListe des clients (triés par solde):");
        clients.stream()
              .sorted(Comparator.comparing(Client::getSolde))
              .forEach(System.out::println);
    }

    public void rechercherClients(String terme) {
        if (terme == null || terme.trim().isEmpty()) {
            System.out.println("Veuillez entrer un terme de recherche valide");
            return;
        }

        List<Client> resultats = clients.stream()
            .filter(c -> c.getNom().toLowerCase().contains(terme.toLowerCase()) ||
                        c.getCode().toLowerCase().contains(terme.toLowerCase()))
            .collect(Collectors.toList());

        if (resultats.isEmpty()) {
            System.out.println("Aucun client trouvé pour: " + terme);
            return;
        }

        System.out.println("\nRésultats de la recherche pour: " + terme);
        resultats.forEach(System.out::println);
    }

    public void afficherStatistiques() {
        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistré.");
            return;
        }

        double soldeTotal = clients.stream().mapToDouble(Client::getSolde).sum();
        double soldeMoyen = soldeTotal / clients.size();
        long nbEndettes = clients.stream().filter(c -> c.getSolde() < 0).count();
        double detteTotale = clients.stream()
            .filter(c -> c.getSolde() < 0)
            .mapToDouble(c -> Math.abs(c.getSolde()))
            .sum();

        System.out.println("\n=== Statistiques ===");
        System.out.printf("Nombre total de clients: %d%n", clients.size());
        System.out.printf("Solde total: %.2f FCFA%n", soldeTotal);
        System.out.printf("Solde moyen: %.2f FCFA%n", soldeMoyen);
        System.out.printf("Nombre de clients endettés: %d%n", nbEndettes);
        System.out.printf("Dette totale: %.2f FCFA%n", detteTotale);
    }

    public Client trouverClient(String code) {
        return clients.stream()
                     .filter(c -> c.getCode().equals(code))
                     .findFirst()
                     .orElse(null);
    }

    public void modifierNom(String code, String nouveauNom) {
        if (!validerNom(nouveauNom)) {
            return;
        }
        Client client = trouverClient(code);
        if (client != null) {
            client.setNom(nouveauNom);
            sauvegarderClients();
            System.out.println("Modification du nom réussie");
        } else {
            System.out.println("Client non trouvé");
        }
    }

    public void modifierAge(String code, int nouvelAge) {
        if (!validerAge(nouvelAge)) {
            return;
        }
        Client client = trouverClient(code);
        if (client != null) {
            client.setAge(nouvelAge);
            sauvegarderClients();
            System.out.println("Modification de l'âge réussie");
        } else {
            System.out.println("Client non trouvé");
        }
    }

    public void effectuerVersement(String code, double montant) {
        if (montant <= 0) {
            System.out.println("Le montant doit être positif");
            return;
        }

        Client client = trouverClient(code);
        if (client != null) {
            double ancienSolde = client.getSolde();
            double nouveauSolde = ancienSolde + montant;
            client.setSolde(nouveauSolde);
            
            Transaction transaction = new Transaction(
                Transaction.Type.VERSEMENT, 
                montant, 
                ancienSolde, 
                nouveauSolde
            );
            client.ajouterTransaction(transaction);
            
            sauvegarderClients();
            System.out.printf("Versement réussi. Nouveau solde = %.2f FCFA%n", nouveauSolde);
        } else {
            System.out.println("Client non trouvé");
        }
    }

    public void effectuerRetrait(String code, double montant) {
        if (montant <= 0) {
            System.out.println("Le montant doit être positif");
            return;
        }

        Client client = trouverClient(code);
        if (client != null) {
            double ancienSolde = client.getSolde();
            double nouveauSolde = ancienSolde - montant;
            client.setSolde(nouveauSolde);
            
            Transaction transaction = new Transaction(
                Transaction.Type.RETRAIT, 
                montant, 
                ancienSolde, 
                nouveauSolde
            );
            client.ajouterTransaction(transaction);
            
            sauvegarderClients();
            System.out.printf("Retrait réussi. Nouveau solde = %.2f FCFA%n", nouveauSolde);
        } else {
            System.out.println("Client non trouvé");
        }
    }

    public void supprimerClient(String code) {
        Client client = trouverClient(code);
        if (client != null) {
            if (client.getSolde() < 0) {
                System.out.println("Impossible de supprimer un client endetté");
                return;
            }
            clients.remove(client);
            sauvegarderClients();
            System.out.println("Suppression réussie");
        } else {
            System.out.println("Client non trouvé");
        }
    }

    public void afficherClientsEndettes() {
        List<Client> endettes = clients.stream()
                                     .filter(c -> c.getSolde() < 0)
                                     .toList();
        if (endettes.isEmpty()) {
            System.out.println("Aucun client endetté.");
            return;
        }
        System.out.println("\nListe des clients endettés:");
        for (Client client : endettes) {
            System.out.printf("Code: %s | Nom: %s | Age: %d | Dette: %.2f FCFA%n",
                client.getCode(), client.getNom(), client.getAge(), Math.abs(client.getSolde()));
        }
    }

    public void annulerDette(String code) {
        Client client = trouverClient(code);
        if (client != null && client.getSolde() < 0) {
            double ancienSolde = client.getSolde();
            client.setSolde(0);
            
            Transaction transaction = new Transaction(
                Transaction.Type.ANNULATION_DETTE, 
                Math.abs(ancienSolde), 
                ancienSolde, 
                0
            );
            client.ajouterTransaction(transaction);
            
            sauvegarderClients();
            System.out.println("Dette annulée avec succès");
        } else if (client != null) {
            System.out.println("Ce client n'a pas de dette");
        } else {
            System.out.println("Client non trouvé");
        }
    }

    public void afficherHistoriqueTransactions(String code) {
        Client client = trouverClient(code);
        if (client == null) {
            System.out.println("Client non trouvé");
            return;
        }

        List<Transaction> transactions = client.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction pour ce client");
            return;
        }

        System.out.println("\nHistorique des transactions pour " + client.getNom() + ":");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}