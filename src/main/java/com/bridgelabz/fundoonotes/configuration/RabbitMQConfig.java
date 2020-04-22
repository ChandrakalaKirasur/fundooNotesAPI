package com.bridgelabz.fundoonotes.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.bridgelabz.fundoonotes.response.RabbitMqReceiver;

/**
 * @author Chandrakala Kirasur @
 *
 */
//@Configuration
public class RabbitMQConfig {
	@Autowired
	private ConnectionFactory rabbitConnection;

	@Bean
	public DirectExchange routeExchange() {
		return new DirectExchange("chandu", true, false);
	}

	@Bean
	public Queue routeQueue() {
		return new Queue("chandu", true);
	}

	@Bean
	public Binding routeExchangeBinding(DirectExchange routeExchange, Queue routeQueue) {
		return BindingBuilder.bind(routeQueue).to(routeExchange).with("cvk");
	}

	@Bean
	public RabbitTemplate routeExchangeTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnection);
		rabbitTemplate.setConnectionFactory(rabbitConnection);
		rabbitTemplate.setExchange("chandu");
		rabbitTemplate.setRoutingKey("cvk");
		return rabbitTemplate;
	}

	@Bean
	SimpleMessageListenerContainer container() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitConnectionFactory());
		container.setQueueNames("chandu");
		container.setMessageListener(exampleListener());
		return container;
	}

	@Bean
	public ConnectionFactory rabbitConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		connectionFactory.setUsername("daniel");
		connectionFactory.setPassword("daniel");
		return connectionFactory;
	}

	@Bean
	public MessageListener exampleListener() {
		return new MessageListener() {
			@Override
			public void onMessage(Message message) {
				System.out.println("received: " + message);

			}
		};
	}

	@Bean
	MessageListenerAdapter listenerAdapter(RabbitMqReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
}
