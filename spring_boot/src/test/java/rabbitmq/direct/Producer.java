package rabbitmq.direct;

import rabbitmq.ConnectionUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Producer {

	private final static String EXCHANGE_NAME = "test_exchange_direct";  
	  
    public static void main(String[] argv) throws Exception {  
        // 获取到连接以及mq通道  
        Connection connection = ConnectionUtils.getConnection();  
        Channel channel = connection.createChannel();  
  
        // 声明exchange  
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");  
  
        // 消息内容  
        String message = "这是消息B";  
        channel.basicPublish(EXCHANGE_NAME, "B", null, message.getBytes());  
        System.out.println(" [生产者] Sent '" + message + "'");  
  
        channel.close();  
        connection.close();  
    } 
}
