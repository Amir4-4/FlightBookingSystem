import java.util.Map;

public interface paymentProccesor {
    boolean process(double amount, Map<String, String> paymentDetails);
    boolean refund(String paymentId);
}
