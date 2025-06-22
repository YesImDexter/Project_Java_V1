// File: QuizModule.java
// Creator: DEXTER SKUDD ANAK JOHN RIZAL (101630)
// Tester: ABDUL HAFIY KAMALUDDIN BIN ABDUL RANI (101476)

import java.util.*;

/**
 * Handles quiz logic, question management, and scoring.
 */
public class QuizModule {
    private List<QuestionInterface> questions = new ArrayList<>();
    private List<Boolean> correctness = new ArrayList<>();
    private List<String> userAnswers = new ArrayList<>();
    private int score = 0;
    private int currentIndex = 0;

    public QuizModule() {
        loadQuestions();
    }

    /**
     * Loads quiz questions into the module.
     */
    private void loadQuestions() {
        questions.add(new MCQQuestion(
            "Which gas contributes the most to climate change?",
            Arrays.asList("Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"),
            1, 10
        ));

        questions.add(new TrueFalseQuestion(
            "Recycling plastic helps reduce pollution.", true, 5
        ));

        questions.add(new FillInBlankQuestion(
            "One source of renewable energy is ____. ",
            Arrays.asList("solar", "wind", "hydro"), 10
        ));

        Map<String, String> match = new HashMap<>();
        match.put("Plastic", "Recycle Bin");
        match.put("Battery", "Hazardous Waste Bin");
        match.put("Food", "Compost");

        questions.add(new MatchingQuestion(
            "Match the items to the correct bin (e.g., A:X,B:Y,...)", match, 15
        ));

        questions.add(new ShortAnswerQuestion(
            "Why are trees important to the environment?",
            Arrays.asList("oxygen", "carbon", "shade", "habitat"), 10
        ));

        // Add more to meet 20+ as needed
    }

    /**
     * Returns the current question.
     */
    public QuestionInterface getCurrentQuestion() {
        if (currentIndex < questions.size()) {
            return questions.get(currentIndex);
        }
        return null;
    }

    /**
     * Submits an answer for the current question and updates score.
     */
    public void submitAnswer(String answer) {
        QuestionInterface q = getCurrentQuestion();
        if (q != null) {
            boolean isCorrect = q.isCorrectAnswer(answer);
            correctness.add(isCorrect);
            userAnswers.add(answer);
            int pointsEarned = 0;
            if (q instanceof MatchingQuestion) {
                pointsEarned = ((MatchingQuestion) q).getPartialScore(answer);
            } else if (isCorrect) {
                pointsEarned = q.getPoints();
            }
            score += pointsEarned;
            currentIndex++;
        }
    }

    /**
     * Checks if there are more questions.
     */
    public boolean hasNext() {
        return currentIndex < questions.size();
    }

    /**
     * Returns the total score earned.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the total number of questions.
     */
    public int getTotalQuestions() {
        return questions.size();
    }

    /**
     * Returns the index of the current question.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    // --- Additional helper methods ---

    /**
     * Returns the question at a specific index.
     */
    public QuestionInterface getQuestionAt(int index) {
        if (index >= 0 && index < questions.size()) {
            return questions.get(index);
        }
        return null;
    }

    /**
     * Returns whether the answer at the given index was correct.
     */
    public boolean wasCorrect(int index) {
        if (index >= 0 && index < correctness.size()) {
            return correctness.get(index);
        }
        return false;
    }

    /**
     * Returns the user's answer at the given index.
     */
    public String getUserAnswer(int index) {
        if (index >= 0 && index < userAnswers.size()) {
            return userAnswers.get(index);
        }
        return "-";
    }

    /**
     * Returns the points earned for a specific question (for display).
     */
    public int getPointsEarned(int index) {
        if (index < 0 || index >= userAnswers.size()) return 0;
        QuestionInterface q = getQuestionAt(index);
        String answer = getUserAnswer(index);
        if (q instanceof MatchingQuestion) {
            return ((MatchingQuestion) q).getPartialScore(answer);
        } else if (q != null && q.isCorrectAnswer(answer)) {
            return q.getPoints();
        }
        return 0;
    }
}