package com.example.IotDemo;


import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.fasterxml.jackson.databind.ObjectMapper;



@Configuration
public class MqttBeans {
	
	@Autowired
	SensorDataRepository sensorDataRepository;
	
	
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		
		options.setServerURIs(new String[] {"tcp://192.168.0.109:8883"});//{"tcp://208.87.132.155:1883"});
		options.setUserName("sammy");//("devmquser");
		String pass = "123";//"AUcfP.73WWW*QMkD";
		options.setPassword(pass.toCharArray());					
		options.setCleanSession(true);
		
		factory.setConnectionOptions(options);
		
		return factory;
	}
	
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
				mqttClientFactory(), "#");

		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}
	
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
				if(topic.equals("myTopic")) {
					System.out.println("This is the topic");
				}
				System.out.println(message.getPayload());
				String payload = message.getPayload().toString();
		        ObjectMapper objectMapper = new ObjectMapper();
		        try {
		            JsonData sensorData = objectMapper.readValue(payload, JsonData.class);
		            sensorDataRepository.save(sensorData);
		            System.out.println("Data saved to the database: " + sensorData);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			}

		};
	}
	
	/*
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
				if(topic.equals("myTopic")) {
					System.out.println("This is the topic");
				}
				System.out.println(message.getPayload());
				String payload = message.getPayload().toString();
				sensorDataRepository.addData(payload);
			}

		};
	}
	 */
	
	

	 
	
	/*
	 * @ServiceActivator(inputChannel = "mqttInputChannel") public MessageHandler
	 * handler(SensorDataRepository sensorDataRepository) { return message -> {
	 * String payload = message.getPayload().toString();
	 * sensorDataRepository.addData(payload);
	 * 
	 * }; }
	 */
	
	
	@Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
	@Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        //clientId is generated using a random number
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("#");
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }
	

}
