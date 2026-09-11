package com.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingOrderFilter extends AbstractGatewayFilterFactory<LoggingOrderFilter.Config> {

    public LoggingOrderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            //Pre execution route filter
            log.info("Pre execution : Order route filter {}", exchange.getRequest().getURI());

            //Post execution route filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Post execution : Order route filter {}", exchange.getResponse().getStatusCode());
            }));
        };
    }

    public static class Config {
    }
}
