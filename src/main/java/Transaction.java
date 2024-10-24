public class Transaction {
    public String email;
    public String generalStatus;
    public double amount;
    public String currency;
    public String createdAt;

    public Transaction(String email, String generalStatus, double amount, String currency, String createdAt) {
        this.email = email;
        this.generalStatus = generalStatus;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGeneralStatus() {
        return generalStatus;
    }

    public void setGeneralStatus(String generalStatus) {
        this.generalStatus = generalStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }




    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", generalStatus='" + generalStatus + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
