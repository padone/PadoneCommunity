var userID = null;
var websocket = null;

function init() {
    // check if webSocket is available
    if("WebSocket" in window){
        console.log("webSocket supported");
    }else{
        console.log("WebSocket not supported");
    }
}

function connectSocket(id) {

    userID = id;
    websocket = new WebSocket('ws://localhost:8080/PadoneCommunity/notice' + userID);
    //websocket = new WebSocket('ws://140.121.196.23:3390/PadoneCommunity/notice' + userID);

    websocket.onopen = function (ev) {
        console.log("socket connected");
    };

    websocket.onmessage = function (ev) {
    };

    websocket.onerror = function (ev) {
    };

    websocket.onclose = function (ev) {
    };

}

function send() {
}

function createMessage(senderID, msg, cont, target){
    return {
        noticeID : 0,
        recipientID : target,
        request : msg,
        content : cont,
        time : "temp",
        checked : false,
        senderID : senderID
    };
}

function parseMessage(){
}

function quit() {
}