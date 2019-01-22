package com.omer.guessthenumber;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class GamesServiceDao {

	private Map<Long, CurrentGame> gamesMap = new HashMap<Long, CurrentGame>();

	public Map<Long, CurrentGame> getGamesMap() {
		return gamesMap;
	}

	public void setGamesMap(Map<Long, CurrentGame> gamesMap) {
		this.gamesMap = gamesMap;
	}

}
