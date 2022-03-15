package com.example.activiti.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Scanner;

public class Consumer {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Code : ");
        String code=scanner.nextLine();
        try {
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection=connectionFactory.createConnection();
            connection.start();
            Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            //Destination destination=session.createQueue("test.queue");
            Destination destination=session.createTopic("test.topic");
            MessageConsumer consumer=session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if(message instanceof TextMessage){
                        try {
                        TextMessage textMessage= (TextMessage) message;
                            System.out.println(textMessage.getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
