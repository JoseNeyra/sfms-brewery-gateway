package com.joseneyra.sfmsbrewerygateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("local-discovery")
@Configuration
public class LoadBalancedRoutesConfig {

    // Here the lb means load balancer, this notation will look in the eureka service for the corresponding uri values
    @Bean
    public RouteLocator loadBalancedRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("beer-service", r -> r.path("/api/v1/beer*","/api/v1/beer/*", "/api/v1/beerUpc/*")
                        .uri("lb://beer-service"))
                .route("order-service", r-> r.path("/api/v1/customers/**")
                        .uri("lb://beer-order-service"))
                .route("inventory-service", r-> r.path("/api/v1/beer/*/inventory")
                        .filters(f -> f.circuitBreaker(c -> c.setName("inventoryCB")            // Setting up the circuit breaker to redirect to the failover-service. This is configured automatically by spring boot because we're using resilience4J
                                .setFallbackUri("forward:/inventory-failover")
                                .setRouteId("inv-failover")))
                        .uri("lb://inventory-service"))
                .route("inventory-failover-service", r-> r.path("/inventory-failover/**")
                        .uri("lb://inventory-failover"))
                .build();
    }
}
