import java.awt.*;
import javax.swing.*;

public class LearningModule {

    static JFrame frame;
    static CardLayout cardLayout;
    static JPanel mainPanel;

    public static void main(String[] args) {
        frame = new JFrame("Environmental Awareness");
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Home Menu
        JPanel homeMenu = new JPanel(new BorderLayout());
        homeMenu.setBackground(new Color(230, 255, 240));
        homeMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome to Environmental Awareness");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20));
        buttonPanel.setBackground(new Color(230, 255, 240));

        JButton learningBtn = new JButton("Open Learning Module");
        JButton videoBtn = new JButton("Watch Awareness Videos");
        JButton backToMainBtn = new JButton("Back to Menu");
        backToMainBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        backToMainBtn.setBackground(new Color(255, 220, 220));
        backToMainBtn.setPreferredSize(new Dimension(120, 32));
        backToMainBtn.addActionListener(e -> {
            frame.setVisible(false);
            // Try to bring main menu back to front if it exists
            for (Frame f : Frame.getFrames()) {
                if (f.getTitle().contains("Environmental Awareness Quiz")) {
                    f.setVisible(true);
                    f.toFront();
                }
            }
        });

        learningBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        videoBtn.setFont(new Font("Arial", Font.PLAIN, 16));

        learningBtn.addActionListener(e -> cardLayout.show(mainPanel, "learning"));
        videoBtn.addActionListener(e -> cardLayout.show(mainPanel, "videos"));

        buttonPanel.add(learningBtn);
        buttonPanel.add(videoBtn);
        buttonPanel.add(backToMainBtn);

        homeMenu.add(welcomeLabel, BorderLayout.NORTH);
        homeMenu.add(buttonPanel, BorderLayout.CENTER);

        // Learning Module Pages
        JPanel contentWrapper = new JPanel(new CardLayout());
        String[] titles = {
            "What is Environmental Awareness?",
            "The Importance of Nature Conservation",
            "Causes of Climate Change",
            "Effects of Global Warming",
            "Plastic Pollution and Its Impact",
            "Deforestation and Biodiversity Loss",
            "Renewable vs Non-Renewable Energy",
            "Sustainable Living Tips",
            "Role of Youth in Environmental Action",
            "Global Environmental Movements"
        };
        String[] contents = {
            "Environmental awareness means understanding the fragility of our environment and the importance of its protection...",
            "Nature conservation is the practice of protecting ecosystems, wildlife, and natural resources...",
            "Climate change is caused by greenhouse gases from burning fossil fuels and deforestation...",
            "Global warming leads to rising sea levels, extreme weather, and loss of biodiversity...",
            "Plastic waste pollutes oceans and harms marine life. Reducing single-use plastics is critical...",
            "Deforestation reduces biodiversity and contributes to global warming...",
            "Renewable energy is sustainable, unlike fossil fuels which emit harmful gases...",
            "Sustainable living includes reducing waste, energy use, and using eco-friendly products...",
            "Youth have a major role in environmental activism and awareness...",
            "Global movements like Earth Day and Fridays for Future push for climate action..."
        };
        String[] images = {
            "Resources/image 1.png",
            "Resources/image 2.png",
            "Resources/image 3.png",
            "Resources/image 4.png",
            "Resources/image 5.png",
            "Resources/image 6.png",
            "Resources/image 7.png",
            "Resources/image 8.png",
            "Resources/image 9.png",
            "Resources/image 10.png"
        };

        for (int i = 0; i < titles.length; i++) {
            contentWrapper.add(new ContentPage(i + 1, titles.length, titles[i], contents[i], images[i]), "page" + (i + 1));
        }

        JPanel navPanel = new JPanel(new FlowLayout());
        JButton prev = new JButton("Prev");
        JButton next = new JButton("Next");
        JButton home = new JButton("Home");

        navPanel.add(prev);
        navPanel.add(next);
        navPanel.add(home);

        prev.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentWrapper.getLayout();
            cl.previous(contentWrapper);
        });

        next.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentWrapper.getLayout();
            cl.next(contentWrapper);
        });

        home.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        JPanel fullLearningPanel = new JPanel(new BorderLayout());
        fullLearningPanel.add(contentWrapper, BorderLayout.CENTER);
        fullLearningPanel.add(navPanel, BorderLayout.SOUTH);

        mainPanel.add(homeMenu, "home");
        mainPanel.add(fullLearningPanel, "learning");
        mainPanel.add(new VideoPage(cardLayout, mainPanel), "videos");

        frame.setContentPane(mainPanel);
        cardLayout.show(mainPanel, "home");
        frame.setVisible(true);
    }
}
