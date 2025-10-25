# Spring Boot Migration Guide

## Overview

This document outlines the migration strategy for integrating the SportRadar Exercise library with Spring Boot. The current codebase has excellent design patterns that complement Spring Boot with minimal refactoring required.

## Migration Analysis

### ✅ Patterns That Align Perfectly with Spring Boot

#### 1. Dependency Injection Ready
- **Current**: Manual singleton pattern (`Scoreboard.getInstance()`)
- **Spring Boot**: Replace with `@Service` and `@Autowired`
- **Refactoring**: **Minimal** - just add annotations

#### 2. Strategy Pattern → Spring Profiles/Conditional Beans
- **Current**: Three strategy implementations (Classic, Functional1, Functional2)
- **Spring Boot**: Use `@ConditionalOnProperty` or `@Profile`
- **Refactoring**: **None** - strategies become Spring beans

#### 3. Observer Pattern → Spring Events
- **Current**: Custom observer implementation
- **Spring Boot**: `@EventListener` and `ApplicationEventPublisher`
- **Refactoring**: **Low** - replace custom observers with Spring events

#### 4. Command Pattern → Spring Components
- **Current**: `UpdateScoreCommand`, `FinishMatchCommand`
- **Spring Boot**: Commands become `@Component` services
- **Refactoring**: **Minimal** - add annotations

### 🔄 Moderate Refactoring Areas

#### 1. Singleton Pattern → Spring Beans
```java
// Current
Scoreboard scoreboard = Scoreboard.getInstance(factory);

// Spring Boot
@Service
public class ScoreboardService {
    @Autowired
    private MatchFactory matchFactory;
}
```

#### 2. Factory Pattern → Spring Configuration
```java
// Current
MatchFactory factory = new FootballMatchFactory();

// Spring Boot
@Configuration
public class MatchConfig {
    @Bean
    @ConditionalOnProperty(name="sport.type", havingValue="football")
    public MatchFactory footballFactory() {
        return new FootballMatchFactory();
    }
}
```

## Spring Boot Benefits

### 1. Enhanced Concurrency
- Replace manual `ExecutorService` with `@Async`
- Use Spring's `TaskExecutor` configuration
- Built-in thread pool management

### 2. Configuration Management
```yaml
# application.yml
scoreboard:
  max-concurrent-matches: 5
  thread-pool-size: 4
  
sport:
  type: football
  strategy: classic
```

### 3. REST API Layer
```java
@RestController
@RequestMapping("/api/matches")
public class MatchController {
    @Autowired
    private ScoreboardService scoreboardService;
    
    @PostMapping
    public ResponseEntity<Match> startMatch(@RequestBody MatchRequest request) {
        return ResponseEntity.ok(scoreboardService.startMatch(request));
    }
}
```

## Refactoring Effort Estimation

| Component | Effort Level | Changes Required |
|-----------|--------------|------------------|
| **Core Domain Logic** | **None** | Keep as-is |
| **Design Patterns** | **Low** | Add Spring annotations |
| **Singleton Management** | **Medium** | Convert to Spring beans |
| **Concurrency** | **Low** | Replace with `@Async` |
| **Configuration** | **Low** | Move to `application.yml` |
| **Testing** | **Medium** | Add Spring Boot test annotations |

## Migration Strategy

### Phase 1: Core Spring Boot Setup (1-2 days)
```java
@SpringBootApplication
public class SportRadarApplication {
    public static void main(String[] args) {
        SpringApplication.run(SportRadarApplication.class, args);
    }
}
```

**Dependencies to add:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Phase 2: Convert Singletons to Services (2-3 days)
```java
@Service
public class ScoreboardService {
    private final MatchFactory matchFactory;
    private final MatchStorage<MatchInterface> matchStorage;
    
    public ScoreboardService(MatchFactory matchFactory, 
                           MatchStorage<MatchInterface> matchStorage) {
        this.matchFactory = matchFactory;
        this.matchStorage = matchStorage;
    }
}
```

**Key Changes:**
- Remove `getInstance()` methods
- Add `@Service`, `@Component`, `@Repository` annotations
- Use constructor injection instead of manual instantiation

### Phase 3: Strategy Pattern Integration (1 day)
```java
@Configuration
public class ScoringStrategyConfig {
    
    @Bean
    @ConditionalOnProperty(name="scoring.strategy", havingValue="classic")
    public ScoringStrategy classicStrategy() {
        return ScoringStrategy.forFootballNormalTime();
    }
    
    @Bean
    @ConditionalOnProperty(name="scoring.strategy", havingValue="functional")
    public BiConsumer<Match, int[]> functionalStrategy() {
        return ScoringStrategiesFunctional1.footballNormalTimeScoringStrategy;
    }
}
```

### Phase 4: Observer Pattern → Spring Events (1 day)
```java
// Current Observer
public class MatchObserver implements Observer<MatchChangeEvent> {
    @Override
    public void update(MatchChangeEvent event) {
        // Handle event
    }
}

// Spring Boot Event Listener
@Component
public class MatchEventListener {
    
    @EventListener
    public void handleMatchEvent(MatchChangeEvent event) {
        // Handle event
    }
}

// Event Publisher
@Service
public class MatchService {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void updateScore(Match match, int homeScore, int awayScore) {
        // Update logic
        eventPublisher.publishEvent(new MatchChangeEvent(match, EventType.SCORE_UPDATE));
    }
}
```

### Phase 5: Add REST API (1-2 days)
```java
@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {
    
    private final ScoreboardService scoreboardService;
    
    public MatchController(ScoreboardService scoreboardService) {
        this.scoreboardService = scoreboardService;
    }
    
    @PostMapping
    public ResponseEntity<MatchResponse> startMatch(@RequestBody StartMatchRequest request) {
        MatchInterface match = scoreboardService.startMatch(request.getHomeTeam(), request.getAwayTeam());
        return ResponseEntity.ok(new MatchResponse(match));
    }
    
    @PutMapping("/{matchId}/score")
    public ResponseEntity<Void> updateScore(@PathVariable Long matchId, 
                                          @RequestBody UpdateScoreRequest request) {
        scoreboardService.updateScore(matchId, request.getHomeScore(), request.getAwayScore());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/summary")
    public ResponseEntity<List<MatchSummary>> getSummary() {
        List<MatchInterface> matches = scoreboardService.getSummary();
        return ResponseEntity.ok(matches.stream()
            .map(MatchSummary::from)
            .collect(Collectors.toList()));
    }
}
```

### Phase 6: Testing Integration (1 day)
```java
@SpringBootTest
@TestPropertySource(properties = {
    "scoring.strategy=classic",
    "scoreboard.max-concurrent-matches=5"
})
class ScoreboardServiceIntegrationTest {
    
    @Autowired
    private ScoreboardService scoreboardService;
    
    @Test
    void testCompleteMatchLifecycle() {
        // Integration test with Spring context
    }
}

@WebMvcTest(MatchController.class)
class MatchControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ScoreboardService scoreboardService;
    
    @Test
    void testStartMatch() throws Exception {
        mockMvc.perform(post("/api/v1/matches")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "homeTeam": "Barcelona",
                    "awayTeam": "Madrid"
                }
                """))
            .andExpect(status().isOk());
    }
}
```

## Configuration Files

### application.yml
```yaml
spring:
  application:
    name: sportradar-scoreboard
  profiles:
    active: development

scoreboard:
  max-concurrent-matches: 5
  thread-pool-size: 4
  timeout-ms: 500

scoring:
  strategy: classic
  
sport:
  type: football

logging:
  level:
    com.sportradar.exercise: INFO
```

### application-production.yml
```yaml
scoreboard:
  max-concurrent-matches: 20
  thread-pool-size: 10

logging:
  level:
    com.sportradar.exercise: WARN
```

## Benefits of Migration

### 1. **Preserved Design Patterns**
- All existing patterns remain intact
- Enhanced by Spring's capabilities
- No architectural changes required

### 2. **Enhanced Features**
- **Auto-configuration**: Automatic bean wiring
- **Health checks**: Built-in monitoring endpoints
- **Metrics**: Actuator integration for performance monitoring
- **Security**: Easy integration with Spring Security
- **Testing**: Comprehensive testing framework

### 3. **Operational Benefits**
- **Configuration management**: Externalized configuration
- **Profiles**: Environment-specific settings
- **Logging**: Structured logging with Logback
- **Monitoring**: Built-in health and metrics endpoints

## Recommended Approach

1. **Keep existing patterns** - they work perfectly with Spring Boot
2. **Add Spring annotations gradually** - minimal code changes
3. **Introduce REST layer** on top of existing logic
4. **Use Spring's dependency injection** instead of manual instantiation
5. **Leverage Spring Boot's auto-configuration** for common concerns

## Total Migration Effort: 5-7 days

**Conclusion**: The well-designed codebase integrates seamlessly with Spring Boot with minimal refactoring. Existing patterns are Spring Boot-friendly and enhanced by the framework's capabilities.