import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable {
    private String code;
    private String nom;
    private int age;
    private double solde;
    private List<Transaction> transactions;
    private LocalDateTime dateCreation;

    public Client(String code, String nom, int age) {
        this.code = code;
        this.nom = nom;
        this.age = age;
        this.solde = 0.0;
        this.transactions = new ArrayList<>();
        this.dateCreation = LocalDateTime.now();
    }

    // Getters
    public String getCode() { return code; }
    public String getNom() { return nom; }
    public int getAge() { return age; }
    public double getSolde() { return solde; }
    public List<Transaction> getTransactions() { return transactions; }
    public LocalDateTime getDateCreation() { return dateCreation; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setAge(int age) { this.age = age; }
    
    public void setSolde(double solde) { 
        this.solde = solde;
    }

    public void ajouterTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return String.format("Code: %s | Nom: %s | Age: %d | Solde: %.2f FCFA", 
            code, nom, age, solde);
    }
}