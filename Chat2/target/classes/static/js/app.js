var ws;

$(document).ready(function () {

    $("#join").click(function() {
        sendData();
    });

})
ws = new WebSocket('ws://localhost:8081/user');

function sendData() {
    var data = JSON.stringify({
        'username': $("#username").val()
    })
    ws.send(data);
}
