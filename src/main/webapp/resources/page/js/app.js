/**
 * 
 */

var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    /*if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
    */
}

function connect() {
    var socket = new SockJS('/boards');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/board/' + boardId, function (message) {
            showGreeting(message);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/board/" + boardId, {}, JSON.stringify({'name': $("#message").val()}));
}

function showGreeting(message) {
    $("#messages").append("<li>" + message + "</li>");
}

$(function () {
    /*$("form").on('submit', function (e) {
        e.preventDefault();
    });*/
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
