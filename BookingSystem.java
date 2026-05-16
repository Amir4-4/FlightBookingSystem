import java.util.ArrayList;
import java.util.List;

public class BookingSystem {
    private List<User>    users    = new ArrayList<>();
    private List<Flight>  flights  = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    // ── Flight management ──────────────────────────────────────────────────
    public List<Flight> getFlights() { return new ArrayList<>(flights); }

    public List<Flight> searchFlights(String origin, String destination) {
        List<Flight> result = new ArrayList<>();
        for (Flight f : flights) {
            if (f.getOrigin().equalsIgnoreCase(origin)
                    && f.getDestination().equalsIgnoreCase(destination)) {
                result.add(f);
            }
        }
        return result;
    }

    // ── Booking management ─────────────────────────────────────────────────
    public Booking createBooking(Customer customer, Flight flight) {
        String ref = "B" + String.format("%03d", bookings.size() + 1);
        Booking b  = new Booking(ref, customer, flight, "Pending", "Unpaid");
        bookings.add(b);
        return b;
    }

    public List<Booking> getBookingsByCustomer(String customerId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getCustomer().getCustomerID().equals(customerId)) result.add(b);
        }
        return result;
    }

    // ── Payment management ─────────────────────────────────────────────────
    public boolean processPayment(Payment payment) {
        if (payment.isValid() && payment.processPayment()) {
            payments.add(payment);
            return true;
        }
        return false;
    }

    // ── Ticket ─────────────────────────────────────────────────────────────
    public String generateTicket(Booking booking) {
        return booking.generateTicket();
    }

    // ── Collection adders ─────────────────────────────────────────────────
    public void addUser(User user)       { users.add(user);     }
    public void addFlight(Flight flight) { flights.add(flight); }
    public void addBooking(Booking b)    { bookings.add(b);     }
    public void addPayment(Payment p)    { payments.add(p);     }

    // ── Getters ────────────────────────────────────────────────────────────
    public List<User>    getUsers()    { return new ArrayList<>(users);    }
    public List<Booking> getBookings() { return new ArrayList<>(bookings); }
    public List<Payment> getPayments() { return new ArrayList<>(payments); }
}
