package com.light.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.light.springboot.dao.impl.UserDaoImpl;
import com.light.springboot.domain.User;
import com.light.springboot.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

@Service("userService")
public class UserServiceImpl {

	private final static String EXCHANGE_NAME = "test_exchange_direct"; 
	private final static String QUEUE_NAME = "test_queue_direct_1";  
	
	
	
	@Autowired
	UserDaoImpl userDao;
	@Transactional
	public int addUser(User user){
		
		//获取删除消息是否成功
				try {
					String message = receiveMessage();
					if("deleteFail".equals(message)){
						throw new Exception("删除失败,不执行新增");
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
		
		try{
			//添加成功，并发送消息
			userDao.insert(user);
			return 1;
		}catch(Exception e){
			try {
				return 0;
			} catch (Exception e1) {
				e1.printStackTrace();
				return 0;
			}
		}
	}
	
	
	
	
	
	private void sendMessage(String message) throws Exception{
		 // 获取到连接以及mq通道  
       Connection connection = ConnectionUtils.getConnection();  
       Channel channel = connection.createChannel();  
 
       // 声明exchange  
       channel.exchangeDeclare(EXCHANGE_NAME, "direct");  
 
       // 消息内容  
       //String message = "这是消息B";  
       channel.basicPublish(EXCHANGE_NAME, "B", null, message.getBytes());  
       System.out.println(" [生产者] Sent '" + message + "'");  
 
       channel.close();  
       connection.close();  
	}
	
	
	private String receiveMessage() throws Exception{
		
		// 获取到连接以及mq通道  
       Connection connection = ConnectionUtils.getConnection();  
       Channel channel = connection.createChannel();  
 
       // 声明队列  
       channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
 
       /* 
        * 绑定队列到交换机 
        * 参数1：队列的名称 
        * 参数2：交换机的名称 
        * 参数3：routingKey 
        */  
       channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "A"); 
 
       // 同一时刻服务器只会发一条消息给消费者  
       channel.basicQos(1);  
 
       // 定义队列的消费者  
       QueueingConsumer consumer = new QueueingConsumer(channel);  
       // 监听队列，手动返回完成  
       channel.basicConsume(QUEUE_NAME, false, consumer);  
 
       // 获取消息  
       while (true) {  
           Delivery delivery = consumer.nextDelivery();  
           String message = new String(delivery.getBody());  
           System.out.println(" [消费者1] Received '" + message + "'");  
           Thread.sleep(10);  
 
           channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);  
           if(message !=null){
           	return message;
           }
       }  
		
		
	}
	
}
