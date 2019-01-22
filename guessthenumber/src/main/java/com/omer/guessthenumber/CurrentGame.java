package com.omer.guessthenumber;

import java.lang.Cloneable;

public class CurrentGame implements Cloneable {
	private Long id;
	private String fullName;
	private String numberToGuess;
	private String userGuess;
	private int countDigitsInPlace = 0;
	private int countDigitsNotInPlace = 0;
	private int attemptsCount = 0;
	private boolean isGameOver=false;
	private boolean isWin = false;

	public int getAttemptsCount() {
		return attemptsCount;
	}

	public void setGuessesCounter() {
		this.attemptsCount++;
	}

	public CurrentGame(Long key, String numberToGuess, String fullName) {
		this.id = key;
		this.numberToGuess = numberToGuess;
		this.fullName=fullName;
	}

	public Long getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullname) {
		this.fullName = fullname;
	}

	public String getNumberToGuess() {
		return numberToGuess;
	}

	public void setNumberToGuess(String numberToGuess) {
		this.numberToGuess = numberToGuess;
	}

	public int getCountDigitsInPlace() {
		return countDigitsInPlace;
	}

	public void setCountDigitsInPlace(int countDigitsInPlace) {
		this.countDigitsInPlace = countDigitsInPlace;
	}

	public int getCountDigitsNotInPlace() {
		return countDigitsNotInPlace;
	}

	public void setCountDigitsNotInPlace(int countScoredDigits) {
		this.countDigitsNotInPlace = countScoredDigits;
	}

	public boolean isWin() {
		return isWin;
	}

	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}
	public String getUserGuess() {
		return userGuess;
	}

	public void setUserGuess(String userGuess) {
		this.userGuess = userGuess;
	}

	protected Object clone() throws CloneNotSupportedException {
		return super.clone();  
	}
}
