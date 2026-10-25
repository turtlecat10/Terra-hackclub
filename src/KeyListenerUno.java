package src;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListenerUno extends JFrame implements KeyListener {
    public KeyListenerUno() {
        // Set up a clear title so players know this window controls the game
        setTitle("UNO Controller");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Add a visual indicator label to prompt the player
        JLabel promptLabel = new JLabel("<html><center>Keep this window selected<br>to play your cards!</center></html>", SwingConstants.CENTER);
        promptLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.add(promptLabel);

        this.addKeyListener(this); 
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        // Force the window to stay on top so it doesn't accidentally get hidden under the console
        setAlwaysOnTop(true);
        
        // Center the window on the screen for easy visibility
        setLocationRelativeTo(null);
        setVisible(true); 
        
        // Explicitly request keyboard focus on launch
        requestFocus();
    }

    @Override 
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT){
            Run.left();
        } else if(keyCode == KeyEvent.VK_RIGHT){
            Run.right();
        } else if(keyCode == KeyEvent.VK_ENTER){
            Run.enter();
        } else if(keyCode == KeyEvent.VK_1){
            Run.one();
        } else if(keyCode == KeyEvent.VK_SPACE){
            Run.space();
        }
    }

    @Override 
    public void keyReleased(KeyEvent e){}

    @Override 
    public void keyTyped(KeyEvent e){}
}
