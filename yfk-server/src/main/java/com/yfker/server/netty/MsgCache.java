package com.yfker.server.netty;

import com.alibaba.fastjson.JSONObject;
import com.lolaage.chat.dto.MsgBodyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 消息缓存
 * @author: lijiayu
 * @date: 2020-10-12 14:22
 **/
@Slf4j
@Component
public class MsgCache {

    /**
     * 记录当前所有的在线用户, 使用会议室号分组
     */
    private Map<String, List<MsgBodyDto>> msgs = new ConcurrentHashMap<>();

    /**
     * 二级缓存用于同步至DB
     */
    private Map<String, List<MsgBodyDto>> msgCacheForDB = new ConcurrentHashMap<>();

    /**
     * 向缓存里添加聊天记录数据, 缓存的消息体
     * @param msgObj
     */
    public void putMsg(JSONObject msgObj) {
        // 处理消息
        String meetNumber = msgObj.getString("meetNumber");
        // 同步至二级缓存
        getCacheByMeetNumber(meetNumber).add(putMsgNotToCache(msgObj));
    }

    public MsgBodyDto putMsgNotToCache(JSONObject msgObj) {
        // 处理消息
        String meetNumber = msgObj.getString("meetNumber");
        MsgBodyDto msg = new MsgBodyDto();
        msg.setMsg(msgObj.toJSONString());
        List<MsgBodyDto> list = getListByMeetNumber(meetNumber);
        list.add(msg);
        return msg;
    }

    public List<MsgBodyDto> getListByMeetNumber(String meetNumber) {
        List<MsgBodyDto> list = msgs.get(meetNumber);
        if (list == null) {
            synchronized (meetNumber) {
                list = msgs.get(meetNumber);
                if (list == null) {
                    list = new ArrayList<>();
                    msgs.put(meetNumber, list);
                    return list;
                }
                return list;
            }
        }
        return list;
    }

    public List<MsgBodyDto> getCacheByMeetNumber(String meetNumber) {
        List<MsgBodyDto> list = msgCacheForDB.get(meetNumber);
        if (list == null) {
            synchronized (meetNumber) {
                list = msgCacheForDB.get(meetNumber);
                if (list == null) {
                    list = new ArrayList<>();
                    msgCacheForDB.put(meetNumber, list);
                    return list;
                }
                return list;
            }
        }
        return list;
    }

    /**
     * 获取需要转存数据的消息
     * @return
     */
    public Map<String, List<MsgBodyDto>> getMsgs() {
        return msgs;
    }

    /**
     * 获取二级缓存
     * @return
     */
    public Map<String, List<MsgBodyDto>> getCaches() {
        return msgCacheForDB;
    }

}
