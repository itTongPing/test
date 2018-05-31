package com.light.springboot.dao.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.light.springboot.configuration.AmqpConfiguration;

//@Component
public class AmqpReceiver {
	
	/**
	   * 简单模式接收
	   *
	   * @param message
	   */
	  @RabbitListener(queues = AmqpConfiguration.SIMPLE_QUEUE)
	  public void simpleReceive(String message) {
	    System.out.println("接收消息:" + message);
	  }
	  /**
	   * 发布/订阅模式接收
	   *
	   * @param message
	   */
	  @RabbitListener(queues = AmqpConfiguration.PS_QUEUE_1)
	  public void psReceive1(String message) {
	    System.out.println(AmqpConfiguration.PS_QUEUE_1 + "接收消息:" + message);
	  }
	  @RabbitListener(queues = AmqpConfiguration.PS_QUEUE_2)
	  public void psReceive2(String message) {
	    System.out.println(AmqpConfiguration.PS_QUEUE_2 + "接收消息:" + message);
	  }
	  /**
	   * 路由模式接收
	   *
	   * @param message
	   */
	  @RabbitListener(queues = AmqpConfiguration.ROUTING_QUEUE_1)
	  public void routingReceive1(String message) {
	    System.out.println(AmqpConfiguration.ROUTING_QUEUE_1 + "接收消息:" + message);
	  }
	  @RabbitListener(queues = AmqpConfiguration.ROUTING_QUEUE_2)
	  public void routingReceive2(String message) {
	    System.out.println(AmqpConfiguration.ROUTING_QUEUE_2 + "接收消息:" + message);
	  }
	  /**
	   * 主题模式接收
	   *
	   * @param message
	   */
	  @RabbitListener(queues = AmqpConfiguration.TOPIC_QUEUE_1)
	  public void topicReceive1(String message) {
	    System.out.println(AmqpConfiguration.TOPIC_QUEUE_1 + "接收消息:" + message);
	  }
	  @RabbitListener(queues = AmqpConfiguration.TOPIC_QUEUE_2)
	  public void topicReceive2(String message) {
	    System.out.println(AmqpConfiguration.TOPIC_QUEUE_2 + "接收消息:" + message);
	  }
	

}
