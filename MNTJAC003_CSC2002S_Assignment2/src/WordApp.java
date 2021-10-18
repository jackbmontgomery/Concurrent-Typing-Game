import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;


public class WordApp {

	static int noWords;		// number of words to appear on screen at given time
	static int totalWords;	// total words to fall

    static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); 

	static WordRecord[] words;
	static Score score = new Score();

	static WordPanel w;
	static JPanel txt;
	static JLabel caught;
	static JLabel missed;
	static JLabel scr;
   
    static JFrame frame;
   
    static JTextField textEntry;

	static volatile boolean ended=false;		// flag the end button has been pressed
	static volatile boolean finished=false; 	// flag the game is finished
	static volatile boolean exceeded=false;	    // flag the word limit has been reached
	private static boolean started = false;     // flag if the game has started
	private static boolean firstRun=true;	    // flag the first run


	/**
	 * Set Up GUI for the WordApp
	 * @param frameX x-size of frame
	 * @param frameY y-seize of frame
	 * @param yLimit limit for words to drop
	 */
	 public static void setupGUI(int frameX,int frameY,int yLimit) {

			// Frame init and dimensions
    	 frame = new JFrame("WordGame");
    	 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	 frame.setSize(frameX, frameY);

			// JPanel creation
    	 JPanel g = new JPanel();
         g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
    	 g.setSize(frameX,frameY);

			// WordPanel creation
	     w = new WordPanel(words,yLimit);
		 w.setSize(frameX,yLimit+100);
	     g.add(w);

			// Jpanel creation for the score text
	     txt = new JPanel();
	     txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS));
	     caught =new JLabel("Caught: " + score.getCaught() + "    ");
	     missed =new JLabel("Missed: " + score.getMissed()+ "    ");
         scr =new JLabel("Score: " + score.getScore()+ "    ");
      
	     txt.add(caught);
	     txt.add(missed);
	     txt.add(scr);

			// for handling entered words
	     textEntry = new JTextField("Click Start",20);
	     textEntry.addActionListener(new ActionListener(){
	         public void actionPerformed(ActionEvent evt) {
				 if (started){
					if (w.testWord(textEntry.getText())){
						AppAudio.correct();
						 }
				     else { AppAudio.wrong(); }
   
					 textEntry.setText("");
   
					 if(!finished){
					     textEntry.requestFocus();
					 } 
				 }
				 else { textEntry.setText("Click Start"); }
	         }
	     });

	     txt.add(textEntry);
	     txt.setMaximumSize( txt.getPreferredSize() );
	     g.add(txt);

		 // JPanel for the buttons
	     JPanel b = new JPanel();
         b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
      
	     JButton startB = new JButton("Start");

			 // added the listener to the jbutton to handle the Start event
			 startB.addActionListener(new ActionListener(){
         
		         public void actionPerformed(ActionEvent e) {
					 started = true;
               
				     if (!firstRun){
						 w.endThreads();
					 }
               
				 firstRun=false;
                 textEntry.setText("");
				 score.resetScore();
               
				 caught.setText("Caught: " + score.getCaught() + "    ");
				 missed.setText("Missed: " + score.getMissed() + "    ");
				 scr.setText("Score: " + score.getScore()+ "    ");
               
				 finished=false;
				 ended=false;
				 exceeded=false;
               
			     textEntry.requestFocus();
               
                 makeWordArray();
				 w.run();
		        }
		  });
        
		 JButton endB = new JButton("End");
         
			// added the listener to the jbutton to handle the "pressed" event
			endB.addActionListener(new ActionListener(){
		         public void actionPerformed(ActionEvent e) {
             
			 	     started = false;
             	     ended = true;
             	     firstRun = true;

			         score.resetScore();
             
			    	 caught.setText("Caught: " + score.getCaught() + "    ");
			    	 missed.setText("Missed: " + score.getMissed() + "    ");
			    	 scr.setText("Score: " + score.getScore()+ "    ");
			    	 textEntry.setText("Click Start");
                }
		    });

			 JButton quitB = new JButton("Quit");
         
				quitB.addActionListener(new ActionListener(){
				     public void actionPerformed(ActionEvent e){
						 System.exit(0);
					}
				});

		 b.add(startB);
		 b.add(endB);
		 b.add(quitB);

		 g.add(b);

  	     frame.setLocationRelativeTo(null);  			// Center window on screen.
  	     frame.add(g); 										   //add contents to window
         frame.setContentPane(g);
         frame.setVisible(true);
	 }
     
	 /**
	  * Closes GUI made in setUpGUI()
	  */
	 public static void closeGUI(){

	     frame.setVisible(false);;
     }  
	 
	 /**
	  * Restart GUI after it has been closed
	  */
     public static void restartGUI(){
     textEntry.setText("Click Start");
	 started = false;
     score.resetScore();
   
	 caught.setText("Caught: " + score.getCaught() + "    ");
	 missed.setText("Missed: " + score.getMissed() + "    ");
	 scr.setText("Score: " + score.getScore()+ "    ");
   
     frame.setVisible(true);
   
}

	 /**
	  * Create an array of words from a file, uses default dictionary if file isn't found
	  * @param filename name of file
	  * @return an array of word from the inputed file
	  */
     public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			 Scanner dictReader = new Scanner(new FileInputStream(filename));
			 int dictLength = dictReader.nextInt();
			 //System.out.println("read '" + dictLength+"'");

			 dictStr=new String[dictLength];
			 for (int i=0;i<dictLength;i++) {
				 dictStr[i]=new String(dictReader.next());
				 //System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			 }
			 dictReader.close();
		 } catch (IOException e) {
	         System.err.println("Problem reading file " + filename + " default dictionary will be used");
	     }
		 return dictStr;

	 }
	  
	 /**
	  * Creates an array of WordRecords from an array of  Strings
	  */
     public static void makeWordArray(){
 	     int x_inc=(int)frameX/noWords;
		 //initialize shared array of current words

 	     for (int i=0;i<noWords;i++) {
	         words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		     }
	     }


     public static void main(String[] args) {

	     //Command line arguments
 	     totalWords=Integer.parseInt(args[0]);  						//total words to fall
 	     noWords=Integer.parseInt(args[1]);							   //total words falling at any point

		  if (totalWords < noWords){
			System.out.println("Fatal Error: Total words is less than words to be on the screen");
			System.exit(0);
			}

 	     String[] tmpDict=getDictFromFile("/home/jack/CSC2002S/Assignment2/dictionaries/"+args[2]);			      //file of words

		 if (tmpDict!=null){
			 dict = new WordDictionary(tmpDict);
		 }
			 

		 WordRecord.dict=dict;									//set the class dictionary for the words.

		 words = new WordRecord[noWords];  					    //shared array of current words

		 setupGUI(frameX, frameY, yLimit);
		 makeWordArray();
	}

}