package com.bridgelabz.fundoonotes.utility;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import com.bridgelabz.fundoonotes.response.RabbitMqReceiver;

//@Component
public class RabbitMQSender implements CommandLineRunner{
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("chandu")
	private String exchange;

	@Value("cvk")
	private String routingkey;
	private final RabbitMqReceiver receiver;

	  public RabbitMQSender(RabbitMqReceiver receiver, RabbitTemplate rabbitTemplate) {
	    this.receiver = receiver;
	    this.rabbitTemplate = rabbitTemplate;
	  }
	public boolean send(MailService message) {
		rabbitTemplate.convertAndSend(exchange, routingkey, message);
		return true;
	}

	@Override
	public void run(String... args) throws Exception {
		 System.out.println("Sending message...");
		 //rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
		 rabbitTemplate.convertAndSend(exchange, routingkey,"this is from cvk");
		    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		  }		

}
