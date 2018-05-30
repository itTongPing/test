package rabbitmq.work;

import rabbitmq.ConnectionUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class Consumer1 {
	
	 private final static String QUEUE_NAME = "test_queue_work";  
	  
	    public static void main(String[] argv) throws Exception {  
	  
	        // 获取到连接以及mq通道  
	        Connection connection = ConnectionUtils.getConnection();  
	        Channel channel = connection.createChannel();  
	  
	        // 声明队列  
	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
	          
	        // 同一时刻服务器只会发一条消息给消费者(能者多劳模式)  
	        channel.basicQos(1);  
	  
	        // 定义队列的消费者  
	        QueueingConsumer consumer = new QueueingConsumer(channel);  
	        /* 
	         * 监听队列，不自动返回ack包,下面手动返回 
	         * 如果不回复，消息不会在服务器删除 
	         */   
	        channel.basicConsume(QUEUE_NAME, false, consumer);  
	  
	        // 获取消息  
	        while (true) {  
	            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
	            String message = new String(delivery.getBody());  
	            System.out.println(" [消费者1] Received '" + message + "'");  
	            //休眠  
	            Thread.sleep(10);  
	            // 手动返回ack包确认状态  
	            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);  
	          //channel.basicReject(); channel.basicNack(); //可以通过这两个函数拒绝消息，可以指定消息在服务器删除还是继续投递给其他消费者  
	        }  
	    } 

}
