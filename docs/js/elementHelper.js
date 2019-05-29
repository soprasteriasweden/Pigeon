var setColoredBackground = true;

function showAndEnableButton(){
	$("#hidden-disabled-button").css("display","inline");
	setTimeout(function(){
		$("#hidden-disabled-button").removeAttr("disabled")
	},500);
}

function hideElement(id){
	$("#"+id).css("visibility","hidden");
}

function switchtotextinput(){
	$("#inputelement").attr("type","text");
}

function showNestedElements(){
	$('.child-ul').css('display','inline-block');
}

var backgroundStriped = false;

function hideAfterTimeout(id, milliseconds){
setTimeout(function(){hideElement(id);},milliseconds);
}

function showAfterTimeout(id, milliseconds){
setTimeout(function(){showElement(id);},milliseconds);
}

function showAfterTimeoutByClass(className, milliseconds){
	setTimeout(function(){showElementByClass(className);},milliseconds);
	}

function removeAfterTimeout(id, milliseconds){
setTimeout(function(){removeElement(id);},milliseconds);	
}
function insertTextAfterTimeout(id, milliseconds,textToInsert){
setTimeout(function(){insertText(id,textToInsert);},milliseconds);
}

function setStripedBackground(milliseconds){
	setTimeout(function(){

	if (backgroundStriped){
		$("#striped-div").css("background-image", "");  
		$("#striped-div").css("background-size", "");
	} else {
	$("#striped-div").css("background-image", "url(../img/stripes.jpg)");  
	$("#striped-div").css("background-size", "contain");
	}
	backgroundStriped = !backgroundStriped;	
	
	},milliseconds);
}

function startRotation(){
	$("#rotatingimage").addClass('imageanimation');
}

function resetColors(){
	$(".colorexample").css('background-color','rgb(0,0,0)');
}

function insertText(id, textToInsert){
insertText(id,textToInsert);
}

function showElement(id){
	$("#"+id).css("display","inline-block");
}
function showElementByClass(className){
	$("."+className).css("display","inline-block");
}
function removeElement(id){
	$("#"+id).remove();
}

function changeBackgroundColorAfterTimeOut(id, color,timeout){
	setTimeout(function(){changeBackgroundColor(id,color);},timeout);
}


function changeBackgroundColor(id,color){
	$("#"+id).css("background-color",color);
}

function insertText(id, string){
$("#"+id).html(string);
}
function removeButton() {
    $("#delayedremovedelement").remove();
}

function showAfterCustomTimeOut(timeout){
setTimeout(function(){hideElement(id);},milliseconds);
}

function appendButton(){
   $("#elementsvisibility-second").append( "<button id='createdelement' type='button' class='btn btn-warning col-xs-6'>#createdelement</button><br>" );
}

function setBlackBackground(){
	$("#headernav").css('background-color','black');
	$("#page-wrapper").css('background-color','black');
	$("body").css('background-color','black');
}

function setWhiteBackground(){
	$("#headernav").css('background-color','white');
	$("#page-wrapper").css('background-color','white');
	$("body").css('background-color','white');
}

function appendandhideButton(){
$("#elementsvisibility").append( "<button id='createdhiddenbutton' type='button' class='btn btn-danger'>#createdhiddenbutton</button>" );
$("#createdhiddenbutton").css("display","none");
$("#createandhidebuttons").append( "Knappen har skapats men är inte synlig" );
}






