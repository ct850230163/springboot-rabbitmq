package com.chentao.demo.rabbitmq.AutoConfiguration;

import com.chentao.demo.rabbitmq.MsgSendConfirmCallBack;
import com.chentao.demo.rabbitmq.properies.RabbitMQProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 *  rabbitMq 的自动配置
 * Created by chentao on 2018/7/20.
 */
@Configuration
@EnableConfigurationProperties({RabbitMQProperties.class})
public class RabbitConfiguration {
    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    /**
     *  创建连接工厂类
     * @return
     */
    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        //该方法配置多个host，在当前连接host down掉的时候会自动去重连后面的host
        connectionFactory.setAddresses(rabbitMQProperties.getAddress());
        connectionFactory.setUsername(rabbitMQProperties.getUsername());
        connectionFactory.setPassword(rabbitMQProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitMQProperties.getVirtualhost());
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        /**若使用confirm-callback或return-callback，必须要配置publisherConfirms或publisherReturns为true
         * 每个rabbitTemplate只能有一个confirm-callback和return-callback*/

        template.setConfirmCallback(msgSendConfirmCallBack());
        /**
         * 使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true，可针对每次请求的消息去确定’mandatory’的boolean值，
         * 只能在提供’return -callback’时使用，与mandatory互斥*/
        //  template.setMandatory(true);
        return template;
    }


    /**
     消息确认机制
     Confirms给客户端一种轻量级的方式，能够跟踪哪些消息被broker处理，哪些可能因为broker宕掉或者网络失败的情况而重新发布。
     确认并且保证消息被送达，提供了两种方式：发布确认和事务。(两者不可同时使用)在channel为事务时，
     不可引入确认模式；同样channel为确认模式下，不可使用事务。

     */
    @Bean
    public MsgSendConfirmCallBack msgSendConfirmCallBack(){
        return new MsgSendConfirmCallBack();
    }


    //创建交换机
    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.directExchange(rabbitMQProperties.getExchange()).durable(true).build();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        return rabbitAdmin;
    }

    @Bean
    public Queue queue(){
        return new Queue(rabbitMQProperties.getQueuename(),true,false,false);
    }

    @Bean
    public Binding queueBinding() {
        return BindingBuilder.bind(this.queue()).to(this.exchange()).with(rabbitMQProperties.getQueuename()).noargs();
    }

    /**
     *  queue listener 观察 监听模式 当有消息到达时会通知鉴定在对应的队列上的监听对象
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer_one() throws IOException {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory());
        simpleMessageListenerContainer.setQueueNames(rabbitMQProperties.getQueuename());
        simpleMessageListenerContainer.setExposeListenerChannel(true);
        simpleMessageListenerContainer.setAutoStartup(false);
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置确认模式手工确认
        return simpleMessageListenerContainer;
    }
}
