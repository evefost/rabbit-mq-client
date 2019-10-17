package com.eve.mq.client.rabbit;

import com.eve.mq.client.BaseMqProducerEndPoint;

/**
 * rabbit mq 端点信息
 * @author xieyang
 * @date 18/7/14
 */
public class RabbitMqProducerEndPoint extends BaseMqProducerEndPoint {

    private String containerName;

    private String exchange;

    private String routeKey;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

}
