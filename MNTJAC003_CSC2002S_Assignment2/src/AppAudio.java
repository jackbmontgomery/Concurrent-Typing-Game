import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AppAudio{
	static final String  path = "/home/jack/CSC2002S/Assignment2/audio/";
	/**
	 * Plays sound for "wrong"
	 */
    public static void wrong(){
        
		try{
			AudioInputStream wrongAudio = AudioSystem.getAudioInputStream(new File(path+"wrong.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(wrongAudio);
			clip.start();
		}
		catch(Exception exception){
		System.out.println("Audio Not Found");
		exception.printStackTrace();
        }
    }

	/**
	 * Plays sound for "correct"
	 */
    public static void correct(){
        
		try{
			AudioInputStream correctAudio = AudioSystem.getAudioInputStream(new File(path+"correct.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(correctAudio);
			clip.start();
		}
		catch(Exception exception){
		System.out.println("Audio Not Found");
		exception.printStackTrace();
		}
    }
}