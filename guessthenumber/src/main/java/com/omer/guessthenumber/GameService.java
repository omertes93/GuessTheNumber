package com.omer.guessthenumber;

public interface GameService {

	public String getRandomAlphaNumeric();

	public Long createIdForNewGame();

	public void checkforscores(CurrentGame currentGame, String newGuess);

	public CurrentGame addNewCurrentGame(String fullName) throws Throwable;

	public void deleteCurrentGameFromRepository(Long key);

	public CurrentGame checkNewGuess(Long id, String guess) throws Throwable;

	public Iterable<TopScoresEntity> fetchAllGames();

	public Long addCurrentTopScore(TopScoresEntity newCurrentScore);

	public String deleteAllTopScores(String password);

}
