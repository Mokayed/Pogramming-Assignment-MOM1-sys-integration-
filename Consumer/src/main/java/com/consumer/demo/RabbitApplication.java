package com.consumer.demo;

import com.rabbitmq.client.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class RabbitApplication
{
    private final static String conQUEUE = "con que";

    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(RabbitApplication.class, args);
        Scanner sc = new Scanner(System.in);
        while(true){
        String msg = sc.next();
        connectQueue(msg);}
    }

    public static void connectQueue(String msg) throws Exception
    {
        // Same as the producer: tries to create a queue, if it wasn't already created
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        try
        {
            channel.queueDeclare(conQUEUE, false, false, false, null);
            channel.basicPublish("", conQUEUE, null, msg.getBytes("UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Register for a queue
        channel.queueDeclare(conQUEUE, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // Get notified, if a message for this receiver arrives
        DeliverCallback deliverCallback = (consumerTag, delivery) ->
        {
            String message = new String(delivery.getBody(), "UTF-8");
            if(message.equals("1")) {
                System.out.println("wait until the producer connect");

            }
            if(!message.equals("1")) {
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(conQUEUE, true, deliverCallback, consumerTag -> {});



    }
}

