import java.util.HashMap;
import java.util.Map;

public class Passenger {
    private String passengerId;
    private String name;
    private String passportNumber;
    private String dateOfBirth;
    private Map<String, String> specialRequests;

    public Passenger(String passengerId, String name,
                     String passportNumber, String dateOfBirth) {
        this.passengerId    = passengerId;
        this.name           = name;
        this.passportNumber = passportNumber;
        this.dateOfBirth    = dateOfBirth;
        this.specialRequests = new HashMap<>();
    }

    public boolean updateInfo(String name, String passportNumber, String dateOfBirth) {
        this.name           = name;
        this.passportNumber = passportNumber;
        this.dateOfBirth    = dateOfBirth;
        System.out.println("Passenger info updated.");
        return true;
    }

    public Map<String, String> getPassengerDetails() {
        Map<String, String> details = new HashMap<>();
        details.put("passengerId",    passengerId);
        details.put("name",           name);
        details.put("passportNumber", passportNumber);
        details.put("dateOfBirth",    dateOfBirth);
        return details;
    }

    public void addSpecialRequest(String requestType, String details) {
        specialRequests.put(requestType, details);
    }

    public String getPassengerId()    { return passengerId; }
    public String getName()           { return name; }
    public String getPassportNumber() { return passportNumber; }
    public String getDateOfBirth()    { return dateOfBirth; }
    public Map<String, String> getSpecialRequests() { return new HashMap<>(specialRequests); }

    public void setName(String name)                  { this.name           = name; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    public void setDateOfBirth(String dateOfBirth)    { this.dateOfBirth    = dateOfBirth; }

    @Override
    public String toString() {
        return passengerId + " | " + name + " | " + passportNumber;
    }
}
