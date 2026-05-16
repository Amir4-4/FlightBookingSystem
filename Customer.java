import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private String customerID;
    private String address;
    private List<Booking> bookingHistory;
    private List<String>  preferences;

    public Customer(String userId, String username, String password,
                    String name, String email, String contactInfo,
                    String customerID, String address) {
        super(userId, username, password, name, email, contactInfo);
        this.customerID     = customerID;
        this.address        = address;
        this.bookingHistory = new ArrayList<>();
        this.preferences    = new ArrayList<>();
    }

    @Override
    public boolean login(String username, String password) {
        return username.equals(getUsername()) && password.equals(getPassword());
    }

    @Override
    public void logout() {
        System.out.println("Customer " + username + " logged out.");
    }

    public List<Flight> searchFlights(List<Flight> allFlights, String origin, String destination) {
        List<Flight> matched = new ArrayList<>();
        for (Flight f : allFlights) {
            if (f.getOrigin().equalsIgnoreCase(origin)
                    && f.getDestination().equalsIgnoreCase(destination)) {
                matched.add(f);
            }
        }
        return matched;
    }

    public void addBookingToHistory(Booking booking) {
        bookingHistory.add(booking);
    }

    public List<Booking> viewBookings() {
        return new ArrayList<>(bookingHistory);
    }

    public boolean cancelBooking(String bookingReference) {
        for (Booking b : bookingHistory) {
            if (b.getBookingReference().equals(bookingReference)) {
                b.cancelBooking();
                return true;
            }
        }
        return false;
    }

    public String getCustomerID()             { return customerID; }
    public String getAddress()                { return address; }
    public List<Booking> getBookingHistory()  { return new ArrayList<>(bookingHistory); }
    public List<String>  getPreferences()     { return new ArrayList<>(preferences); }

    public void setCustomerID(String id) { this.customerID = id; }
    public void setAddress(String a)     { this.address    = a;  }
    public void addPreference(String p)  { preferences.add(p);   }

    @Override
    public String toString() {
        return super.toString() + " | Customer | " + customerID;
    }
}
