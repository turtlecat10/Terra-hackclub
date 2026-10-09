import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class KeyListenerUno extends JFrame implements KeyListener{
    public KeyListenerUno() {
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this); 
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setVisible(true); 
    }
    @Override 
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT){
            Run.left();
        }else if(keyCode == KeyEvent.VK_RIGHT){
            Run.right();
        }else if(keyCode == KeyEvent.VK_ENTER){
            Run.enter();
        }else if(keyCode == KeyEvent.VK_1){
            Run.one();
        }
    }
    @Override 
    public void keyReleased(KeyEvent e){}
    @Override 
    public void keyTyped(KeyEvent e){}
}
