package com.chentao.demo.rabbitmq;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * Created by chentao on 2018/7/20.
 */
public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {
    Logger logger = LoggerFactory.getLogger(MsgSendConfirmCallBack.class);
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            if (null != correlationData){
                logger.info("发送数据至消息队列成功!,消息队列id：{}",correlationData.getId());
            }else {
                logger.info("发送数据至消息队列成功!");
            }

        } else {
            logger.info("消息消费失败:" + cause+"\n重新发送");
        }
    }
}
