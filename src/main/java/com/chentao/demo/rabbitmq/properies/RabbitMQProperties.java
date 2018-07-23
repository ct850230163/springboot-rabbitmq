package com.chentao.demo.rabbitmq.properies;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Created by chentao on 2018/7/20.
 */
@ConfigurationProperties(prefix = "rabbitproper")
@Component
public class RabbitMQProperties {
    private String address;
    private String exchange;
    private String username;
    private String password;
    private String virtualhost;
    private String queuename;
    private boolean durable = false;
    private boolean exclusive = false;
    private boolean autoDelete = false;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualhost() {
        return virtualhost;
    }

    public void setVirtualhost(String virtualhost) {
        this.virtualhost = virtualhost;
    }

    public String getQueuename() {
        return queuename;
    }

    public void setQueuename(String queuename) {
        this.queuename = queuename;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }
}
