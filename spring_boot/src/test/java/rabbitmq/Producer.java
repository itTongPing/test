package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
/**
 *   1.客户端连接到消息队列服务器，打开一个channel。
          2.客户端声明一个exchange，并设置相关属性。
          3.客户端声明一个queue，并设置相关属性。
          4.客户端使用routing key，在exchange和queue之间建立好绑定关系。
          5.客户端投递消息到exchange。
 * @author a
 *
 */
public class Producer {

	
	private final static String QUEUE_NAME = "test_queue";  
	  
    public static void main(String[] argv) throws Exception {  
        // 获取到连接以及mq通道  
        Connection connection = ConnectionUtils.getConnection();  
        // 从连接中创建通道  
        Channel channel = connection.createChannel();  
        /* 
         * 声明（创建）队列 
         * 参数1：队列名称 
         * 参数2：为true时server重启队列不会消失 
         * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常 
         * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息) 
         * 参数5：建立队列时的其他参数 
         */  
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
  
        // 消息内容  
        String message = "Hello World!";  
        /* 
         * 向server发布一条消息 
         * 参数1：exchange名字，若为空则使用默认的exchange 
         * 参数2：routing key 
         * 参数3：其他的属性 
         * 参数4：消息体 
         * RabbitMQ默认有一个exchange，叫default exchange，它用一个空字符串表示，它是direct exchange类型， 
         * 直接交换器，工作方式类似于单播，Exchange会将消息发送完全匹配ROUTING_KEY的Queue
         * 任何发往这个exchange的消息都会被路由到routing key的名字对应的队列上，如果没有对应的队列，则消息会被丢弃 
         */  
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());  
        System.out.println(" [生产者] Sent '" + message + "'");  
  
        //关闭通道和连接  
        channel.close();  
        connection.close();  
    }  
}
