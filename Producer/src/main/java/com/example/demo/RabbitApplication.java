package com.example.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

@SpringBootApplication
public class RabbitApplication {
    private final static String proQUEUE = "producerqueue";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RabbitApplication.class, args);

        Scanner sc = new Scanner(System.in);

        String message = "Hello, Welcome to Hallur&Mo car rent, please pick a car that you would like to rent:" +
                "\n1-Mercdes CLA200 Benz," +
                " \n2-BMW i530 Benz!," +
                "\n3-Toyota Aygo 2010-benz, " +
                "\n3-mercedes E200 2015-Ben";
        createQueue(message);
        System.out.println(" [x] Sent '" + message + "'");
    }

    public static void createQueue(String message) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        try
        {
            channel.queueDeclare(proQUEUE, false, false, false, null);
            channel.basicPublish("", proQUEUE, null, message.getBytes("UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        channel.queueDeclare(proQUEUE, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        // Get notified, if a message for this receiver arrives
        DeliverCallback deliverCallback = (consumerTag, delivery) ->
        {
            String message2 = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message2 + "'");
            if(message2.equals("1")){
                String msg = "you order mercdes";
                channel.basicPublish("", proQUEUE, null, msg.getBytes("UTF-8"));
            }
        };
        channel.basicConsume(proQUEUE, true, deliverCallback, consumerTag -> {});
    }

}
