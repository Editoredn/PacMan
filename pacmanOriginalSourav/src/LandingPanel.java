package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LandingPanel extends JPanel {
    private Image backgroundImage;
    private SoundManager soundManager;

    public LandingPanel(JFrame frame, int width, int height, SoundManager soundManager) {
        this.soundManager = soundManager;

        backgroundImage = new ImageIcon("src/Pacman.png").getImage(); 

        setLayout(null);
        setPreferredSize(new Dimension(width, height));

        
        JLabel playLabel = new JLabel("PLAY");
        playLabel.setForeground(Color.WHITE);
        playLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playLabel.setBounds(width / 2, height / 2, 2240, 1400); 

        
        playLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        
        playLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                soundManager.stopEffect("LandingPage");
                PacMan pacmanGame = new PacMan(soundManager);
                frame.setContentPane(pacmanGame);
                frame.pack();
                pacmanGame.requestFocus();
            }
        });

        add(playLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
