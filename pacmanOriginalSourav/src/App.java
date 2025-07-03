package src;
import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {

        int rowCount = 23;
        int colCount = 21;
        int tileSize = 32;
        int widthBoard = colCount*tileSize;
        int heightBoard = rowCount*tileSize;

        JFrame frame = new JFrame("Pac Man"); 
        frame.setSize(widthBoard  , heightBoard);   
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SoundManager soundManager = new SoundManager(1);

        LandingPanel landingPanel = new LandingPanel(frame, widthBoard, heightBoard , soundManager);
        frame.setContentPane(landingPanel);
        frame.setVisible(true);
        soundManager.playEffect("LandingPage");
        
    }
}
