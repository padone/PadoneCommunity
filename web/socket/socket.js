var userID = null;
var websocket = null;

function socket_init() {
    // check if webSocket is available
    if("WebSocket" in window){
        console.log("webSocket supported");
    }else{
        console.log("WebSocket not supported");
    }
}

function connectSocket(id) {

    console.log(id);
    userID = id;
    websocket = new WebSocket('ws://localhost:8080/PadoneCommunity/notice/' + userID);
    //websocket = new WebSocket('ws://140.121.196.23:3390/PadoneCommunity/notice' + userID);

    websocket.onopen = function (ev) {
        console.log("socket connected");
    };

    websocket.onmessage = function (ev) {
        console.log("message receive from server");
        // check message request type
        // single message, multiple message, notice number count ?
        var msg = JSON.parse(ev.data);
        console.table(msg);
        parseMessage(msg);
        // handle message posting
        // return json value
    };

    websocket.onerror = function (ev) {
        console.log("error occur");
    };

    websocket.onclose = function (ev) {
        console.log("onclose triggered in client");
        cleanup();
        var reason = (ev.reason && ev.reason != null) ? ev.reason : 'Bye';
        console.log(reason);
    };

}

function send(request, content, target) {
    var message = createMessage(userID, request, content, target);
    console.log("send this: " + JSON.stringify(message));
    websocket.send(JSON.stringify(message));
}

function createMessage(senderID, req, cont, target){
    return {
        noticeID : 0,
        recipientID : target,
        request : req,
        content : cont,
        time : "temp",
        checked : false,
        senderID : senderID
    };
    // noticeID, time are create by server
}

function parseMessage(message){
    var request = message.request;
    var content;
    switch (request) {
        case 'unread_message':
            // multiple message
            content = JSON.parse(message.content);
            console.log(content.length + " messages received");
            for(var i=0; i< content.length; i++){
                console.table(i);
            }
            break;
        default:
            // single message
            content = message;
            console.table(message);
    }
    receiver(content);
}

function cleanup() {
    userID = null;
    websocket = null;
}

function closeConnection(event){
    console.log("close socket connection to server");
    websocket.close();
    cleanup();
}

/*
    design of notification data table
{
    "noticeID": ,        int       as title, the id
    "recipientID": "",   varchar   id of the guy who will get this notice
    "request": "",       varchar   what is this purpose
    "content": "",       varchar   detail
    "time": "",          varchar   when did this notification send
    "checked": "",       boolean   if this notification was checked or not
    "senderID": ""       varchar   optional, another guy's id if need to send notification from user a to b
}
 */