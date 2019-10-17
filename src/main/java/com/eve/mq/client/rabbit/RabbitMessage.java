package com.eve.mq.client.rabbit;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/17
 */
public class RabbitMessage<T> {

    private String containerFactory;

    private String exchange;

    private String routeKey;

    private T data;

    public String getContainerFactory() {
        return containerFactory;
    }

    public void setContainerFactory(String containerFactory) {
        this.containerFactory = containerFactory;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
