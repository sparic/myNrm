<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.myee.niuroumian.controller.WebWxController" %>
<%@ page import="com.myee.niuroumian.response.WeixinCfg" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>


<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>牛肉面</title>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript">
        <%

    WebWxController wwc = new WebWxController();
    WeixinCfg cfg = wwc.weixinCfg(request);
    System.out.println(cfg.getAppId());

%>
        wx.config({
            debug: false,
            appId:"<%=cfg.getAppId() %>" ,
            timestamp: <%=cfg.getTimestamp() %>,
            nonceStr: "<%=cfg.getNonceStr() %>",
            signature: "<%=cfg.getSignature() %>",
            jsApiList: [
                'onMenuShareTimeline',
                'onMenuShareAppMessage'
            ]
        });
        wx.ready(function () {
            wx.onMenuShareAppMessage({
                title: '河南红烧牛肉面',
                desc: '河南红烧牛肉面',
                link: 'http://pay.myee7.com/nrm',
                imgUrl: 'http://pay.myee7.com/nrm/skin/images/menu/1.jpg',
                trigger: function (res) {
                    alert('用户点击发送给朋友');
                },
                success: function (res) {
                    alert('已分享');
                },
                cancel: function (res) {
                    alert('已取消');
                },
                fail: function (res) {
                    alert(JSON.stringify(res));
                }
            });


            wx.onMenuShareTimeline({
                title: '河南红烧牛肉面',
                link: 'http://pay.myee7.com/nrm',
                imgUrl: 'http://pay.myee7.com/nrm/skin/images/menu/1.jpg',
                trigger: function (res) {

                },
                success: function (res) {
                    alert('已分享');
                },
                cancel: function (res) {
                    alert('已取消');
                },
                fail: function (res) {
                    alert(JSON.stringify(res));
                }
            });
        });
    </script>
</head>
<body>

<span>Hello !ffff<%=cfg.getAppId() %></span>
</div>
</body>