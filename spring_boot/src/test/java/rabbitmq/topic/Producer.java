package rabbitmq.topic;

import rabbitmq.ConnectionUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
/**
 *        1.客户端连接到消息队列服务器，打开一个channel。
          2.客户端声明一个exchange，并设置相关属性。
          3.客户端声明一个queue，并设置相关属性。
          4.客户端使用routing key，在exchange和queue之间建立好绑定关系。
          5.客户端投递消息到exchange。
 * @author a
 *
 */
public class Producer {

	
	private final static String EXCHANGE_NAME = "test_exchange_topic";  
	  
    public static void main(String[] argv) throws Exception {  
        // 获取到连接以及mq通道  
        Connection connection = ConnectionUtils.getConnection();  
        Channel channel = connection.createChannel();  
  
        // 声明exchange  
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");  //主题交换器，工作方式类似于组播，Exchange会将消息转发和ROUTING_KEY匹配模式相同的所有队列
  
        // 消息内容  模拟 有人购物下订单  
        String message = "新增订单:id=101";  
        channel.basicPublish(EXCHANGE_NAME, "order.insert", null, message.getBytes());  
        System.out.println(" [生产者] Sent '" + message + "'");  
  
        channel.close();  
        connection.close();  
    } 
}
