package com.omer.guessthenumber;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * THIS SERVICE IS THE "BRAIN" OF THE GAME
 * IT IS MANAGING THE GAME LOGIC AND 
 * CONNECTING BETWEEN REPOSITORIES TO CONTROLLER 
 */
@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GamesServiceDao gamesMap;

	@Autowired
	private TopScoresService topScoresService;

	private AtomicLong gameId = new AtomicLong(100);
	private static final String DIGITS = "0123456789";
	private static final String ADMINPASSWORD = "1234";
	private static final int MAXATTEMPTS = 20;
	private static final int DIGITSCOUNT = 4;

	// STARTING NEW GAME - GET ID AND RANDOM NUMBER AND ADD IT TO HASHMAP
	public CurrentGame addNewCurrentGame(String fullName) throws Throwable {
		Long key = createIdForNewGame();
		String numberToGuess = "1234";// getRandomAlphaNumeric();
		CurrentGame currentGme = new CurrentGame(key, numberToGuess, fullName);
		gamesMap.getGamesMap().put(key, currentGme);

		return cloneAndsetNullInProperties(currentGme);

	}

	// BEFORE RETURNING AN ANSWER TO CLIENT SET UNNECESSARY FIELDS TO NULL
	private CurrentGame cloneAndsetNullInProperties(CurrentGame currentGme) throws CloneNotSupportedException {
		CurrentGame resultsResponse = (CurrentGame) currentGme.clone();
		resultsResponse.setNumberToGuess(null);
		return resultsResponse;
	}

	/*
	 * WHEN USER SEND A NEW GUESS UPDATE COUNTERS OF DIGITS IN PLACE AND SCORE
	 * DIGITS AND UPDATE AND IN CASE OF WIN UPDATE THE WIN FLAG AND USING
	 * topScoresService TO CHECK IF THE RESULT IS BETWEEN TOP 10 SCORE GAMES
	 */
	public CurrentGame checkNewGuess(Long key, String newGuess) throws Throwable {
		CurrentGame currentGme = gamesMap.getGamesMap().get(key);

		// CHECKING IF THE NUMBER OF GUESSES IS UNDER MAX POSSIBLE
		if (currentGme.getAttemptsCount() < MAXATTEMPTS) {
			checkforscores(currentGme, newGuess);

			// CHECK IF THERE IS A WIN
			// IF THERE IS A WIN THEN CHECK IN THE TOP SCORES TABLE ON DB
			// IF THE TOP SCORES COUNT IS MORE THEN 10 THEN CHECKING
			// IF THERE IS A SCORE THAT IS BIGGER - THEN DELETE IT AND INSERT THE NEW SCORE

			if (currentGme.isWin() == true) {
				// FIND THE RECORD THAT HAS THE BIGGEST NUMBER OF ATTEMPTS
				// AND IF CURRENT GAME HAS LESS ATTEMPTS THEN INSERT THE SCORE TO TOPSCORES
				// TABLE

				if (topScoresService.countScores() == 10) {
					TopScoresEntity topScoreFromDb = topScoresService.findTop1ByOrderByAttemptscountDesc();
					if (topScoreFromDb.getAttemptscount() > currentGme.getAttemptsCount()) {
						topScoresService.delete(topScoreFromDb.getId());
					}
				}

				topScoresService.addTopScore(
						new TopScoresEntity(null, currentGme.getFullName(), currentGme.getAttemptsCount()));
				deleteCurrentGameFromRepository(key);

			}

		} else { // in case user passed the allow number of attempts - then set the game over
					// flag to true
			currentGme.setGameOver(true);
		}

		// CLONING THE CURRENTGAME OBJECT AND SETTING NULLS IN PROPERTIES
		// THAT SHOULDN'T BE SEEN ON CLIENT SIDE
		return cloneAndsetNullInProperties(currentGme);

	}

	// WHEN THE USER SCORE A WIN THEN ALL TOP SCORES GAMES ARE SHOWN
	@Override
	public Iterable<TopScoresEntity> fetchAllGames() {
		return topScoresService.fetchAllGames();
	}

	// WHEN CURRENT GAME IS OVER DELETE IT FROM HASHMAP IN REPOSITORY
	@Override
	public void deleteCurrentGameFromRepository(Long key) {
		gamesMap.getGamesMap().remove(key);

	}

	// **************************************************************

	// THIS METHOD GET ARGUMENT FOR THE LENGTH OF STRING AND RETURN A RANDOM NUMBER
	// OF DISTINCT NUMERIC DIGITS
	public String getRandomAlphaNumeric() {

		int count = DIGITSCOUNT;
		StringBuilder builder = new StringBuilder();

		while (count-- != 0) {
			int character = (int) (Math.random() * DIGITS.length());
			// Check if digit exist already
			if (builder.indexOf(Integer.toString(character)) == -1) {
				builder.append(DIGITS.charAt(character));
			} else {
				count++;
			}
		}
		return builder.toString();
	}

	// THIS METHOD CREATS A NEW ID FOR NEW GAME BY USING ATOMIC LONG
	public Long createIdForNewGame() {
		return this.gameId.incrementAndGet();
	}

	public void checkforscores(CurrentGame currentGame, String newGuess) {
		// This function is looking for the correct digits from the input number that
		// the user entered
		int countDigitsInPlace = 0;
		int countScoredDigits = 0;
		int pos;

		String inputNumber = newGuess;
		String numberToGeuss = currentGame.getNumberToGuess();

		// loop on numbers and check if the digits is a score and if it is in same index
		// in string
		for (int i = 0; i < DIGITSCOUNT; i++) {

			// Checking if the digit exist in given string
			pos = numberToGeuss.indexOf(inputNumber.charAt(i));

			// System.out.print(inputNumber.charAt(i));

			if (pos != -1) {
				// If digit is found in string then continue to check if the location of digit
				// fit the location of the digit in number to guess

				if (inputNumber.charAt(i) == numberToGeuss.charAt(i)) {
					countDigitsInPlace = countDigitsInPlace + 1;
				} else {
					countScoredDigits = countScoredDigits + 1;
				}

			}

		}

		/*
		 * UPDATING THE RESUTS IN CURRENT GAME OBJECT. IF ALL DIGITS ARE IN PLACE
		 * TURNING THE WIN FLAG TO TRUE
		 */
		currentGame.setCountDigitsInPlace(countDigitsInPlace);
		currentGame.setCountDigitsNotInPlace(countScoredDigits);
		currentGame.setGuessesCounter();

		if (countDigitsInPlace == DIGITSCOUNT) {
			currentGame.setWin(true);
			currentGame.setGameOver(true);
		}
	}

	public void increaseGuessesCounter(CurrentGame currentGame) {
		currentGame.setGuessesCounter();
	}

	@Override
	public Long addCurrentTopScore(TopScoresEntity newCurrentScore) {
		return topScoresService.addTopScore(newCurrentScore);
	}

	@Override
	public String deleteAllTopScores(String password) {
		if (!password.equals(ADMINPASSWORD)) {
			return "You can not clear the table, you are not authorized!";
		}
		topScoresService.delete();
		return "You have cleared the table";
	}

}
