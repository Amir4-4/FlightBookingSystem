import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Payment {
    private String paymentId;
    private String bookingReference;
    private double amount;
    private String method;
    private String status;
    private String transactionDate;

    public Payment(String paymentId, String bookingReference, double amount,
                   String method, String status, String transactionDate) {
        this.paymentId        = paymentId;
        this.bookingReference = bookingReference;
        this.amount           = amount;
        this.method           = method;
        this.status           = status;
        this.transactionDate  = transactionDate;
    }

    public boolean processPayment() {
        if (amount > 0 && method != null && !method.isEmpty()) {
            status           = "Completed";
            transactionDate  = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println("Payment " + paymentId + " processed. Amount: $"
                    + String.format("%.2f", amount));
            return true;
        }
        status          = "Failed";
        transactionDate = "Failed";
        return false;
    }

    public void updateStatus(String newStatus) {
        if (newStatus != null && !newStatus.isEmpty()) {
            this.status = newStatus;
        }
    }

    public boolean isValid() {
        return amount > 0 && method != null && !method.isEmpty();
    }

    public String getPaymentId()        { return paymentId; }
    public String getBookingReference() { return bookingReference; }
    public double getAmount()           { return amount; }
    public String getMethod()           { return method; }
    public String getStatus()           { return status; }
    public String getTransactionDate()  { return transactionDate; }

    @Override
    public String toString() {
        return paymentId + " | " + bookingReference + " | $"
                + String.format("%.2f", amount) + " | " + status;
    }
}
