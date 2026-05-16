import java.util.Map;

public class CreditCardPaymentProcessor implements paymentProccesor {

    private boolean validateCreditCardDetails(Map<String, String> details) {
        if (!details.containsKey("cardNumber") || !details.containsKey("expiryDate")
                || !details.containsKey("cvv") || !details.containsKey("cardholderName")) {
            return false;
        }
        String cardNumber = details.get("cardNumber").replaceAll("\\s", "");
        String cvv        = details.get("cvv");

        if (cardNumber.length() < 13 || cardNumber.length() > 19) return false;
        if (cvv.length() < 3 || cvv.length() > 4)                 return false;
        return true;
    }

    @Override
    public boolean process(double amount, Map<String, String> paymentDetails) {
        if (!validateCreditCardDetails(paymentDetails)) {
            System.out.println("Invalid credit card details.");
            return false;
        }
        System.out.println("Processing credit card payment of $"
                + String.format("%.2f", amount));
        return true;
    }

    @Override
    public boolean refund(String paymentId) {
        System.out.println("Refunding payment: " + paymentId);
        return true;
    }
}
