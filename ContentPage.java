// File: ContentPage.java
// Creator: IZZ EZZAD SYAMEIR BIN ISMAIL (97460)
// Tester: ABDUL HAFIY KAMALUDDIN BIN ABDUL RANI (101476)

import java.awt.*;
import javax.swing.*;

/**
 * Represents a single page of learning content.
 */
public class ContentPage extends JPanel {
    public ContentPage(int pageNumber, int totalPages, String titleText, String contentText, String imagePath) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(10));

        // Image (if available)
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaledImage = icon.getImage().getScaledInstance(280, 160, Image.SCALE_SMOOTH);
                JLabel img = new JLabel(new ImageIcon(scaledImage));
                img.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(img);
                add(Box.createVerticalStrut(10));
            } catch (Exception e) {
                System.out.println("Image not found: " + imagePath);
            }
        }

        // Content text area (scrollable)
        JTextArea content = new JTextArea(contentText);
        content.setWrapStyleWord(true);
        content.setLineWrap(true);
        content.setFont(new Font("Serif", Font.PLAIN, 14));
        content.setEditable(false);
        content.setBackground(new Color(248, 248, 248));
        content.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        content.setMaximumSize(new Dimension(320, 300));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setPreferredSize(new Dimension(320, 300));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(scrollPane);

        add(Box.createVerticalStrut(10));

        // Page number label
        JLabel pageNumberLabel = new JLabel("Page " + pageNumber + " of " + totalPages);
        pageNumberLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        pageNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(pageNumberLabel);
    }
}


