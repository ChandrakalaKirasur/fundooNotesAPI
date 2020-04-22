package com.bridgelabz.fundoonotes.response;

import java.util.concurrent.CountDownLatch;

//@Component
public class RabbitMqReceiver {
	/*
	 * count down latch will waits until the other threads will executes and it is
	 * initialized with one number. Each receiveMessage called by listener and each
	 * time it will counts down decremented until it becomes zero.
	 */
	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(String message) {
		System.out.println("Received <" + message + ">");
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}
