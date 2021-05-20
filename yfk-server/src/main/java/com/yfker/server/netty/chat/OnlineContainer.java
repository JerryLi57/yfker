package com.yfker.server.netty.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yfker.server.pool.ITask;
import com.yfker.server.pool.TaskDispatchCenter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 存储在线ws用户的容器
 * @author: lijiayu
 * @date: 2020-09-29 14:20
 **/
@Slf4j
@Component
public class OnlineContainer {

    /**
     * 记录当前所有的在线用户, 使用会议室号分组
     */
    private Map<String, List<OnlineUser>> meetsUsers = new ConcurrentHashMap<>();

    /**
     * 消息缓存
     */
    @Autowired
    MsgCache msgCache;
    @Autowired
    TaskDispatchCenter taskDispatchCenter;

    /***
     * 添加用户信息
     * */
    public void put(String meetNumber, OnlineUser user) {
        List<OnlineUser> list = getListByMeetNumber(meetNumber);
        list.add(user);
        log.info("用户 [ {}-{} ] 上线了", user.getId(), user.getName());
        // 向刚刚上线的用户推送聊天信息 异步推送
        taskDispatchCenter.registerTask(new PushMsg(user.getCtx(), meetNumber));
    }

    private List<OnlineUser> getListByMeetNumber(String meetNumber) {
        List<OnlineUser> list = meetsUsers.get(meetNumber);
        if (list == null) {
            synchronized (meetNumber) {
                list = meetsUsers.get(meetNumber);
                if (list == null) {
                    list = new ArrayList<>();
                    // 进入这里表示第一次建立会议室
                    firstInitMeet(meetNumber, list);
                    return list;
                }
                return list;
            }
        }
        return list;
    }

    /**
     * 初始化会议室
     * @param meetNumber
     * @param list
     */
    private void firstInitMeet(String meetNumber, List<OnlineUser> list) {
        meetsUsers.put(meetNumber, list);
        /*// 注册聊天记录定时同步任务
        taskDispatchCenter.registerTask("autoPersistenceChat_" + meetNumber, 1, 11, (short) 5, new ChatMsgToDbTask(meetNumber, meetChatRecordedService));
        // 注册一个自动清理缓存的任务
        taskDispatchCenter.registerTask("autoClearChat_" + meetNumber, 1, 5 * 60L, (short) 5, new ChatMsgClearTask(meetNumber));
        // 同步数据库数据
        MeetChatRecordedQueryDto queryDto = new MeetChatRecordedQueryDto();
        queryDto.setMeetNumber(meetNumber);
        queryDto.setPageSize(999);
        IPage<MeetChatRecordedListDto> page = meetChatRecordedService.listPageByDto(queryDto);
        if (page.getRecords()!= null && page.getRecords().size() > 0) {
            for (MeetChatRecordedListDto dto : page.getRecords()) {
                JSONObject obj = new JSONObject();
                obj.put("meetNumber", dto.getMeetNumber());
                obj.put("uid", dto.getUserId()+"");
                obj.put("name", dto.getUserName());
                obj.put("text", dto.getMsg());
                obj.put("date", dto.getMsgTime());
                msgCache.putMsgNotToCache(obj);
            }
        }*/
    }

    /**
     * 根据会议室号，获取当前在线人用户
     * @param meetNumber
     * @return
     */
    public List<OnlineUser> getOnlineUserByMeetNumber(String meetNumber) {
        return meetsUsers.get(meetNumber);
    }

    /**
     * 彻底清空所有的在线人员
     * @param meetNumber
     */
    public void removeAllUserByMeetNumber(String meetNumber) {
        meetsUsers.remove(meetNumber);
    }

    /***
     * 删除session信息
     * */
    public void remove(String channelId) {
        //如果存在则删除
        if (null == channelId || channelId.length() <= 0) {
            return;
        }
        meetsUsers.entrySet().stream().forEach(entry -> {
            Iterator<OnlineUser> users = entry.getValue().iterator();
            while (users.hasNext()) {
                OnlineUser user = users.next();
                if (user.getChannelId().equals(channelId)) {
                    log.info("用户 [ {}-{} ] 离线了", user.getId(), user.getName());
                    users.remove();
                }
            }
        });
    }

    public void postMsg(TextWebSocketFrame tmsg) {
        // 处理消息
        log.info("接收到客户端的消息:{}", tmsg.text());
        JSONObject obj = JSON.parseObject(tmsg.text());
        pushMsgToClient(obj);
    }

    /**
     * 后台推送的消息
     * @param obj
     */
    public void backPushMsg(JSONObject obj) {
        // 处理消息
        log.info("后台推送的消息:{}", obj);
        pushMsgToClient(obj);
    }

    private void pushMsgToClient(JSONObject obj) {
        obj.put("date", System.currentTimeMillis());
        String meetNumber = obj.getString("meetNumber");
        log.info("发送给客户端的消息:{}", obj.toJSONString());
        List<OnlineUser> list = getListByMeetNumber(meetNumber);
        for (OnlineUser user : list) {
            // 如果是向客户端发送文本消息，则需要发送 TextWebSocketFrame 消息
            user.getCtx().channel().writeAndFlush(new TextWebSocketFrame(obj.toJSONString()));
        }
        // 消息发送完成之后 缓存起来
        msgCache.putMsg(obj);
    }

    /**
     * 推送消息异步推送
     */
    private class PushMsg implements ITask {

        ChannelHandlerContext ctx;
        String meetNumber;

        public PushMsg(ChannelHandlerContext ctx, String meetNumber) {
            this.ctx = ctx;
            this.meetNumber = meetNumber;
        }

        @Override
        public void work() {
            try {
                Thread.sleep(200);
                /*List<MsgBodyDto> msgs = msgCache.getListByMeetNumber(meetNumber);
                if (msgs == null || msgs.isEmpty()) {
                    return;
                }
                for (MsgBodyDto msg : msgs) {
                    log.info("推送消息：{}", msg.getMsg());
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(msg.getMsg()));
                }*/
            } catch (Exception e) {
                // 因为任务都是 schedule 的，所以不考虑异常情况下的重试机制，仅仅只做日志打印
                log.error("PushMsg exception：" + e.getMessage(), e);
            }
        }

        @Override
        public String getTaskName() {
            return "PushMsg";
        }
    }
}
