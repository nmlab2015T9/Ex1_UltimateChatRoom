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
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MainHead extends JFrame{
	private static int sx = 60, sy = 60, sx2 = 80, sy2 = 80; //sx2, sy2 = cross size
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
        cross.setLocation(15, 15);
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
            	/*timer = new Timer();
            	timer.scheduleAtFixedRate(new TimerTask(){  
            		@Override
            		public void run() {
            			if(i<=20){
            				if(p.y > 20)
            					setLocation(p.x + ((width - sx) - p.x)*i/20, p.y - (p.y - 20)*i/20);
            				else if(p.y < 20)
            					setLocation(p.x + ((width - sx) - p.x)*i/20, p.y + (20 - p.y)*i/20);
            				i++;
            				mainframe.setSize(gui.MainFrame.frameSX*i/20, gui.MainFrame.frameSY*i/20);
            			}
            			else{
            					timer.cancel();
            					i = 0;
            			}
            		}},10 ,1);*/
            }
            
            public void mouseReleased(MouseEvent e){
            	Point p = getLocation();
            	if(p.x < gap){
            		p.x = 0;
            		setLocation(p);
            	}
            	else if(p.y < gap){
            		p.y = 20;
            		setLocation(p);
            	}
            	else if(Math.abs(p.x - width) < (gap + sx)){
            		p.x = width-sx;
            		setLocation(p);
            	}
            	else if(Math.abs(p.y - height) < (gap + sy)){
            		p.y = height-sy;
            		setLocation(p);
            	}
            	
                //if(p.x > (width/2 - sx2/2 - 200) && p.x < (width/2 + sx2/2 + 200) && p.y > (height - sy2 - 200)){
                	closeProcess();
            	//}
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
            	
                Point p = getLocation();
                setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
                //if(p.x > (width/2 - sx2/2 - 200) && p.x < (width/2 + sx2/2 + 200) && p.y > (height - sy2 - 200)){
                	closeArea.setVisible(true);
                	closeArea.setLocation((width/2 - sx2/2) + ((p.x + e.getX() - point.x) - (width/2 - sx2/2)) / 50, 
                						  (height - sy2) + ((p.y + e.getY() - point.y) - (height - sy2)) / 50);
                //	inArea = true;
            	//}
                //else if(inArea && (p.x < (width/2 - sx2/2 - 200) || p.x > (width/2 + sx2/2 + 200) || p.y < (height - sy2 - 200))){
                //	closeProcess();
                //	inArea = false;
                //}
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
	    				//for the main head to set in the center of closeArea
	    				if(p.x > (width/2 - sx2/2) && p.x < (width/2 + sx2/2) && p.y > (height - sy2))
	    					setLocation(width/2 - sx2/2 + 10, (height - sy2 + 10)+300*i/50);
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
    
 // "/q+" command
    public void addNewUser ( String user , int color ) {
        mainframe.addNewUser(user, color);
    }

    // "/q-" command
    public void delUser ( String user ) {
    	mainframe.delUser(user);
    }

    // "/r+" command
    /*public void addUser ( String user , int color , int roomID ) {
        ChatTab tab = map.get(roomID);
        tab.addUser(user, color);
    }*/

    // "/r-" command
   /* public void delUser ( String user , int roomID ) {
        ChatTab tab = map.get(roomID);
        tab.delUser(user);
    }*/

    //add new lines
    public void addNewLine ( String text , String color , int roomID ) {
        if (roomID == 0) {
        	mainframe.addNewLine(text, color);
        }
        //else {
       //   ChatTab tab = map.get(roomID);
       //    tab.addNewLine(text, color);
       // }
    }

    public void addSysLine ( String text ) {
    	mainframe.addSysLine(text);
    }

    public void addWarnLine ( String text ) {
    	mainframe.addWarnLine(text);
    }

    /*public void setLastWhisper ( String name , int roomID ) {
        if (roomID == 0) {
            MainChatTab.setLastWhisper(name);
        }
        else {
            ChatTab tab = map.get(roomID);
            tab.setLastWhisper(name);
        }
    }*/

    // "/a" command
    /*public void addTab ( int roomID ) {
        ChatTab newTab = new ChatTab(roomID, username);
        newTab.setFrame(this);
        newTab.setClientObject(ClientObject);
        tabs.add(newTab);
        map.put(roomID, newTab);
        TabPane.add("Room "+roomID, newTab);
    }

    // "/l" command
    public void delTab ( int roomID ) {
        ChatTab tab = map.get(roomID);
        tabs.remove(tab);
        map.remove(roomID);
        TabPane.remove(tab);
    }*/

    // "/c" command
    /*public void userChangeColor ( String name, int c ) {
        MainChatTab.userChangeColor(name, c);
        for (ChatTab t:tabs) {
            t.userChangeColor(name, c);
        }
    }*/

    // "/f" command
    /*public void sendFile( String dest ) {
    	ClientObject.sendFile(username, dest);
    }*/


    public void clear () {
    	mainframe.clear();
    }
}