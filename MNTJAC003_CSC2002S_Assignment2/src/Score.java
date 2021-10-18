import java.util.concurrent.atomic.AtomicInteger;

public class Score {
	private static AtomicInteger missedWords;
	private static AtomicInteger caughtWords;
	private static AtomicInteger score;

	Score() {
		missedWords= new AtomicInteger(0);
		caughtWords= new AtomicInteger(0);
		score= new AtomicInteger(0);
	}

	/**
	 * getter :missed words
	 * @return number of words missed
	 */
	public synchronized int getMissed() {
		return missedWords.get();
	}

	/**
	 * getter: the caught words
	 * @return number of words caught
	 */
	public synchronized int getCaught() {
		return caughtWords.get();
	}

	/**
	 * getter: the total words
	 * @return the total words
	 */
	public synchronized int getTotal() {
		return (missedWords.get()+caughtWords.get());
	}

	/**
	 * gets the score for the game
	 * @return the score for the game
	 */
	public synchronized int getScore() {
		return score.get();
	}

	/**
	 * incrementing missed words
	 */
	public synchronized void missedWord() {
		missedWords.getAndIncrement();
	}

	/**
	 * incrementing caught words and updates the score
	 * @param length number of characters of caught word
	 */
	public synchronized static void caughtWord(int length) {
		caughtWords.getAndIncrement();
		score.getAndAdd(length);
	}

	/**
	 * reset score to 0
	 */
	public void resetScore() {
		caughtWords.set(0);
		missedWords.set(0);
		score.set(0);
	}
}