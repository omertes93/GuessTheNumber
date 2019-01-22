package com.omer.guessthenumber;

public interface TopScoresService {

	public Iterable<TopScoresEntity> fetchAllGames();

	public Long addTopScore(TopScoresEntity newGame);

	public TopScoresEntity findTop1ByOrderByAttemptscountDesc();

	public void delete(Long id);
	
	public void delete();
	
	public int countScores();

}
