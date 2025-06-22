import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.*;

public class VideoPage extends JPanel {
    public VideoPage(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Environmental Awareness Videos");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Center panel with buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1, 15, 15));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        centerPanel.add(createVideoButton("What is Climate Change?", "https://youtu.be/tMwFNMfjFuU?si=MN1PVjDsIpB1m5XL"));
        centerPanel.add(createVideoButton("Reduce, Reuse, Recycle", "https://youtu.be/MqAyI6caMv4?si=AWzcClqliuncbM7o"));
        centerPanel.add(createVideoButton("How deforestation affects the environment", "https://youtu.be/60zCK4h3FEU?si=WKzqRUbqZEgnCXdW"));

        add(centerPanel, BorderLayout.CENTER);

        // Back button
        JButton homeBtn = new JButton(" Back to Home");
        homeBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        homeBtn.setPreferredSize(new Dimension(200, 40));
        homeBtn.addActionListener(e -> cardLayout.show(mainPanel, "home"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        bottomPanel.add(homeBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createVideoButton(String label, String url) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(230, 255, 240));
        button.setPreferredSize(new Dimension(280, 40));
        button.addActionListener((ActionEvent e) -> {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return button;
    }
}
