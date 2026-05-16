import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * FlightBookingGUI - Swing front-end for the Flight Booking Management System.
 * Supports three roles: Customer, Agent, Administrator.
 */
public class FlightBookingGUI extends JFrame {

    // -- Shared state -------------------------------------------------------
    private List<User>      users      = new ArrayList<>();
    private List<Flight>    flights    = new ArrayList<>();
    private List<Booking>   bookings   = new ArrayList<>();
    private List<Passenger> passengers = new ArrayList<>();
    private List<Payment>   payments   = new ArrayList<>();
    private BookingSystem   system     = new BookingSystem();
    private Filemanager     fm         = new Filemanager();
    private User            currentUser;

    // -- Design tokens ------------------------------------------------------
    private static final Color C_BG        = new Color(245, 245, 245);
    private static final Color C_WHITE     = Color.WHITE;
    private static final Color C_PRIMARY   = new Color(30,  30,  30);
    private static final Color C_ACCENT    = new Color(37, 99, 235);
    private static final Color C_SUCCESS   = new Color(22, 163,  74);
    private static final Color C_DANGER    = new Color(220,  38,  38);
    private static final Color C_WARNING   = new Color(217, 119,   6);
    private static final Color C_MUTED     = new Color(107, 114, 128);
    private static final Color C_BORDER    = new Color(229, 231, 235);
    private static final Font  FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  FONT_H2     = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font  FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font  FONT_MONO   = new Font("Consolas", Font.PLAIN, 12);

    // -- Card panel ---------------------------------------------------------
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public FlightBookingGUI() {
        initData();
        buildFrame();
        showLogin();
    }

    // =======================================================================
    //  DATA INITIALISATION
    // =======================================================================
    private void initData() {
        flights.add(new Flight("FL001","AirLine1","New York","London","2025-06-15 10:00","2025-06-15 22:00"));
        flights.add(new Flight("FL002","AirLine2","London","Paris","2025-06-16 08:00","2025-06-16 10:30"));
        flights.add(new Flight("FL003","AirLine3","Cairo","Dubai","2025-07-01 14:00","2025-07-01 19:30"));
        flights.add(new Flight("FL004","AirLine1","New York","Paris","2025-07-05 09:00","2025-07-05 23:00"));

        users.add(new Customer("U001","customer1","Pass123","John Doe","john@example.com","123-456-7890","C001","123 Main St"));
        users.add(new Agent("U002","agent1","Pass123","Agent Smith","agent@example.com","555-111-2222","A001","Sales",0.05));
        users.add(new Administrator("U003","admin1","Pass123","Admin User","admin@system.com","555-999-8888","ADM001",5));

        Passenger p1 = new Passenger("P001","John Doe","AB123456","1980-01-15");
        p1.addSpecialRequest("meal","Vegetarian");
        passengers.add(p1);
    }

    // =======================================================================
    //  FRAME SETUP
    // =======================================================================
    private void buildFrame() {
        setTitle("SkyBook - Flight Booking Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 560));
        setPreferredSize(new Dimension(960, 640));
        getContentPane().setBackground(C_BG);

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(C_BG);
        add(cardPanel, BorderLayout.CENTER);

        setSize(getPreferredSize());
        setLocationRelativeTo(null);
    }

    private void showCard(String name) { cardLayout.show(cardPanel, name); }

    // =======================================================================
    //  LOGIN / SIGNUP SCREEN
    // =======================================================================
    // Color palette for auth screen
    private static final Color AUTH_BG        = new Color(15,  23,  42);   // dark navy
    private static final Color AUTH_CARD      = new Color(30,  41,  59);   // slate card
    private static final Color AUTH_ACCENT    = new Color(56, 189, 248);   // sky blue
    private static final Color AUTH_ACCENT2   = new Color(99, 102, 241);   // indigo
    private static final Color AUTH_TEXT      = new Color(241, 245, 249);  // near white
    private static final Color AUTH_MUTED     = new Color(148, 163, 184);  // slate-400
    private static final Color AUTH_INPUT_BG  = new Color(51,  65,  85);   // slate-700
    private static final Color AUTH_INPUT_BD  = new Color(71,  85, 105);   // slate-600
    private static final Color AUTH_ERROR     = new Color(248, 113, 113);  // red-400

    private void showLogin() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(AUTH_BG);

        // Card
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(AUTH_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        card.setPreferredSize(new Dimension(420, 520));

        // ---- Tab bar (Login / Sign Up) ----
        JPanel tabBar = new JPanel(new GridLayout(1, 2));
        tabBar.setBackground(new Color(15, 23, 42));
        tabBar.setPreferredSize(new Dimension(420, 46));

        JButton tabLogin  = new JButton("Sign In");
        JButton tabSignup = new JButton("Sign Up");
        for (JButton tb : new JButton[]{tabLogin, tabSignup}) {
            tb.setFont(new Font("Segoe UI", Font.BOLD, 13));
            tb.setOpaque(true);
            tb.setBorderPainted(false);
            tb.setFocusPainted(false);
            tb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        tabBar.add(tabLogin);
        tabBar.add(tabSignup);

        // ---- Inner card panel (switches between login/signup forms) ----
        JPanel inner = new JPanel(new CardLayout());
        inner.setBackground(AUTH_CARD);
        CardLayout innerLayout = (CardLayout) inner.getLayout();

        // ===== LOGIN FORM =====
        JPanel loginForm = new JPanel(new GridLayout(0, 1, 0, 8));
        loginForm.setBackground(AUTH_CARD);
        loginForm.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel loginTitle = new JLabel("Welcome back", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loginTitle.setForeground(AUTH_TEXT);

        JLabel loginSub = new JLabel("Sign in to your SkyBook account", SwingConstants.CENTER);
        loginSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginSub.setForeground(AUTH_MUTED);

        JLabel lblUser = authLabel("Username");
        JTextField tfUser = authField();

        JLabel lblPass = authLabel("Password");
        JPasswordField tfPass = authPassField();

        JLabel loginErr = new JLabel(" ", SwingConstants.CENTER);
        loginErr.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginErr.setForeground(AUTH_ERROR);

        JButton btnLogin = authPrimaryBtn("Sign In", AUTH_ACCENT);

        JLabel loginHint = new JLabel("Demo: customer1 | agent1 | admin1  (Pass123)", SwingConstants.CENTER);
        loginHint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        loginHint.setForeground(AUTH_MUTED);

        loginForm.add(loginTitle);
        loginForm.add(loginSub);
        loginForm.add(new JLabel(" "));
        loginForm.add(lblUser);
        loginForm.add(tfUser);
        loginForm.add(lblPass);
        loginForm.add(tfPass);
        loginForm.add(loginErr);
        loginForm.add(btnLogin);
        loginForm.add(loginHint);

        // ===== SIGN UP FORM =====
        JPanel signupForm = new JPanel(new GridLayout(0, 1, 0, 6));
        signupForm.setBackground(AUTH_CARD);
        signupForm.setBorder(BorderFactory.createEmptyBorder(22, 32, 22, 32));

        JLabel signupTitle = new JLabel("Create account", SwingConstants.CENTER);
        signupTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        signupTitle.setForeground(AUTH_TEXT);

        JLabel signupSub = new JLabel("Join SkyBook today", SwingConstants.CENTER);
        signupSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        signupSub.setForeground(AUTH_MUTED);

        JTextField tfSuName   = authField();
        JTextField tfSuUser   = authField();
        JPasswordField tfSuPass = authPassField();
        JTextField tfSuEmail  = authField();
        JTextField tfSuPhone  = authField();
        JTextField tfSuAddr   = authField();

        JLabel signupErr = new JLabel(" ", SwingConstants.CENTER);
        signupErr.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        signupErr.setForeground(AUTH_ERROR);

        JButton btnSignup = authPrimaryBtn("Create Account", AUTH_ACCENT2);

        signupForm.add(signupTitle);
        signupForm.add(signupSub);
        signupForm.add(authLabel("Full Name *"));
        signupForm.add(tfSuName);
        signupForm.add(authLabel("Username *"));
        signupForm.add(tfSuUser);
        signupForm.add(authLabel("Password *  (min 6 chars, letters+numbers)"));
        signupForm.add(tfSuPass);
        signupForm.add(authLabel("Email *"));
        signupForm.add(tfSuEmail);
        signupForm.add(authLabel("Phone"));
        signupForm.add(tfSuPhone);
        signupForm.add(authLabel("Address"));
        signupForm.add(tfSuAddr);
        signupForm.add(signupErr);
        signupForm.add(btnSignup);

        inner.add(loginForm,  "login");
        inner.add(signupForm, "signup");
        innerLayout.show(inner, "login");

        // ---- Tab switching logic ----
        Runnable showLoginTab = () -> {
            tabLogin.setBackground(AUTH_CARD);
            tabLogin.setForeground(AUTH_ACCENT);
            tabSignup.setBackground(new Color(15, 23, 42));
            tabSignup.setForeground(AUTH_MUTED);
            innerLayout.show(inner, "login");
        };
        Runnable showSignupTab = () -> {
            tabSignup.setBackground(AUTH_CARD);
            tabSignup.setForeground(AUTH_ACCENT2);
            tabLogin.setBackground(new Color(15, 23, 42));
            tabLogin.setForeground(AUTH_MUTED);
            innerLayout.show(inner, "signup");
        };
        showLoginTab.run();

        tabLogin.addActionListener(e  -> showLoginTab.run());
        tabSignup.addActionListener(e -> showSignupTab.run());

        // ---- Login action ----
        Runnable doLogin = () -> {
            String u = tfUser.getText().trim();
            String p = new String(tfPass.getPassword());
            User found = null;
            for (User usr : users) {
                if (usr.getUsername().equals(u) && usr.getPassword().equals(p)) { found = usr; break; }
            }
            if (found == null) { loginErr.setText("Invalid username or password."); return; }
            currentUser = found;
            openDashboard();
        };
        btnLogin.addActionListener(e -> doLogin.run());
        tfPass.addActionListener(e -> doLogin.run());
        tfUser.addActionListener(e -> tfPass.requestFocus());

        // ---- Sign up action ----
        btnSignup.addActionListener(e -> {
            String name  = tfSuName.getText().trim();
            String uname = tfSuUser.getText().trim();
            String pass  = new String(tfSuPass.getPassword());
            String email = tfSuEmail.getText().trim();
            String phone = tfSuPhone.getText().trim();
            String addr  = tfSuAddr.getText().trim();

            if (name.isEmpty() || uname.isEmpty() || pass.isEmpty() || email.isEmpty()) {
                signupErr.setText("Please fill in all required fields."); return;
            }
            if (users.stream().anyMatch(u -> u.getUsername().equals(uname))) {
                signupErr.setText("Username already taken."); return;
            }
            if (pass.length() < 6 || !pass.matches(".*[a-zA-Z].*") || !pass.matches(".*[0-9].*")) {
                signupErr.setText("Password: min 6 chars, needs letters AND numbers."); return;
            }
            String uid = "U" + String.format("%03d", users.size() + 1);
            String cid = "C" + String.format("%03d", (int) users.stream().filter(u -> u instanceof Customer).count() + 1);
            Customer newUser = new Customer(uid, uname, pass, name, email, phone, cid, addr);
            users.add(newUser);
            currentUser = newUser;
            openDashboard();
        });

        card.add(tabBar,  BorderLayout.NORTH);
        card.add(inner,   BorderLayout.CENTER);
        root.add(card);
        cardPanel.add(root, "login");
        showCard("login");
        setVisible(true);
    }

    // ---- Auth form helpers ----
    private JLabel authLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(AUTH_MUTED);
        return l;
    }
    private JTextField authField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBackground(AUTH_INPUT_BG);
        tf.setForeground(AUTH_TEXT);
        tf.setCaretColor(AUTH_TEXT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AUTH_INPUT_BD),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        return tf;
    }
    private JPasswordField authPassField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pf.setBackground(AUTH_INPUT_BG);
        pf.setForeground(AUTH_TEXT);
        pf.setCaretColor(AUTH_TEXT);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AUTH_INPUT_BD),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        return pf;
    }
    private JButton authPrimaryBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(300, 40));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // =======================================================================
    //  ROUTE TO DASHBOARD
    // =======================================================================
    private void openDashboard() {
        if (currentUser instanceof Administrator) buildAdminView();
        else if (currentUser instanceof Agent)    buildAgentView();
        else                                       buildCustomerView();
        showCard("dashboard");
    }

    // =======================================================================
    //  SHARED DASHBOARD SHELL
    // =======================================================================
    private JPanel dashboardShell(String role, Color roleColor, Component contentArea) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(C_BG);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(C_WHITE);
        topBar.setBorder(new CompoundBorder(
                new MatteBorder(0,0,1,0, C_BORDER),
                new EmptyBorder(12,20,12,20)));

        JLabel titleLbl = new JLabel("SkyBook");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(C_PRIMARY);

        JPanel rightBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBar.setOpaque(false);
        JLabel roleBadge = pill(role, roleColor);
        JLabel nameLbl   = new JLabel(currentUser.getName());
        nameLbl.setFont(FONT_BODY);
        nameLbl.setForeground(C_MUTED);
        JButton logoutBtn = ghostBtn("Sign out");
        logoutBtn.addActionListener(e -> { currentUser = null; cardPanel.removeAll(); buildFrame(); showLogin(); });

        rightBar.add(roleBadge); rightBar.add(nameLbl); rightBar.add(logoutBtn);
        topBar.add(titleLbl, BorderLayout.WEST);
        topBar.add(rightBar, BorderLayout.EAST);

        root.add(topBar, BorderLayout.NORTH);
        root.add(contentArea, BorderLayout.CENTER);
        return root;
    }

    // =======================================================================
    //  CUSTOMER DASHBOARD
    // =======================================================================
    private void buildCustomerView() {
        JTabbedPane tabs = styledTabs();
        tabs.addTab("Search Flights",  searchFlightsPanel());
        tabs.addTab("New Booking",     newBookingPanel());
        tabs.addTab("My Bookings",     myBookingsPanel());
        tabs.addTab("Profile",         profilePanel());

        JPanel shell = dashboardShell("Customer", C_ACCENT, tabs);
        cardPanel.add(shell, "dashboard");
    }

    // -- Search Flights -----------------------------------------------------
    private JPanel searchFlightsPanel() {
        JPanel p = padded(C_BG);
        p.setLayout(new BorderLayout(0, 16));

        // Search bar
        JPanel bar = card(-1, -1);
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        JTextField tfFrom = styledField(14); JTextField tfTo = styledField(14);
        JButton    btnSrc = accentBtn("Search");
        bar.add(label("From:")); bar.add(tfFrom);
        bar.add(label("To:"));   bar.add(tfTo);
        bar.add(btnSrc);

        // Results table
        String[] cols = {"Flight","Airline","From","To","Departure","Arrival","Economy","Business","First Class"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;} };
        for (Flight f : flights) model.addRow(flightRow(f));
        JTable table = styledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(C_BORDER));

        btnSrc.addActionListener(e -> {
            String from = tfFrom.getText().trim().toLowerCase();
            String to   = tfTo.getText().trim().toLowerCase();
            model.setRowCount(0);
            for (Flight f : flights) {
                if ((from.isEmpty() || f.getOrigin().toLowerCase().contains(from))
                 && (to.isEmpty()   || f.getDestination().toLowerCase().contains(to)))
                    model.addRow(flightRow(f));
            }
            if (model.getRowCount() == 0) toast(p, "No flights found for this route.", C_WARNING);
        });

        p.add(bar, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private Object[] flightRow(Flight f) {
        return new Object[]{
            f.getFlightNumber(), f.getAirline(), f.getOrigin(), f.getDestination(),
            f.getDepartureTime(), f.getArrivalTime(),
            "$"+f.calculatePrice("Economy"),
            "$"+f.calculatePrice("Business"),
            "$"+f.calculatePrice("First Class")
        };
    }

    // -- New Booking --------------------------------------------------------
    private JPanel newBookingPanel() {
        JPanel p = padded(C_BG);
        p.setLayout(new BorderLayout(0,16));

        // Step card
        JPanel top = new JPanel(new GridLayout(1, 3, 10, 0));
        top.setOpaque(false);
        JLabel[] steps = {stepLabel("1","Choose Flight",true), stepLabel("2","Add Passengers",false), stepLabel("3","Payment",false)};
        for (JLabel s : steps) top.add(s);

        JPanel body = new JPanel(new CardLayout());
        CardLayout bodyLayout = (CardLayout) body.getLayout();

        // -- Step 1: pick flight ------------------------------------------
        JPanel step1 = padded(C_WHITE);
        step1.setLayout(new BorderLayout(0,10));
        step1.setBorder(roundedBorder());

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
        searchBar.setOpaque(false);
        JTextField s1From = styledField(14); JTextField s1To = styledField(14);
        JButton s1Search = accentBtn("Search");
        searchBar.add(label("From:")); searchBar.add(s1From);
        searchBar.add(label("To:"));   searchBar.add(s1To);
        searchBar.add(s1Search);

        String[] fcols = {"#","Flight","Airline","From","To","Departure","Economy","Business","First Class"};
        DefaultTableModel fm1 = new DefaultTableModel(fcols,0){ public boolean isCellEditable(int r,int c){return false;} };
        JTable ft = styledTable(fm1);
        ft.getColumnModel().getColumn(0).setMaxWidth(30);
        JScrollPane fsc = new JScrollPane(ft);
        fsc.setBorder(BorderFactory.createLineBorder(C_BORDER));
        JButton s1Next = accentBtn("Next: Add Passengers");
        s1Next.setEnabled(false);

        s1Search.addActionListener(e -> {
            String from = s1From.getText().trim().toLowerCase();
            String to   = s1To.getText().trim().toLowerCase();
            fm1.setRowCount(0);
            int idx = 1;
            for (Flight f : flights) {
                if ((from.isEmpty()||f.getOrigin().toLowerCase().contains(from))
                 && (to.isEmpty()  ||f.getDestination().toLowerCase().contains(to))) {
                    fm1.addRow(new Object[]{idx++,f.getFlightNumber(),f.getAirline(),
                        f.getOrigin(),f.getDestination(),f.getDepartureTime(),
                        "$"+f.calculatePrice("Economy"),"$"+f.calculatePrice("Business"),
                        "$"+f.calculatePrice("First Class")});
                }
            }
            s1Next.setEnabled(fm1.getRowCount()>0);
        });
        ft.getSelectionModel().addListSelectionListener(e -> s1Next.setEnabled(ft.getSelectedRow()>=0));

        // Passenger form container (step 2)
        JPanel step2 = padded(C_WHITE);
        step2.setLayout(new BorderLayout(0,10));
        step2.setBorder(roundedBorder());

        // Payment form container (step 3)
        JPanel step3 = padded(C_WHITE);
        step3.setLayout(new BorderLayout(0,10));
        step3.setBorder(roundedBorder());

        // step2 content
        JPanel paxForm = new JPanel();
        paxForm.setLayout(new BoxLayout(paxForm, BoxLayout.Y_AXIS));
        paxForm.setBackground(C_WHITE);
        JScrollPane paxScroll = new JScrollPane(paxForm);
        paxScroll.setBorder(null);

        List<JTextField> paxNames     = new ArrayList<>();
        List<JTextField> paxPassports = new ArrayList<>();
        List<JTextField> paxDobs      = new ArrayList<>();
        List<JComboBox<String>> paxSeats = new ArrayList<>();

        JButton addPaxBtn  = ghostBtn("Add Passenger");
        JButton s2Next     = accentBtn("Continue to payment >");
        JButton s2Back     = ghostBtn("Back");

        Runnable refreshPaxForm = () -> {
            paxForm.removeAll();
            for (int i = 0; i < paxNames.size(); i++) {
                JPanel pRow = new JPanel(new GridLayout(2, 4, 8, 4));
                pRow.setBackground(C_WHITE);
                pRow.setBorder(new CompoundBorder(
                        new MatteBorder(0,0,1,0,C_BORDER), new EmptyBorder(8,0,8,0)));
                pRow.add(label("Passenger " + (i + 1) + " name")); pRow.add(label("Passport #"));
                pRow.add(label("Date of birth"));                   pRow.add(label("Seat class"));
                pRow.add(paxNames.get(i));    pRow.add(paxPassports.get(i));
                pRow.add(paxDobs.get(i));     pRow.add(paxSeats.get(i));
                paxForm.add(pRow);
            }
            paxForm.revalidate(); paxForm.repaint();
        };

        Runnable addPaxRow = () -> {
            paxNames.add(styledField(12));
            paxPassports.add(styledField(10));
            paxDobs.add(styledField(10));
            paxSeats.add(new JComboBox<>(new String[]{"Economy","Business","First Class"}));
            styleCombo(paxSeats.get(paxSeats.size()-1));
            refreshPaxForm.run();
        };

        addPaxBtn.addActionListener(e -> addPaxRow.run());

        JPanel s2Bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        s2Bottom.setBackground(C_WHITE);
        s2Bottom.add(s2Back); s2Bottom.add(addPaxBtn); s2Bottom.add(s2Next);
        step2.add(h2("Add Passengers"), BorderLayout.NORTH);
        step2.add(paxScroll, BorderLayout.CENTER);
        step2.add(s2Bottom, BorderLayout.SOUTH);

        // Payment form (step 3)
        JPanel payForm = new JPanel(new GridLayout(0,2,12,10));
        payForm.setBackground(C_WHITE);
        JTextField tfHolder = styledField(20); JTextField tfCard = styledField(20);
        JTextField tfExpiry = styledField(8);  JTextField tfCvv   = styledField(6);
        payForm.add(label("Cardholder name")); payForm.add(tfHolder);
        payForm.add(label("Card number"));     payForm.add(tfCard);
        payForm.add(label("Expiry (MM/YY)")); payForm.add(tfExpiry);
        payForm.add(label("CVV"));             payForm.add(tfCvv);
        JLabel s3SummaryLbl = new JLabel(" ");
        s3SummaryLbl.setFont(FONT_BODY);

        JButton s3Pay  = accentBtn("Confirm & Pay");
        JButton s3Back = ghostBtn("Back");
        JPanel  s3Bot  = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        s3Bot.setBackground(C_WHITE);
        s3Bot.add(s3Back); s3Bot.add(s3Pay);
        step3.add(h2("Payment"), BorderLayout.NORTH);
        JPanel s3Mid = padded(C_WHITE); s3Mid.setLayout(new BorderLayout(0,12));
        s3Mid.add(payForm, BorderLayout.NORTH); s3Mid.add(s3SummaryLbl, BorderLayout.CENTER);
        step3.add(s3Mid, BorderLayout.CENTER);
        step3.add(s3Bot, BorderLayout.SOUTH);

        // Navigation
        final Flight[] selectedFlight = {null};

        s1Next.addActionListener(e -> {
            int row = ft.getSelectedRow();
            if (row < 0) { toast(p,"Please select a flight.", C_WARNING); return; }
            String fn = (String) fm1.getValueAt(row,1);
            for (Flight f : flights) if (f.getFlightNumber().equals(fn)) { selectedFlight[0]=f; break; }
            if (paxNames.isEmpty()) addPaxRow.run();
            steps[0].setForeground(C_MUTED); steps[1].setForeground(C_ACCENT);
            bodyLayout.show(body,"step2");
        });

        s2Back.addActionListener(e -> { steps[1].setForeground(C_MUTED); steps[0].setForeground(C_ACCENT); bodyLayout.show(body,"step1"); });

        s2Next.addActionListener(e -> {
            for (int i=0;i<paxNames.size();i++) {
                if (paxNames.get(i).getText().trim().isEmpty() || paxPassports.get(i).getText().trim().isEmpty()) {
                    toast(p,"Fill in all passenger details.",C_DANGER); return;
                }
            }
            if (selectedFlight[0]!=null) {
                double total = 0;
                for (JComboBox<String> sc : paxSeats) total += selectedFlight[0].calculatePrice((String)sc.getSelectedItem());
                s3SummaryLbl.setText("<html><b>Flight:</b> "+selectedFlight[0].getFlightNumber()+" "+selectedFlight[0].getOrigin()+" > "+selectedFlight[0].getDestination()
                        +"<br><b>Passengers:</b> "+paxNames.size()+"<br><b>Total: $"+String.format("%.2f",total)+"</b></html>");
            }
            steps[1].setForeground(C_MUTED); steps[2].setForeground(C_ACCENT);
            bodyLayout.show(body,"step3");
        });

        s3Back.addActionListener(e -> { steps[2].setForeground(C_MUTED); steps[1].setForeground(C_ACCENT); bodyLayout.show(body,"step2"); });

        s3Pay.addActionListener(e -> {
            if (tfCard.getText().replaceAll("\\s","").length()<13 || tfCvv.getText().length()<3 || tfHolder.getText().trim().isEmpty()) {
                toast(p,"Invalid card details.",C_DANGER); return;
            }
            Customer cust = (Customer) currentUser;
            String ref = "B"+String.format("%03d",bookings.size()+1);
            Booking bk = new Booking(ref, cust, selectedFlight[0], "Pending","Unpaid");
            List<Passenger> newPax = new ArrayList<>();
            for (int i=0;i<paxNames.size();i++) {
                String pid = "P"+String.format("%03d",passengers.size()+newPax.size()+1);
                Passenger px = new Passenger(pid, paxNames.get(i).getText().trim(),
                        paxPassports.get(i).getText().trim(), paxDobs.get(i).getText().trim());
                bk.addPassenger(px,(String)paxSeats.get(i).getSelectedItem());
                newPax.add(px);
            }
            passengers.addAll(newPax);
            bk.setPaymentStatus("Paid");
            bk.confirmBooking();
            bookings.add(bk);
            cust.addBookingToHistory(bk);

            String payId = "PAY"+String.format("%03d",payments.size()+1);
            Payment pay = new Payment(payId,ref,bk.calculateTotalPrice(),"Credit Card","Completed",new Date().toString());
            payments.add(pay);

            JOptionPane.showMessageDialog(this,
                    "Booking confirmed!\nReference: "+ref+"\n"+bk.generateTicket(),
                    "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

            // reset
            paxNames.clear(); paxPassports.clear(); paxDobs.clear(); paxSeats.clear();
            tfHolder.setText(""); tfCard.setText(""); tfExpiry.setText(""); tfCvv.setText("");
            fm1.setRowCount(0); selectedFlight[0]=null;
            steps[0].setForeground(C_ACCENT); steps[1].setForeground(C_MUTED); steps[2].setForeground(C_MUTED);
            bodyLayout.show(body,"step1");
        });

        JLabel selectHint = new JLabel("  Click a row to select a flight, then click the button below:");
        selectHint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        selectHint.setForeground(new Color(100, 100, 100));

        JPanel s1Bot = new JPanel(new BorderLayout(8, 0));
        s1Bot.setBackground(new Color(245, 245, 245));
        s1Bot.setBorder(new EmptyBorder(8, 8, 8, 8));
        s1Next.setPreferredSize(new Dimension(220, 38));
        s1Bot.add(selectHint, BorderLayout.WEST);
        s1Bot.add(s1Next, BorderLayout.EAST);

        step1.add(h2("Find a flight"), BorderLayout.NORTH);
        step1.add(searchBar, BorderLayout.CENTER);
        JPanel s1Main = new JPanel(new BorderLayout());
        s1Main.setBackground(C_WHITE);
        s1Main.add(fsc, BorderLayout.CENTER);
        s1Main.add(s1Bot, BorderLayout.SOUTH);
        step1.add(s1Main, BorderLayout.SOUTH);

        body.add(step1,"step1"); body.add(step2,"step2"); body.add(step3,"step3");
        bodyLayout.show(body,"step1");

        p.add(top,  BorderLayout.NORTH);
        p.add(body, BorderLayout.CENTER);
        return p;
    }

    // -- My Bookings --------------------------------------------------------
    private JPanel myBookingsPanel() {
        JPanel p = padded(C_BG);
        p.setLayout(new BorderLayout(0,10));

        String[] cols = {"Reference","Flight","Route","Departure","Passengers","Total","Status","Payment"};
        DefaultTableModel model = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable table = styledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(C_BORDER));

        JButton btnRefresh = ghostBtn("Refresh");
        JButton btnCancel  = new JButton("Cancel selected");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setBackground(new Color(220, 38, 38));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setOpaque(true); btnCancel.setBorderPainted(false); btnCancel.setFocusPainted(false);
        btnCancel.setBorder(new EmptyBorder(8, 14, 8, 14));

        Runnable reload = () -> {
            model.setRowCount(0);
            for (Booking b : bookings) {
                if (b.getCustomer().getUserId().equals(currentUser.getUserId())) {
                    model.addRow(new Object[]{
                        b.getBookingReference(), b.getFlight().getFlightNumber(),
                        b.getFlight().getOrigin()+" > "+b.getFlight().getDestination(),
                        b.getFlight().getDepartureTime(), b.getPassengers().size(),
                        "$"+String.format("%.2f",b.calculateTotalPrice()),
                        b.getStatus(), b.getPaymentStatus()
                    });
                }
            }
        };
        reload.run();

        btnRefresh.addActionListener(e -> reload.run());
        btnCancel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row<0) { toast(p,"Select a booking to cancel.",C_WARNING); return; }
            String ref = (String) model.getValueAt(row,0);
            for (Booking b : bookings) {
                if (b.getBookingReference().equals(ref)) {
                    if (b.getStatus().equals("Cancelled")) { toast(p,"Already cancelled.",C_WARNING); return; }
                    int ok = JOptionPane.showConfirmDialog(this,"Cancel booking "+ref+" ","Confirm",JOptionPane.YES_NO_OPTION);
                    if (ok==JOptionPane.YES_OPTION) { b.cancelBooking(); reload.run(); toast(p,"Booking cancelled. Refund in 5-7 days.",C_SUCCESS); }
                    return;
                }
            }
        });

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        toolbar.setOpaque(false);
        toolbar.add(btnRefresh); toolbar.add(btnCancel);
        p.add(toolbar, BorderLayout.NORTH);
        p.add(scroll,  BorderLayout.CENTER);
        return p;
    }

    // =======================================================================
    //  AGENT DASHBOARD
    // =======================================================================
    private void buildAgentView() {
        JTabbedPane tabs = styledTabs();
        tabs.addTab("Manage Flights",   agentFlightsPanel());
        tabs.addTab("All Bookings",     allBookingsPanel());
        tabs.addTab("Passengers",       passengersPanel());
        tabs.addTab("Profile",          profilePanel());
        JPanel shell = dashboardShell("Agent", C_WARNING, tabs);
        cardPanel.add(shell,"dashboard");
    }

    private JPanel agentFlightsPanel() {
        JPanel p = padded(C_BG);
        p.setLayout(new BorderLayout(0,10));

        String[] cols = {"Flight #","Airline","From","To","Departure","Arrival","Econ seats","Biz seats","FC seats"};
        DefaultTableModel model = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable table = styledTable(model);

        Runnable reload = () -> {
            model.setRowCount(0);
            for (Flight f : flights)
                model.addRow(new Object[]{
                    f.getFlightNumber(),f.getAirline(),f.getOrigin(),f.getDestination(),
                    f.getDepartureTime(),f.getArrivalTime(),
                    f.getAvailableSeats().getOrDefault("Economy",0),
                    f.getAvailableSeats().getOrDefault("Business",0),
                    f.getAvailableSeats().getOrDefault("First Class",0)
                });
        };
        reload.run();

        JButton btnAdd = accentBtn("Add Flight");
        JButton btnEdit = ghostBtn("Edit");
        JButton btnDel  = new JButton("Delete"); btnDel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDel.setBackground(new Color(220, 38, 38));
        btnDel.setForeground(Color.WHITE);
        btnDel.setOpaque(true); btnDel.setBorderPainted(false); btnDel.setFocusPainted(false);
        btnDel.setBorder(new EmptyBorder(8, 14, 8, 14));

        btnAdd.addActionListener(e -> flightDialog(null, reload));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row<0) { toast(p,"Select a flight to edit.",C_WARNING); return; }
            String fn = (String)model.getValueAt(row,0);
            for (Flight f : flights) if (f.getFlightNumber().equals(fn)) { flightDialog(f, reload); return; }
        });
        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row<0) { toast(p,"Select a flight to delete.",C_WARNING); return; }
            String fn = (String)model.getValueAt(row,0);
            int ok = JOptionPane.showConfirmDialog(this,"Delete flight "+fn+" ","Confirm",JOptionPane.YES_NO_OPTION);
            if (ok==JOptionPane.YES_OPTION) { flights.removeIf(f->f.getFlightNumber().equals(fn)); reload.run(); }
        });

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        toolbar.setOpaque(false);
        toolbar.add(btnEdit); toolbar.add(btnDel); toolbar.add(btnAdd);
        p.add(toolbar, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void flightDialog(Flight existing, Runnable onSave) {
        JDialog dlg = new JDialog(this, existing==null ? "Add Flight" : "Edit Flight", true);
        dlg.setLayout(new BorderLayout(0,0));
        dlg.setSize(460, 340);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(0,2,10,8));
        form.setBorder(new EmptyBorder(20,24,10,24));
        form.setBackground(C_WHITE);

        JTextField tfFN  = styledField(14); JTextField tfAL = styledField(14);
        JTextField tfOr  = styledField(14); JTextField tfDs = styledField(14);
        JTextField tfDep = styledField(14); JTextField tfArr = styledField(14);

        if (existing!=null) {
            tfFN.setText(existing.getFlightNumber()); tfAL.setText(existing.getAirline());
            tfOr.setText(existing.getOrigin());       tfDs.setText(existing.getDestination());
            tfDep.setText(existing.getDepartureTime()); tfArr.setText(existing.getArrivalTime());
        }

        form.add(label("Flight number *")); form.add(tfFN);
        form.add(label("Airline *"));       form.add(tfAL);
        form.add(label("Origin *"));        form.add(tfOr);
        form.add(label("Destination *"));   form.add(tfDs);
        form.add(label("Departure time"));  form.add(tfDep);
        form.add(label("Arrival time"));    form.add(tfArr);

        JButton btnSave   = accentBtn("Save");
        JButton btnCancel = ghostBtn("Cancel");
        JPanel  btns      = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,10));
        btns.setBackground(C_WHITE);
        btns.add(btnCancel); btns.add(btnSave);

        btnCancel.addActionListener(e -> dlg.dispose());
        btnSave.addActionListener(e -> {
            String fn = tfFN.getText().trim();
            if (fn.isEmpty()||tfAL.getText().trim().isEmpty()||tfOr.getText().trim().isEmpty()||tfDs.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg,"Fill in required fields.","Error",JOptionPane.ERROR_MESSAGE); return;
            }
            if (existing==null) {
                if (flights.stream().anyMatch(f->f.getFlightNumber().equals(fn))) {
                    JOptionPane.showMessageDialog(dlg,"Flight number already exists.","Error",JOptionPane.ERROR_MESSAGE); return;
                }
                flights.add(new Flight(fn,tfAL.getText().trim(),tfOr.getText().trim(),tfDs.getText().trim(),tfDep.getText().trim(),tfArr.getText().trim()));
            } else {
                existing.setFlightNumber(fn); existing.setAirline(tfAL.getText().trim());
                existing.setOrigin(tfOr.getText().trim()); existing.setDestination(tfDs.getText().trim());
                existing.setDepartureTime(tfDep.getText().trim()); existing.setArrivalTime(tfArr.getText().trim());
            }
            onSave.run(); dlg.dispose();
        });

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private JPanel allBookingsPanel() {
        JPanel p = padded(C_BG);
        p.setLayout(new BorderLayout(0,10));
        String[] cols = {"Reference","Customer","Flight","Route","Pax","Total","Status","Payment"};
        DefaultTableModel model = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable table = styledTable(model);

        Runnable reload = () -> {
            model.setRowCount(0);
            for (Booking b : bookings)
                model.addRow(new Object[]{
                    b.getBookingReference(), b.getCustomer().getName(),
                    b.getFlight().getFlightNumber(),
                    b.getFlight().getOrigin()+" > "+b.getFlight().getDestination(),
                    b.getPassengers().size(),
                    "$"+String.format("%.2f",b.calculateTotalPrice()),
                    b.getStatus(), b.getPaymentStatus()
                });
        };
        reload.run();

        JButton btnRefresh = ghostBtn("Refresh");
        JButton btnCancel  = new JButton("Cancel booking"); btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setBackground(new Color(220, 38, 38));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setOpaque(true); btnCancel.setBorderPainted(false); btnCancel.setFocusPainted(false);
        btnCancel.setBorder(new EmptyBorder(8, 14, 8, 14));

        btnRefresh.addActionListener(e -> reload.run());
        btnCancel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row<0) return;
            String ref = (String)model.getValueAt(row,0);
            for (Booking b : bookings) if (b.getBookingReference().equals(ref)) { b.cancelBooking(); reload.run(); return; }
        });

        JPanel tb = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); tb.setOpaque(false);
        tb.add(btnRefresh); tb.add(btnCancel);
        p.add(tb, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JPanel passengersPanel() {
        JPanel p = padded(C_BG);
        p.setLayout(new BorderLayout(0,10));
        String[] cols = {"ID","Name","Passport","Date of Birth","Special Requests"};
        DefaultTableModel model = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        for (Passenger px : passengers) {
            StringBuilder req = new StringBuilder();
            px.getSpecialRequests().forEach((k,v)->req.append(k).append(": ").append(v).append("; "));
            model.addRow(new Object[]{px.getPassengerId(),px.getName(),px.getPassportNumber(),px.getDateOfBirth(),req.toString()});
        }
        p.add(new JScrollPane(styledTable(model)), BorderLayout.CENTER);
        return p;
    }

    // =======================================================================
    //  ADMIN DASHBOARD
    // =======================================================================
    private void buildAdminView() {
        JTabbedPane tabs = styledTabs();
        tabs.addTab("Dashboard",       adminDashboard());
        tabs.addTab("User Management", userMgmtPanel());
        tabs.addTab("Flights",         agentFlightsPanel());
        tabs.addTab("All Bookings",    allBookingsPanel());
        tabs.addTab("Profile",         profilePanel());
        JPanel shell = dashboardShell("Administrator", new Color(139,92,246), tabs);
        cardPanel.add(shell,"dashboard");
    }

    private JPanel adminDashboard() {
        JPanel p = padded(C_BG);
        p.setLayout(new BorderLayout(0,16));

        // Metric cards
        JPanel metrics = new JPanel(new GridLayout(1,4,12,0));
        metrics.setOpaque(false);

        long confirmed = bookings.stream().filter(b->b.getStatus().equals("Confirmed")).count();
        double revenue = payments.stream().filter(b->b.getStatus().equals("Completed")).mapToDouble(Payment::getAmount).sum();
        long customers = users.stream().filter(u->u instanceof Customer).count();

        metrics.add(metricCard("Total Revenue","$"+String.format("%.0f",revenue),C_SUCCESS));
        metrics.add(metricCard("Active Bookings",String.valueOf(confirmed),C_ACCENT));
        metrics.add(metricCard("Total Users",String.valueOf(users.size()),C_WARNING));
        metrics.add(metricCard("Flights",String.valueOf(flights.size()),C_PRIMARY));

        // Recent bookings
        String[] bCols = {"Reference","Customer","Route","Total","Status"};
        DefaultTableModel bModel = new DefaultTableModel(bCols,0){public boolean isCellEditable(int r,int c){return false;}};
        bookings.stream().sorted((a,b)->b.getBookingReference().compareTo(a.getBookingReference())).limit(10).forEach(b->
            bModel.addRow(new Object[]{
                b.getBookingReference(), b.getCustomer().getName(),
                b.getFlight().getOrigin()+" > "+b.getFlight().getDestination(),
                "$"+String.format("%.2f",b.calculateTotalPrice()), b.getStatus()
            })
        );
        JTable bTable = styledTable(bModel);
        JPanel bottom = new JPanel(new BorderLayout(0,8));
        bottom.setOpaque(false);
        bottom.add(h2("Recent Bookings"), BorderLayout.NORTH);
        bottom.add(new JScrollPane(bTable), BorderLayout.CENTER);

        p.add(metrics, BorderLayout.NORTH);
        p.add(bottom,  BorderLayout.CENTER);
        return p;
    }

    private JPanel userMgmtPanel() {
        JPanel p = padded(C_BG);
        p.setLayout(new BorderLayout(0,10));

        String[] cols = {"ID","Username","Name","Email","Phone","Role"};
        DefaultTableModel model = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable table = styledTable(model);

        Runnable reload = () -> {
            model.setRowCount(0);
            for (User u : users) {
                String role = u instanceof Administrator ? "Administrator" : u instanceof Agent ? "Agent" : "Customer";
                model.addRow(new Object[]{u.getUserId(),u.getUsername(),u.getName(),u.getEmail(),u.getContactInfo(),role});
            }
        };
        reload.run();

        JButton btnAdd = accentBtn("Create User");
        JButton btnDel = new JButton("Remove"); btnDel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDel.setBackground(new Color(220, 38, 38));
        btnDel.setForeground(Color.WHITE);
        btnDel.setOpaque(true); btnDel.setBorderPainted(false); btnDel.setFocusPainted(false);
        btnDel.setBorder(new EmptyBorder(8, 14, 8, 14));

        btnAdd.addActionListener(e -> userDialog(reload));
        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row<0) { toast(p,"Select a user to remove.",C_WARNING); return; }
            String uid = (String)model.getValueAt(row,0);
            if (uid.equals(currentUser.getUserId())) { toast(p,"Cannot remove yourself.",C_DANGER); return; }
            int ok = JOptionPane.showConfirmDialog(this,"Remove user "+uid+" ","Confirm",JOptionPane.YES_NO_OPTION);
            if (ok==JOptionPane.YES_OPTION) { users.removeIf(u->u.getUserId().equals(uid)); reload.run(); }
        });

        JPanel tb = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); tb.setOpaque(false);
        tb.add(btnDel); tb.add(btnAdd);
        p.add(tb, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void userDialog(Runnable onSave) {
        JDialog dlg = new JDialog(this,"Create User",true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(460,380);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(0,2,10,8));
        form.setBorder(new EmptyBorder(20,24,10,24));
        form.setBackground(C_WHITE);

        JTextField   tfUn   = styledField(14); JPasswordField tfPw = new JPasswordField(14); stylePwField(tfPw);
        JTextField   tfName = styledField(14); JTextField tfEmail = styledField(14);
        JTextField   tfPh   = styledField(14);
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"Customer","Agent","Administrator"});
        styleCombo(cbRole);
        JTextField tfExtra1 = styledField(14); JTextField tfExtra2 = styledField(14);
        JLabel lblExtra1 = label("Address"); JLabel lblExtra2 = label(" ");

        form.add(label("Username *")); form.add(tfUn);
        form.add(label("Password *")); form.add(tfPw);
        form.add(label("Full name *")); form.add(tfName);
        form.add(label("Email *"));    form.add(tfEmail);
        form.add(label("Phone"));      form.add(tfPh);
        form.add(label("Role"));       form.add(cbRole);
        form.add(lblExtra1);           form.add(tfExtra1);
        form.add(lblExtra2);           form.add(tfExtra2);

        cbRole.addActionListener(e -> {
            String role = (String)cbRole.getSelectedItem();
            if ("Customer".equals(role))       { lblExtra1.setText("Address");    lblExtra2.setText(" "); }
            else if ("Agent".equals(role))     { lblExtra1.setText("Agent ID");   lblExtra2.setText("Department"); }
            else                               { lblExtra1.setText("Admin ID");   lblExtra2.setText("Security level"); }
        });

        JButton btnSave = accentBtn("Create"); JButton btnCan = ghostBtn("Cancel");
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,10)); btns.setBackground(C_WHITE);
        btns.add(btnCan); btns.add(btnSave);
        btnCan.addActionListener(e->dlg.dispose());
        btnSave.addActionListener(e -> {
            String un = tfUn.getText().trim(), pw = new String(tfPw.getPassword());
            String nm = tfName.getText().trim(), em = tfEmail.getText().trim();
            if (un.isEmpty()||pw.isEmpty()||nm.isEmpty()||em.isEmpty()) {
                JOptionPane.showMessageDialog(dlg,"Fill in required fields.","Error",JOptionPane.ERROR_MESSAGE); return;
            }
            if (users.stream().anyMatch(u->u.getUsername().equals(un))) {
                JOptionPane.showMessageDialog(dlg,"Username already exists.","Error",JOptionPane.ERROR_MESSAGE); return;
            }
            String uid  = "U"+String.format("%03d",users.size()+1);
            String role = (String)cbRole.getSelectedItem();
            User newUser;
            if ("Customer".equals(role)) {
                String cid = "C"+String.format("%03d",(int)users.stream().filter(u->u instanceof Customer).count()+1);
                newUser = new Customer(uid,un,pw,nm,em,tfPh.getText().trim(),cid,tfExtra1.getText().trim());
            } else if ("Agent".equals(role)) {
                newUser = new Agent(uid,un,pw,nm,em,tfPh.getText().trim(),
                        tfExtra1.getText().trim(),tfExtra2.getText().trim(),0.05);
            } else {
                int sl = 1;
                try { sl = Integer.parseInt(tfExtra2.getText().trim()); } catch(Exception ex){}
                newUser = new Administrator(uid,un,pw,nm,em,tfPh.getText().trim(),tfExtra1.getText().trim(),sl);
            }
            users.add(newUser);
            onSave.run(); dlg.dispose();
        });

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // =======================================================================
    //  PROFILE PANEL (shared)
    // =======================================================================
    private JPanel profilePanel() {
        JPanel p = padded(C_BG);
        p.setLayout(new GridLayout(1,2,16,0));

        // Info card
        JPanel info = card(-1,-1);
        info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(20,20,20,20));
        info.add(h2("Personal information")); info.add(Box.createVerticalStrut(12));

        JTextField tfName = styledField(18); tfName.setText(currentUser.getName());
        JTextField tfEmail = styledField(18); tfEmail.setText(currentUser.getEmail());
        JTextField tfPhone = styledField(18); tfPhone.setText(currentUser.getContactInfo());

        info.add(fieldRow("Full name", tfName));    info.add(Box.createVerticalStrut(8));
        info.add(fieldRow("Email",     tfEmail));   info.add(Box.createVerticalStrut(8));
        info.add(fieldRow("Phone",     tfPhone));   info.add(Box.createVerticalStrut(16));
        JButton btnSave = accentBtn("Save changes");
        btnSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSave.addActionListener(e -> {
            currentUser.setName(tfName.getText().trim());
            currentUser.setEmail(tfEmail.getText().trim());
            currentUser.setContactInfo(tfPhone.getText().trim());
            JOptionPane.showMessageDialog(this,"Profile updated.","Success",JOptionPane.INFORMATION_MESSAGE);
        });
        info.add(btnSave);

        // Password card
        JPanel pw = card(-1,-1);
        pw.setLayout(new BoxLayout(pw,BoxLayout.Y_AXIS));
        pw.setBorder(new EmptyBorder(20,20,20,20));
        pw.add(h2("Change password")); pw.add(Box.createVerticalStrut(12));

        JPasswordField tfOld = new JPasswordField(18); stylePwField(tfOld);
        JPasswordField tfNew = new JPasswordField(18); stylePwField(tfNew);
        JPasswordField tfCon = new JPasswordField(18); stylePwField(tfCon);

        pw.add(fieldRow("Current password",  tfOld)); pw.add(Box.createVerticalStrut(8));
        pw.add(fieldRow("New password",      tfNew)); pw.add(Box.createVerticalStrut(8));
        pw.add(fieldRow("Confirm password",  tfCon)); pw.add(Box.createVerticalStrut(16));
        JButton btnPw = accentBtn("Change password");
        btnPw.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPw.addActionListener(e -> {
            String old = new String(tfOld.getPassword()), nw = new String(tfNew.getPassword()), cn = new String(tfCon.getPassword());
            if (!old.equals(currentUser.getPassword())) { JOptionPane.showMessageDialog(this,"Incorrect current password.","Error",JOptionPane.ERROR_MESSAGE); return; }
            if (!nw.equals(cn)) { JOptionPane.showMessageDialog(this,"Passwords do not match.","Error",JOptionPane.ERROR_MESSAGE); return; }
            try { currentUser.setPassword(nw); JOptionPane.showMessageDialog(this,"Password changed.","Success",JOptionPane.INFORMATION_MESSAGE); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); }
        });
        pw.add(btnPw);

        p.add(info); p.add(pw);
        return p;
    }

    // =======================================================================
    //  UI FACTORY HELPERS
    // =======================================================================
    private JPanel padded(Color bg) {
        JPanel p = new JPanel(); p.setBackground(bg);
        p.setBorder(new EmptyBorder(16,20,16,20));
        return p;
    }
    private JPanel card(int w, int h) {
        JPanel c = new JPanel();
        c.setBackground(C_WHITE);
        c.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                new EmptyBorder(4,4,4,4)));
        if (w>0||h>0) c.setPreferredSize(new Dimension(w>0 ? w : c.getPreferredSize().width, h>0 ? h : c.getPreferredSize().height));
        return c;
    }
    private JPanel metricCard(String title, String value, Color accent) {
        JPanel c = new JPanel(new BorderLayout(0,4));
        c.setBackground(C_WHITE);
        c.setBorder(new CompoundBorder(BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(16,16,16,16)));
        JLabel vLbl = new JLabel(value); vLbl.setFont(new Font("Segoe UI",Font.BOLD,24)); vLbl.setForeground(accent);
        JLabel tLbl = smallLabel(title);
        c.add(tLbl,  BorderLayout.NORTH);
        c.add(vLbl,  BorderLayout.CENTER);
        return c;
    }
    private JTextField styledField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(FONT_BODY); tf.setBackground(C_WHITE);
        tf.setBorder(new CompoundBorder(BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(6,8,6,8)));
        return tf;
    }
    private void stylePwField(JPasswordField pf) {
        pf.setFont(FONT_BODY); pf.setBackground(C_WHITE);
        pf.setBorder(new CompoundBorder(BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(6,8,6,8)));
    }
    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(FONT_BODY); cb.setBackground(C_WHITE);
    }
    private JButton accentBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(new Color(30, 30, 30));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(9, 18, 9, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    private JButton ghostBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setBackground(new Color(240, 240, 240));
        b.setForeground(new Color(30, 30, 30));
        b.setOpaque(true);
        b.setBorderPainted(true);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 14, 8, 14)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    private JLabel label(String text) {
        JLabel l = new JLabel(text); l.setFont(FONT_BODY); l.setForeground(C_PRIMARY); return l;
    }
    private JLabel smallLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(FONT_SMALL); l.setForeground(C_MUTED); return l;
    }
    private JLabel h2(String text) {
        JLabel l = new JLabel(text); l.setFont(FONT_H2); l.setForeground(C_PRIMARY); return l;
    }
    private JLabel pill(String text, Color bg) {
        JLabel l = new JLabel(text); l.setFont(FONT_SMALL); l.setForeground(Color.WHITE);
        l.setOpaque(true); l.setBackground(bg);
        l.setBorder(new EmptyBorder(2,8,2,8));
        return l;
    }
    private JLabel stepLabel(String num, String title, boolean active) {
        JLabel l = new JLabel(num+". "+title);
        l.setFont(FONT_BODY); l.setForeground(active ? C_ACCENT : C_MUTED);
        l.setBorder(new EmptyBorder(0,0,8,0));
        return l;
    }
    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(FONT_BODY); t.setRowHeight(30); t.setGridColor(C_BORDER);
        t.setSelectionBackground(new Color(219,234,254)); t.setSelectionForeground(C_PRIMARY);
        t.setShowGrid(true); t.setIntercellSpacing(new Dimension(1,1));
        JTableHeader header = t.getTableHeader();
        header.setFont(new Font("Segoe UI",Font.BOLD,12));
        header.setBackground(new Color(249,250,251)); header.setForeground(C_MUTED);
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDER));
        return t;
    }
    private JTabbedPane styledTabs() {
        JTabbedPane tp = new JTabbedPane();
        tp.setFont(FONT_BODY); tp.setBackground(C_BG);
        tp.setBorder(new EmptyBorder(8,12,0,12));
        return tp;
    }
    private Border roundedBorder() {
        return new CompoundBorder(BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(6,12,6,12));
    }
    private JPanel fieldRow(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(6,2));
        row.setOpaque(false); row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(labelText); lbl.setFont(new Font("Segoe UI",Font.PLAIN,11)); lbl.setForeground(C_MUTED);
        row.add(lbl, BorderLayout.NORTH);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
    private void toast(JPanel parent, String message, Color color) {
        JDialog toast = new JDialog(this, false);
        toast.setUndecorated(true);
        JLabel lbl = new JLabel("  "+message+"  "); lbl.setFont(FONT_BODY);
        lbl.setForeground(Color.WHITE); lbl.setOpaque(true); lbl.setBackground(color);
        lbl.setBorder(new EmptyBorder(8,16,8,16));
        toast.add(lbl); toast.pack();
        toast.setLocation(getX()+(getWidth()-toast.getWidth())/2, getY()+getHeight()-80);
        toast.setVisible(true);
        new javax.swing.Timer(2800, e -> toast.dispose()).start();
    }

    // =======================================================================
    //  MAIN
    // =======================================================================
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(FlightBookingGUI::new);
    }
}
