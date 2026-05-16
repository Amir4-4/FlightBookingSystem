import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Booking {
    private String bookingReference;
    private Customer customer;
    private Flight   flight;
    private ArrayList<Passenger>    passengers;
    private Map<Passenger, String>  seatClass;
    private String status;
    private String paymentStatus;

    public Booking(String bookingReference, Customer customer, Flight flight,
                   String status, String paymentStatus) {
        this.bookingReference = bookingReference;
        this.customer         = customer;
        this.flight           = flight;
        this.status           = status;
        this.paymentStatus    = paymentStatus;
        this.passengers       = new ArrayList<>();
        this.seatClass        = new HashMap<>();
    }

    public boolean addPassenger(Passenger passenger, String seatclass) {
        if (flight.checkAvailability(seatclass)) {
            passengers.add(passenger);
            seatClass.put(passenger, seatclass);
            flight.reserveSeat(seatclass);
            return true;
        }
        return false;
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (Map.Entry<Passenger, String> entry : seatClass.entrySet()) {
            total += flight.calculatePrice(entry.getValue());
        }
        return total;
    }

    public boolean confirmBooking() {
        if (paymentStatus.equalsIgnoreCase("paid")) {
            status = "Confirmed";
            System.out.println("Booking " + bookingReference + " confirmed.");
            return true;
        }
        return false;
    }

    public String generateTicket() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== TICKET ==========\n");
        sb.append("Booking Ref : ").append(bookingReference).append("\n");
        sb.append("Customer    : ").append(customer.getName()).append("\n");
        sb.append("Flight      : ").append(flight.getFlightNumber()).append("\n");
        sb.append("Route       : ").append(flight.getOrigin())
          .append(" -> ").append(flight.getDestination()).append("\n");
        sb.append("Departure   : ").append(flight.getDepartureTime()).append("\n");
        sb.append("Passengers  :\n");
        for (Map.Entry<Passenger, String> e : seatClass.entrySet()) {
            sb.append("  - ").append(e.getKey().getName())
              .append(" [").append(e.getValue()).append("]\n");
        }
        sb.append("Total Price : $").append(String.format("%.2f", calculateTotalPrice())).append("\n");
        sb.append("Status      : ").append(status).append("\n");
        sb.append("============================\n");
        return sb.toString();
    }

    public void cancelBooking() {
        this.status = "Cancelled";
        System.out.println("Booking " + bookingReference + " cancelled.");
    }

    public String getBookingReference()          { return bookingReference; }
    public Customer getCustomer()                { return customer; }
    public Flight   getFlight()                  { return flight; }
    public List<Passenger> getPassengers()       { return new ArrayList<>(passengers); }
    public Map<Passenger, String> getSeatClass() { return seatClass; }
    public String getStatus()                    { return status; }
    public String getPaymentStatus()             { return paymentStatus; }

    public void setStatus(String status)               { this.status        = status; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setSeatClass(Map<Passenger, String> sc){ this.seatClass     = sc; }

    @Override
    public String toString() {
        return bookingReference + " | " + flight.getFlightNumber()
                + " | " + status + " | " + paymentStatus;
    }
}
