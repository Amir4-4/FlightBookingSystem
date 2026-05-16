public abstract class User {
    protected String userId;
    protected String username;
    protected String password;
    protected String name;
    protected String email;
    protected String contactInfo;

    public User(String userId, String username, String password,
                String name, String email, String contactInfo) {
        this.userId      = userId;
        this.username    = username;
        this.password    = password;
        this.name        = name;
        this.email       = email;
        this.contactInfo = contactInfo;
    }

    public abstract boolean login(String username, String password);
    public abstract void logout();

    public boolean validatePassword(String password) {
        return password.length() >= 6
                && password.matches(".*[a-zA-Z].*")
                && password.matches(".*[0-9].*");
    }

    public void updateProfile(String name, String email, String contactInfo) {
        this.name        = name;
        this.email       = email;
        this.contactInfo = contactInfo;
        System.out.println("Profile updated.");
    }

    public String getUserId()      { return userId; }
    public String getUsername()    { return username; }
    public String getPassword()    { return password; }
    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public String getContactInfo() { return contactInfo; }

    public void setName(String name)               { this.name        = name; }
    public void setEmail(String email)             { this.email       = email; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public void setPassword(String password) {
        if (validatePassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException(
                    "Password must be at least 6 characters with letters and numbers.");
        }
    }

    @Override
    public String toString() {
        return userId + " | " + username + " | " + name + " | " + email;
    }
}
