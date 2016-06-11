<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>注册</title>
   <%-- <script type="text/javascript" src="/assets/sockjs/sockjs.js"></script>
    <script type="text/javascript" src="/assets/stomp/stomp.js"></script>--%>
    <style type="text/css">
        input#chat {
            width: 410px
        }

        #console-container {
            width: 1000px;
        }

        #console {
            border: 1px solid #CCCCCC;
            border-right-color: #999999;
            border-bottom-color: #999999;
            height: 800px;
            overflow-y: scroll;
            padding: 5px;
            width: 100%;
        }

        #console p {
            padding: 0;
            margin: 0;
        }
    </style>
    <script type="text/javascript">
        var Chat = {};

        Chat.socket = null;
        Chat.connect = (function(host) {
            if ('WebSocket' in window) {
                Chat.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                Chat.socket = new MozWebSocket(host);
            } else {
                alert('Error: WebSocket is not supported by this browser.');
                return;
            }

            Chat.socket.onopen = function () {
                Console.log('Info: WebSocket connection opened.');
                document.getElementById('chat').onkeydown = function(event) {
                    if (event.keyCode == 13) {
                        Chat.sendMessage();
                    }
                };
            };

            Chat.socket.onclose = function () {
                document.getElementById('chat').onkeydown = null;
                Console.log('Info: WebSocket closed.');
            };

            Chat.socket.onmessage = function (message) {
                var test = message.data;
                Console.log(test);
            };
        });

        Chat.initialize = function() {
            if (window.location.protocol == 'http:') {
//                Chat.connect('ws://' + window.location.host + '/order/orderOnline');
                Chat.connect('ws://pay.myee7.com/nrm/order/orderOnline');
            } else {
//                Chat.connect('wss://' + window.location.host + '/order/orderOnline');
                Chat.connect('ws://pay.myee7.com/nrm/order/orderOnline');
            }
        };

        Chat.sendMessage = (function() {
            var message = new Object();
            message.messageType = "REGISTER";
            message.body="";
            message.userId="";
            message.shopId="233331";
            Chat.socket.send(JSON.stringify(message));
        });


        var Console = {};
        Console.log = (function(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            while (console.childNodes.length > 25) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        });

        document.addEventListener("DOMContentLoaded", function() {
            // Remove elements with "noscript" class - <noscript> is not allowed in XHTML  
            var noscripts = document.getElementsByClassName("noscript");
            for (var i = 0; i < noscripts.length; i++) {
                noscripts[i].parentNode.removeChild(noscripts[i]);
            }
        }, false);

        window.onload =  Chat.initialize();
//        window.setInterval("Chat.sendMessage()",60000);

    </script>
</head>
<body>
<div class="noscript"><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websockets rely on Javascript being enabled. Please enable
    Javascript and reload this page!</h2></div>
<div>
    <p>
        <input type="text" placeholder="请输入内容:" id="chat" />
    </p>
    <div id="console-container">
        <div id="console"/>
    </div>

    <input type="button" value="注册" onclick=" Chat.sendMessage()"/>
</div>
</body>