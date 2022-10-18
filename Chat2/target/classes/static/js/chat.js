var ws;
$(document).ready(function () {
    ws = new WebSocket('ws://localhost:8081/user');

    function sendData() {
        var data = JSON.stringify({
            'text': $("#text").val()
        })
        ws.send(data);
    }


    function sendMessage() {
        $("#message").append("<br><p style='padding:0px 0px 0px 2px; border: solid 1px #00FF00; background:#00FF7F ;color: #080808 ;' dir='rtl';>" + $("#text").val() + "</p>")
        $("#text").val('');

    }


    function getMessage(message) {
        $("#message").append("<p  style='padding:0px 0px 0px 2px; border: solid 1px #008000; background:#00FF7F;color: #080808;'dir='ltr';>" + message + "</p>");

    }

    $("form").on("submit", function (e) {
        e.preventDefault();
    });
    setTimeout(function () {
        ws.onmessage = function (data) {
            getMessage(data.data);
        }
    }, 1000);

    $("#send").click(function () {
            if ($('#text').val() == '') {
            } else {
                sendData();
                sendMessage();
            }
        }
    );
});