package gui;


import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MainHead extends JFrame{
	private static int sx = 60, sy = 60, sx2 = 100, sy2 = 100; //sx2, sy2 = cross size
	private static int gap = 150;
	private static int i = 0;
    private static Point point = new Point();
    private static Ellipse2D ellipse = new Ellipse2D.Double(0, 0, sx, sy), ellipse2 = new Ellipse2D.Double(0, 0, sx2, sy2);   
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = gd.getDisplayMode().getWidth();
    private static int height = gd.getDisplayMode().getHeight();
    private static final JFrame closeArea = new JFrame();
    private static Timer timer;
    private static boolean inArea;
    private MainFrame mainframe;

    public MainHead(MainFrame mainFrame){
    	mainframe = mainFrame;
    	initComponent();
}
    
    private void initComponent(){
    	setUndecorated(true);
        setAlwaysOnTop(true);
        setShape(ellipse);
        setSize(sx, sy);
        setLocation(width-sx, 100);
        setLayout(new BorderLayout());
    	URL url = client.DisplayMgr.class.getResource("/res/lobby.png");
		ImageIcon lobbyImage = new ImageIcon(url);
        JLabel mainhead = new JLabel(lobbyImage);
        getContentPane().add(mainhead,BorderLayout.CENTER);
        
        //setting of the close area
    	URL url2 = client.DisplayMgr.class.getResource("/res/cross.png");
		ImageIcon crossImage = new ImageIcon(url2);
        JLabel cross = new JLabel(crossImage);
        closeArea.getContentPane().setLayout(null);
        cross.setSize(50, 50);
        cross.setLocation(25, 25);
        closeArea.getContentPane().add(cross);
        closeArea.setUndecorated(true);
        closeArea.setShape(ellipse2);
        closeArea.setSize(sx2, sy2);
        closeArea.setLocation(width/2 - sx2/2, height - sy2);
        closeArea.setOpacity(0.5f);
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher(){
        	 public boolean dispatchKeyEvent(KeyEvent e) {
                 if(e.getID() == KeyEvent.KEY_PRESSED && (e.getKeyCode() == KeyEvent.VK_Q) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
                	 System.exit(0);
                 }
                 return false;
             }
        });
        
        //
        // The mouse listener and mouse motion listener we add here is to simply
        // make our frame dragable.
        //
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }
            
            public void mouseClicked(MouseEvent e){
            	Point p = getLocation();
            	if(!mainframe.isVisible()){
            		mainframe.setVisible(true);
            	}
            	else
            		mainframe.setVisible(false);
            	timer = new Timer();
            	timer.scheduleAtFixedRate(new TimerTask(){  
            		@Override
            		public void run() {
            			if(i<=10){
            				if(p.y > 20)
            					setLocation(p.x + ((width - sx) - p.x)*i/10, p.y - (p.y - 20)*i/10);
            				else if(p.y < 20)
            					setLocation(p.x + ((width - sx) - p.x)*i/10, p.y + (20 - p.y)*i/10);
            				i++;
            			}
            			else{
            					timer.cancel();
            					i = 0;
            			}
            		}},10 ,10);
            }
            
            public void mouseReleased(MouseEvent e){
            	Point p = getLocation();
            	if(p.x < gap){
            		p.x = 0;
            		setLocation(p);
            	}
            	if(p.y < gap){
            		p.y = 20;
            		setLocation(p);
            	}
            	if(Math.abs(p.x - width) < (gap + sx)){
            		p.x = width-sx;
            		setLocation(p);
            	}
            	if(Math.abs(p.y - height) < (gap + sy)){
            		p.y = height-sy;
            		setLocation(p);
            	}
            	
                if(p.x > (width/2 - sx2/2 - 200) && p.x < (width/2 + sx2/2 + 200) && p.y > (height - sy2 - 200)){
                	closeProcess();
            	}
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
            	
                Point p = getLocation();
                setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
                if(p.x > (width/2 - sx2/2 - 200) && p.x < (width/2 + sx2/2 + 200) && p.y > (height - sy2 - 200)){
                	closeArea.setVisible(true);
                	inArea = true;
            	}
                else if(inArea && (p.x < (width/2 - sx2/2 - 200) || p.x > (width/2 + sx2/2 + 200) || p.y < (height - sy2 - 200))){
                	closeProcess();
                	inArea = false;
                }
            }
        });
        
    }
    
    private void closeProcess(){
    	final Point p = getLocation();
    	timer = new Timer();
    	timer.scheduleAtFixedRate(new TimerTask(){  
    		public void run(){  
    			SwingUtilities.invokeLater(new Runnable(){
    		
    			
				@Override
				public void run() {
					if(i<=50){
	    				closeArea.setLocation(width/2 - sx2/2, (height - sy2)+300*i/50);
	    				if(p.x > (width/2 - sx2/2) && p.x < (width/2 + sx2/2) && p.y > (height - sy2))
	    					setLocation(width/2 - sx2/2 + 20, (height - sy2 + 20)+300*i/50);
	    				i++;
	    			}
	    			else{
	    				if(p.x > (width/2 - sx2/2) && p.x < (width/2 + sx2/2) && p.y > (height - sy2))
	    					System.exit(0);
	    				else {
	    					closeArea.setVisible(false);
	    					closeArea.setLocation(width/2 - sx2/2, height - sy2);
	    					timer.cancel();
	    					i = 0;
	    				}
	    			}
				}
    		});
    			}},10 ,10);
    }
}
