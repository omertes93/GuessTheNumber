package com.omer.guessthenumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopScoresServiceImpl implements TopScoresService {

	@Autowired
	private TopScoresRepository dao;

	@Override
	public Iterable<TopScoresEntity> fetchAllGames() {
		return dao.findAll();
	}

	@Override
	public Long addTopScore(TopScoresEntity newScore) {
		return dao.save(newScore).getId();
	}

	@Override
	public TopScoresEntity findTop1ByOrderByAttemptscountDesc() {
		return dao.findTop1ByOrderByAttemptscountDesc();
	}

	@Override
	public void delete(Long id) {
		//DELETE ONE RECORD BY ID
		dao.delete(id);

	}
	
	@Override
	public void delete() {
		//DELETE ALL RECORDS
		dao.deleteAll();
		
	}

	@Override
	public int countScores() {
		return dao.getCountId();
	}

	

}
