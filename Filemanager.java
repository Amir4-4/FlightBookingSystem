import java.io.*;
import java.util.*;

public class Filemanager {
    private static final String USERS_FILE      = "users.txt";
    private static final String FLIGHTS_FILE    = "flights.txt";
    private static final String BOOKINGS_FILE   = "bookings.txt";
    private static final String PASSENGERS_FILE = "passengers.txt";

    // ── Save Users ─────────────────────────────────────────────────────────
    public void saveUsers(List<User> users) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                StringBuilder sb = new StringBuilder();
                sb.append(u.getUserId()).append("|")
                  .append(u.getUsername()).append("|")
                  .append(u.getPassword()).append("|")
                  .append(u.getName()).append("|")
                  .append(u.getEmail()).append("|")
                  .append(u.getContactInfo()).append("|");

                if (u instanceof Customer) {
                    Customer c = (Customer) u;
                    sb.append("CUSTOMER|").append(c.getCustomerID())
                      .append("|").append(c.getAddress());
                } else if (u instanceof Agent) {
                    Agent a = (Agent) u;
                    sb.append("AGENT|").append(a.getAgentId())
                      .append("|").append(a.getDepartment())
                      .append("|").append(a.getCommission());
                } else if (u instanceof Administrator) {
                    Administrator ad = (Administrator) u;
                    sb.append("ADMIN|").append(ad.getAdminId())
                      .append("|").append(ad.getSecurityLevel());
                }
                w.write(sb.toString());
                w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // ── Load Users ─────────────────────────────────────────────────────────
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length < 7) continue;
                String uid = p[0], uname = p[1], pass = p[2],
                       nm  = p[3], email = p[4], ci   = p[5], type = p[6];
                switch (type) {
                    case "CUSTOMER":
                        if (p.length >= 9)
                            users.add(new Customer(uid, uname, pass, nm, email, ci, p[7], p[8]));
                        break;
                    case "AGENT":
                        if (p.length >= 10)
                            users.add(new Agent(uid, uname, pass, nm, email, ci,
                                    p[7], p[8], Double.parseDouble(p[9])));
                        break;
                    case "ADMIN":
                        if (p.length >= 9)
                            users.add(new Administrator(uid, uname, pass, nm, email, ci,
                                    p[7], Integer.parseInt(p[8])));
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    // ── Save Flights ───────────────────────────────────────────────────────
    public void saveFlights(List<Flight> flights) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FLIGHTS_FILE))) {
            for (Flight f : flights) {
                StringBuilder sb = new StringBuilder();
                sb.append(f.getFlightNumber()).append("|")
                  .append(f.getAirline()).append("|")
                  .append(f.getOrigin()).append("|")
                  .append(f.getDestination()).append("|")
                  .append(f.getDepartureTime()).append("|")
                  .append(f.getArrivalTime()).append("|");

                StringBuilder seats = new StringBuilder();
                for (Map.Entry<String, Integer> e : f.getAvailableSeats().entrySet())
                    seats.append(e.getKey()).append("=").append(e.getValue()).append(",");
                sb.append(seats).append("|");

                StringBuilder prices = new StringBuilder();
                for (Map.Entry<String, Double> e : f.getPrices().entrySet())
                    prices.append(e.getKey()).append("=").append(e.getValue()).append(",");
                sb.append(prices);

                w.write(sb.toString());
                w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving flights: " + e.getMessage());
        }
    }

    // ── Load Flights ───────────────────────────────────────────────────────
    public List<Flight> loadFlights() {
        List<Flight> flights = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(FLIGHTS_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length < 6) continue;
                Flight f = new Flight(p[0], p[1], p[2], p[3], p[4], p[5]);
                if (p.length >= 7 && !p[6].isEmpty()) {
                    for (String token : p[6].split(",")) {
                        String[] kv = token.split("=");
                        if (kv.length == 2) f.setAvailableSeats(kv[0], Integer.parseInt(kv[1]));
                    }
                }
                if (p.length >= 8 && !p[7].isEmpty()) {
                    for (String token : p[7].split(",")) {
                        String[] kv = token.split("=");
                        if (kv.length == 2) f.setPrices(kv[0], Double.parseDouble(kv[1]));
                    }
                }
                flights.add(f);
            }
        } catch (IOException e) {
            System.out.println("Error loading flights: " + e.getMessage());
        }
        return flights;
    }

    // ── Save Passengers ────────────────────────────────────────────────────
    public void savePassengers(List<Passenger> passengers) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(PASSENGERS_FILE))) {
            for (Passenger p : passengers) {
                StringBuilder sb = new StringBuilder();
                sb.append(p.getPassengerId()).append("|")
                  .append(p.getName()).append("|")
                  .append(p.getPassportNumber()).append("|")
                  .append(p.getDateOfBirth()).append("|");
                StringBuilder req = new StringBuilder();
                for (Map.Entry<String, String> e : p.getSpecialRequests().entrySet())
                    req.append(e.getKey()).append("=").append(e.getValue()).append(",");
                sb.append(req);
                w.write(sb.toString());
                w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving passengers: " + e.getMessage());
        }
    }

    // ── Load Passengers ────────────────────────────────────────────────────
    public List<Passenger> loadPassengers() {
        List<Passenger> list = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(PASSENGERS_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length < 4) continue;
                Passenger pass = new Passenger(p[0], p[1], p[2], p[3]);
                if (p.length >= 5 && !p[4].isEmpty()) {
                    for (String token : p[4].split(",")) {
                        String[] kv = token.split("=");
                        if (kv.length == 2) pass.addSpecialRequest(kv[0], kv[1]);
                    }
                }
                list.add(pass);
            }
        } catch (IOException e) {
            System.out.println("Error loading passengers: " + e.getMessage());
        }
        return list;
    }

    // ── Save Bookings ──────────────────────────────────────────────────────
    public void saveBookings(List<Booking> bookings) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking b : bookings) {
                StringBuilder sb = new StringBuilder();
                sb.append(b.getBookingReference()).append("|")
                  .append(b.getCustomer().getCustomerID()).append("|")
                  .append(b.getFlight().getFlightNumber()).append("|")
                  .append(b.getStatus()).append("|")
                  .append(b.getPaymentStatus()).append("|");
                StringBuilder pids = new StringBuilder();
                for (Passenger p : b.getPassengers())
                    pids.append(p.getPassengerId()).append(",");
                sb.append(pids).append("|");
                StringBuilder seats = new StringBuilder();
                for (Map.Entry<Passenger, String> e : b.getSeatClass().entrySet())
                    seats.append(e.getKey().getPassengerId()).append("=").append(e.getValue()).append(",");
                sb.append(seats);
                w.write(sb.toString());
                w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    // ── Load Bookings ──────────────────────────────────────────────────────
    public List<Booking> loadBookings(List<Customer> customers, List<Flight> flights,
                                      List<Passenger> allPassengers) {
        Map<String, Customer>  cMap = new HashMap<>();
        Map<String, Flight>    fMap = new HashMap<>();
        Map<String, Passenger> pMap = new HashMap<>();
        for (Customer c : customers)  cMap.put(c.getCustomerID(),   c);
        for (Flight   f : flights)    fMap.put(f.getFlightNumber(),  f);
        for (Passenger p : allPassengers) pMap.put(p.getPassengerId(), p);

        List<Booking> list = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length < 5) continue;
                Customer c = cMap.get(p[1]);
                Flight   f = fMap.get(p[2]);
                if (c == null || f == null) continue;
                Booking b = new Booking(p[0], c, f, p[3], p[4]);
                if (p.length >= 7 && !p[5].isEmpty()) {
                    Map<String, String> seatMap = new HashMap<>();
                    if (!p[6].isEmpty()) {
                        for (String t : p[6].split(",")) {
                            String[] kv = t.split("=");
                            if (kv.length == 2) seatMap.put(kv[0], kv[1]);
                        }
                    }
                    for (String pid : p[5].split(",")) {
                        Passenger pass = pMap.get(pid);
                        if (pass != null) {
                            String sc = seatMap.getOrDefault(pid, "Economy");
                            b.addPassenger(pass, sc);
                        }
                    }
                }
                list.add(b);
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
        return list;
    }
}
