import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUIController {
    private JFrame frame;
    private QuizModule quiz;
    private JPanel panel;
    private JTextField inputField;
    private JLabel pointCounter;
    private java.util.List<String[]> leaderboard = new ArrayList<>(); // [name, score]

    public GUIController() {
        quiz = new QuizModule();
        frame = new JFrame("Environmental Awareness Quiz");
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 350);
        pointCounter = new JLabel("Points: 0");
        pointCounter.setFont(new Font("Arial", Font.BOLD, 15));
        pointCounter.setHorizontalAlignment(SwingConstants.RIGHT);
        pointCounter.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));
        showMainMenu();
    }

    private void updatePointCounter() {
        pointCounter.setText("Points: " + quiz.getScore());
    }

    private void loadQuestion() {
        panel.removeAll();
        // Add top bar with point counter (only in quiz module)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        topBar.setOpaque(false);
        topBar.add(pointCounter, BorderLayout.EAST);
        panel.add(topBar);

        QuestionInterface q = quiz.getCurrentQuestion();
        if (q == null) {
            // Responsive results panel
            JPanel outerPanel = new JPanel(new GridBagLayout());
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

            // Responsive table OUTSIDE the resultPanel container
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

            // Add Restart Quiz button
            JButton restartBtn = new JButton("Restart Quiz");
            restartBtn.setFont(new Font("Arial", Font.BOLD, 15));
            restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            restartBtn.addActionListener(e -> {
                quiz = new QuizModule();
                updatePointCounter();
                loadQuestion();
            });

            // Leaderboard section
            JPanel leaderboardPanel = new JPanel();
            leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
            leaderboardPanel.setBorder(BorderFactory.createTitledBorder("Leaderboard"));
            leaderboardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Prompt for name and add to leaderboard
            String name = JOptionPane.showInputDialog(frame, "Enter your name for the leaderboard:", "Leaderboard", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.trim().isEmpty()) name = "Anonymous";
            leaderboard.add(new String[]{name, String.valueOf(userScore)});
            // Sort leaderboard by score descending
            leaderboard.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
            StringBuilder lbHtml = new StringBuilder("<html><table width='250'>");
            lbHtml.append("<tr><th align='left'>Rank</th><th align='left'>Name</th><th align='left'>Score</th></tr>");
            int rank = 1;
            for (String[] entry : leaderboard) {
                lbHtml.append("<tr><td>").append(rank++).append("</td><td>").append(entry[0]).append("</td><td>").append(entry[1]).append("</td></tr>");
                if (rank > 10) break;
            }
            lbHtml.append("</table></html>");
            JLabel lbLabel = new JLabel(lbHtml.toString());
            lbLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
            leaderboardPanel.add(lbLabel);

            // Layout: resultPanel (summary) + table (full width) + leaderboard + restart button
            panel.removeAll();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(topBar); // <-- keep point counter at top right on results too
            panel.add(Box.createVerticalGlue());
            panel.add(resultPanel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(scrollPane);
            panel.add(Box.createVerticalStrut(10));
            panel.add(leaderboardPanel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(restartBtn);
            panel.add(Box.createVerticalGlue());

            // Add Main Menu button
            JButton mainMenuBtn = new JButton("Main Menu");
            mainMenuBtn.setFont(new Font("Arial", Font.BOLD, 15));
            mainMenuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainMenuBtn.addActionListener(e -> showMainMenu());
            panel.add(mainMenuBtn);

            frame.setContentPane(panel);
            frame.setVisible(true);
            frame.revalidate();
            frame.repaint();
            return;
        }

        // Responsive question panel
        JPanel qPanel = new JPanel();
        qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));
        qPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        qPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

        JLabel questionLabel = new JLabel("Q" + (quiz.getCurrentIndex() + 1) + ": " + q.getQuestionText());
        questionLabel.setFont(new Font("Arial", Font.BOLD, 17));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        qPanel.add(questionLabel);
        qPanel.add(Box.createVerticalStrut(10));

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

        final ButtonGroup[] group = {null};
        final java.util.List<JRadioButton> mcqButtons = new ArrayList<>();
        final java.util.Map<String, JComboBox<String>> matchBoxes = new java.util.HashMap<>();

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridBagLayout());
        answerPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
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
                    // Format matching answer for better readability
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

        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(topBar); // <-- keep point counter at top right during quiz
        panel.add(Box.createVerticalGlue());
        panel.add(qPanel);
        panel.add(Box.createVerticalGlue());
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
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

    public void showMainMenu() {
        panel.removeAll();
        // No point counter in main menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        menuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Main Menu");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(title);
        menuPanel.add(Box.createVerticalStrut(30));

        JButton quizBtn = new JButton("Quiz Module");
        quizBtn.setFont(new Font("Arial", Font.BOLD, 18));
        quizBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizBtn.addActionListener(e -> {
            quiz = new QuizModule();
            updatePointCounter();
            loadQuestion();
        });
        menuPanel.add(quizBtn);
        menuPanel.add(Box.createVerticalStrut(20));

        JButton learnBtn = new JButton("Learning Module");
        learnBtn.setFont(new Font("Arial", Font.BOLD, 18));
        learnBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        learnBtn.addActionListener(e -> {
            // Show the actual learning module window from LearningModule.java
            LearningModule.main(new String[]{});
            // Optionally, hide this main menu window while learning module is open
            frame.setVisible(false);
        });
        menuPanel.add(learnBtn);

        panel.add(Box.createVerticalGlue());
        panel.add(menuPanel);
        panel.add(Box.createVerticalGlue());
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIController::new);
    }
}
