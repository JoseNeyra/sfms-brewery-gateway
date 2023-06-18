package com.joseneyra.sfmsbrewerygateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("google")
@Configuration
public class GoogleConfig {

    @Bean
    public RouteLocator googleRouteConfig(RouteLocatorBuilder builder) {
        return builder.routes()
                .route( "google", r -> r.path("/googlesearch1")
                        .filters(f -> f.rewritePath("/googlesearch1(?<segment>/?.*)", "/${segment}"))       // Rewrite filter: Rewrites the path
                        .uri("https://google.com"))
                .build();
    }
}
