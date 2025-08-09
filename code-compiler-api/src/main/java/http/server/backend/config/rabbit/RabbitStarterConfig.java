package http.server.backend.config.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class RabbitStarterConfig {

    private final RabbitConfig.Login loginConfig;

    private final RabbitConfig config;

    public RabbitStarterConfig(RabbitConfig config) {
        this.config = config;
        loginConfig = config.getLogin();
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(loginConfig.getAddress());
        cachingConnectionFactory.setPort(loginConfig.getPort());
        cachingConnectionFactory.setUsername(loginConfig.getUsername());
        cachingConnectionFactory.setPassword(loginConfig.getPassword());
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public Queue queue() {
        return new Queue(config.getQueue(), true, false, false);
    }

    @Bean
    public Queue resultCodeQueue() {
        return new Queue("result.queue", true, false, false);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(config.getExchange());
    }

    @Bean
    public RabbitTemplate template() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(config.getKey());
    }
}
