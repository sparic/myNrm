package com.myee.niuroumian.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.myee.niuroumian.domain.OrderInfo;
import com.myee.niuroumian.domain.OrderState;
import com.myee.niuroumian.domain.PayState;
import com.myee.niuroumian.domain.TTradingOrders;
import com.myee.niuroumian.dto.ClientAjaxResult;
import com.myee.niuroumian.dto.OrderRelDto;
import com.myee.niuroumian.protocol.Message;
import com.myee.niuroumian.protocol.MessageType;
import com.myee.niuroumian.response.WeixinCfg;
import com.myee.niuroumian.service.UserService;
import com.myee.niuroumian.server.OrderServer;
import com.myee.niuroumian.service.OrderService;
import com.myee.niuroumian.service.WeixinService;
import com.myee.niuroumian.util.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.*;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.popular.api.*;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ray.Fu on 2016/6/1.
 */
@Controller
@RequestMapping("wxitf")
public class WebWxController {
    private Logger logger = LoggerFactory.getLogger(WebWxController.class);
    private static final String wpSite = "http://www.myee7.com/biplus";
    //要返回的模板消息类型
    private static final int KIND_PAY_SUCCESS = 1;//付款成功
    private static final int KIND_SIMPLE_MENU = 2;//模板菜单

    @Autowired
    private WeixinService weixinService;//

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    protected WxMpConfigStorage wxMpConfigStorage;

    @Autowired
    private WxMpMessageRouter wxMpMessageRouter;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderServer orderServer;

    //用户的openId
    private static String openId;
    //保存公众号的Id
    private static String officialAccountId;

    @RequestMapping(value = "service")
    @ResponseBody
    public AjaxResultObj service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WeixinCfg cfg = new WeixinCfg();
        logger.info("微信公众号请求信息");
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = resp.getWriter();
        String signature = req.getParameter("signature");
        if(signature!=null){
            Constant.signature = signature;
            System.out.println("===================signature:"+signature);
        }
        String nonce = req.getParameter("nonce");
        if(nonce!=null){
            Constant.nonceStr = nonce;
            System.out.println("================nonce:"+nonce);
        }
        String timestamp = req.getParameter("timestamp");
        if(timestamp!=null){
            Constant.timestamp = timestamp;
            System.out.println("=====================timestamp:"+timestamp);
        }
        String echostr = req.getParameter("echostr");
        System.out.println("=================echostr:"+echostr);

        // 默认返回的文本消息内容
        String respContent = "请求处理异常，请稍候尝试！";
        try {
            if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
                out.print("非法请求");
            } else if (StringUtils.isNotBlank(echostr)) {
                out.write(echostr);
            }
            Map map = new HashMap();
            String encryptType = StringUtils.isBlank(req.getParameter("encrypt_type")) ? "raw" : req.getParameter("encrypt_type");
            if ("raw".equals(encryptType)) {
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(req.getInputStream());
                openId = inMessage.getFromUserName();
                officialAccountId = inMessage.getToUserName();
                System.out.println("扫码获取的信息->" + inMessage.toString());
                //已关注
                if(inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent() != null && inMessage.getEvent().equals(WxConsts.EVT_SCAN)) {
                    if(inMessage.getEventKey() != null && inMessage.getEventKey().trim().length() > 0) {
                        String url = "";
                        //获取快速订餐的菜单
//                        System.out.println("进这里1->" + inMessage.getEventKey());
                        map = quickOrderMenu(Long.valueOf(inMessage.getEventKey()));
//                        System.out.println("进这里2->" + map.get("dishList"));
                        map.put("openId", openId);
                        map.put("url", url);
                    } else {

                    }

                }
                //TODO 点击事件
                //点击按钮
                if(inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent()!= null && inMessage.getEvent().equals(WxConsts.EVT_CLICK)) {
                    String url = "";
                    //获取快速订餐的菜单
                    map = quickOrderMenu(1L);
                    map.put("openId", openId);
                    map.put("url", url);
                    if(inMessage.getEventKey().equals("V1001_QUERY_TEXT")){
                        pushModelMessage(KIND_PAY_SUCCESS,12345678,"河南牛肉拉面(定威路协和路店)");//1代表支付成功后的模板消息
                    }
                }
                //微信自带的扫二维码，未关注
                if(inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent() != null && inMessage.getEvent().equals(WxConsts.EVT_SUBSCRIBE) && inMessage.getTicket() != null) {
                    if(inMessage.getEventKey() != null && inMessage.getEventKey().trim().length() > 0) {
                        String url = "";
                        //获取快速订餐的菜单
//                        System.out.println("进这里1->" + inMessage.getEventKey());
                        map = quickOrderMenu(Long.valueOf(inMessage.getEventKey().substring(8,inMessage.getEventKey().length()-1)));
//                        System.out.println("进这里2->" + map.get("dishList"));
                        map.put("openId", openId);
                        map.put("url", url);
                    } else {

                    }
                }
                saveUserInfo(openId);
                inMessage.setMap(map);
                WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
                String str = outMessage.toXml();
                out.print(str);
            }else if ("aes".equals(encryptType)) {
                String msgSignature = req.getParameter("msg_signature");
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(req.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
                WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
                out.write(outMessage.toEncryptedXml(wxMpConfigStorage));
            } else {
                logger.error("不可识别的加密类型");
            }
            cfg.setOpenId(openId);
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            out.close();
        }
        return AjaxResultObj.success(cfg,"获取微信用户信息成功！");
    }



    /**
     * 根据openId保存用户信息
     * @param openId
     * @return
     */
    public boolean saveUserInfo(String openId) {
        String lang = "zh_CN"; //语言
        try {
            WxMpUser user = wxMpService.userInfo(openId, lang);
            System.out.println("user=========="+user);
            userService.saveOrUpdate(user);
            System.out.println("保存成功!");
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 快速点餐菜单推送
     * @return
     */
    public Map quickOrderMenu(Long storeId) {
        Map map = weixinService.findQuickOrderMenu(storeId);
        return map;
    }

    /**
     * 快速点餐菜单推送1
     * @return
     */
    @RequestMapping(value = "quickPushMenu")
    @ResponseBody
    public Map quickOrderMenu2(Long storeId) {
        Map map = weixinService.findQuickOrderMenu(storeId);
        return map;
    }

    /**
     * 发送模板消息
     * @param map
     */
    public void sendModelMsg(Map map) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setToUser(map.get("openId").toString());
        templateMessage.setTemplateId(Constant.TEMPLEID_WELCOME);
        templateMessage.setUrl(map.get("url").toString());
        templateMessage.setTopColor("#36b2cc");
        templateMessage.getDatas().add(new WxMpTemplateData("keyword1", map.get("content1").toString(), "#36b2cc"));
        templateMessage.getDatas().add(new WxMpTemplateData("keyword2", map.get("content2").toString(), "#36b2cc"));

        try {
            wxMpService.templateSend(templateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送用户信息
     */
    @RequestMapping(value = "send_user_info")
    @ResponseBody
    public WxMpUser sendUserInfo() {
        String lang = "zh_CN"; //语言
        WxMpUser user = null;
        try {
            user = wxMpService.userInfo(openId, lang);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return  user;
    }

//
//    @RequestMapping("/home")
//    public SnsToken home(HttpServletRequest request, HttpServletResponse response) {
//        String code = request.getParameter("code");
//        System.out.println("===========code:" + code);
//        SnsToken snsToken = new SnsToken();
//        if (!"authdeny".equals(code) & code != null) {
//            snsToken = getSnsToken("wxe67244505b4041b6","ae3b4cd8a550fab663c90ab16d548579",code);
//        } else {
//
//        }
//
//        return snsToken;
//    }

    /**
     * <暂时无用>
     * 获取用户基本信息
     * @param access_token access_token
     * @param openid openid
     * @return User
     */
    public User getUserInfo(String access_token,String openid){
        User user = UserAPI.userInfo(access_token, openid);
        return user;
    }

    /**
     * *<暂时无用>
     * 获取access_token
     * @param appid appid
     * @param secret secret
     * @return Token
     */
    public Token getToken(String appid,String secret){
        Token token = TokenAPI.token(appid, secret);
        return token;
    }

    /**
     * 通过code换取网页授权access_token
     * @param appid appid
     * @param secret secret
     * @param code code
     * @return SnsToken
     */
    public SnsToken getSnsToken(String appid,String secret,String code){
        SnsToken snsToken = SnsAPI.oauth2AccessToken(appid, secret, code);
        return snsToken;
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     * @param access_token access_token
     * @param openid openid
     * @param lang 国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return User
     */
    public User getUser(String access_token,String openid,String lang){
        User user = SnsAPI.userinfo(access_token, openid, lang);
        return user;
    }


//    @RequestMapping(value = "/weixinCfg")
//    @ResponseBody
//    public AjaxResultObj weixinCfg(HttpServletRequest req, HttpServletResponse resp) {
//        String  strBackUrl = "http://" + req.getServerName() +"/nrm";
//        System.out.println("===============strBackUrl:" + strBackUrl);
//        //获取Token
//        Token token = TokenAPI.token("wxe67244505b4041b6","ae3b4cd8a550fab663c90ab16d548579");
//        //获取Ticket
//        Ticket ticket = TicketAPI.ticketGetticket(token.getAccess_token());
//        System.out.println("===================ticket:" + ticket.getTicket());
//        Map<String, String> ret = Sign.sign(ticket.getTicket(), "http://pay.myee7.com/nrm");
//        WeixinCfg cfg = new WeixinCfg();
//        for (Map.Entry entry : ret.entrySet()) {
//            if(entry.getKey().equals("timestamp")){
//                cfg.setTimestamp(entry.getValue().toString());
//                System.out.println("===============timestamp:" + entry.getValue().toString());
//            }else if(entry.getKey().equals("nonceStr")){
//                System.out.println("===============nonceStr:"+entry.getValue().toString());
//                cfg.setNonceStr(entry.getValue().toString());
//            }else if(entry.getKey().equals("signature")){
//                System.out.println("===============signature:"+entry.getValue().toString());
//                cfg.setSignature(entry.getValue().toString());
//            }
//            cfg.setAppId("wxe67244505b4041b6");
//        }
//
//        try {
//            return AjaxResultObj.success(cfg,"获取微信用户信息成功！");
//
//        }  catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.getMessage(), e);
//            return AjaxResultObj.failed("糟了...系统出错了...");
//        }
//    }


    @RequestMapping(value = "/weixinCfg")
    @ResponseBody
    public WeixinCfg weixinCfg(HttpServletRequest req) {
//        String code = req.getParameter("code");
        System.out.println("++++++++++++++++++++++code:"+Constant.CODE);

        String basePath = "http://" + req.getServerName()+"/nrm"
                + req.getServletPath()      //请求页面或其他地址
                + "?" + (req.getQueryString()); //参数

        System.out.println("basePath======:"+basePath);
//        String  strBackUrl = "http://pay.myee7.com/nrm/index.jsp";
//        String  strBackUrl = "http://" + req.getServerName() +"/nrm?openid=";//http://pay.myee7.com/nrm/?openid=" + snsToken.getOpenid()
        System.out.println("===============strBackUrl:"+basePath);
        //获取Token
        Token token = TokenAPI.token("wxe67244505b4041b6","ae3b4cd8a550fab663c90ab16d548579");
        //获取Ticket
        Ticket ticket = TicketAPI.ticketGetticket(token.getAccess_token());
        System.out.println("===================ticket:"+ticket.getTicket());
        Map<String, String> ret = Sign.sign(ticket.getTicket(), basePath);
        WeixinCfg cfg = new WeixinCfg();
        for (Map.Entry entry : ret.entrySet()) {
            if(entry.getKey().equals("timestamp")){
                cfg.setTimestamp(entry.getValue().toString());
                System.out.println("===============timestamp:" + entry.getValue().toString());
            }else if(entry.getKey().equals("nonceStr")){
                System.out.println("===============nonceStr:"+entry.getValue().toString());
                cfg.setNonceStr(entry.getValue().toString());
            }else if(entry.getKey().equals("signature")){
                System.out.println("===============signature:"+entry.getValue().toString());
                cfg.setSignature(entry.getValue().toString());
            }
            cfg.setAppId("wxe67244505b4041b6");
        }
        cfg.setCode(Constant.CODE);

        try {
//            return AjaxResultObj.success(cfg,"获取微信用户信息成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
//            return AjaxResultObj.failed("糟了...系统出错了...");
        }
        return cfg;
    }

    /**
     * 支付成功回调函数
     * @param request
     */
    @ResponseBody
    @RequestMapping(value = "/busNoticeWeiXin", method = RequestMethod.POST)
    public void busNoticeWeiXin(HttpServletRequest request) {
        try {
            String inputLine;
            String notityXml = "";
            while ((inputLine = request.getReader().readLine()) != null) {
                notityXml += inputLine;
            }
            request.getReader().close();
            WeiXinPayResData weiXinPayResData = (WeiXinPayResData) ControllerUtil.getObjectFromXML(notityXml, WeiXinPayResData.class);
            String result_code = weiXinPayResData.getResult_code();


            String orderId = weiXinPayResData.getOut_trade_no();
            logger.info("================获取订单信息     orderId=    "+orderId);
            OrderInfo orderInfo = orderService.getOrderInfo(Long.parseLong(orderId));
            if(orderInfo.getPayState() != 1){ //未支付成功，执行下面操作
                Long shopId = orderInfo.getShopId();
                String userId = orderInfo.getUserId();

                int payState = "SUCCESS".endsWith(result_code)? PayState.SUCCESS.getValue():PayState.FAILED.getValue();
                orderInfo.setPayState(payState);
                int waitNo = orderService.generateWaitNo(shopId);
                orderInfo.setWaitNo(waitNo);
                orderInfo.setOrderState(OrderState.WAITING.getValue()); //更新订单状态
                Date date = new Date();
                orderInfo.setPayTime(new Timestamp(date.getTime()));
                orderService.createOrder(orderInfo); //保存订单
                //推送订单信息去到商户
                logger.info("================start  send websocket     ");
                logger.info("================orderInfo            "+JSON.toJSONString(orderInfo));
                List<OrderInfo> lists = new ArrayList<OrderInfo>();
                lists.add(orderInfo);
                Message message = new Message(MessageType.NOTIFICATION,lists,userId,String.valueOf(shopId));
                orderServer.handleMessage(message);
                logger.info("============================end  send websocket" );
                //往消费者推送排号消息
                // 设置消息的内容等信息
                /* 推送文本消息
                WxMpCustomMessage tempMessage = WxMpCustomMessage.TEXT().toUser(userId).content("您好，您的排号是：" + waitNo).build();
                wxMpService.customMessageSend(tempMessage);
                */
                //模板消息推送
                //TODO 模板消息推送
                pushModelMessage(KIND_PAY_SUCCESS,waitNo,"河南牛肉拉面(定威路协和路店)");
//                WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
//                templateMessage.setToUser(userId);
//                templateMessage.setTemplateId("wVnsBiJ-C03xz08DUlRTWKlL2373krKOr_TpVJWrRUk");
//                templateMessage.setUrl("http://pay.myee7.com/nrm/#/my");//点模板消息跳转链接
//                templateMessage.getDatas().add(new WxMpTemplateData("first", "取号成功", "#c37160"));
//                templateMessage.getDatas().add(new WxMpTemplateData("keyword1", waitNo+"号", "#36b2cc"));
//                templateMessage.getDatas().add(new WxMpTemplateData("keyword2", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "#ff9900"));
//                templateMessage.getDatas().add(new WxMpTemplateData("remark", "河南牛肉面欢迎您的到来!", "#053eea"));
//                wxMpService.templateSend(templateMessage);
//                weixinService.pushTokenToCustomer(waitNo,openId,officialAccountId);
            }
            logger.info("===========微信调用保存支付订单信息start~~~~weiXinPayResData.getOut_trade_no():"+weiXinPayResData.getOut_trade_no());
//            TTradingOrders tradingOrdersData = new TTradingOrders();
//            tradingOrdersData.setMerchantNo(weiXinPayResData.getMch_id());
//            tradingOrdersData.setOrderStatus(result_code.equals("SUCCESS") ? "SUCCESS" : "FAILURE");
//            tradingOrdersData.setPayAmount(String.valueOf(Double.valueOf(weiXinPayResData.getTotal_fee()) / 100));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
//            Date parse = sdf2.parse(weiXinPayResData.getTime_end());
//            String payTime2 = sdf.format(parse);
//            tradingOrdersData.setPayTime(payTime2);
//            tradingOrdersData.setPrepayId(weiXinPayResData.getPrepay_id());
//            tradingOrdersData.setRespMsg(weiXinPayResData.getReturn_msg());
//            tradingOrdersData.setSignData(weiXinPayResData.getSign());
//            tradingOrdersData.setTradingStatus(Byte.valueOf("1"));// '交易状态：1=支付成功;2=支付失败',
//            tradingOrdersData.setTransactionOrder(weiXinPayResData.getTransaction_id());
//            tradingOrdersData.setOpenId(weiXinPayResData.getOpenid());
//            tradingOrdersData.setOrdersNo(weiXinPayResData.getOut_trade_no());
//            System.out.println("============tradingOrdersData:"+tradingOrdersData);
//            Map<String, String> hm = new HashMap<String, String>();
//            hm.put("transactionOrder", weiXinPayResData.getTransaction_id());
            logger.info("===========微信调用保存支付订单信息end~~~~");
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("BackRcvResponse接收后台通知结束");
    }

    //推送模板消息
    private void pushModelMessage(int kind,int waitNo,String storeName) throws WxErrorException {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        if(kind == KIND_PAY_SUCCESS){
            System.out.println("######付款成功返回模板消息");
            templateMessage.setToUser(openId);
            templateMessage.setTemplateId("ufHCkB3PI-1z2cZyGCEZ-NfiOmCxQuh-CBFar4fIgUw");
            templateMessage.setUrl("http://pay.myee7.com/nrm/#/my");//点模板消息跳转链接
            templateMessage.getDatas().add(new WxMpTemplateData("first", "您好，订单支付成功！ "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "#c37160"));
            templateMessage.getDatas().add(new WxMpTemplateData("keyword1", storeName, "#36b2cc"));
            templateMessage.getDatas().add(new WxMpTemplateData("keyword2", waitNo+"号", "#36b2cc"));
//            templateMessage.getDatas().add(new WxMpTemplateData("keyword3", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "#ff9900"));
            templateMessage.getDatas().add(new WxMpTemplateData("keyword3", "--", "#36b2cc"));
            templateMessage.getDatas().add(new WxMpTemplateData("keyword4", "5分钟","#36b2cc"));
            templateMessage.getDatas().add(new WxMpTemplateData("keyword5", "排队中", "#36b2cc"));
            templateMessage.getDatas().add(new WxMpTemplateData("remark", "感谢您的使用!", "#053eea"));
            wxMpService.templateSend(templateMessage);
        }
        else if(kind == KIND_SIMPLE_MENU){

        }
    }
    /**
     * 根据用户openid获取微信用户
     * @param openid
     * @return
     */
    @RequestMapping(value = "/queryUserObj")
    @ResponseBody
    public AjaxResultObj queryUserObj(@RequestParam("openid") String openid) {
        logger.info("获取微信用户信息");
        try {
            if(org.apache.commons.lang3.StringUtils.isEmpty(openid)){
                return AjaxResultObj.failed("请输入openid");
            }
            Token token = getToken("wxe67244505b4041b6","ae3b4cd8a550fab663c90ab16d548579");
            User user = getUserInfo(token.getAccess_token(),openid);
            return AjaxResultObj.success(user,"获取微信用户信息成功！");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return AjaxResultObj.failed("糟了...系统出错了...");
        }
    }

    @RequestMapping(value = "/home")
    @ResponseBody
    public void userInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        Constant.CODE = code;
        String str =  req.getServletPath().toString();
        String shopId =str.substring((str.lastIndexOf("|")) + 1);
        System.out.println("shopId========:"+shopId);
        System.out.println("getOpen Id========code:"+code);
        Token token = getToken("wxe67244505b4041b6", "ae3b4cd8a550fab663c90ab16d548579");
        System.out.println("token========token:" + token.getAccess_token());
        SnsToken snsToken = SnsAPI.oauth2AccessToken("wxe67244505b4041b6", "ae3b4cd8a550fab663c90ab16d548579", code);
//        resp.sendRedirect("http://pay.myee7.com/nrm/");
        resp.sendRedirect("http://pay.myee7.com/nrm/index.jsp?openid=" + snsToken.getOpenid());
    }

    @RequestMapping(value = "create")
    @ResponseBody
    public ClientAjaxResult menuCreate() {
        try {
            WxMenu menu = new WxMenu();
            WxMenu.WxMenuButton button1 = new WxMenu.WxMenuButton();
            button1.setType(WxConsts.BUTTON_VIEW);
            button1.setName("立即下单");
            button1.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe67244505b4041b6&redirect_uri=http%3A%2F%2Fpay.myee7.com%2Fnrm%2Fwxitf%2Fhome.do&response_type=code&scope=snsapi_userinfo&state=#wechat_redirect");
            button1.setKey("V1002_QUERY_ORDER");

            WxMenu.WxMenuButton button2 = new WxMenu.WxMenuButton();
            button2.setType(WxConsts.BUTTON_CLICK);
            button2.setName("扫码点餐");
            button2.setKey("V1001_QUERY_TEXT");

            WxMenu.WxMenuButton button3 = new WxMenu.WxMenuButton();
            button3.setType(WxConsts.BUTTON_CLICK);
            button3.setName("个人中心");
            button3.setKey("V1003_QUERY_ME");

            menu.getButtons().add(button1);
            menu.getButtons().add(button2);
            menu.getButtons().add(button3);

            System.out.println(menu.toJson());
            wxMpService.menuCreate(menu);

        } catch (WxErrorException e) {
            logger.error(e.toString());
        }
        return ClientAjaxResult.success("菜单创建成功！");
    }

    /**
     * 扫码进入公众号快速菜单
     */
    @RequestMapping(value = "push_quickMenu")
    @ResponseBody
    public void pushShopQuickMenu(String shopId) {

    }

    /**
     * 生成商家二维码
     */
    @RequestMapping(value = "generate_qrPic")
    @ResponseBody
    public void generateQrCodePic() {
        try {
            // 生成永久ticket,商店ID->233331
            WxMpQrCodeTicket ticket = wxMpService.qrCodeCreateLastTicket("233331");
            // 获得一个在系统临时目录下的文件，是jpg格式的
            File file = wxMpService.qrCodePicture(ticket);
            FileUtil.saveFile("/tempFile/", "QrCodePic.jpg", file);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据传来的起止页码和OPENID以及店铺ID，查询顾客订单记录
     * @param openId
     * @param shopId
     * @param start
     * @param end
     */
    @RequestMapping(value = "cust_orderList",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String getCustomerOrderList(String openId, Long shopId, Long start, Long end) {
        List<Map<String,List<OrderRelDto>>> list = orderService.queryCustomerOrder(openId, shopId, start, end);
        String testStr = null;
        if (list != null) {
            testStr = JSON.toJSONString(AjaxResultObj.success(list), SerializerFeature.DisableCircularReferenceDetect);
            return testStr;
        }
        return JSON.toJSONString(AjaxResultObj.failed());
    }

    /**
     * 根据传来的起止页码和店铺ID，查询商家订单记录
     * @param shopId
     * @param start
     * @param end
     */
    @RequestMapping(value = "shop_orderList",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String getShopOrderList(Long shopId, Long start, Long end) {
        List<Map<String,List<OrderRelDto>>> list = orderService.queryShopOwnerOrder(shopId, start, end);
        if (list != null) {
            return JSON.toJSONString(AjaxResultObj.success(list), SerializerFeature.DisableCircularReferenceDetect);
        }
        return JSON.toJSONString(AjaxResultObj.failed());
    }

    /**
     * 根据用户openid获取微信用户
     * @return
     */
    @ResponseBody
    public void userSession(HttpServletRequest req) {
        //获取微信提供的code
        String code = req.getSession().getAttribute("code").toString();
        System.out.println("userSession.code===:"+req.getSession().getAttribute("code"));//
        Token token = getToken("wxe67244505b4041b6", "ae3b4cd8a550fab663c90ab16d548579");
        SnsToken snsToken = SnsAPI.oauth2AccessToken("wxe67244505b4041b6", "ae3b4cd8a550fab663c90ab16d548579", code);
        req.getSession().setAttribute("openId",snsToken.getOpenid());
    }

    /**
     * 根据用户openid获取微信用户
     * @return
     */
    @RequestMapping(value = "/test")
    @ResponseBody
    public void test(HttpServletRequest req) {
        //获取微信提供的code
        String code = req.getSession().getAttribute("code").toString();
        System.out.println("test.code===:"+code);//
        String openId = req.getSession().getAttribute("openId").toString();
        System.out.println("test.openId===:" + openId);//
    }

    public SnsToken cfgSnsToken(String code){
        SnsToken snsToken = SnsAPI.oauth2AccessToken("wxe67244505b4041b6", "ae3b4cd8a550fab663c90ab16d548579", code);
        return snsToken;
    }
}
