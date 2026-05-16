import java.util.List;

public class Administrator extends User {
    private String adminId;
    private int    securityLevel;

    public Administrator(String userId, String username, String password,
                         String name, String email, String contactInfo,
                         String adminId, int securityLevel) {
        super(userId, username, password, name, email, contactInfo);
        this.adminId       = adminId;
        this.securityLevel = securityLevel;
    }

    @Override
    public boolean login(String username, String password) {
        return username.equals(getUsername()) && password.equals(getPassword());
    }

    @Override
    public void logout() {
        System.out.println("Administrator " + username + " logged out.");
    }

    public void createUser(User user, List<User> userList) {
        userList.add(user);
        System.out.println("User created: " + user.getUsername());
    }

    public void removeUser(String userId, List<User> userList) {
        boolean removed = userList.removeIf(u -> u.getUserId().equals(userId));
        System.out.println("User " + userId + (removed ? " removed." : " not found."));
    }

    public void modifySystemSettings(String setting, String newValue) {
        System.out.println("System setting [" + setting + "] updated to: " + newValue);
    }

    public void viewSystemLogs(List<Booking> bookings, List<Payment> payments) {
        System.out.println("\n===== System Logs =====");
        System.out.println("Total bookings : " + bookings.size());
        System.out.println("Total payments : " + payments.size());
        double totalRevenue = payments.stream()
                .filter(p -> p.getStatus().equals("Completed"))
                .mapToDouble(Payment::getAmount).sum();
        System.out.printf ("Total revenue  : $%.2f%n", totalRevenue);
        System.out.println("=======================");
    }

    public void manageUserAccess(String userId, String newRole, List<User> userList) {
        for (User u : userList) {
            if (u.getUserId().equals(userId)) {
                System.out.println("User " + userId + " access updated to: " + newRole);
                return;
            }
        }
        System.out.println("User " + userId + " not found.");
    }

    public String getAdminId()       { return adminId; }
    public int    getSecurityLevel() { return securityLevel; }

    public void setAdminId(String id)          { this.adminId       = id; }
    public void setSecurityLevel(int level)    { this.securityLevel = level; }

    @Override
    public String toString() {
        return super.toString() + " | Admin | " + adminId + " | Level " + securityLevel;
    }
}
