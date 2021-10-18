import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;


public class WordPanel extends JPanel implements Runnable, ActionListener {

		private WordRecord[] words;
		private int noWords;							// number of words to be displayed on screen at any time
		private int maxY;
		private Timer timer;                            // for the scheduling of tasks
	    private WordThread [] thrds;
		
		public void paintComponent(Graphics g) {

		     int width = getWidth();
		     int height = getHeight();

		     g.clearRect(0,0,width,height);
		     g.setColor(Color.red);
		     g.fillRect(0,maxY-10,width,height);

		     g.setColor(Color.black);
		     g.setFont(new Font("Helvetica", Font.PLAIN, 26));

		     for (int i=0;i<noWords;i++){
		    	 g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);
		    }

	    }
		 
		/**
		 * Constructor for he WordPanel
		 * @param words array of WordRecords from dictionary
		 * @param maxY limit for words to fall
		 */
		 WordPanel(WordRecord[] words, int maxY) {
			 this.words=words;
			 noWords = words.length;
			 this.maxY=maxY;
			 timer = new Timer(100, this);
			 timer.setInitialDelay(0);
		}
		 
		/**
		 * Creates and starts each thread in the WordThread array
		 */
		public void run() {

			 timer.start();

			 thrds = new WordThread[noWords];

			 for (int i=0;i<noWords;i++) {
				thrds[i] = new WordThread(words[i]);
				thrds[i].start();
			 }
		 }
      
		 public void actionPerformed(ActionEvent e){
			 validate();
			 repaint();
         
         if (WordApp.finished){
             new EndGui();
		     timer.stop();
			 endThreads();
             WordApp.closeGUI();
             }
		 }

		 /**
		  * 
		  * @param word typed word by user
		  * @return boolean if there is a match (or multiple) in the WordThreads
		  */
		public boolean testWord(String word) {
			boolean match = false;
			for (int i=0;i<noWords;i++){
				if (thrds[i].match(word)){
					match = true;
				}
			}
			return match;
		}
		 
		/**
		 * Ends all WordThreads in execution
		 */
		public void endThreads(){
			for (int i=0;i<noWords;i++){
				thrds[i].endThread();
			}
		} 

}

