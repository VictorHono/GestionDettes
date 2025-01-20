import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static GestionClients gestion = new GestionClients();

    public static void main(String[] args) {
        boolean continuer = true;
        while (continuer) {
            afficherMenuPrincipal();
            int choix = lireChoixNumerique();

            switch (choix) {
                case 1:
                    menuGestionClients();
                    break;
                case 2:
                    menuGestionDettes();
                    break;
                case 3:
                    menuRapportsEtStatistiques();
                    break;
                case 4:
                    continuer = false;
                    System.out.println("Au revoir!");
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Gestion des clients");
        System.out.println("2. Gestion des dettes");
        System.out.println("3. Rapports et statistiques");
        System.out.println("4. Quitter");
        System.out.print("Votre choix: ");
    }

    private static int lireChoixNumerique() {
        while (true) {
            try {
                int choix = scanner.nextInt();
                scanner.nextLine(); // Vider le buffer
                return choix;
            } catch (Exception e) {
                System.out.println("Veuillez entrer un nombre valide");
                scanner.nextLine(); // Vider le buffer
                System.out.print("Votre choix: ");
            }
        }
    }

    private static double lireMontant() {
        while (true) {
            try {
                double montant = scanner.nextDouble();
                scanner.nextLine(); // Vider le buffer
                if (montant <= 0) {
                    System.out.println("Le montant doit être positif");
                    System.out.print("Montant: ");
                    continue;
                }
                return montant;
            } catch (Exception e) {
                System.out.println("Veuillez entrer un montant valide");
                scanner.nextLine(); // Vider le buffer
                System.out.print("Montant: ");
            }
        }
    }

    private static void menuGestionClients() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n=== GESTION DES CLIENTS ===");
            System.out.println("A. Liste des clients");
            System.out.println("B. Ajouter un nouveau client");
            System.out.println("C. Modifier un client");
            System.out.println("D. Supprimer un client");
            System.out.println("E. Versement");
            System.out.println("F. Retraits");
            System.out.println("G. Rechercher un client");
            System.out.println("H. Historique des transactions");
            System.out.println("I. Retour");
            System.out.print("Votre choix: ");

            String choix = scanner.nextLine().toUpperCase();
            switch (choix) {
                case "A":
                    menuListeClients();
                    break;
                case "B":
                    ajouterClient();
                    break;
                case "C":
                    modifierClient();
                    break;
                case "D":
                    supprimerClient();
                    break;
                case "E":
                    effectuerVersement();
                    break;
                case "F":
                    effectuerRetrait();
                    break;
                case "G":
                    rechercherClient();
                    break;
                case "H":
                    afficherHistoriqueTransactions();
                    break;
                case "I":
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }
    }

    private static void menuListeClients() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n=== LISTE DES CLIENTS ===");
            System.out.println("1. Liste simple");
            System.out.println("2. Triés par nom");
            System.out.println("3. Triés par solde");
            System.out.println("4. Retour");
            System.out.print("Votre choix: ");

            int choix = lireChoixNumerique();
            switch (choix) {
                case 1:
                    gestion.afficherClients();
                    attendreTouche();
                    break;
                case 2:
                    gestion.afficherClientsTriesParNom();
                    attendreTouche();
                    break;
                case 3:
                    gestion.afficherClientsTriesParSolde();
                    attendreTouche();
                    break;
                case 4:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }
    }

    private static void menuGestionDettes() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n=== GESTION DES DETTES ===");
            System.out.println("A. Liste des clients endettés");
            System.out.println("B. Annuler une dette");
            System.out.println("C. Retour");
            System.out.print("Votre choix: ");

            String choix = scanner.nextLine().toUpperCase();
            switch (choix) {
                case "A":
                    gestion.afficherClientsEndettes();
                    attendreTouche();
                    break;
                case "B":
                    annulerDette();
                    break;
                case "C":
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }
    }

    private static void menuRapportsEtStatistiques() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n=== RAPPORTS ET STATISTIQUES ===");
            System.out.println("1. Statistiques générales");
            System.out.println("2. Retour");
            System.out.print("Votre choix: ");

            int choix = lireChoixNumerique();
            switch (choix) {
                case 1:
                    gestion.afficherStatistiques();
                    attendreTouche();
                    break;
                case 2:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }
    }

    private static void ajouterClient() {
        System.out.print("Nom du client: ");
        String nom = scanner.nextLine();
        System.out.print("Age du client: ");
        int age = lireChoixNumerique();

        gestion.ajouterClient(nom, age);
        attendreTouche();
    }

    private static void modifierClient() {
        gestion.afficherClients();
        System.out.print("Code du client à modifier: ");
        String code = scanner.nextLine();

        boolean retour = false;
        while (!retour) {
            System.out.println("\n=== MODIFICATION CLIENT ===");
            System.out.println("1. Modifier le nom");
            System.out.println("2. Modifier l'âge");
            System.out.println("3. Modifier le solde");
            System.out.println("4. Retour");
            System.out.print("Votre choix: ");

            int choix = lireChoixNumerique();

            switch (choix) {
                case 1:
                    System.out.print("Nouveau nom: ");
                    String nouveauNom = scanner.nextLine();
                    gestion.modifierNom(code, nouveauNom);
                    attendreTouche();
                    retour = true;
                    break;
                case 2:
                    System.out.print("Nouvel âge: ");
                    int nouvelAge = lireChoixNumerique();
                    gestion.modifierAge(code, nouvelAge);
                    attendreTouche();
                    retour = true;
                    break;
                case 3:
                    System.out.print("Nouveau solde: ");
                    double nouveauSolde = lireMontant();
                    Client client = gestion.trouverClient(code);
                    if (client != null) {
                        client.setSolde(nouveauSolde);
                        System.out.println("Modification du solde réussie");
                    }
                    attendreTouche();
                    retour = true;
                    break;
                case 4:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }
    }

    private static void supprimerClient() {
        gestion.afficherClients();
        System.out.print("Code du client à supprimer: ");
        String code = scanner.nextLine();
        gestion.supprimerClient(code);
        attendreTouche();
    }

    private static void effectuerVersement() {
        gestion.afficherClients();
        System.out.print("Code du client: ");
        String code = scanner.nextLine();
        System.out.print("Montant du versement: ");
        double montant = lireMontant();
        gestion.effectuerVersement(code, montant);
        attendreTouche();
    }

    private static void effectuerRetrait() {
        gestion.afficherClients();
        System.out.print("Code du client: ");
        String code = scanner.nextLine();
        System.out.print("Montant du retrait: ");
        double montant = lireMontant();
        gestion.effectuerRetrait(code, montant);
        attendreTouche();
    }

    private static void rechercherClient() {
        System.out.print("Entrez le nom ou le code du client: ");
        String terme = scanner.nextLine();
        gestion.rechercherClients(terme);
        attendreTouche();
    }

    private static void afficherHistoriqueTransactions() {
        gestion.afficherClients();
        System.out.print("Code du client: ");
        String code = scanner.nextLine();
        gestion.afficherHistoriqueTransactions(code);
        attendreTouche();
    }

    private static void annulerDette() {
        gestion.afficherClientsEndettes();
        System.out.print("Code du client: ");
        String code = scanner.nextLine();
        gestion.annulerDette(code);
        attendreTouche();
    }

    private static void attendreTouche() {
        System.out.println("\nAppuyez sur une touche pour retourner...");
        scanner.nextLine();
    }
}