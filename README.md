# Live Football World Cup Scoreboard Library

## Overview

This Live Football Scoreboard Library is designed to simulate real-time tracking of football matches. It provides functionalities to start new matches, update ongoing match scores, finish matches, and generate a summary of matches ordered by their total score and start times. The current version of the library is v0.2.0.
## Version

The current version of the Live Football Scoreboard Library is **v0.2.0**. This version builds upon the initial functionalities with the introduction of functional programming concepts, specifically for implementing flexible scoring strategies. It allows for dynamic changes in scoring rules based on match context, such as switching to extra time rules.

## Features

- **Start a Match**: Begin a match between two teams with an initial score of 0 – 0. Matches can now be started with specified scoring strategies to accommodate different phases of the game (e.g., normal time, extra time).
- **Update Score**: Update the score of ongoing matches, with the applied scoring strategy determining how scores are modified. This offers flexibility for different types of matches and scoring rules.
- **Change Scoring Strategy**: Dynamically change the scoring strategy of a match in progress to adapt to different situations, such as moving from normal time to extra time scoring.
- **Finish Match**: Mark a match as finished, removing it from the list of active matches on the scoreboard. This concludes the tracking of the match in the system.
- **Get Summary**: Retrieve a sorted summary of active matches. The summary is sorted first by total score, and for matches with equal scores, by start time. This provides a clear overview of ongoing matches, making it easy to see which matches are most competitive or interesting.

## Design Patterns, Principles and Good Practices

The library utilizes several design patterns and adheres to SOLID principles to ensure code quality, readability, and maintainability:

### Design Patterns

- **Builder Pattern**: Facilitates constructing complex `Match` objects. This pattern is crucial for creating instances with multiple parameters, avoiding confusion with multiple constructors.
- **Abstract Factory Pattern**: Enables the instantiation of `Match` objects with pre-defined configurations, allowing for flexibility in creating matches for different types of sports.
- **Observer Pattern**: Supports notifying interested parties of changes in match states, ensuring that components such as the scoreboard UI are updated in real time.
- **Command Pattern**: Used to encapsulate all requests to the scoreboard as executable commands, allowing for undo operations and logging changes.
- **State Pattern**: Manages changes in match state (e.g., from not started, in progress, to finished) in a robust and extensible manner.
- **Strategy Pattern**: Employs flexible scoring strategies that can adapt to various game rules or phases, such as regular time or extra time. The library enhances this pattern by incorporating functional programming principles, enabling the dynamic application of scoring strategies during a match. This approach not only allows for easy adjustments to scoring logic based on the match context but also streamlines the implementation of diverse and complex scoring rules with minimal code changes.

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

## How to Use

This library offers a flexible way to handle football matches, including starting matches, updating scores, finishing matches, and retrieving summaries of ongoing matches. Additionally, it supports different scoring strategies to accommodate various match scenarios, such as normal time, extra time, or custom rules.

## How to Use

This library offers a flexible way to handle football matches, including starting matches, updating scores, finishing matches, and retrieving summaries of ongoing matches. Additionally, it supports different scoring strategies to accommodate various match scenarios, such as normal time, extra time, or custom rules.

### Starting a Match

You can start a match by directly creating a `Match` object with a factory or by using the `Scoreboard` class to manage it for you.

```java
Scoreboard scoreboard = new Scoreboard();
MatchFactory footballMatchFactory = new FootballMatchFactory();

// Starting a match with the default scoring strategy using the factory
MatchInterface match = footballMatchFactory.createMatchBuilder("Home Team", "Away Team").build();
scoreboard.addMatch(match);

// Starting a match with a specific scoring strategy using the scoreboard
scoreboard.startMatch("Home Team", "Away Team", ScoringStrategies.footballNormalTimeScoringStrategy);

// Starting a match with a specific strategy mode
MatchInterface match = footballMatchFactory.createMatchBuilder("Home Team", "Away Team")
                                            .scoringStrategyMode(ScoringStrategyMode.CLASSIC) // or FUNCTIONAL1, FUNCTIONAL2
                                            .build();

// Start a match using the classic strategy pattern
MatchFactory footballMatchFactory = new FootballMatchFactory();
MatchInterface match = footballMatchFactory.createMatchBuilder("Home Team", "Away Team")
                                            .scoringStrategyMode(ScoringStrategyMode.CLASSIC) 
                                            .scoringStrategy(ScoringStrategy.forFootballNormalTime())
                                            .build();
scoreboard.addMatch(match);

// Start a match using the first functional strategy approach
MatchInterface match = footballMatchFactory.createMatchBuilder("Home Team", "Away Team")
                                            .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL1) 
                                            .scoringStrategyFunctional1(ScoringStrategiesFunctional1.footballNormalTimeScoringStrategy)
                                            .build();
scoreboard.addMatch(match);

// Start a match using the second functional strategy approach with enums
MatchInterface match = footballMatchFactory.createMatchBuilder("Home Team", "Away Team")
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

// Finishing a match and removing it from the active scoreboard
scoreboard.finishMatch(match);

// Retrieving a summary of ongoing matches, sorted by total score and start time
List<MatchInterface> summary = scoreboard.getSummary();
