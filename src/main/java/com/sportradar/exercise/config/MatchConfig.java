package com.sportradar.exercise.config;

import com.sportradar.exercise.abstract_factory.BasketballMatchFactory;
import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.abstract_factory.MatchFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MatchConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "sport.type", havingValue = "football", matchIfMissing = true)
    public MatchFactory footballMatchFactory() {
        return new FootballMatchFactory();
    }

    @Bean
    @ConditionalOnProperty(name = "sport.type", havingValue = "basketball")
    public MatchFactory basketballMatchFactory() {
        return new BasketballMatchFactory();
    }
}