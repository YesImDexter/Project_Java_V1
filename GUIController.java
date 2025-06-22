import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class GUIController {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private QuizModule quiz;
    private JPanel quizPanel;
    private JLabel pointCounter;
    private JTextField inputField;
    private String currentUser = null;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private JPanel homeMenuPanel;
    private JPanel leaderboardPanel;
    private JLabel loginErrorLabel;
    private JLabel registerErrorLabel;
    private JLabel homeWelcomeLabel;

    public GUIController() {
        frame = new JFrame("Environmental Awareness Quiz");
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Login and Register panels
        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();

        // Home menu
        homeMenuPanel = createHomeMenu();
        // Quiz panel (will be reused)
        quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));
        quizPanel.setBackground(Color.WHITE);
        // Leaderboard panel
        leaderboardPanel = createLeaderboardPanel();

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(homeMenuPanel, "home");
        mainPanel.add(quizPanel, "quiz");
        mainPanel.add(leaderboardPanel, "leaderboard");

        frame.setContentPane(mainPanel);
        cardLayout.show(mainPanel, "login");
        frame.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 245, 230));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 40, 40, 40));

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        JTextField userField = new JTextField();
        userField.setMaximumSize(new Dimension(200, 30));
        userField.setFont(new Font("Arial", Font.PLAIN, 15));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        userField.setBorder(BorderFactory.createTitledBorder("Username"));
        panel.add(userField);
        panel.add(Box.createVerticalStrut(10));

        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(200, 30));
        passField.setFont(new Font("Arial", Font.PLAIN, 15));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passField.setBorder(BorderFactory.createTitledBorder("Password"));
        panel.add(passField);
        panel.add(Box.createVerticalStrut(10));

        loginErrorLabel = new JLabel("");
        loginErrorLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        loginErrorLabel.setForeground(Color.RED.darker());
        loginErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loginErrorLabel);
        panel.add(Box.createVerticalStrut(10));

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 15));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loginBtn);
        panel.add(Box.createVerticalStrut(10));

        JButton toRegisterBtn = new JButton("No account? Register");
        toRegisterBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        toRegisterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toRegisterBtn.setBorderPainted(false);
        toRegisterBtn.setContentAreaFilled(false);
        toRegisterBtn.setForeground(new Color(0, 102, 204));
        panel.add(toRegisterBtn);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                loginErrorLabel.setText("Please enter username and password.");
                return;
            }
            if (authenticateUser(username, password)) {
                currentUser = username;
                userField.setText("");
                passField.setText("");
                loginErrorLabel.setText("");
                updateHomeMenuWelcome();
                cardLayout.show(mainPanel, "home");
            } else {
                loginErrorLabel.setText("Invalid username or password.");
            }
        });
        toRegisterBtn.addActionListener(e -> {
            loginErrorLabel.setText("");
            cardLayout.show(mainPanel, "register");
        });
        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 245, 230));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 40, 40, 40));

        JLabel title = new JLabel("Register");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        JTextField userField = new JTextField();
        userField.setMaximumSize(new Dimension(200, 30));
        userField.setFont(new Font("Arial", Font.PLAIN, 15));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        userField.setBorder(BorderFactory.createTitledBorder("Username"));
        panel.add(userField);
        panel.add(Box.createVerticalStrut(10));

        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(200, 30));
        passField.setFont(new Font("Arial", Font.PLAIN, 15));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passField.setBorder(BorderFactory.createTitledBorder("Password"));
        panel.add(passField);
        panel.add(Box.createVerticalStrut(10));

        registerErrorLabel = new JLabel("");
        registerErrorLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        registerErrorLabel.setForeground(Color.RED.darker());
        registerErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(registerErrorLabel);
        panel.add(Box.createVerticalStrut(10));

        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 15));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(registerBtn);
        panel.add(Box.createVerticalStrut(10));

        JButton toLoginBtn = new JButton("Already have an account? Login");
        toLoginBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        toLoginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toLoginBtn.setBorderPainted(false);
        toLoginBtn.setContentAreaFilled(false);
        toLoginBtn.setForeground(new Color(0, 102, 204));
        panel.add(toLoginBtn);

        registerBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                registerErrorLabel.setText("Please enter username and password.");
                return;
            }
            if (!username.matches("[A-Za-z0-9_]{3,20}")) {
                registerErrorLabel.setText("Username must be 3-20 letters, numbers, or _");
                return;
            }
            if (password.length() < 3) {
                registerErrorLabel.setText("Password must be at least 3 characters.");
                return;
            }
            if (userExists(username)) {
                registerErrorLabel.setText("Username already exists.");
                return;
            }
            if (username.contains(":") || password.contains(":")) {
                registerErrorLabel.setText("Invalid character ':' in username or password.");
                return;
            }
            if (registerUser(username, password)) {
                currentUser = username;
                userField.setText("");
                passField.setText("");
                registerErrorLabel.setText("");
                updateHomeMenuWelcome();
                cardLayout.show(mainPanel, "home");
            } else {
                registerErrorLabel.setText("Registration failed. Try again.");
            }
        });
        toLoginBtn.addActionListener(e -> {
            registerErrorLabel.setText("");
            cardLayout.show(mainPanel, "login");
        });
        return panel;
    }

    private Map<String, String> loadUserDatabase() {
        Map<String, String> users = new HashMap<>();
        File dbFile = new File("user_database.txt");
        if (!dbFile.exists()) return users;
        try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || !line.contains(":")) continue;
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    users.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            // ignore, return what we have
        }
        return users;
    }

    private boolean authenticateUser(String username, String password) {
        Map<String, String> users = loadUserDatabase();
        System.out.println("Loaded users: " + users);
        String user = username.trim();
        String pass = password.trim();
        if (!users.containsKey(user)) {
            System.out.println("Username not found: " + user);
            return false;
        }
        if (!users.get(user).equals(pass)) {
            System.out.println("Password mismatch for user: " + user + ". Expected: [" + users.get(user) + "] Got: [" + pass + "]");
            return false;
        }
        return true;
    }

    private boolean userExists(String username) {
        Map<String, String> users = loadUserDatabase();
        return users.containsKey(username);
    }

    private boolean registerUser(String username, String password) {
        File dbFile = new File("user_database.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dbFile, true))) {
            bw.write(username + ":" + password);
            bw.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void updateHomeMenuWelcome() {
        if (homeWelcomeLabel != null) {
            homeWelcomeLabel.setText("Welcome, " + (currentUser != null ? currentUser : "Guest"));
        }
    }

    private JPanel createHomeMenu() {
        JPanel homeMenu = new JPanel(new BorderLayout());
        homeMenu.setBackground(new Color(220, 245, 230));
        homeMenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Logo/Icon
        JLabel logoLabel;
        try {
            ImageIcon logo = new ImageIcon("Resources/logo.png");
            Image scaled = logo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            logoLabel = new JLabel("🌱");
            logoLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createVerticalStrut(10));

        homeWelcomeLabel = new JLabel("Welcome, Guest");
        homeWelcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        homeWelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homeWelcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(homeWelcomeLabel);

        JLabel subtitle = new JLabel("<html><div style='text-align:center;'>Test your knowledge and see how you rank!</div></html>");
        subtitle.setFont(new Font("Arial", Font.ITALIC, 13));
        subtitle.setForeground(new Color(34, 139, 34));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitle);

        centerPanel.add(Box.createVerticalStrut(30));

        // --- Load custom icons for buttons ---
        ImageIcon quizIcon = null, learningIcon = null, leaderboardIcon = null;
        try {
            quizIcon = new ImageIcon(new ImageIcon("Icons/result-a-plus-icon.png").getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH));
        } catch (Exception e) { quizIcon = null; }
        try {
            learningIcon = new ImageIcon(new ImageIcon("Icons/book-icon.png").getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH));
        } catch (Exception e) { learningIcon = null; }
        try {
            leaderboardIcon = new ImageIcon(new ImageIcon("Icons/leaderboard-icon.png").getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH));
        } catch (Exception e) { leaderboardIcon = null; }

        // --- Set button width and padding ---
        int btnWidth = 240;
        int btnHeight = 44;
        Insets btnPadding = new Insets(8, 18, 8, 18);

        JButton quizBtn = new JButton("Start Quiz");
        if (quizIcon != null) quizBtn.setIcon(quizIcon);
        quizBtn.setHorizontalAlignment(SwingConstants.LEFT);
        quizBtn.setIconTextGap(12);
        quizBtn.setFont(new Font("Arial", Font.BOLD, 16));
        quizBtn.setBackground(new Color(200, 240, 215));
        quizBtn.setFocusPainted(false);
        quizBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        quizBtn.setMinimumSize(new Dimension(btnWidth, btnHeight));
        quizBtn.setPreferredSize(new Dimension(btnWidth, btnHeight));
        quizBtn.setMargin(btnPadding);

        JButton learningBtn = new JButton("Learning Module");
        if (learningIcon != null) learningBtn.setIcon(learningIcon);
        learningBtn.setHorizontalAlignment(SwingConstants.LEFT);
        learningBtn.setIconTextGap(12);
        learningBtn.setFont(new Font("Arial", Font.BOLD, 16));
        learningBtn.setBackground(new Color(200, 240, 215));
        learningBtn.setFocusPainted(false);
        learningBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        learningBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        learningBtn.setMinimumSize(new Dimension(btnWidth, btnHeight));
        learningBtn.setPreferredSize(new Dimension(btnWidth, btnHeight));
        learningBtn.setMargin(btnPadding);

        JButton leaderboardBtn = new JButton("View Leaderboard");
        if (leaderboardIcon != null) leaderboardBtn.setIcon(leaderboardIcon);
        leaderboardBtn.setHorizontalAlignment(SwingConstants.LEFT);
        leaderboardBtn.setIconTextGap(12);
        leaderboardBtn.setFont(new Font("Arial", Font.BOLD, 16));
        leaderboardBtn.setBackground(new Color(255, 240, 200));
        leaderboardBtn.setFocusPainted(false);
        leaderboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        leaderboardBtn.setMinimumSize(new Dimension(btnWidth, btnHeight));
        leaderboardBtn.setPreferredSize(new Dimension(btnWidth, btnHeight));
        leaderboardBtn.setMargin(btnPadding);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 13));
        logoutBtn.setBackground(new Color(255, 220, 220));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        logoutBtn.setMinimumSize(new Dimension(btnWidth, btnHeight));
        logoutBtn.setPreferredSize(new Dimension(btnWidth, btnHeight));
        logoutBtn.setMargin(btnPadding);

        quizBtn.addActionListener(e -> {
            quiz = new QuizModule();
            showQuizPanel();
        });
        learningBtn.addActionListener(e -> {
            LearningModule.main(new String[]{});
            frame.setVisible(false);
        });
        leaderboardBtn.addActionListener(e -> {
            updateLeaderboardPanel();
            cardLayout.show(mainPanel, "leaderboard");
        });
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "login");
        });

        centerPanel.add(quizBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(learningBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(leaderboardBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(logoutBtn);

        centerPanel.add(Box.createVerticalStrut(30));
        JLabel credits = new JLabel("© 2024 GreenEarth Initiative");
        credits.setFont(new Font("Arial", Font.PLAIN, 11));
        credits.setForeground(new Color(100, 120, 100));
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(credits);

        homeMenu.add(centerPanel, BorderLayout.CENTER);
        return homeMenu;
    }

    private void showQuizPanel() {
        if (currentUser == null) {
            cardLayout.show(mainPanel, "login");
            return;
        }
        quizPanel.removeAll();
        quizPanel.setBackground(Color.WHITE);

        // Top bar with point counter
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        topBar.setOpaque(false);
        pointCounter = new JLabel("Points: 0");
        pointCounter.setFont(new Font("Arial", Font.BOLD, 15));
        pointCounter.setHorizontalAlignment(SwingConstants.RIGHT);
        pointCounter.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));
        topBar.add(pointCounter, BorderLayout.EAST);
        quizPanel.add(topBar);

        loadQuestion();

        frame.setContentPane(mainPanel);
        cardLayout.show(mainPanel, "quiz");
        frame.revalidate();
        frame.repaint();
    }

    private void updatePointCounter() {
        if (pointCounter != null && quiz != null)
            pointCounter.setText("Points: " + quiz.getScore());
    }

    private void loadQuestion() {
        quizPanel.removeAll();
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        topBar.setOpaque(false);
        if (pointCounter != null)
            topBar.add(pointCounter, BorderLayout.EAST);
        quizPanel.add(topBar);

        QuestionInterface q = quiz.getCurrentQuestion();
        if (q == null) {
            showQuizResult();
            return;
        }

        JPanel qPanel = new JPanel();
        qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));
        qPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        qPanel.setMaximumSize(new Dimension(340, 400));
        qPanel.setPreferredSize(new Dimension(340, 400));
        qPanel.setBackground(Color.WHITE);

        // --- Use JTextArea for question text for wrapping ---
        JTextArea questionArea = new JTextArea("Q" + (quiz.getCurrentIndex() + 1) + ": " + q.getQuestionText());
        questionArea.setFont(new Font("Arial", Font.BOLD, 16));
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);
        questionArea.setOpaque(false);
        questionArea.setFocusable(false);
        questionArea.setBorder(BorderFactory.createEmptyBorder());
        questionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionArea.setMaximumSize(new Dimension(320, 60));
        questionArea.setPreferredSize(new Dimension(320, 60));
        qPanel.add(questionArea);
        qPanel.add(Box.createVerticalStrut(10));

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

        final ButtonGroup[] group = {null};
        final java.util.List<JRadioButton> mcqButtons = new ArrayList<>();
        final java.util.Map<String, JComboBox<String>> matchBoxes = new java.util.HashMap<>();

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridBagLayout());
        answerPanel.setMaximumSize(new Dimension(320, 180));
        answerPanel.setPreferredSize(new Dimension(320, 180));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        if (q instanceof MCQQuestion) {
            MCQQuestion mcq = (MCQQuestion) q;
            group[0] = new ButtonGroup();
            int optIdx = 0;
            for (String option : mcq.getOptions()) {
                gbc.gridx = 0;
                JLabel optLabel = new JLabel("Option " + (char)('A' + optIdx) + ":");
                optLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                answerPanel.add(optLabel, gbc);
                gbc.gridx = 1;
                JRadioButton button = new JRadioButton(option);
                button.setFont(new Font("Arial", Font.PLAIN, 14));
                group[0].add(button);
                mcqButtons.add(button);
                answerPanel.add(button, gbc);
                button.addActionListener(e -> inputField.setText(button.getText()));
                gbc.gridy++;
                optIdx++;
            }
        } else if (q instanceof TrueFalseQuestion) {
            gbc.gridx = 0;
            JLabel tfLabel = new JLabel("Select:");
            tfLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            answerPanel.add(tfLabel, gbc);
            gbc.gridx = 1;
            JPanel tfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            JButton trueBtn = new JButton("True");
            JButton falseBtn = new JButton("False");
            trueBtn.setFont(new Font("Arial", Font.PLAIN, 14));
            falseBtn.setFont(new Font("Arial", Font.PLAIN, 14));
            tfPanel.add(trueBtn);
            tfPanel.add(falseBtn);
            answerPanel.add(tfPanel, gbc);
            trueBtn.addActionListener(e -> inputField.setText("true"));
            falseBtn.addActionListener(e -> inputField.setText("false"));
            gbc.gridy++;
        } else if (q instanceof MatchingQuestion) {
            MatchingQuestion mq = (MatchingQuestion) q;
            java.util.List<String> values = new ArrayList<>(mq.getValues());
            for (String key : mq.getKeys()) {
                gbc.gridx = 0;
                JLabel keyLabel = new JLabel(key + ":");
                keyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                answerPanel.add(keyLabel, gbc);
                gbc.gridx = 1;
                JComboBox<String> combo = new JComboBox<>(values.toArray(new String[0]));
                combo.setFont(new Font("Arial", Font.PLAIN, 14));
                matchBoxes.put(key, combo);
                answerPanel.add(combo, gbc);
                gbc.gridy++;
            }
        } else {
            gbc.gridx = 0;
            JLabel ansLabel = new JLabel(q instanceof FillInBlankQuestion ? "Fill in the blank:" : "Your Answer:");
            ansLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            answerPanel.add(ansLabel, gbc);
            gbc.gridx = 1;
            inputField.setText("");
            inputField.setFont(new Font("Arial", Font.PLAIN, 14));
            inputField.setPreferredSize(new Dimension(150, 26));
            inputField.setMaximumSize(new Dimension(Short.MAX_VALUE, 26));
            answerPanel.add(inputField, gbc);
            gbc.gridy++;
        }
        answerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        qPanel.add(answerPanel);

        qPanel.add(Box.createVerticalStrut(10));
        JButton submit = new JButton("Submit Answer");
        submit.setFont(new Font("Arial", Font.BOLD, 14));
        submit.setMaximumSize(new Dimension(150, 30));
        qPanel.add(submit);
        qPanel.add(Box.createVerticalStrut(8));

        JLabel feedback = new JLabel("");
        feedback.setFont(new Font("Arial", Font.ITALIC, 14));
        feedback.setAlignmentX(Component.CENTER_ALIGNMENT);
        qPanel.add(feedback);

        JButton nextBtn = new JButton("Next");
        nextBtn.setFont(new Font("Arial", Font.BOLD, 14));
        nextBtn.setVisible(false);
        nextBtn.setMaximumSize(new Dimension(120, 28));
        qPanel.add(nextBtn);

        submit.addActionListener(e -> {
            String answer;
            if (q instanceof MatchingQuestion) {
                StringBuilder sb = new StringBuilder();
                for (String key : matchBoxes.keySet()) {
                    String value = (String) matchBoxes.get(key).getSelectedItem();
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(key).append(": ").append(value);
                }
                answer = sb.toString();
            } else {
                answer = inputField.getText();
            }
            QuestionInterface currentQuestion = quiz.getCurrentQuestion();
            quiz.submitAnswer(answer);
            updatePointCounter();
            boolean isCorrect = currentQuestion.isCorrectAnswer(answer);
            String feedbackText;
            if (isCorrect) {
                feedbackText = "<html><span style='color:green;font-weight:bold;'>Correct!</span></html>";
            } else {
                String correct = currentQuestion.getCorrectAnswer();
                if (q instanceof MatchingQuestion) {
                    StringBuilder formatted = new StringBuilder("<html><span style='color:red;font-weight:bold;'>Incorrect!</span> <br>Correct answer:<br><ul>");
                    String[] pairs = correct.replaceAll("[{}]", "").split(",");
                    for (String pair : pairs) {
                        String[] kv = pair.split("=");
                        if (kv.length == 2) {
                            formatted.append("<li>").append(kv[0].trim()).append(" : <b>").append(kv[1].trim()).append("</b></li>");
                        }
                    }
                    formatted.append("</ul></html>");
                    feedbackText = formatted.toString();
                } else {
                    feedbackText = "<html><span style='color:red;font-weight:bold;'>Incorrect!</span> Correct answer: <b>" + correct + "</b></html>";
                }
            }
            feedback.setText(feedbackText);
            feedback.setForeground(isCorrect ? Color.GREEN.darker() : Color.RED);
            submit.setEnabled(false);
            inputField.setEnabled(false);
            nextBtn.setVisible(true);
            if (group[0] != null) {
                for (JRadioButton btn : mcqButtons) btn.setEnabled(false);
            }
            if (q instanceof MatchingQuestion) {
                for (JComboBox<String> box : matchBoxes.values()) box.setEnabled(false);
            }
        });

        nextBtn.addActionListener(e -> loadQuestion());

        // --- Wrap qPanel in a scroll pane for overflow ---
        JScrollPane scrollPane = new JScrollPane(qPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(340, 420));
        scrollPane.getViewport().setBackground(Color.WHITE);

        quizPanel.add(Box.createVerticalGlue());
        quizPanel.add(scrollPane);
        quizPanel.add(Box.createVerticalGlue());
        quizPanel.revalidate();
        quizPanel.repaint();
    }

    private void showQuizResult() {
        quizPanel.removeAll();

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        topBar.setOpaque(false);
        if (pointCounter != null)
            topBar.add(pointCounter, BorderLayout.EAST);
        quizPanel.add(topBar);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        resultPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int totalScore = 0;
        for (int i = 0; i < quiz.getTotalQuestions(); i++) {
            totalScore += quiz.getQuestionAt(i).getPoints();
        }
        int userScore = quiz.getScore();
        double percent = (quiz.getTotalQuestions() > 0) ? (userScore * 100.0 / totalScore) : 0;
        String percentStr = String.format("%.0f", percent);
        String message;
        if (percent >= 80) message = "Outstanding!";
        else if (percent >= 60) message = "That’s good!";
        else if (percent >= 40) message = "Good try!";
        else if (percent >= 20) message = "You can do better!";
        else message = "Don’t give up!";

        JLabel resultTitle = new JLabel("Quiz Complete!");
        resultTitle.setFont(new Font("Arial", Font.BOLD, 22));
        resultTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(resultTitle);
        resultPanel.add(Box.createVerticalStrut(10));

        JLabel scoreLabel = new JLabel("Your score: " + userScore + " / " + totalScore + " (" + percentStr + "%)");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(scoreLabel);
        resultPanel.add(Box.createVerticalStrut(5));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(new Color(0, 102, 204));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(messageLabel);
        resultPanel.add(Box.createVerticalStrut(10));

        // Table of answers
        String[] headers = {"Q#", "Your Answer", "Correct Answer", "Points", "Earned"};
        Object[][] data = new Object[quiz.getTotalQuestions()][5];
        for (int i = 0; i < quiz.getTotalQuestions(); i++) {
            QuestionInterface question = quiz.getQuestionAt(i);
            String userAnswer = quiz.getUserAnswer(i);
            String correctAns = question.getCorrectAnswer();
            int pts = question.getPoints();
            int earned = quiz.getPointsEarned(i);
            if (question instanceof MatchingQuestion) {
                userAnswer = formatMatchingNative(userAnswer, "→");
                correctAns = formatMatchingNative(correctAns, "→");
            }
            data[i][0] = "Q" + (i + 1);
            data[i][1] = userAnswer;
            data[i][2] = correctAns;
            data[i][3] = pts;
            data[i][4] = earned;
        }
        JTable table = new JTable(data, headers) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String val = getValueAt(row, column).toString();
                // Only increase height for matching questions (columns 1 and 2)
                if ((column == 1 || column == 2) && val.contains("→")) {
                    int lines = val.split("\\n").length;
                    setRowHeight(row, Math.max(22, lines * 22));
                } else {
                    setRowHeight(row, 22);
                }
                return c;
            }
        };
        table.setFont(new Font("Monospaced", Font.PLAIN, 13));
        table.setRowHeight(22);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value != null && value.toString().contains("\n")) {
                    setText("<html>" + value.toString().replace("\n", "<br>") + "</html>");
                } else {
                    setText(value == null ? "" : value.toString());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(null);
        scrollPane.setMinimumSize(new Dimension(300, 100));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Save score to DB and show leaderboard
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBorder(BorderFactory.createTitledBorder("Leaderboard"));
        leaderboardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Use current user for leaderboard name, skip prompt
        String name = currentUser != null ? currentUser : System.getProperty("user.name");
        // If you have a login system, use the logged-in username variable instead
        // String name = currentUser != null ? currentUser : "Anonymous";
        try {
            DataStorage db = new DataStorage();
            db.saveScore(name, userScore);
            Map<String, List<Integer>> allScores = db.getAllScores();
            // Build a list of name/score pairs for all scores (not just max per user)
            List<Map.Entry<String, Integer>> topList = new ArrayList<>();
            for (Map.Entry<String, List<Integer>> entry : allScores.entrySet()) {
                for (Integer score : entry.getValue()) {
                    topList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), score));
                }
            }
            topList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
            StringBuilder lbHtml = new StringBuilder("<html><table width='250'>");
            lbHtml.append("<tr><th align='left'>Rank</th><th align='left'>Name</th><th align='left'>Score</th></tr>");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : topList) {
                lbHtml.append("<tr><td>").append(rank++).append("</td><td>").append(entry.getKey()).append("</td><td>").append(entry.getValue()).append("</td></tr>");
                if (rank > 10) break;
            }
            lbHtml.append("</table></html>");
            JLabel lbLabel = new JLabel(lbHtml.toString());
            lbLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
            leaderboardPanel.add(lbLabel);
        } catch (Exception ex) {
            JLabel lbLabel = new JLabel("Leaderboard unavailable.");
            leaderboardPanel.add(lbLabel);
        }

        // Restart and menu buttons
        JButton restartBtn = new JButton("Restart Quiz");
        restartBtn.setFont(new Font("Arial", Font.BOLD, 15));
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartBtn.addActionListener(e -> {
            quiz = new QuizModule();
            showQuizPanel();
        });

        JButton mainMenuBtn = new JButton("Main Menu");
        mainMenuBtn.setFont(new Font("Arial", Font.BOLD, 15));
        mainMenuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuBtn.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));
        quizPanel.add(topBar);
        quizPanel.add(Box.createVerticalGlue());
        quizPanel.add(resultPanel);
        quizPanel.add(Box.createVerticalStrut(10));
        quizPanel.add(scrollPane);
        quizPanel.add(Box.createVerticalStrut(10));
        quizPanel.add(leaderboardPanel);
        quizPanel.add(Box.createVerticalStrut(10));
        quizPanel.add(restartBtn);
        quizPanel.add(mainMenuBtn);
        quizPanel.add(Box.createVerticalGlue());

        quizPanel.revalidate();
        quizPanel.repaint();
    }

    private JPanel createLeaderboardPanel() {
        JPanel leaderboardPanel = new JPanel(new BorderLayout());
        leaderboardPanel.setBackground(new Color(255, 255, 245));

        JLabel title = new JLabel("🏆 Leaderboard");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        leaderboardPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Placeholder, will be updated before showing
        JLabel tableLabel = new JLabel();
        tableLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(tableLabel);

        leaderboardPanel.add(centerPanel, BorderLayout.CENTER);

        JButton backBtn = new JButton("⟵ Back to Home");
        backBtn.setFont(new Font("Arial", Font.BOLD, 15));
        backBtn.setBackground(new Color(220, 245, 230));
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(255, 255, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(18, 10, 18, 10));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(backBtn);

        leaderboardPanel.add(bottomPanel, BorderLayout.SOUTH);

        leaderboardPanel.putClientProperty("tableLabel", tableLabel);
        return leaderboardPanel;
    }

    private void updateLeaderboardPanel() {
        // Use stored leaderboardPanel reference
        JPanel lbPanel = leaderboardPanel;
        JLabel tableLabel = (JLabel) lbPanel.getClientProperty("tableLabel");
        try {
            DataStorage db = new DataStorage();
            Map<String, List<Integer>> allScores = db.getAllScores();
            // Build a list of name/score pairs for all scores (not just max per user)
            List<Map.Entry<String, Integer>> topList = new ArrayList<>();
            for (Map.Entry<String, List<Integer>> entry : allScores.entrySet()) {
                for (Integer score : entry.getValue()) {
                    topList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), score));
                }
            }
            topList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
            StringBuilder lbHtml = new StringBuilder("<html><table width='250'>");
            lbHtml.append("<tr><th align='left'>Rank</th><th align='left'>Name</th><th align='left'>Score</th></tr>");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : topList) {
                lbHtml.append("<tr><td>").append(rank++).append("</td><td>").append(entry.getKey()).append("</td><td>").append(entry.getValue()).append("</td></tr>");
                if (rank > 10) break;
            }
            lbHtml.append("</table></html>");
            tableLabel.setText(lbHtml.toString());
            tableLabel.setFont(new Font("Monospaced", Font.PLAIN, 15));
        } catch (Exception ex) {
            tableLabel.setText("Leaderboard unavailable.");
        }
    }

    // Helper for formatting matching answers with native line breaks
    private String formatMatchingNative(String answer, String arrow) {
        if (answer == null || answer.isEmpty()) return "-";
        answer = answer.replaceAll("[{}]", "");
        String[] pairs = answer.split(",");
        StringBuilder sb = new StringBuilder();
        for (String pair : pairs) {
            String[] kv = pair.split(":|=");
            if (kv.length == 2) {
                sb.append(kv[0].trim()).append(" ").append(arrow).append(" ").append(kv[1].trim()).append("\n");
            }
        }
        return sb.length() > 0 ? sb.toString().trim() : answer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIController::new);
    }
}
