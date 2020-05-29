package io.github.logtube.rocketmq;

import io.github.logtube.Logtube;
import io.github.logtube.LogtubeConstants;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.ChannelInterceptorAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;

/**
 * - 在消息发送渠道初始化bean时添加拦截器
 *
 * @author chenkeguang 2019年7月17日
 */
@Configuration
public class RocketMqConfig {
    /**
     * RocketMQ 消息对象 MessageExt对应的key
     */
    private static final String ROCKETMQ_MESSAGE_OBJECT_KEY = "ORIGINAL_ROCKETMQ_MESSAGE";

    private static final String BEAN_NAME_INPUT = "input";

    private static final String BEAN_NAME_OUTPUT = "output";

    @Bean
    public BeanPostProcessor channelConfigurer() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (BEAN_NAME_INPUT.equals(beanName) && bean instanceof ChannelInterceptorAware) {
                    ((ChannelInterceptorAware) bean).addInterceptor(new ChannelInterceptorAdapter() {

                        @Override
                        public Message<?> preSend(Message<?> message, MessageChannel channel) {
                            message.getHeaders().entrySet().forEach(h -> {
                                if (ROCKETMQ_MESSAGE_OBJECT_KEY.equals(h.getKey())) {
                                    MessageExt me = (MessageExt) h.getValue();
                                    Logtube.getProcessor().setCrid(me.getProperty(LogtubeConstants.DUBBO_CRID_KEY));
                                    Logtube.getProcessor().setCrsrc(me.getProperty(LogtubeConstants.DUBBO_CRSRC_KEY));
                                }
                            });
                            return message;
                        }
                    });
                } else if (BEAN_NAME_OUTPUT.equals(beanName) && bean instanceof ChannelInterceptorAware) {
                    ((ChannelInterceptorAware) bean).addInterceptor(new ChannelInterceptorAdapter() {

                        @Override
                        public Message<?> preSend(Message<?> message, MessageChannel channel) {
                            MessageBuilder<?> messageBuilder = MessageBuilder.fromMessage(message);
                            messageBuilder.setHeader(LogtubeConstants.DUBBO_CRID_KEY, Logtube.getProcessor().getCrid());
                            messageBuilder.setHeader(LogtubeConstants.DUBBO_CRSRC_KEY, Logtube.getProcessor().getProject());
                            return messageBuilder.build();
                        }

                    });
                }
                return bean;
            }

        };
    }
}