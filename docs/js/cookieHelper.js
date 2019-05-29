$(document).ready(function () {
    isUserLoggedIn();
});

var timer;
var countDownMilliSeconds = 10000;
var countDownClock;	
function isUserLoggedIn(){
	var cookie = getCookie("loggedin");
	if (cookie!=null){
    logIn();
    setClickEventListener();
    }
}
		
function resetTimer() {
	clearAndStartTimer();
	displayLoggedInMessage();
}

function logOut() {
	hideLoggedInMessage();
	removeCookie();
	removeClickEventListener();
}

function logIn(){
	displayLoggedInMessage();
	startTimer();
	setClickEventListener();
	saveCookie();
}

function printCookie(message){
	alert(message + document.cookie)
}

function removeCookie(){
var expireString = setTime(-10);
document.cookie = "loggedin=true"+expireString+"; path=/";
}

function saveCookie(seconds){
	var expireString = setTime(seconds);
	document.cookie = "loggedin=true"+expireString+"; path=/";
}

function startTimer(){
	timer = setTimeout(logOut,countDownMilliSeconds);
	
}

function clearAndStartTimer(){
	clearTimeout(timer);
	startTimer();
}
		
	function setClickEventListener(){
		document.addEventListener("click", resetTimer);
	}
	function removeClickEventListener(){
		document.removeEventListener("click", resetTimer)
	}
	function displayLoggedInMessage(){
		$("#loggedinmessage").css('display','inline-block')
	}
	
	function hideLoggedInMessage(){
		$("#loggedinmessage").css('display','none')
	}
	
function getCookie(key){
	var cookies = document.cookie;
	var name = key + "=";
	var beginningIndex = cookies.indexOf(";"+name);
	if(beginningIndex == -1){
		beginningIndex = cookies.indexOf(name);
		if(beginningIndex != 0) return null;
	} else {
		beginningIndex +=2;
		var endingIndex = cookies.indexOf(";", beginningIndex);
		if (end == -1){
			endingIndex = cookies.length;
		}
	}
	return decodeURI(cookies.substring(beginningIndex + name.length, endingIndex));
}

function setTime(seconds){
	var date = new Date();
	date.setTime(date.getTime()+(seconds*1000));
	var expiresString = "; expires="+date.toGMTString();
	return expiresString;
}



