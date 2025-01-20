import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    public enum Type {
        VERSEMENT("Versement"),
        RETRAIT("Retrait"),
        ANNULATION_DETTE("Annulation de dette");

        private final String libelle;

        Type(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    private LocalDateTime date;
    private Type type;
    private double montant;
    private double soldePrecedent;
    private double nouveauSolde;

    public Transaction(Type type, double montant, double soldePrecedent, double nouveauSolde) {
        this.date = LocalDateTime.now();
        this.type = type;
        this.montant = montant;
        this.soldePrecedent = soldePrecedent;
        this.nouveauSolde = nouveauSolde;
    }

    public LocalDateTime getDate() { return date; }
    public Type getType() { return type; }
    public double getMontant() { return montant; }
    public double getSoldePrecedent() { return soldePrecedent; }
    public double getNouveauSolde() { return nouveauSolde; }

    @Override
    public String toString() {
        return String.format("%s | %s | Montant: %.2f FCFA | Ancien solde: %.2f FCFA | Nouveau solde: %.2f FCFA",
            date.toString(), type.getLibelle(), montant, soldePrecedent, nouveauSolde);
    }
}