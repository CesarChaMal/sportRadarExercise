# Live Football World Cup Scoreboard Library

## Overview

This Live Football Scoreboard Library provides a simulation of real-time tracking for football matches. It offers functionalities to start matches, update scores, finish matches, and generate a sorted summary of matches. The library is now upgraded to version v0.3.0, which introduces support for basketball alongside improved strategies for scoring and state management.

## Version

The current version of the Live Football Scoreboard Library is **v0.3.0**. This version introduces basketball match management and enhancements in the scoring system to accommodate different sports.

## Features

### Match Management
- **Start a Match**: Initiate a football or basketball match specifying initial scores and strategies.
- **Update Score**: Dynamically update the score during a match based on defined scoring strategies.
- **Finish Match**: Conclude a match and update its status to finished.

### Scoring Strategies
- **Flexible Scoring Strategies**: Define scoring behaviors that can change dynamically during the match to reflect different phases like normal time or overtime.
- **Sport-Specific Strategies**: Implement sport-specific scoring strategies for football and basketball.

### Match Summarization
- **Get Summary**: Retrieve a summary of matches, sorted by total score and start time, providing insights into the most competitive and high-scoring games.

### Observability
- **Real-time Updates**: Utilize the observer pattern to notify all registered observers about changes in the match state or score.

## Design Patterns, Principles and Good Practices

The library utilizes several design patterns and adheres to SOLID principles to ensure code quality, readability, and maintainability:

### Design Patterns

- **Builder Pattern**: Facilitates constructing complex `Match` objects. This pattern is crucial for creating instances with multiple parameters, avoiding confusion with multiple constructors.
- **Abstract Factory Pattern**: Enables the instantiation of `Match` objects with pre-defined configurations, allowing for flexibility in creating matches for different types of sports.
- **Observer Pattern**: Supports notifying interested parties of changes in match states, ensuring that components such as the scoreboard UI are updated in real time.
- **Command Pattern**: Used to encapsulate all requests to the scoreboard as executable commands, allowing for undo operations and logging changes.
- **State Pattern**: Manages changes in match state (e.g., from not started, in progress, to finished) in a robust and extensible manner.
- **Strategy Pattern**: Employs flexible scoring strategies that can adapt to various game rules or phases, such as regular time or extra time. The library enhances this pattern by incorporating functional programming principles, enabling the dynamic application of scoring strategies during a match. This approach not only allows for easy adjustments to scoring logic based on the match context but also streamlines the implementation of diverse and complex scoring rules with minimal code changes.
- **Singleton Pattern**: Ensures there is a single instance of the Scoreboard class throughout the application, providing a global point of access to it. This pattern is used to control scoreboard interactions and maintain a consistent state across different parts of the system.
- **Decorator Pattern**: Enhances or modifies the behavior of Match objects during overtime without altering the original classes. This pattern allows the addition of new functionalities such as different scoring strategies or rules specific to overtime periods. Specific implementations like FootballMatchOvertime or BasketballMatchOvertime can extend from base decorator implementations to encapsulate and augment behaviors dynamically.

### SOLID Principles

The design and implementation strive to adhere to SOLID principles for object-oriented design, focusing on:
- Single Responsibility Principle
- Open/Closed Principle
- Liskov Substitution Principle
- Interface Segregation Principle
- Dependency Inversion Principle

### Recommendations from Effective Java

- **Consider static factory methods instead of constructors**: Provides clarity and flexibility in instance creation. Applied in the creation of scoring strategies and state transitions.
- **Use Builder when faced with many constructor parameters**: Applied in the creation of `Match` instances, enhancing code readability and maintainability.
- **Favor composition over inheritance**: Ensures the system remains flexible and prevents issues related to tight coupling.
- **Design and document for inheritance or else prohibit it**: The library's core classes are designed to be easily extendable or securely closed for modification.
- **Prefer interfaces to abstract classes**: Allows the library to define multiple implementations for strategies and states, adhering to the open/closed principle.
- **Prefer lists to arrays**: Lists provide more flexibility and safety, and are used throughout the library for managing collections of matches and observers.
- **Use of Immutable Collections**: To prevent unintended modifications, `Collections.unmodifiableList` is used when returning lists from the library's methods.
- **Use Enum instead of int constants**: Enums are used for defining types and options that are known at compile time to ensure type safety and clarity.

### Utilization of Functional Programming

This library incorporates functional programming principles to enhance readability, maintainability, and code efficiency:

- **Functional Programming**: Incorporates the use of Optional for better handling of null values, ensuring that operations on potentially null objects are handled more safely. The functional strategy selector further exemplifies the application of functional programming by enabling dynamic strategy selection based on runtime criteria.
- **Comparator Chains**: Utilizes `Comparator` chains for sorting matches, leveraging lambda expressions and method references for concise and readable sorting logic.
- **Optional**: Employs `Optional` for safe retrieval of matches, minimizing the risk of `NullPointerException` and simplifying conditional logic.
- **Stream API**: Leverages the Stream API for filtering and collecting matches, showcasing the power of streams in processing collections.

## Development Approach

- **Test-Driven Development (TDD)**: Development began with writing tests for each functionality, ensuring each piece of code is properly tested before implementation.
- **Clean Code**: Effort was made to write readable, simple, and refactored code to enhance maintainability and understandability.

## Requirements

### Java Version
This project is built using **Java SE 22**. Ensure that you have the JDK for Java 22 installed on your machine to compile and run the project successfully.

### Maven
The project uses Maven for dependency management and builds processes. It is recommended to use the Maven Wrapper included in the project to ensure the correct version of Maven is utilized.

## Getting Started

### Running the Project

1. **Clone the Repository**
- First, clone the repository to your local machine using Git:
```bash
git clone https://github.com/CesarChaMal/sportRadarExercise.git
cd SportRadarExercise
  ```

2. **Building the Project**
- Once the Maven Wrapper is set up, you can build the project using the following command. This command will clean the previous builds and compile the project, run tests, and package the application:
```bash
./mvnw clean install
```

- On Windows systems, use `mvnw.cmd` instead of `./mvnw`:
```bash
mvnw.cmd clean install
```

### Run Specific Tests
To run specific tests during the development process, you can use the following Maven command:
```bash
./mvnw test -Dtest=ClassNameTest
```
## How to Use

This library offers a flexible way to handle football matches, including starting matches, updating scores, finishing matches, and retrieving summaries of ongoing matches. Additionally, it supports different scoring strategies to accommodate various match scenarios, such as normal time, extra time, or custom rules.

### Starting a Match

You can start a match by directly creating a `Match` object with a factory or by using the `Scoreboard` class to manage it for you.

```java
Scoreboard scoreboard = new Scoreboard();
MatchFactory footballMatchFactory = new FootballMatchFactory();

// Create teams for the football match
Team<FootballPlayer> homeFootballTeam = new FootballTeam.Builder().name("Home Football Team").build();
Team<FootballPlayer> awayFootballTeam = new FootballTeam.Builder().name("Away Football Team").build();

// Start a football match
MatchFactory<FootballMatch> footballMatchFactory = new FootballMatchFactory();
MatchInterface footballMatch = footballMatchFactory.createMatchBuilder(homeFootballTeam, awayFootballTeam).build();
scoreboard.addMatch(footballMatch);

// Create teams for the basketball match
Team<BasketballPlayer> homeBasketballTeam = new BasketballTeam.Builder().name("Home Basketball Team").build();
Team<BasketballPlayer> awayBasketballTeam = new BasketballTeam.Builder().name("Away Basketball Team").build();

// Start a basketball match
MatchFactory<BasketballMatch> basketballMatchFactory = new BasketballMatchFactory();
MatchInterface basketballMatch = basketballMatchFactory.createMatchBuilder(homeBasketballTeam, awayBasketballTeam).build();
scoreboard.addMatch(basketballMatch);

// Start a football match with a specific scoring strategy mode
MatchFactory<FootballMatch> footballMatchFactory = new FootballMatchFactory();
MatchInterface footballMatch = footballMatchFactory.createMatchBuilder(homeFootballTeam, awayFootballTeam)
                                            .scoringStrategyMode(ScoringStrategyMode.CLASSIC) // or FUNCTIONAL1, FUNCTIONAL2
                                            .build();
scoreboard.addMatch(footballMatch);

// Start a match using the classic strategy pattern
MatchFactory footballMatchFactory = new FootballMatchFactory();
MatchInterface match = footballMatchFactory.createMatchBuilder(homeFootballTeam, awayFootballTeam)
                                            .scoringStrategyMode(ScoringStrategyMode.CLASSIC) 
                                            .scoringStrategy(ScoringStrategy.forFootballNormalTime())
                                            .build();
scoreboard.addMatch(match);

// Start a match using the first functional strategy approach
MatchInterface match = footballMatchFactory.createMatchBuilder(homeFootballTeam, awayFootballTeam)
                                            .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL1) 
                                            .scoringStrategyFunctional1(ScoringStrategiesFunctional1.footballNormalTimeScoringStrategy)
                                            .build();
scoreboard.addMatch(match);

// Start a match using the second functional strategy approach with enums
MatchInterface match = footballMatchFactory.createMatchBuilder(homeFootballTeam, awayFootballTeam)
                                            .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL2) 
                                            .scoringStrategyFunctional2(ScoringStrategyType.FOOTBALL_NORMAL_TIME)
                                            .build();
scoreboard.addMatch(match);

// Switch to extra time scoring strategy for a match using the classic strategy
matchClassic.setScoringStrategy(ScoringStrategy.forFootballExtraTime());

// For functional strategy 1
matchFunctional1.setScoringStrategyFunctional1(ScoringStrategiesFunctional1.footballExtraTimeScoringStrategy);

// For functional strategy 2
matchFunctional2.setScoringStrategyFunctional2(ScoringStrategyType.FOOTBALL_EXTRA_TIME);

// Updating score with the default scoring strategy
scoreboard.updateScore(match, 1, 0);

// Finishing a match and removing it from the active scoreboard****
scoreboard.finishMatch(match);

// Retrieving a summary of ongoing matches, sorted by total score and start time
List<MatchInterface> summary = scoreboard.getSummary();

// Create a Match and Register Observers
// Create teams
Team<FootballPlayer> homeFootballTeam = new FootballTeam.Builder().name("Home Football Team").build();
Team<FootballPlayer> awayFootballTeam = new FootballTeam.Builder().name("Away Football Team").build();

// Create a football match
MatchFactory<FootballMatch> footballMatchFactory = new FootballMatchFactory();
FootballMatch footballMatch = footballMatchFactory.createMatchBuilder(homeFootballTeam, awayFootballTeam)
                                                   .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
                                                   .build();

// Create and register an observer
MatchObserver matchObserver = new MatchObserver();
footballMatch.registerObserver(matchObserver);

// Add Events During the Match
// Simulate a goal event
FootballPlayer scorer = new FootballPlayer.Builder().name("John Doe").team(homeFootballTeam).build();
FootballPlayer assistant = new FootballPlayer.Builder().name("Jane Doe").team(homeFootballTeam).build();
((FootballEventManager)footballMatch.getEventManager()).addGoalEvent(scorer, assistant);

// Notify all observers about the goal event
footballMatch.notifyObservers(new MatchChangeEvent(footballMatch, EventType.GOAL));

// Using the Basketball Event Manager
// Create basketball teams
Team<BasketballPlayer> homeBasketballTeam = new BasketballTeam.Builder().name("Home Basketball Team").build();
Team<BasketballPlayer> awayBasketballTeam = new BasketballTeam.Builder().name("Away Basketball Team").build();

// Create a basketball match
MatchFactory<BasketballMatch> basketballMatchFactory = new BasketballMatchFactory();
BasketballMatch basketballMatch = basketballMatchFactory.createMatchBuilder(homeBasketballTeam, awayBasketballTeam)
                                                        .build();

// Register an observer for basketball events
basketballMatch.getEventManager().registerObserver(matchObserver);

// Simulate points scored
BasketballPlayer scorerBasketball = new BasketballPlayer.Builder().name("James Smith").team(homeBasketballTeam).build();
((BasketballEventManager)basketballMatch.getEventManager()).addPointsScoredEvent(scorerBasketball, 3);

// Notify all observers about the points scored event
basketballMatch.notifyObservers(new MatchChangeEvent(basketballMatch, EventType.POINTS_SCORED));
```

## Example: Using the Live Football World Cup Scoreboard Library

### Setting Up and Running a Football Match

This example demonstrates how to create a football match, score goals, end the match, and validate the outcomes using an observer to monitor events.

```java
package com.sportradar.exercise.match;

import com.sportradar.exercise.abstract_factory.FootballMatchFactory;
import com.sportradar.exercise.observer.MatchObserver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FootballMatchUsageExample {
    private FootballMatchFactory factory;
    private Team<FootballPlayer> homeTeam;
    private Team<FootballPlayer> awayTeam;
    private FootballMatch match;
    private MatchObserver observer;

    @Before
    public void setUp() {
        // Initialize factory and create teams
        factory = new FootballMatchFactory();
        homeTeam = new Team.Builder<FootballPlayer>().name("Team A").build();
        awayTeam = new Team.Builder<FootballPlayer>().name("Team B").build();

        // Add players to teams
        homeTeam.addPlayer(new FootballPlayer.Builder().name("John Doe").team(homeTeam).build());
        awayTeam.addPlayer(new FootballPlayer.Builder().name("Joe Smith").team(awayTeam).build());

        // Create the match and register an observer
        match = factory.createMatchBuilder(homeTeam, awayTeam).build();
        observer = new MatchObserver();
        match.registerObserver(observer);
    }

    @Test
    public void testMatchFlow() {
        // Start the match
        match.startMatch();

        // Simulate scoring events
        match.scoreGoal(homeTeam.getRoster().get(0), null);
        match.scoreGoal(homeTeam.getRoster().get(0), null);

        // End the match
        match.finishMatch();

        // Assertions to ensure the match flowed as expected
        assertEquals("Expected number of events should be correct", 6, match.getEvents().size());
        MatchEvent<?> firstGoalEvent = match.getEvents().stream()
                .filter(e -> e.getEventType() == EventType.GOAL)
                .findFirst()
                .orElse(null);

        assertNotNull("Goal event should exist", firstGoalEvent);
        assertEquals("First goal should be scored by Maradona", "John Doe", firstGoalEvent.getInvolvedPlayers().get(0).getName());
        assertTrue("Observer should have received the event", observer.isEventReceived());
    }
}
```
### Explanation

#### Setup Phase
- **Teams and Players**: The teams (`Team A` and `Team B`) and players (`John Doe` and `Joe Smith`) are created using builders, ensuring detailed setup before the match begins.
- **Observer Registration**: A `MatchObserver` is initialized and registered to the match to monitor and react to various match events.

#### Match Execution
- **Starting the Match**: The match is initiated, transitioning from a non-started to an in-progress state.
- **Scoring Events**: John Doe scores two goals. These events are dynamically added to the match's event list.
- **Ending the Match**: The match is concluded, and its status is updated to finished.

#### Assertions
- **Event Verification**: The test confirms that six events were correctly logged (starting, two goals, and ending the match, with each goal triggering a score update).
- **Goal Event Details**: The details of the first goal event are examined to verify that John Doe is correctly recorded as the scorer.
- **Observer Notification**: It is verified that the observer received notifications, demonstrating the observer pattern's effectiveness within the library.

This example serves as a comprehensive guide for leveraging the Live Football Scoreboard Library to manage and monitor football matches effectively, showcasing real-world application of match setup, event handling, and observer notifications.


