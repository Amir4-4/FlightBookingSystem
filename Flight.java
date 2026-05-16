import java.util.HashMap;
import java.util.Map;

public class Flight {
    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private Map<String, Integer> availableSeats;
    private Map<String, Double>  prices;

    public Flight(String flightNumber, String airline, String origin,
                  String destination, String departureTime, String arrivalTime) {
        this.flightNumber   = flightNumber;
        this.airline        = airline;
        this.origin         = origin;
        this.destination    = destination;
        this.departureTime  = departureTime;
        this.arrivalTime    = arrivalTime;
        this.availableSeats = new HashMap<>();
        this.prices         = new HashMap<>();

        availableSeats.put("Economy",     150);
        availableSeats.put("Business",     30);
        availableSeats.put("First Class",  10);

        prices.put("Economy",      500.0);
        prices.put("Business",    1200.0);
        prices.put("First Class", 2500.0);
    }

    public double calculatePrice(String seatClass) {
        return prices.getOrDefault(seatClass, 0.0);
    }

    public boolean checkAvailability(String seatClass) {
        return availableSeats.getOrDefault(seatClass, 0) > 0;
    }

    public boolean reserveSeat(String seatClass) {
        int available = availableSeats.getOrDefault(seatClass, 0);
        if (available > 0) {
            availableSeats.put(seatClass, available - 1);
            return true;
        }
        return false;
    }

    public void setAvailableSeats(String seatClass, int count) {
        availableSeats.put(seatClass, count);
    }

    public void setPrices(String seatClass, double price) {
        prices.put(seatClass, price);
    }

    public String getFlightNumber()             { return flightNumber; }
    public String getAirline()                  { return airline; }
    public String getOrigin()                   { return origin; }
    public String getDestination()              { return destination; }
    public String getDepartureTime()            { return departureTime; }
    public String getArrivalTime()              { return arrivalTime; }
    public Map<String, Integer> getAvailableSeats() { return availableSeats; }
    public Map<String, Double>  getPrices()     { return new HashMap<>(prices); }

    public void setFlightNumber(String fn)   { this.flightNumber  = fn; }
    public void setAirline(String a)         { this.airline       = a;  }
    public void setOrigin(String o)          { this.origin        = o;  }
    public void setDestination(String d)     { this.destination   = d;  }
    public void setDepartureTime(String dt)  { this.departureTime = dt; }
    public void setArrivalTime(String at)    { this.arrivalTime   = at; }

    @Override
    public String toString() {
        return flightNumber + " | " + airline + " | " + origin
                + " -> " + destination + " | Dep: " + departureTime;
    }
}
