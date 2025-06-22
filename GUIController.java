import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
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

    public GUIController() {
        frame = new JFrame("Environmental Awareness Quiz");
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Home menu
        JPanel homeMenu = createHomeMenu();

        // Quiz panel (will be reused)
        quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));
        quizPanel.setBackground(Color.WHITE);

        // Leaderboard panel
        JPanel leaderboardPanel = createLeaderboardPanel();

        mainPanel.add(homeMenu, "home");
        mainPanel.add(quizPanel, "quiz");
        mainPanel.add(leaderboardPanel, "leaderboard");

        frame.setContentPane(mainPanel);
        cardLayout.show(mainPanel, "home");
        frame.setVisible(true);
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

        JLabel welcomeLabel = new JLabel("Welcome to Environmental Quiz");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(welcomeLabel);

        JLabel subtitle = new JLabel("<html><div style='text-align:center;'>Test your knowledge and see how you rank!</div></html>");
        subtitle.setFont(new Font("Arial", Font.ITALIC, 13));
        subtitle.setForeground(new Color(34, 139, 34));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitle);

        centerPanel.add(Box.createVerticalStrut(30));

        JButton quizBtn = new JButton("📝  Start Quiz");
        JButton learningBtn = new JButton("📖  Open Learning Module"); // <-- Added
        JButton leaderboardBtn = new JButton("🏆  View Leaderboard");

        quizBtn.setFont(new Font("Arial", Font.BOLD, 16));
        learningBtn.setFont(new Font("Arial", Font.BOLD, 16));
        leaderboardBtn.setFont(new Font("Arial", Font.BOLD, 16));
        quizBtn.setBackground(new Color(200, 240, 215));
        learningBtn.setBackground(new Color(200, 240, 215));
        leaderboardBtn.setBackground(new Color(255, 240, 200));
        quizBtn.setFocusPainted(false);
        learningBtn.setFocusPainted(false);
        leaderboardBtn.setFocusPainted(false);
        quizBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        learningBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        centerPanel.add(quizBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(learningBtn); // <-- Added
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(leaderboardBtn);

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

        String name = JOptionPane.showInputDialog(frame, "Enter your name for the leaderboard:", "Leaderboard", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) name = "Anonymous";
        try {
            DataStorage db = new DataStorage();
            db.saveScore(name, userScore);
            Map<String, List<Integer>> allScores = db.getAllScores();
            List<Map.Entry<String, Integer>> topList = allScores.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().isEmpty() ? 0 : Collections.max(e.getValue())))
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(10)
                .collect(Collectors.toList());
            StringBuilder lbHtml = new StringBuilder("<html><table width='250'>");
            lbHtml.append("<tr><th align='left'>Rank</th><th align='left'>Name</th><th align='left'>Score</th></tr>");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : topList) {
                lbHtml.append("<tr><td>").append(rank++).append("</td><td>").append(entry.getKey()).append("</td><td>").append(entry.getValue()).append("</td></tr>");
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
        JPanel leaderboardPanel = (JPanel) mainPanel.getComponent(2); // index of leaderboard panel
        JLabel tableLabel = (JLabel) leaderboardPanel.getClientProperty("tableLabel");
        try {
            DataStorage db = new DataStorage();
            Map<String, List<Integer>> allScores = db.getAllScores();
            List<Map.Entry<String, Integer>> topList = allScores.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().isEmpty() ? 0 : Collections.max(e.getValue())))
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(10)
                .collect(Collectors.toList());
            StringBuilder lbHtml = new StringBuilder("<html><table width='250'>");
            lbHtml.append("<tr><th align='left'>Rank</th><th align='left'>Name</th><th align='left'>Score</th></tr>");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : topList) {
                lbHtml.append("<tr><td>").append(rank++).append("</td><td>").append(entry.getKey()).append("</td><td>").append(entry.getValue()).append("</td></tr>");
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
