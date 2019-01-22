package com.omer.guessthenumber;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopScoresRepository extends CrudRepository<TopScoresEntity, Long> {

	public TopScoresEntity findTop1ByOrderByAttemptscountDesc();
	
	public @Query("SELECT COUNT(t.id) from TopScoresEntity t")
	    int getCountId();

}
