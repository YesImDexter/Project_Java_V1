// File: LearningModule.java
// Creator: IZZ EZZAD SYAMEIR BIN ISMAIL (97460)
// Tester: DEXTER SKUDD ANAK JOHN RIZAL (101630)

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

        // Home Menu (match GUIController theme)
        JPanel homeMenu = new JPanel(new BorderLayout());
        homeMenu.setBackground(new Color(220, 245, 230));
        homeMenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Logo/Icon (optional, similar to GUIController)
        JLabel logoLabel;
        try {
            ImageIcon logo = new ImageIcon("Resources/logo.png");
            Image scaled = logo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            logoLabel = new JLabel("\uD83C\uDF31");
            logoLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createVerticalStrut(10));

        JLabel welcomeLabel = new JLabel("Welcome to Environmental Awareness");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("<html><div style='text-align:center;'>Explore learning resources and videos!</div></html>");
        subtitle.setFont(new Font("Arial", Font.ITALIC, 13));
        subtitle.setForeground(new Color(34, 139, 34));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitle);

        centerPanel.add(Box.createVerticalStrut(30));

        // Standardize button sizes and fonts
        Dimension menuBtnSize = new Dimension(240, 40);
        Dimension backBtnSize = new Dimension(240, 40);

        JButton learningBtn = new JButton("Open Learning Module");
        JButton videoBtn = new JButton("Watch Awareness Videos");
        JButton backToMainBtn = new JButton("Back to Menu");

        Font menuFont = new Font("Arial", Font.BOLD, 16);

        learningBtn.setFont(menuFont);
        videoBtn.setFont(menuFont);
        backToMainBtn.setFont(menuFont);

        learningBtn.setBackground(new Color(200, 240, 215));
        videoBtn.setBackground(new Color(200, 240, 215));
        backToMainBtn.setBackground(new Color(255, 220, 220));

        learningBtn.setPreferredSize(menuBtnSize);
        learningBtn.setMaximumSize(menuBtnSize);
        learningBtn.setMinimumSize(menuBtnSize);
        videoBtn.setPreferredSize(menuBtnSize);
        videoBtn.setMaximumSize(menuBtnSize);
        videoBtn.setMinimumSize(menuBtnSize);
        backToMainBtn.setPreferredSize(backBtnSize);
        backToMainBtn.setMaximumSize(backBtnSize);
        backToMainBtn.setMinimumSize(backBtnSize);

        learningBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        videoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToMainBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        learningBtn.setFocusPainted(false);
        videoBtn.setFocusPainted(false);
        backToMainBtn.setFocusPainted(false);

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

        learningBtn.addActionListener(e -> cardLayout.show(mainPanel, "learning"));
        videoBtn.addActionListener(e -> cardLayout.show(mainPanel, "videos"));

        centerPanel.add(learningBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(videoBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(backToMainBtn);

        centerPanel.add(Box.createVerticalStrut(30));
        JLabel credits = new JLabel("© 2025 JAVALover G02/SE-G01");
        credits.setFont(new Font("Arial", Font.PLAIN, 11));
        credits.setForeground(new Color(100, 120, 100));
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);

        homeMenu.add(centerPanel, BorderLayout.CENTER);

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
