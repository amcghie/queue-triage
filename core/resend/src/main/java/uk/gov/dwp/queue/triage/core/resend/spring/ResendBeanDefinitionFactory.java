package uk.gov.dwp.queue.triage.core.resend.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.env.Environment;
import uk.gov.dwp.queue.triage.core.jms.activemq.spring.ActiveMQConnectionFactoryBeanDefinitionFactory;
import uk.gov.dwp.queue.triage.core.jms.spring.JmsTemplateBeanDefinitionFactory;
import uk.gov.dwp.queue.triage.core.jms.spring.SpringMessageSenderBeanDefinitionFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ResendBeanDefinitionFactory implements BeanDefinitionRegistryPostProcessor {

    private final Environment environment;
    private final ActiveMQConnectionFactoryBeanDefinitionFactory activeMQConnectionFactoryBeanDefinitionFactory;
    private final ResendFailedMessageServiceBeanDefinitionFactory resendFailedMessageServiceBeanDefinitionFactory;
    private final SpringMessageSenderBeanDefinitionFactory springMessageSenderBeanDefinitionFactory;
    private final JmsTemplateBeanDefinitionFactory jmsTemplateBeanDefinitionFactory;
    private final ResendScheduledExecutorServiceBeanDefinitionFactory resendScheduledExecutorServiceBeanDefinitionFactory;

    public ResendBeanDefinitionFactory(Environment environment,
                                       ActiveMQConnectionFactoryBeanDefinitionFactory activeMQConnectionFactoryBeanDefinitionFactory,
                                       ResendFailedMessageServiceBeanDefinitionFactory resendFailedMessageServiceBeanDefinitionFactory,
                                       SpringMessageSenderBeanDefinitionFactory springMessageSenderBeanDefinitionFactory,
                                       JmsTemplateBeanDefinitionFactory jmsTemplateBeanDefinitionFactory,
                                       ResendScheduledExecutorServiceBeanDefinitionFactory resendScheduledExecutorServiceBeanDefinitionFactory) {
        this.environment = environment;
        this.activeMQConnectionFactoryBeanDefinitionFactory = activeMQConnectionFactoryBeanDefinitionFactory;
        this.resendFailedMessageServiceBeanDefinitionFactory = resendFailedMessageServiceBeanDefinitionFactory;
        this.springMessageSenderBeanDefinitionFactory = springMessageSenderBeanDefinitionFactory;
        this.jmsTemplateBeanDefinitionFactory = jmsTemplateBeanDefinitionFactory;
        this.resendScheduledExecutorServiceBeanDefinitionFactory = resendScheduledExecutorServiceBeanDefinitionFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        int index = 0;
        while (hasMoreBrokers(index)) {
            String brokerName = environment.getProperty(getPropertyKey(index, "name"));

            // Create ConnectionFactory
            String connectionFactoryBeanName = activeMQConnectionFactoryBeanDefinitionFactory.createBeanName("resender-" + brokerName);
            registry.registerBeanDefinition(
                    connectionFactoryBeanName,
                    activeMQConnectionFactoryBeanDefinitionFactory.create(
                            environment.getProperty(getPropertyKey(index, "url")))
            );

            // Create JmsTemplate
            String jmsTemplateBeanName = jmsTemplateBeanDefinitionFactory.createBeanName(brokerName);
            registry.registerBeanDefinition(
                    jmsTemplateBeanName,
                    jmsTemplateBeanDefinitionFactory.create(connectionFactoryBeanName)
            );

            // Create MessageSender
            String springMessageSenderBeanName = springMessageSenderBeanDefinitionFactory.createBeanName(brokerName);
            registry.registerBeanDefinition(
                    springMessageSenderBeanName,
                    springMessageSenderBeanDefinitionFactory.create(jmsTemplateBeanName)
            );

            // Create a ResendFailedMessageService
            String resendFailedMessageServiceBeanName = resendFailedMessageServiceBeanDefinitionFactory.createBeanName(brokerName);
            registry.registerBeanDefinition(
                    resendFailedMessageServiceBeanName,
                    resendFailedMessageServiceBeanDefinitionFactory.create(brokerName, springMessageSenderBeanName)
            );

            // Create a ScheduledExecutor
            registry.registerBeanDefinition(
                    resendScheduledExecutorServiceBeanDefinitionFactory.createBeanName(brokerName),
                    createResendScheduledExecutorBeanDefinition(index, resendFailedMessageServiceBeanName)
            );

            index++;
        }
    }

    private AbstractBeanDefinition createResendScheduledExecutorBeanDefinition(int index,
                                                                              String resendFailedMessageServiceBeanName) {
        Long initialDelay = environment.getProperty(
                getPropertyKey(index, "resend.initialDelay"),
                Long.class,
                0L);
        Long executionFrequency = environment.getProperty(
                getPropertyKey(index,"resend.frequency"),
                Long.class,
                60L);
        TimeUnit timeUnit = environment.getProperty(
                getPropertyKey(index, "resend.unit"),
                TimeUnit.class,
                TimeUnit.SECONDS);
        return resendScheduledExecutorServiceBeanDefinitionFactory.create(
                Executors.newSingleThreadScheduledExecutor(),
                resendFailedMessageServiceBeanName,
                initialDelay,
                executionFrequency,
                timeUnit);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private boolean hasMoreBrokers(int index) {
        return environment.containsProperty(getPropertyKey(index, "name"));
    }

    private String getPropertyKey(int index, String propertyName) {
        return "jms.activemq.brokers[" + index + "]." + propertyName;
    }
}
