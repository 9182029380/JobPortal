package com.example.JobPortal.Configuration;

import com.example.JobPortal.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.Map;

@Configuration
public class JmsConfig {

    @Value("${spring.activemq.user}")
    private String username;

    @Value("${spring.activemq.password}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://localhost:61616");
        factory.setUser("artemis");  // Correct method
        factory.setPassword("artemis");  // Correct method
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() throws JMSException {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() throws JMSException {
        return new JmsTemplate(connectionFactory());
    }
}
