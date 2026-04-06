import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// CUSTOM BACKGROUND PANEL
class BackgroundPanel extends JPanel {
    Image bg;
    BackgroundPanel() {
        try {
            bg = new ImageIcon(getClass().getResource("/Bg.jpg")).getImage();
        } catch (Exception e) {
            setBackground(new Color(210, 225, 250)); 
        }
        setLayout(null);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}

public class LibraryManagementSystem {
    JFrame frame;
    CardLayout card;
    JPanel mainPanel;
    JTextArea viewArea, memberArea;
    Connection conn;
    int currentMemberId = -1; 
String currentMemberName = "";
    String currentUserType = "admin"; 
    String adminPass = "admin", userPass = "user", SECRET_KEY = "1234";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManagementSystem().start());
    }

    void start() {
        conn = DBConnection.getConnection();
        frame = new JFrame("Library Management System");
        card = new CardLayout();
        mainPanel = new JPanel(card);

        mainPanel.add(loginPanel(), "login");
        mainPanel.add(registrationPanel(), "signup");
        mainPanel.add(adminPanel(), "admin");
        mainPanel.add(userPanel(), "user"); 
        mainPanel.add(addBookPanel(), "add");
        mainPanel.add(viewBookPanel(), "view");
        mainPanel.add(issueBookPanel(), "issue");
        mainPanel.add(addMemberPanel(), "addMember");
        mainPanel.add(viewMemberPanel(), "viewMembers");
        mainPanel.add(returnBookPanel(), "return");

        frame.add(mainPanel);
        frame.setSize(1400, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        card.show(mainPanel, "login");
    }

    JPanel loginPanel() {
        JPanel purpleBG = new JPanel(null);
        purpleBG.setBackground(new Color(111, 66, 193));
        JPanel whiteBox = new JPanel(null);
        whiteBox.setBackground(Color.WHITE);
        whiteBox.setBounds(100, 150, 1200, 600);

        JLabel title = new JLabel("Library Management System", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        title.setForeground(new Color(111, 66, 193));
        title.setBounds(50, 30, 600, 50);
        whiteBox.add(title);

        JLabel leftImg = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/loginpic.jpg"));
            Image img = icon.getImage().getScaledInstance(550, 400, Image.SCALE_SMOOTH);
            leftImg.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            leftImg.setText("IMAGE PLACEHOLDER");
            leftImg.setHorizontalAlignment(SwingConstants.CENTER);
            leftImg.setOpaque(true);
            leftImg.setBackground(new Color(245, 245, 245));
            leftImg.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
        leftImg.setBounds(50, 120, 550, 430);
        whiteBox.add(leftImg);

        JPanel loginContainer = new JPanel(null);
        loginContainer.setBackground(new Color(111, 66, 193));
        loginContainer.setBounds(680, 50, 470, 500);

        JLabel welcome = new JLabel("WELCOME BACK!", SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcome.setForeground(Color.WHITE);
        welcome.setBounds(0, 20, 470, 40);
        loginContainer.add(welcome);

        JRadioButton adminR = new JRadioButton("Admin", true);
        JRadioButton userR = new JRadioButton("User");
        adminR.setBounds(120, 80, 100, 30); adminR.setForeground(Color.WHITE); adminR.setBackground(new Color(111, 66, 193));
        userR.setBounds(270, 80, 100, 30); userR.setForeground(Color.WHITE); userR.setBackground(new Color(111, 66, 193));
        ButtonGroup bgGroup = new ButtonGroup(); bgGroup.add(adminR); bgGroup.add(userR);
        loginContainer.add(adminR); loginContainer.add(userR);

        JLabel uL = new JLabel("Username"); uL.setForeground(Color.WHITE); uL.setBounds(50, 140, 100, 20);
        loginContainer.add(uL);
        JTextField uT = new JTextField(); uT.setBounds(50, 165, 370, 45);
        loginContainer.add(uT);

        JLabel pL = new JLabel("Password"); pL.setForeground(Color.WHITE); pL.setBounds(50, 220, 100, 20);
        loginContainer.add(pL);
        JPasswordField pT = new JPasswordField(); pT.setBounds(50, 245, 370, 45);
       // --- SHOW/HIDE BUTTON (CONSTANT GA KANIPISTHUNDHI) ---
        JButton eye = new JButton("Show");
        eye.setBounds(345, 252, 70, 30); // Password field right side lo
        
        // EE RENDU LINES BUTTON NI STARTING NUNDE KANAPADELA CHESTHAYI:
        eye.setOpaque(true); 
        eye.setContentAreaFilled(true); 
        
        // Contrast color isthe field meedha neat ga kanipisthundhi
        eye.setBackground(new Color(220, 220, 220)); // Light Grey
        eye.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        eye.setFocusPainted(false);
        eye.setFont(new Font("Segoe UI", Font.BOLD, 11));

        // Add this below your loginB (Login Button)
JButton registerLink = new JButton("New User? Register Here");
registerLink.setBounds(50, 400, 370, 30);
registerLink.setForeground(Color.WHITE);
registerLink.setContentAreaFilled(false);
registerLink.setBorderPainted(false);
registerLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
registerLink.addActionListener(e -> card.show(mainPanel, "signup"));
loginContainer.add(registerLink);

        eye.addActionListener(e -> {
            if (pT.getEchoChar() != (char) 0) {
                pT.setEchoChar((char) 0); // Password chupisthundhi
                eye.setText("Hide");
            } else {
                pT.setEchoChar('*'); // Malli dots la marchesthundhi
                eye.setText("Show");
            }
        });
        loginContainer.add(eye);
        // --- FORGOT PASSWORD START (Line 109 daggara paste chey) ---
        JLabel forgotPass = new JLabel("Forgot Password?");
        forgotPass.setForeground(new Color(255, 193, 7)); // Yellow color
        forgotPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        forgotPass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        forgotPass.setBounds(50, 295, 150, 20); // Password box kindha exact ga untundi

        forgotPass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String secret = JOptionPane.showInputDialog(frame, "Enter Secret Key (1234):");
                if (secret != null && secret.equals(SECRET_KEY)) {
                    String newPass = JOptionPane.showInputDialog(frame, "Enter New Password:");
                    if (newPass != null && !newPass.isEmpty()) {
                        if (adminR.isSelected()) adminPass = newPass;
                        else userPass = newPass; 
                        JOptionPane.showMessageDialog(frame, "Password Reset Successful!");
                    }
                } else if (secret != null) {
                    JOptionPane.showMessageDialog(frame, "Wrong Secret Key!");
                }
            }
        });
        loginContainer.add(forgotPass);
        // --- FORGOT PASSWORD END ---
        loginContainer.add(pT);


        JButton loginB = createStyledBtn("LOGIN", 50, 340, 370, 45, null);
        loginB.setBackground(new Color(255, 193, 7)); loginB.setForeground(Color.BLACK);
       loginB.addActionListener(e -> {
    String u = uT.getText().trim();
    String p = new String(pT.getPassword());
    
    if (adminR.isSelected() && u.equals("admin") && p.equals(adminPass)) {
        currentUserType = "admin"; 
        card.show(mainPanel, "admin");
    } else if (userR.isSelected() && p.equals(userPass)) {
        try {
            // Name mismatch avvakunda lowercase lo check chesthunnam
            PreparedStatement pst = conn.prepareStatement("SELECT member_id, member_name, phone FROM members WHERE LOWER(member_name) = LOWER(?)");
            pst.setString(1, u);
            ResultSet rs = pst.executeQuery();

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(frame, "Member Name match avvaledu!");
                return;
            }

            // Privacy check: Direct ga phone number adagali, list chupinchoddu
            String inputPhone = JOptionPane.showInputDialog(frame, "Security Check: Enter your Phone Number:");
            
            if (inputPhone != null) {
                boolean found = false;
                while (rs.next()) {
                    // Database lo unna phone number tho match chesthunnam
                    if (rs.getString("phone").trim().equals(inputPhone.trim())) {
                        currentMemberId = rs.getInt("member_id");
                        currentMemberName = rs.getString("member_name");
                        currentUserType = "user";
                        card.show(mainPanel, "user");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(frame, "Phone Number correct ga ivvandi!");
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    } else {
        JOptionPane.showMessageDialog(frame, "Invalid Credentials!");
    }
});
        loginContainer.add(loginB);

        whiteBox.add(loginContainer);
        purpleBG.add(whiteBox);
        return purpleBG;
    }

    JPanel registrationPanel() {
    BackgroundPanel p = new BackgroundPanel();
    
    JLabel head = new JLabel("STUDENT REGISTRATION", SwingConstants.CENTER);
    head.setBounds(400, 80, 600, 50);
    head.setFont(new Font("Segoe UI", Font.BOLD, 30));
    p.add(head);

    // Inputs - Just Name, Phone, and Email
    JLabel nL = new JLabel("Full Name:"); nL.setBounds(450, 180, 150, 30);
    JTextField mname = new JTextField(); mname.setBounds(450, 210, 400, 45);

    JLabel phL = new JLabel("Mobile Number:"); phL.setBounds(450, 270, 150, 30);
    JTextField mphone = new JTextField(); mphone.setBounds(450, 300, 400, 45);

    JLabel gmL = new JLabel("Email ID:"); gmL.setBounds(450, 360, 150, 30);
    JTextField mgmail = new JTextField(); mgmail.setBounds(450, 390, 400, 45);

    // Register Button
    p.add(createStyledBtn("REGISTER NOW", 450, 470, 400, 50, e -> {
        try {
            if(mname.getText().isEmpty() || mphone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your Name and Mobile!");
                return;
            }

            // Database handles the ID automatically
            String sql = "INSERT INTO members(member_name, phone, email) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, mname.getText());
            pst.setString(2, mphone.getText());
            pst.setString(3, mgmail.getText());
            pst.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Registration Successful! You can now login with your name.");
            card.show(mainPanel, "login"); 
            
        } catch (Exception ex) { 
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage()); 
        }
    }));

    p.add(createStyledBtn("BACK TO LOGIN", 450, 530, 400, 40, e -> card.show(mainPanel, "login")));
    
    p.add(nL); p.add(mname); p.add(phL); p.add(mphone); p.add(gmL); p.add(mgmail);
    return p;
}

    JPanel adminPanel() {
        BackgroundPanel root = new BackgroundPanel();
        int x = 500, y = 120, w = 400, h = 45, gap = 15;
        JLabel head = new JLabel("ADMIN CONTROL", SwingConstants.CENTER);
        head.setFont(new Font("Segoe UI", Font.BOLD, 35)); head.setBounds(0, 40, 1400, 50); root.add(head);
        root.add(createStyledBtn("Manage Members", x, y, w, h, e -> card.show(mainPanel, "addMember")));
        root.add(createStyledBtn("Add / Delete Book", x, y + (h+gap), w, h, e -> card.show(mainPanel, "add")));
        root.add(createStyledBtn("View Library Books", x, y + 2*(h+gap), w, h, e -> { refreshView(); card.show(mainPanel, "view"); }));
        root.add(createStyledBtn("View Member List", x, y + 3*(h+gap), w, h, e -> { refreshMembers(); card.show(mainPanel, "viewMembers"); }));
        root.add(createStyledBtn("Issue Module", x, y + 4*(h+gap), w, h, e -> card.show(mainPanel, "issue")));
        root.add(createStyledBtn("Return & Pay Fine", x, y + 5*(h+gap), w, h, e -> card.show(mainPanel, "return")));
        root.add(createStyledBtn("Generate Report", x, y + 6*(h+gap), w, h, e -> showReport()));
        JButton logout = createStyledBtn("Logout", x, y + 7*(h+gap) + 10, w, h, e -> card.show(mainPanel, "login"));
        logout.setBackground(new Color(220, 53, 69)); root.add(logout);
        return root;
    }

    JPanel userPanel() {
    BackgroundPanel root = new BackgroundPanel();
    // Buttons align avvadaniki y-coordinates ni adjust chesam
    int x = 500, y = 200, w = 400, h = 45, gap = 20;
    
    JLabel head = new JLabel("STUDENT PORTAL", SwingConstants.CENTER);
    head.setFont(new Font("Segoe UI", Font.BOLD, 35)); 
    head.setBounds(0, 80, 1400, 60); 
    root.add(head);

    // 1. View Available Books - MATHRAME UNCHAM
    root.add(createStyledBtn("View Available Books", x, y, w, h, e -> { 
        refreshView(); 
        card.show(mainPanel, "view"); 
    }));

    // 2. My Report - IDI SECOND BUTTON GA MARCHAM (Y coordinate adjust chesi)
    root.add(createStyledBtn("My Report", x, y + (h + gap), w, h, e -> showReport()));

    // 3. Logout
    JButton logout = createStyledBtn("Logout", x, y + 2 * (h + gap), w, h, e -> card.show(mainPanel, "login"));
    logout.setBackground(new Color(220, 53, 69)); 
    root.add(logout);

    return root;
}

    // --- MODULES (FIXED COLUMN NAMES) ---
    JPanel addBookPanel() {
        BackgroundPanel p = new BackgroundPanel();
        JLabel head = new JLabel("MANAGE BOOKS", SwingConstants.CENTER); head.setBounds(500, 80, 400, 40); head.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JLabel idL = new JLabel("Book ID:"); idL.setBounds(500, 180, 100, 30);
        JTextField idT = new JTextField(); idT.setBounds(600, 180, 250, 40);
        JLabel tL = new JLabel("Title:"); tL.setBounds(500, 240, 100, 30);
        JTextField tT = new JTextField(); tT.setBounds(600, 240, 250, 40);

        p.add(createStyledBtn("ADD BOOK", 500, 330, 350, 45, e -> {
            try {
                // FIXED: Column name 'book_id'
                String sql = "INSERT INTO books(book_id, title, status, issued) VALUES (?, ?, 'Available', 0)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(idT.getText()));
                pst.setString(2, tT.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Book Added!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, ex.getMessage()); }
        }));

      p.add(createStyledBtn("DELETE BOOK", 500, 390, 350, 45, e -> {
    try {
        int bId = Integer.parseInt(idT.getText());

        // Step 1: Issue table records delete chesthunnam
        PreparedStatement pst1 = conn.prepareStatement("DELETE FROM issue WHERE book_id = ?");
        pst1.setInt(1, bId);
        pst1.executeUpdate();

        // Step 2: Ippudu main book table nundi delete chesthunnam
        PreparedStatement pst2 = conn.prepareStatement("DELETE FROM books WHERE book_id = ?");
        pst2.setInt(1, bId);
        
        if (pst2.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(frame, "Book and its history deleted!");
        } else {
            JOptionPane.showMessageDialog(frame, "Book Not Found!");
        }
    } catch (Exception ex) { 
        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage()); 
    }
}));

        p.add(createStyledBtn("BACK", 500, 450, 350, 45, e -> goBack()));
        p.add(head); p.add(idL); p.add(idT); p.add(tL); p.add(tT);
        return p;
    }

    JPanel viewBookPanel() {
        BackgroundPanel p = new BackgroundPanel();
        viewArea = new JTextArea(); viewArea.setFont(new Font("Consolas", Font.BOLD, 18)); viewArea.setEditable(false);
        JScrollPane sp = new JScrollPane(viewArea); sp.setBounds(250, 100, 900, 500);
        p.add(createStyledBtn("Back", 600, 650, 200, 50, e -> goBack()));
        p.add(sp); return p;
    }

   void refreshView() {
    // 1. Heading set chesthunnam
    viewArea.setText("         Book ID        |        Book Name        |        Status\n");
    viewArea.append("         ------------------------------------------------------------\n");
    
    try {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM books");
        while (rs.next()) {
            // 2. Database column names check chesko (book_id, title)
            int id = rs.getInt("book_id"); 
            String title = rs.getString("title");
            String status = (rs.getInt("issued") == 1 ? "ISSUED" : "AVAILABLE");

            // 3. String.format valla columns center lo align avthayi
            viewArea.append(String.format("         %-15d | %-25s | %-10s\n", id, title, status));
        }
    } catch (Exception e) { 
        e.printStackTrace(); 
    }
}

    JPanel addMemberPanel() {
        BackgroundPanel p = new BackgroundPanel();
       // Row 1: Member ID (x: 400, y: 150)
        JLabel idL = new JLabel("Member ID:"); idL.setBounds(400, 150, 150, 30);
        JTextField mid = new JTextField(); mid.setBounds(560, 150, 300, 40);

        // Row 2: Full Name (y position + 60)
        JLabel nL = new JLabel("Full Name:"); nL.setBounds(400, 210, 150, 30);
        JTextField mname = new JTextField(); mname.setBounds(560, 210, 300, 40);

        // Row 3: Phone Number (y position + 60)
        JLabel phL = new JLabel("Phone Number:"); phL.setBounds(400, 270, 150, 30);
        JTextField mphone = new JTextField(); mphone.setBounds(560, 270, 300, 40);

        // Row 4: Gmail ID (y position + 60)
        JLabel gmL = new JLabel("Gmail ID:"); gmL.setBounds(400, 330, 150, 30);
        JTextField mgmail = new JTextField(); mgmail.setBounds(560, 330, 300, 40);


        p.add(createStyledBtn("Register", 450, 410, 180, 45, e -> {
            try {
                // Members table correct column name unchali (Usually 'id' or 'member_id')
                PreparedStatement pst = conn.prepareStatement("INSERT INTO members(member_id, member_name,phone,email) VALUES (?, ? , ? , ?)");
                pst.setInt(1, Integer.parseInt(mid.getText()));
                pst.setString(2, mname.getText());
                pst.setString(3, mphone.getText());
                pst.setString(4, mgmail.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Member Registered!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, ex.getMessage()); }
        }));
        p.add(createStyledBtn("Back", 650, 410, 180, 45, e -> goBack()));
        p.add(idL); p.add(mid); p.add(nL); p.add(mname);p.add(phL); p.add(mphone); 
        p.add(gmL); p.add(mgmail);
        return p;
    }

    JPanel viewMemberPanel() {
        BackgroundPanel p = new BackgroundPanel();
        memberArea = new JTextArea(); memberArea.setFont(new Font("Consolas", Font.BOLD, 18)); memberArea.setEditable(false);
        JScrollPane sp = new JScrollPane(memberArea); sp.setBounds(250, 100, 900, 500);
        p.add(createStyledBtn("Back", 600, 650, 200, 50, e -> goBack()));
        p.add(sp); return p;
    }

   void refreshMembers() {
    // Heading set chesthunnam - starting spaces valla center ki vasthundhi
    memberArea.setText("         Member ID     |       Full Name       |      Phone      |      Gmail ID\n");
    memberArea.append("         ------------------------------------------------------------------------------\n");
    
    try {
        // SQL Query nee table name 'members' ki match avvali
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM members");
        while (rs.next()) {
            // Database column names: member_id, member_name
            int mid = rs.getInt("member_id"); 
            String mname = rs.getString("member_name");
            String ph = rs.getString("phone"); //
            String em = rs.getString("email"); //
            
            // Format chesi append chesthunnam alignment neat ga raavadaniki
            memberArea.append(String.format("         %-13d | %-21s | %-15s | %-20s\n", mid, mname, ph, em));
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Data Fetch Error: " + e.getMessage());
    }
}
    JPanel issueBookPanel() {
        BackgroundPanel p = new BackgroundPanel();
        JLabel head = new JLabel("ISSUE BOOK", SwingConstants.CENTER); head.setBounds(500, 150, 400, 40); head.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JLabel idL = new JLabel("Book ID:"); idL.setBounds(500, 230, 100, 30);
        JTextField idT = new JTextField(); idT.setBounds(600, 230, 200, 40);
        JLabel memL = new JLabel("Member ID:"); memL.setBounds(500, 290, 100, 30);
        JTextField memT = new JTextField(); memT.setBounds(600, 290, 200, 40);

        p.add(createStyledBtn("Issue", 500, 360, 200, 45, e -> {
            try {
                int bId = Integer.parseInt(idT.getText());
                PreparedStatement pst = conn.prepareStatement("UPDATE books SET issued = 1, status = 'Issued' WHERE book_id = ? AND issued = 0");
                pst.setInt(1, bId);
                if (pst.executeUpdate() > 0) {
                    PreparedStatement ipst = conn.prepareStatement("INSERT INTO issue(book_id, member_id, issue_date) VALUES (?, ?, CURDATE())");
                    ipst.setInt(1, bId); ipst.setInt(2, Integer.parseInt(memT.getText()));
                    ipst.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Book Issued!");
                } else JOptionPane.showMessageDialog(frame, "Not Available!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, ex.getMessage()); }
        }));
        p.add(createStyledBtn("Back", 720, 360, 150, 45, e -> goBack()));
        p.add(head); p.add(idL); p.add(idT); p.add(memL); p.add(memT);
        return p;
    }

    JPanel returnBookPanel() {
        BackgroundPanel p = new BackgroundPanel();
        JLabel idL = new JLabel("Book ID:"); idL.setBounds(500, 230, 100, 30);
        JTextField idT = new JTextField(); idT.setBounds(600, 230, 250, 40);
        p.add(createStyledBtn("Return & Fine 20/-", 500, 320, 350, 50, e -> {
            try {
                int bId = Integer.parseInt(idT.getText());
                conn.createStatement().executeUpdate("UPDATE issue SET return_date = CURDATE(), fine = 20 WHERE book_id = " + bId + " AND return_date IS NULL");
                conn.createStatement().executeUpdate("UPDATE books SET issued = 0, status = 'Available' WHERE book_id = " + bId);
                JOptionPane.showMessageDialog(frame, "Returned!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, ex.getMessage()); }
        }));
        p.add(createStyledBtn("Back", 500, 400, 350, 45, e -> goBack()));
        p.add(idL); p.add(idT); return p;
    }

    JButton createStyledBtn(String txt, int x, int y, int w, int h, java.awt.event.ActionListener al) {
        JButton b = new JButton(txt);
        b.setBounds(x, y, w, h); b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(new Color(111, 66, 193)); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); if(al != null) b.addActionListener(al);
        return b;
    }

   void showReport() {
    try {
        if (currentUserType.equals("admin")) {
            // SQL JOIN query: Books, Members, and Issue table nundi data testhundi
            String sql = "SELECT b.title, m.member_name, i.issue_date, i.return_date " +
                         "FROM issue i " +
                         "JOIN books b ON i.book_id = b.book_id " +
                         "JOIN members m ON i.member_id = m.member_id " +
                         "ORDER BY i.issue_date DESC";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            StringBuilder sb = new StringBuilder("--- ALL ISSUED BOOKS REPORT ---\n\n");
            sb.append(String.format("%-20s | %-15s | %-12s | %-10s\n", "Book Name", "Issued To", "Date", "Status"));
            sb.append("----------------------------------------------------------------------\n");

            boolean recordsFound = false;
            while (rs.next()) {
                recordsFound = true;
                String bName = rs.getString("title");
                String mName = rs.getString("member_name");
                String iDate = rs.getDate("issue_date").toString();
                String rDate = rs.getDate("return_date") != null ? "Returned" : "Still Out";

                sb.append(String.format("%-20s | %-15s | %-12s | %-10s\n", bName, mName, iDate, rDate));
            }

            if (!recordsFound) {
                sb.append("No books issued yet.");
            }

            // Report peddhaga untundhi kabatti JTextArea lo chupisthe scroll cheskovachu
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(frame, scrollPane, "Detailed Library Report", JOptionPane.INFORMATION_MESSAGE);

        } else {
           // --- STUDENT REPORT UPDATED LOGIC ---
            String sql = "SELECT b.title, i.issue_date, i.return_date, i.fine " +
                         "FROM issue i " +
                         "JOIN books b ON i.book_id = b.book_id " +
                         "WHERE i.member_id = ? " +
                         "ORDER BY i.issue_date DESC";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, currentMemberId);
            ResultSet rs = pst.executeQuery();

            StringBuilder sb = new StringBuilder("--- MY REPORT (" + currentMemberName + ") ---\n\n");
            // Table Header
            sb.append(String.format("%-20s | %-12s | %-12s | %-6s\n", "Book Name", "Issue Date", "Return", "Fine"));
            sb.append("--------------------------------------------------------------------------\n");

            boolean recordsFound = false;
            while (rs.next()) {
                recordsFound = true;
                String title = rs.getString("title");
                String iDate = rs.getDate("issue_date").toString();
                
                // Return date check
                java.sql.Date rDateObj = rs.getDate("return_date");
                String rStatus = (rDateObj != null) ? rDateObj.toString() : "PENDING";
                
                // Fine check
                int fineAmount = rs.getInt("fine");
                String fineStr = (fineAmount > 0) ? "Rs." + fineAmount : "0";

                sb.append(String.format("%-20s | %-12s | %-12s | %-6s\n", title, iDate, rStatus, fineStr));
            }

            if (!recordsFound) {
                sb.append("Meeru ippativaraku ey books tiskoledhu.");
            }

            // Scrollable view for better readability
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(650, 350));

            JOptionPane.showMessageDialog(frame, scrollPane, "My Library History", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
    }

}

    void goBack() { card.show(mainPanel, currentUserType); }
}