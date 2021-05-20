package com.yfker.server.netty.center;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yfker.server.constant.MsgTypeEnum;
import com.yfker.server.pool.TaskDispatchCenter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 存储在线ws设备的容器
 * @author: lijiayu
 * @date: 2020-09-29 14:20
 **/
@Slf4j
@Component
public class OnlineDeviceContainer {

    /**
     * 记录当前所有的web端在线设备， key = 设备编号_端(web/app)
     */
    private List<OnlineDevice> webDevices = new ArrayList<>();

    /**
     * 记录当前所有的在线设备， key = 设备编号
     */
    private Map<String, OnlineDevice> AppDevices = new ConcurrentHashMap<>();

    /**
     * 用于对应编号与channelID
     */
    private Map<String, String> AppChannelId = new ConcurrentHashMap<>();

    @Autowired
    TaskDispatchCenter taskDispatchCenter;


    public void putDevice(OnlineDevice device) {
        // type 区分消息的来源
        if (device.getType() == MsgTypeEnum.WEB.getCode()) {
            webDevices.add(device);
        } else {
            AppDevices.put(device.getEsm(), device);
            AppChannelId.put(device.getChannelId(), device.getEsm());
        }
        log.info("设备 [ {}-{} ] 上线了", device.getType(), device.getChannelId());
    }


    /***
     * 删除session信息
     * */
    public void remove(String channelId) {
        //如果存在则删除
        if (null == channelId || channelId.length() <= 0) {
            return;
        }
        // 删除 web 列表里面的
        Iterator<OnlineDevice> webIt = webDevices.iterator();
        while (webIt.hasNext()) {
            OnlineDevice e = webIt.next();
            if (e.getChannelId().equals(channelId)) {
                webIt.remove();
                log.info("设备 [ {} ] 离线了", channelId);
            }
        }
        // 删除 App设备列表里面的
        String esm = AppChannelId.get(channelId);
        if (!StringUtils.isEmpty(esm)) {
            AppChannelId.remove(channelId);
            AppDevices.remove(esm);
            log.info("设备 [ {} ] 离线了", esm);
        }
    }

    public void postMsg(TextWebSocketFrame tmsg) {
        // 处理消息 消息体 {type:"1", esm:"XXX123",}
        log.info("接收到客户端的消息:{}", tmsg.text());
        JSONObject obj = JSON.parseObject(tmsg.text());
        pushMsgToClient(obj);
    }

    private void pushMsgToClient(JSONObject obj) {
        Integer type = obj.getInteger("type");
        if (null == type) {
            return;
        }
        // 对来源web端的消息，根据esm 设备编号 发送给指定的app
        if (type == MsgTypeEnum.WEB.getCode()) {
            String esm = obj.getString("esm");
            OnlineDevice device = AppDevices.get(esm);
            if (null == device) {
                return;
            }
            log.info("推送给设备的消息:{}", obj.toJSONString());
            device.getCtx().channel().writeAndFlush(new TextWebSocketFrame(obj.toJSONString()));
        }
        // 对来源app端的消息，推送给所有的在线web端
        for (OnlineDevice device : webDevices) {
            log.info("推送给Web端的消息:{}", obj.toJSONString());
            device.getCtx().channel().writeAndFlush(new TextWebSocketFrame(obj.toJSONString()));
        }
    }

}
