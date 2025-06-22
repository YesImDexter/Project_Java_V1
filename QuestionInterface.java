// File: QuestionInterface.java
// Creator: DEXTER SKUDD ANAK JOHN RIZAL (101630), JEREMY TOMMY AJENG EMANG (99286)
// Tester: ABDUL HAFIY KAMALUDDIN BIN ABDUL RANI (101476)

import java.util.*;

/**
 * Interface for all question types.
 */
public interface QuestionInterface {
    String getQuestionText();
    boolean isCorrectAnswer(String userAnswer);
    int getPoints();
    String getCorrectAnswer();
}

/**
 * Abstract base class for all questions.
 */
abstract class Question implements QuestionInterface {
    protected String questionText;
    protected int points;

    public Question(String questionText, int points) {
        this.questionText = questionText;
        this.points = points;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getPoints() {
        return points;
    }

    public abstract String getCorrectAnswer();
}

/**
 * Multiple Choice Question implementation.
 */
class MCQQuestion extends Question {
    private List<String> options;
    private int correctOption;

    public MCQQuestion(String questionText, List<String> options, int correctOption, int points) {
        super(questionText, points);
        this.options = options;
        this.correctOption = correctOption;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isCorrectAnswer(String userAnswer) {
        return userAnswer.trim().equalsIgnoreCase(options.get(correctOption));
    }

    public String getCorrectAnswer() {
        return options.get(correctOption);
    }
}

/**
 * True/False Question implementation.
 */
class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(String questionText, boolean correctAnswer, int points) {
        super(questionText, points);
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrectAnswer(String userAnswer) {
        return Boolean.parseBoolean(userAnswer.trim().toLowerCase()) == correctAnswer;
    }

    public String getCorrectAnswer() {
        return String.valueOf(correctAnswer);
    }
}

/**
 * Fill-in-the-blank Question implementation.
 */
class FillInBlankQuestion extends Question {
    private List<String> correctAnswers;

    public FillInBlankQuestion(String questionText, List<String> correctAnswers, int points) {
        super(questionText, points);
        this.correctAnswers = correctAnswers;
    }

    public boolean isCorrectAnswer(String userAnswer) {
        return correctAnswers.stream().anyMatch(ans -> ans.equalsIgnoreCase(userAnswer.trim()));
    }

    public String getCorrectAnswer() {
        return correctAnswers.get(0);
    }
}

/**
 * Matching Question implementation.
 */
class MatchingQuestion extends Question {
    private Map<String, String> correctPairs;

    public MatchingQuestion(String questionText, Map<String, String> correctPairs, int points) {
        super(questionText, points);
        this.correctPairs = correctPairs;
    }

    public Set<String> getKeys() {
        return correctPairs.keySet();
    }

    public Collection<String> getValues() {
        return correctPairs.values();
    }

    public boolean isCorrectAnswer(String userAnswer) {
        try {
            String[] pairs = userAnswer.split(",");
            Map<String, String> submitted = new HashMap<>();
            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length == 2) submitted.put(kv[0].trim(), kv[1].trim());
            }
            return correctPairs.equals(submitted);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calculates partial score for matching questions.
     */
    public int getPartialScore(String userAnswer) {
        try {
            String[] pairs = userAnswer.split(",");
            Map<String, String> submitted = new HashMap<>();
            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length == 2) submitted.put(kv[0].trim(), kv[1].trim());
            }
            int correctCount = 0;
            for (String key : correctPairs.keySet()) {
                if (submitted.containsKey(key) && correctPairs.get(key).equals(submitted.get(key))) {
                    correctCount++;
                }
            }
            int total = correctPairs.size();
            return (int) Math.round((getPoints() * correctCount) / (double) total);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getCorrectAnswer() {
        return correctPairs.toString();
    }
}

/**
 * Short Answer Question implementation.
 */
class ShortAnswerQuestion extends Question {
    private List<String> keywords;

    public ShortAnswerQuestion(String questionText, List<String> keywords, int points) {
        super(questionText, points);
        this.keywords = keywords;
    }

    public boolean isCorrectAnswer(String userAnswer) {
        String answer = userAnswer.toLowerCase();
        return keywords.stream().anyMatch(answer::contains);
    }

    public String getCorrectAnswer() {
        return String.join(", ", keywords);
    }
}
