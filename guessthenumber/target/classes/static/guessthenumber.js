'use strict';

var app = {
	baseURL : 'http://localhost:8081/rest/api/game'
};

var gameObj = {
	id : 0,
	fullName : null
};

var topScores = {
	fullname : '',
	attemptsCount : 0
};

function requestExe(elementId) {

	var xhttp = new XMLHttpRequest();
	xhttp.HEADERS_RECEIVED;

	switch (elementId) {
	// START A NEW GAME
	case 'btnStartNewGame':
		var fullName = element('txtfullname').value;

		if (fullName.length == 0) {
			setMsg("Please insert full name");
			return false;
		}

		setMsg("Please enter 4 digits between 0 to 9");

		// Get Id for new game, and sending the full name
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {

				var obj = JSON.parse(this.responseText);
				if (this.responseText.indexOf("error") == -1) {
					gameObj.id = obj.result['id'];
					gameObj.fullName = obj.result['fullName'];
					showResultsTable();
				} else {
					setMsg("An error occurred " + obj.error['message']
							+ ", Please report to site admin")
				}

			}
		};
		xhttp.open("POST", app.baseURL + '/newgame', true);
		xhttp.setRequestHeader("Content-type", "application/json");
		xhttp.send(fullName);

		show(element('guessDiv'));

		break;

	case 'btnCheck':
		// GET THE USER GUESS NUMBER AND
		// SEND TO SERVER TO GET AN ANSWER HOW
		// MANY SCORES
		var userGuess = element('txtNumber').value;

		if (userGuess.length < 4) {
			setMsg("Please - must enter 4 digits");
			return

		}
		xhttp.onreadystatechange = function() {
			if (this.readyState === 4 && this.status == 200) {

				var obj = JSON.parse(this.responseText);
				// log(this.responseText);
				if (element("divResultsTable").style.visibility = 'hidden') {
					show(element("divResultsTable"));
				}
				if (this.responseText.indexOf("error") == -1) {
					addRawToResultsTable(obj.result['countDigitsInPlace'],
							obj.result['countDigitsNotInPlace'], userGuess,
							obj.result['attemptsCount']);

					if (obj.result['gameOver'] == true) {
						showTopScoresTable();
						disableEnableCheckButton();
						if (obj.result['win'] == true) {
							setMsg("congratulations! ! you have won the game");
						} else {
							setMsg("no win :( ! please try again");
							resetGameBoard();
						}
						show(element('divClear'));
					}

				} else {
					// log(obj.error['cause']);
					// log(obj.error['message']);
					setMsg("An error occurred " + obj.error['message']
							+ ", Please report to site admin")
				}

			}
		}

		xhttp.open("GET", app.baseURL + "/getuserguessscores/" + gameObj.id
				+ "/" + userGuess, true);
		xhttp.setRequestHeader("Content-type", "application/json");
		xhttp.send();

		break;

	case 'btnClearTopScoreTable':
		// IF ADMIN PASS IS CORRECT THEN DELETE ALL TOP SCORES FROM TABLE
		var adminPass = prompt("Please enter your password", "Admin password");

		if (adminPass != null) {
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					var strResult = this.responseText;
					var msg = strResult.substring(strResult.indexOf(":") + 2,
							strResult.indexOf("}") - 1)
					setMsg(msg);
					showTopScoresTable();
				}
			};
			xhttp.open("DELETE", app.baseURL
					+ "/deletealltopscorestable?password=" + adminPass, true);
			xhttp.setRequestHeader("Content-type", "application/json");
			xhttp.send();
		}
		break;
	}

}

function showTopScoresTable() {
	// gets all the records from top score table and show them in table
	if (element("divHighScoresTable").style.visibility = 'hidden') {
		show(element("divHighScoresTable"));
	}
	
	var xhttp = new XMLHttpRequest();
	xhttp.HEADERS_RECEIVED;
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var obj = JSON.parse(this.responseText);

			// log('json: ' + obj.length);
			if (this.responseText.indexOf("error") == -1) {
				var t = '';
				t += '<table>';
				t += '<tr>';
				t += '<th>#</th>';
				t += '<th>Name</th>';
				t += '<th>Number Of Guesses</th>';
				t += '</tr>';
				for (var i = 0; i < obj.result.length; i++) {
					const topScore = obj.result[i];
					t += "<tr>";
					t += "<td>" + (i + 1) + "</td>";
					t += "<td>" + topScore.fullname + "</td>";
					t += "<td>" + topScore.attemptscount + "</td>";
					t += "</tr>";
				}
				t += '</table>';
				element('divHighScoresTable').innerHTML = '<p><h1>Top 10 High Scores</h1></p>'
						+ t;
			} else {
				setMsg("An error occurred " + obj.error['message']
						+ ", Please report to site admin")
			}
		}
	};

	xhttp.open('GET', app.baseURL + '/all', true);
	xhttp.send();
}

function showResultsTable() {
	// Showing the results table headers when the game starts and after clearing
	// the board
	var t = '';
	t += '<table id=resultsTableId class=table-title>';
	t += '<tr>';
	t += '<th>#</th>';
	t += '<th>Guess</th>';
	t += '<th>Result</th>';
	t += '</tr>';
	t += '</table>';
	element('divResultsTable').innerHTML = t;
}

function addRawToResultsTable(numOfDigitsInPlace, NumOfDigitsNotInPlace,
		userGuess, counter) {
	var table = document.getElementById("resultsTableId");
	var row = table.insertRow(1);

	var cell1 = row.insertCell(0);
	var cell2 = row.insertCell(1);
	var cell3 = row.insertCell(2);

	var t = '';
	for (var j = 0; j < numOfDigitsInPlace; j++) {
		t += ' <img src=images/digitsInPlace.png> ';
	}
	for (j = 0; j < NumOfDigitsNotInPlace; j++) {
		t += ' <img src=images/digitsNotInPlace.png> ';
	}
	cell1.innerHTML = counter;
	cell2.innerHTML = userGuess;
	cell3.innerHTML = t;

	element('txtNumber').value = '';

}

function hide(e) {
	e.style.visibility = 'hidden';
}

function show(e) {
	e.style.visibility = 'visible';
}

function element(elementId) {
	return document.getElementById(elementId);
}

function log(item) {
	console.log(item);
}

function setMsg(sMsg) {
	document.getElementById('msg').innerHTML = '<h2>' + sMsg + '</h2>';
}

function isNumber(evt) {
	// This function doesnt let the user insert chars that are not digits
	var iKeyCode = (evt.which) ? evt.which : evt.keyCode
	if (iKeyCode != 46 && iKeyCode > 31 && (iKeyCode < 48 || iKeyCode > 57)) {
		setMsg('Please insert only digits between 0 to 9')
		return false;
	}

	let num = element('txtNumber').value;
	if (num.length >= 4) {
		setMsg('Please insert only 4 digits')
		return false;
	}
	return true;
}

function isOnlyChars(event) {
	// // This function let the user insert only chars
	if ((event.keyCode > 64 && event.keyCode < 91)
			|| (event.keyCode > 96 && event.keyCode < 123)
			|| event.keyCode == 8) {
		return true;
	}

	else {
		setMsg('Please insert only chars')
		return false;
	}

}

function disableEnableCheckButton() {
	var btn = element('btnCheck');
	if (btn.disabled == true) {
		btn.disabled = false
	} else {
		btn.disabled = true;
	}
}

function resetGameBoard() {
	hide(element('guessDiv'));
	hide(element('divResultsTable'));
	hide(element('divHighScoresTable'));
	hide(element('divClear'));
	element('divHighScoresTable').innerHTML = ''
	element('divResultsTable').innerHTML = ''
	disableEnableCheckButton();
}
