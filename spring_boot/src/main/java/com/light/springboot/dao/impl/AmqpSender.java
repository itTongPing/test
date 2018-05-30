package com.light.springboot.dao.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.light.springboot.configuration.AmqpConfiguration;
@Component
public class AmqpSender {
	
	 @Autowired
	  private AmqpTemplate amqpTemplate;
	  /**
	   * 简单模式发送
	   *
	   * @param message
	   */
	  public void simpleSend(String message) {
	    this.amqpTemplate.convertAndSend(AmqpConfiguration.SIMPLE_QUEUE, message);
	  }
	  /**
	   * 发布/订阅模式发送
	   *
	   * @param message
	   */
	  public void psSend(String message) {
	    this.amqpTemplate.convertAndSend(AmqpConfiguration.FANOUT_EXCHANGE, "", message);
	  }
	  /**
	   * 路由模式发送
	   *
	   * @param message
	   */
	  public void routingSend(String routingKey, String message) {
	    this.amqpTemplate.convertAndSend(AmqpConfiguration.DIRECT_EXCHANGE, routingKey, message);
	  }
	  /**
	   * 主题模式发送
	   *
	   * @param routingKey
	   * @param message
	   */
	  public void topicSend(String routingKey, String message) {
	    this.amqpTemplate.convertAndSend(AmqpConfiguration.TOPIC_EXCHANGE, routingKey, message);
	  }

}
