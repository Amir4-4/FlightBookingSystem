import java.util.List;

public class Agent extends User {
    private String agentId;
    private String department;
    private double commission;

    public Agent(String userId, String username, String password,
                 String name, String email, String contactInfo,
                 String agentId, String department, double commission) {
        super(userId, username, password, name, email, contactInfo);
        this.agentId     = agentId;
        this.department  = department;
        this.commission  = commission;
    }

    @Override
    public boolean login(String username, String password) {
        return username.equals(getUsername()) && password.equals(getPassword());
    }

    @Override
    public void logout() {
        System.out.println("Agent " + username + " logged out.");
    }

    public boolean manageFlights(String action, Flight flight,
                                 List<Flight> flightList) {
        switch (action.toLowerCase()) {
            case "add":
                flightList.add(flight);
                System.out.println("Flight " + flight.getFlightNumber() + " added.");
                return true;
            case "delete":
                boolean removed = flightList.removeIf(
                        f -> f.getFlightNumber().equals(flight.getFlightNumber()));
                System.out.println("Flight " + flight.getFlightNumber()
                        + (removed ? " deleted." : " not found."));
                return removed;
            default:
                System.out.println("Unknown action: " + action);
                return false;
        }
    }

    public Booking createBookingForCustomer(Customer customer, Flight flight,
                                            List<Passenger> passengers,
                                            List<String> seatClasses,
                                            List<Booking> bookingList) {
        String ref = "B" + String.format("%03d", bookingList.size() + 1);
        Booking booking = new Booking(ref, customer, flight, "Pending", "Unpaid");
        for (int i = 0; i < passengers.size(); i++) {
            String sc = (i < seatClasses.size()) ? seatClasses.get(i) : "Economy";
            booking.addPassenger(passengers.get(i), sc);
        }
        bookingList.add(booking);
        customer.addBookingToHistory(booking);
        System.out.println("Booking created: " + ref);
        return booking;
    }

    public void modifyBooking(Booking booking, String newStatus) {
        booking.setStatus(newStatus);
        System.out.println("Booking " + booking.getBookingReference()
                + " status updated to: " + newStatus);
    }

    public void generateReports(List<Booking> bookings) {
        System.out.println("\n===== Agent Report =====");
        System.out.println("Total bookings: " + bookings.size());
        long confirmed  = bookings.stream().filter(b -> b.getStatus().equals("Confirmed")).count();
        long cancelled  = bookings.stream().filter(b -> b.getStatus().equals("Cancelled")).count();
        double revenue  = bookings.stream()
                .filter(b -> b.getPaymentStatus().equalsIgnoreCase("paid"))
                .mapToDouble(Booking::calculateTotalPrice).sum();
        System.out.println("Confirmed: " + confirmed);
        System.out.println("Cancelled: " + cancelled);
        System.out.printf ("Revenue  : $%.2f%n", revenue);
        System.out.println("========================");
    }

    public String getAgentId()    { return agentId; }
    public String getDepartment() { return department; }
    public double getCommission() { return commission; }

    public void setAgentId(String id)       { this.agentId    = id;  }
    public void setDepartment(String dept)  { this.department = dept;}
    public void setCommission(double c)     { this.commission = c;   }

    @Override
    public String toString() {
        return super.toString() + " | Agent | " + agentId + " | " + department;
    }
}
