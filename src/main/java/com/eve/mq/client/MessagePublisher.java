package com.eve.mq.client;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/17
 */

public interface MessagePublisher<M> {

    /**
     * xxxx
     *
     * @param message
     */
    void publish(M message);

}
