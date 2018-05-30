package rabbitmq.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import rabbitmq.ConnectionUtils;

public class Producer {
	//交换机的名称  
    private final static String EXCHANGE_NAME = "test_exchange_fanout";  
  
    public static void main(String[] argv) throws Exception {  
        // 获取到连接以及mq通道  
        Connection connection = ConnectionUtils.getConnection();  
        Channel channel = connection.createChannel();  
  
        /* 
         * 声明exchange(交换机) 
         * 参数1：交换机名称 
         * 参数2：交换机类型     Direct 、Fanout 、Topic 广播是式交换器，不管消息的ROUTING_KEY设置为什么，Exchange都会将消息转发给所有绑定的Queue
         * 参数3：交换机持久性，如果为true则服务器重启时不会丢失 
         * 参数4：交换机在不被使用时是否删除 
         * 参数5：交换机的其他属性 
         */  
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout",true,true,null);  
  
        // 消息内容  
        String message = "订阅消息";  
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());  
        System.out.println(" [生产者] Sent '" + message + "'");  
  
        channel.close();  
        connection.close();  
    } 

}
