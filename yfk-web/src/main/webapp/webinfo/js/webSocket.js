var esm = '';
var type = 1;
var socketPort = 30003;
var socketUrl = ''

$(function () {
    initPage()
    init()
})

function initPage() {
    $('#1001').click(function () {
        let status = $('#1001').data("status")
        if (status === "stop") {
            window.CHAT.chat("1001", 1);
        } else {
            window.CHAT.chat("1001", 2);
        }

    });

}


function init() {
    let host = document.location.hostname;
    socketUrl = 'ws://' + host + ':' + socketPort + '/ws?type=' + type
    if (document.location.href.indexOf("https") > -1) {
        socketUrl = 'wss://' + host + ':' + socketPort + '/ws?type=' + type
    }
    CHAT.init()
}

// websocket的方式连接后台
window.CHAT = {
    socket: null,
    init: function () {
        if (window.WebSocket) {
            // ws://机器地址:netty绑定的端口/服务端定义socket路径
            console.log(socketUrl)
            CHAT.socket = new WebSocket(socketUrl)
            CHAT.socket.onopen = function () {
                console.log('连接成功')
                //后端每60秒检测一次，这里只要小于60秒就行了
                setInterval("CHAT.keepalive()", 55000);
            }
            CHAT.socket.onclose = function () {
                console.log('连接关闭')
            }
            CHAT.socket.onerror = function () {
                console.log('异常')
            }
            CHAT.socket.onmessage = function (e) {
                console.log(e.data)
                let dt = JSON.parse(e.data)
                if(dt.action === 1){
                    $('#'+dt.esm).css("background-color", "#1E9FFF")
                    $('#'+dt.esm).data("status", "start")
                } else {
                    $('#'+dt.esm).css("background-color", "#FF5722")
                    $('#'+dt.esm).data("status", "stop")
                }
            }
        } else {
            alert('浏览器不支持websocket协议.....')
        }
    },
    chat: function (esm, status) {
        var data = {}
        data.type = 1
        data.esm = esm
        data.action = status
        CHAT.socket.send(JSON.stringify(data));
    },
    keepalive: function () {
        // 发送心跳
        CHAT.socket.send("yfk888_ALIVE")
    },
    clean: function () {
        $('#content').html('')
    }
}



