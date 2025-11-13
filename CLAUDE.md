# CLAUDE.md - AI Assistant Guide for SportRadar Exercise

## Project Overview

**Project Name:** Live Football World Cup Scoreboard Library
**Version:** v0.4.0
**Language:** Java SE 22
**Build System:** Maven
**Repository:** sportRadarExercise

This is a production-quality, thread-safe match scoring library supporting both football and basketball matches. It demonstrates advanced Java design patterns, concurrency mechanisms, and modern Java 22 features. The library provides real-time match scoring with comprehensive state management, multiple scoring strategy implementations, and extensive observability.

## Quick Reference

### Key Directories
- `/src/main/java/com/sportradar/exercise/` - Main source code (12 packages)
- `/src/test/java/com/sportradar/exercise/` - Comprehensive test suite (29 test files)
- `/doc/` - Project documentation
- Root - Build scripts and Maven configuration

### Build & Run Commands
```bash
# Build and test
./mvnw clean install

# Run tests only
./mvnw test

# Run specific test class
./mvnw test -Dtest=ClassNameTest

# Automated build (with Java 22 setup via SDKMAN)
./run.sh          # Unix/Linux/macOS
run.bat           # Windows
```

## Codebase Structure

### Package Organization (12 Main Packages)

#### 1. `abstract_factory/` - Factory Pattern
**Purpose:** Sport-specific match creation with preconfigured defaults

**Key Files:**
- `MatchFactory.java` - Generic factory interface
- `FootballMatchFactory.java` - Football match creation
- `BasketballMatchFactory.java` - Basketball match creation
- `MatchOrganizer.java` - Organizes match creation and management

**When to Use:**
- Creating new match types with consistent configuration
- Adding support for a new sport type

#### 2. `match/` - Core Match Entities (18 files)
**Purpose:** Core domain models for matches, teams, players, events

**Critical Classes:**
- `Match.java` (line ~25) - Abstract base class with Builder pattern
  - Thread-safe ID generation via `AtomicLong`
  - Supports 3 scoring strategy modes (CLASSIC, FUNCTIONAL1, FUNCTIONAL2)
  - Uses `ReentrantLock` for state protection
- `MatchInterface.java` - Composed interface (MatchInfo, Scorable, StateManageable, etc.)
- `FootballMatch.java` / `BasketballMatch.java` - Sport-specific implementations
- `Team.java`, `Player.java` - Generic base classes with type parameters
- `EventManager.java`, `CommonEventManager.java` - Event tracking
- `EventType.java` - Enum with 19+ event types
- `MatchEvent.java` - Generic event object

**Patterns Used:**
- Builder pattern for complex object construction
- Template method in abstract classes
- Generics for type-safe team/player relationships

#### 3. `scoring/` - Scoreboard Management
**Purpose:** Central scoreboard singleton managing all matches

**Key File:**
- `Scoreboard.java` (line ~25) - Singleton with thread-safe operations
  - `getInstance(MatchFactory)` - Get singleton instance
  - Semaphore-controlled match starts (max 5 concurrent)
  - `ExecutorService` with 4 fixed threads
  - Delegates to `MatchStorage` for persistence
  - Uses `CommandExecutor` for operations

**Thread Safety:**
- `ReentrantLock` for singleton initialization
- `Semaphore` limiting concurrent match starts
- `ExecutorService` with 500ms timeout for async operations

#### 4. `state/` - State Pattern (6 files)
**Purpose:** Match lifecycle state machine

**Key Files:**
- `MatchStateManager.java` (line ~20) - **CRITICAL FOR STATE MANAGEMENT**
  - Uses `EnumMap` for O(1) state-to-strategy mapping
  - Thread-safe with `ReentrantLock`
  - Validates transitions before allowing state changes
- `MatchState.java` - Interface defining state behavior
- `NotStartedState.java`, `InProgressState.java`, `InPauseState.java`, `FinishedState.java`

**Valid State Transitions:**
```
NOT_STARTED → IN_PROGRESS → FINISHED
           ↓               ↗
        IN_PAUSE ──────────┘
```

**Convention:** Always validate state before operations that depend on state

#### 5. `strategy/` - Classic Strategy Pattern (9 files)
**Purpose:** Pluggable scoring logic (Classic OOP approach)

**Key Files:**
- `ScoringStrategy.java` - Interface with static factory methods
- `ScoringStrategyMode.java` - Enum: CLASSIC, FUNCTIONAL1, FUNCTIONAL2
- `DefaultScoringStrategy.java` - Default implementation
- Sport/time-specific strategies (FootballNormalTime, FootballExtraTime, etc.)

**When to Use:**
- Traditional OOP approach preferred
- Need full control over strategy class hierarchy

#### 6. `strategy_functional1/` - Functional Approach 1
**Purpose:** BiConsumer-based functional strategies

**Key File:**
- `ScoringStrategiesFunctional1.java` - Static `BiConsumer<Match, int[]>` instances
  - `defaultScoringStrategy`
  - `forFootballNormalTimeScoringStrategy`
  - `forFootballExtraTimeScoringStrategy`
  - Basketball variants

**When to Use:**
- Prefer functional programming style
- Simple lambda-based strategies

#### 7. `strategy_functional2/` - Functional Approach 2
**Purpose:** Enum-based functional dispatch

**Key Files:**
- `ScoringStrategyType.java` - Enum (DEFAULT, FOOTBALL_NORMAL_TIME, etc.)
- `ScoringStrategiesFunctional2.java` - `EnumMap`-based dispatch

**When to Use:**
- Type-safe strategy selection
- O(1) lookup performance critical

#### 8. `command/` - Command Pattern (5 files)
**Purpose:** Encapsulated operations for logging, undo/redo

**Key Files:**
- `MatchCommand.java` - Interface with `execute()`
- `CommandExecutor.java` - Manages command execution
- `StartMatchCommand.java`, `UpdateScoreCommand.java`, `FinishMatchCommand.java`

**Convention:** All scoreboard operations should go through commands

#### 9. `observer/` - Observer Pattern (6 files)
**Purpose:** Real-time match event notifications

**Key Files:**
- `Observer.java` - Generic observer interface
- `Subject.java` - Subject interface for observer management
- `MatchEventNotifier.java` - Notifier implementation
- `MatchChangeEvent.java` - Event object
- `MatchObserver.java` - Concrete observer

**Usage:**
```java
MatchObserver observer = new MatchObserver();
match.registerObserver(observer);
match.notifyObservers(new MatchChangeEvent(match, EventType.GOAL));
```

#### 10. `decorator/` - Decorator Pattern (2 files)
**Purpose:** Add overtime functionality dynamically

**Key Files:**
- `MatchDecorator.java` - Base decorator wrapping Match
- `MatchOverTime.java` - Adds overtime capability

**When to Use:**
- Need to switch strategies from normal to extra time dynamically

#### 11. `analytics/` - Match Analytics
**Purpose:** Match summary generation with sorting

**Key File:**
- `MatchSummaryGenerator.java`
  - O(n log n) dual-sort: total score DESC, creation time DESC
  - Returns `List<MatchInterface>` (unmodifiable)

#### 12. `storage/` - Data Storage Abstraction (2 files)
**Purpose:** Pluggable storage implementations

**Key Files:**
- `MatchStorage.java` - Interface (addMatch, removeMatch, getMatchById, etc.)
- `InMemoryMatchStorage.java` - Thread-safe in-memory implementation
  - `ConcurrentHashMap` for lock-free reads
  - `ReentrantReadWriteLock` for coordinated access

**Convention:** Always use storage interface, never direct collection manipulation

#### 13. `timing/` - Time-Based Functionality
**Purpose:** Time-aware match tracking

**Key File:**
- `TimedMatch.java` - Interface for `getStartTime()`, `getCreationTime()`

## Development Workflows

### Adding a New Feature

1. **Start with Tests** (TDD Approach)
   ```bash
   # Create test file first
   src/test/java/com/sportradar/exercise/<package>/<Feature>Test.java

   # Run specific test
   ./mvnw test -Dtest=<Feature>Test
   ```

2. **Implement Feature**
   - Follow existing package structure
   - Use appropriate design patterns (see Pattern Decision Matrix below)
   - Ensure thread safety if applicable

3. **Validate**
   ```bash
   # Run all tests
   ./mvnw test

   # Build full project
   ./mvnw clean install
   ```

### Pattern Decision Matrix

| Scenario | Pattern to Use | Package | Example |
|----------|----------------|---------|---------|
| Creating sport-specific matches | Abstract Factory | `abstract_factory/` | `FootballMatchFactory` |
| Complex object construction | Builder | `match/` | `Match.Builder` |
| Pluggable scoring logic (OOP) | Strategy (Classic) | `strategy/` | `ScoringStrategy` |
| Pluggable scoring logic (Functional) | Strategy (Functional) | `strategy_functional1/2/` | `BiConsumer` or Enum |
| Match lifecycle management | State | `state/` | `MatchStateManager` |
| Encapsulated operations | Command | `command/` | `StartMatchCommand` |
| Real-time notifications | Observer | `observer/` | `MatchObserver` |
| Dynamic capability addition | Decorator | `decorator/` | `MatchOverTime` |
| Data access abstraction | Repository/DAO | `storage/` | `MatchStorage` |
| Single global instance | Singleton | `scoring/` | `Scoreboard` |

### Common Development Tasks

#### Adding a New Sport Type

1. Create new match class extending `Match`:
   ```java
   // src/main/java/com/sportradar/exercise/match/NewSportMatch.java
   public class NewSportMatch extends Match {
       protected NewSportMatch(Builder builder) {
           super(builder);
       }
       // Sport-specific methods
   }
   ```

2. Create factory:
   ```java
   // src/main/java/com/sportradar/exercise/abstract_factory/NewSportMatchFactory.java
   public class NewSportMatchFactory implements MatchFactory<NewSportMatch> {
       @Override
       public Match.Builder<NewSportMatch> createMatchBuilder(Team<?> homeTeam, Team<?> awayTeam) {
           // Return configured builder
       }
   }
   ```

3. Add sport-specific scoring strategies in `strategy/` package

4. Create comprehensive tests

#### Adding a New Event Type

1. Add to `EventType` enum:
   ```java
   // src/main/java/com/sportradar/exercise/match/EventType.java
   NEW_EVENT_TYPE
   ```

2. Update appropriate EventManager:
   ```java
   // src/main/java/com/sportradar/exercise/match/FootballEventManager.java
   public void addNewEventType(Player player) {
       addEvent(new MatchEvent<>(EventType.NEW_EVENT_TYPE, player));
   }
   ```

3. Add test coverage in relevant test file

#### Modifying State Transitions

**CRITICAL:** State transitions are managed in `MatchStateManager.java`

1. Locate the state class in `state/` package
2. Modify the appropriate state method
3. Update `MatchStateManager` if adding new transitions
4. **MUST** add comprehensive tests in `StateTest.java` and `MatchStateManagerTest.java`

## Key Conventions

### Code Style

1. **Naming Conventions:**
   - Classes: PascalCase (e.g., `MatchStateManager`)
   - Methods: camelCase (e.g., `startMatch()`)
   - Constants: UPPER_SNAKE_CASE (e.g., `MAX_CONCURRENT_STARTS`)
   - Packages: lowercase (e.g., `abstract_factory`)

2. **Builder Pattern Usage:**
   ```java
   // Always use builders for complex objects
   Match match = factory.createMatchBuilder(homeTeam, awayTeam)
       .scoringStrategyMode(ScoringStrategyMode.CLASSIC)
       .scoringStrategy(ScoringStrategy.forFootballNormalTime())
       .build();
   ```

3. **Immutability:**
   - Return `Collections.unmodifiableList()` for lists
   - Use `final` for fields that shouldn't change
   - Prefer immutable objects where possible

4. **Null Safety:**
   - Use `Optional<T>` for potentially null returns
   - Validate parameters at method entry
   - Throw `IllegalArgumentException` for invalid inputs

5. **Exception Handling:**
   - Use specific exception types
   - Don't catch exceptions you can't handle
   - Log exceptions before re-throwing (where appropriate)

### Thread Safety Conventions

**CRITICAL:** This codebase has extensive concurrency mechanisms

1. **Lock Usage:**
   ```java
   private final Lock lock = new ReentrantLock();

   public void criticalSection() {
       lock.lock();
       try {
           // Protected code
       } finally {
           lock.unlock(); // ALWAYS in finally block
       }
   }
   ```

2. **Semaphore Pattern:**
   ```java
   // In Scoreboard.java
   if (!matchStartSemaphore.tryAcquire()) {
       return false; // Max concurrent starts reached
   }
   try {
       // Start match
   } finally {
       // Release in finishMatch()
   }
   ```

3. **ExecutorService Pattern:**
   ```java
   Future<Boolean> future = executorService.submit(() -> {
       // Async operation
   });
   future.get(500, TimeUnit.MILLISECONDS); // Always set timeout
   ```

4. **Atomic Operations:**
   ```java
   // Use AtomicLong for counters
   private static final AtomicLong idGenerator = new AtomicLong(0);
   long id = idGenerator.getAndIncrement();
   ```

5. **ConcurrentHashMap:**
   - Used in `InMemoryMatchStorage` for lock-free reads
   - No external synchronization needed for basic operations

### Testing Conventions

1. **Test Organization:**
   - Unit tests: `src/test/java/.../ClassTest.java`
   - Integration tests: `src/test/java/.../IntegrationTest.java`
   - Concurrency tests: `src/test/java/.../*ConcurrencyTest.java`

2. **Test Structure:**
   ```java
   @Before
   public void setUp() {
       // Initialize test fixtures
   }

   @Test
   public void testMethodName_Scenario_ExpectedBehavior() {
       // Arrange
       // Act
       // Assert
   }
   ```

3. **Concurrency Testing:**
   ```java
   // Use CountDownLatch for concurrent test coordination
   CountDownLatch latch = new CountDownLatch(threadCount);
   ExecutorService executor = Executors.newFixedThreadPool(threadCount);
   // Submit tasks
   latch.await();
   executor.shutdown();
   ```

4. **Mocking:**
   - Use Mockito 3.6.0 for mocking dependencies
   - Mock external dependencies, not domain objects
   - Verify behavior, not implementation

### Strategy Selection Guidelines

**When to use each strategy mode:**

1. **CLASSIC (Traditional OOP):**
   - Need inheritance hierarchy
   - Strategy has complex state
   - Traditional team preference
   - Example: Custom validation logic in strategy

2. **FUNCTIONAL1 (BiConsumer):**
   - Simple lambda-based strategies
   - No strategy state needed
   - Functional programming preference
   - Example: Direct score assignment

3. **FUNCTIONAL2 (Enum-based):**
   - Type-safe strategy selection required
   - Performance critical (O(1) lookup)
   - Fixed set of strategies known at compile time
   - Example: Well-defined sport/time combinations

## Architecture Patterns

### SOLID Principles Application

1. **Single Responsibility Principle (SRP):**
   - Each class has one reason to change
   - Example: `MatchStateManager` only manages state, doesn't handle scoring

2. **Open/Closed Principle (OCP):**
   - Open for extension: Add new strategies, states, sports
   - Closed for modification: Core logic stable
   - Example: Add new sport without modifying `Match` base class

3. **Liskov Substitution Principle (LSP):**
   - Subclasses honor superclass contracts
   - Example: `FootballMatch` and `BasketballMatch` are interchangeable as `Match`

4. **Interface Segregation Principle (ISP):**
   - Multiple specific interfaces vs. one monolithic interface
   - Example: `MatchInterface` composes `MatchInfo`, `Scorable`, `StateManageable`, etc.

5. **Dependency Inversion Principle (DIP):**
   - Depend on abstractions, not concretions
   - Example: `Scoreboard` depends on `MatchStorage` interface, not `InMemoryMatchStorage`

### Concurrency Architecture

**Synchronization Strategy:**

```
┌─────────────────────────────────────────┐
│         Scoreboard (Singleton)          │
│  ┌────────────────────────────────────┐ │
│  │ Semaphore (5 permits)              │ │ ← Max 5 concurrent starts
│  │ ExecutorService (4 threads)        │ │ ← Fixed thread pool
│  │ ReentrantLock (singleton init)     │ │ ← Thread-safe initialization
│  └────────────────────────────────────┘ │
└─────────────────────────────────────────┘
             │
             ├─► MatchStorage (ConcurrentHashMap + ReadWriteLock)
             │
             ├─► CommandExecutor
             │        └─► Individual Commands (async execution)
             │
             └─► Match (ReentrantLock for state changes)
                      └─► MatchStateManager (ReentrantLock for transitions)
```

**Performance Characteristics:**
- State lookup: O(1) via EnumMap
- Storage operations: O(1) average via ConcurrentHashMap
- Match summary: O(n log n) via Stream sorting
- Score update: O(1) direct field update

### Data Flow

**Starting a Match:**
```
User → Scoreboard.startMatch()
    → Semaphore.tryAcquire() [Check < 5 concurrent]
    → ExecutorService.submit(StartMatchCommand)
    → StartMatchCommand.execute()
    → MatchFactory.createMatch()
    → Match.startMatch()
    → MatchStateManager.startMatch()
    → State transition: NOT_STARTED → IN_PROGRESS
    → MatchStorage.addMatch()
    → Observers notified
```

**Updating Score:**
```
User → Scoreboard.updateScore()
    → ExecutorService.submit(UpdateScoreCommand)
    → UpdateScoreCommand.execute()
    → Match.updateScore()
    → Check state: must be IN_PROGRESS
    → Apply scoring strategy (CLASSIC/FUNCTIONAL1/FUNCTIONAL2)
    → Update Score object
    → Observers notified
```

## Testing Strategy

### Test Coverage (29 Test Files)

**Unit Tests:**
- `match/*Test.java` - Core domain logic
- `strategy/*Test.java` - All 3 strategy implementations
- `state/*Test.java` - State transitions and edge cases
- `storage/InMemoryMatchStorageTest.java` - Storage operations
- `observer/ObserverTest.java` - Observer notifications
- `decorator/DecoratorTest.java` - Decorator functionality
- `analytics/MatchSummaryGeneratorTest.java` - Summary logic

**Integration Tests:**
- `IntegrationTest.java` - **CRITICAL**: End-to-end match lifecycle
- `match/FootballMatchUsageExample.java` - Usage scenarios
- `observer/MatchObserverIntegrationTest.java` - Observer integration

**Concurrency Tests:**
- `scoring/ScoreboardConcurrencyTest.java` - Concurrent operations
- `match/*SummaryConcurrencyTest.java` - Summary generation under load

**Error Handling:**
- `ErrorHandlingTest.java` - Edge cases and exceptions

### Running Tests

```bash
# All tests
./mvnw test

# Specific test class
./mvnw test -Dtest=IntegrationTest

# Specific test method
./mvnw test -Dtest=IntegrationTest#testMatchFlow

# Pattern matching
./mvnw test -Dtest=*ConcurrencyTest

# With verbose output
./mvnw test -X
```

### Writing New Tests

**Template for Unit Test:**
```java
package com.sportradar.exercise.yourpackage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class YourClassTest {
    private YourClass instance;

    @Before
    public void setUp() {
        instance = new YourClass();
    }

    @Test
    public void testMethod_Scenario_ExpectedOutcome() {
        // Arrange
        int input = 5;

        // Act
        int result = instance.method(input);

        // Assert
        assertEquals(expectedValue, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethod_InvalidInput_ThrowsException() {
        instance.method(-1);
    }
}
```

**Template for Concurrency Test:**
```java
@Test
public void testConcurrentOperation() throws InterruptedException {
    int threadCount = 10;
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch endLatch = new CountDownLatch(threadCount);
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    for (int i = 0; i < threadCount; i++) {
        executor.submit(() -> {
            try {
                startLatch.await(); // Wait for all threads
                // Perform concurrent operation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                endLatch.countDown();
            }
        });
    }

    startLatch.countDown(); // Start all threads
    endLatch.await(); // Wait for completion
    executor.shutdown();

    // Assert expected state
}
```

## Modern Java 22 Features Used

1. **Pattern Matching with instanceof:**
   ```java
   if (obj instanceof Match m) {
       // Use 'm' directly
   }
   ```

2. **Switch Expressions:**
   ```java
   String result = switch (state) {
       case NOT_STARTED -> "Not started";
       case IN_PROGRESS -> "In progress";
       case FINISHED -> "Finished";
       default -> throw new IllegalStateException();
   };
   ```

3. **Records (for DTOs):**
   ```java
   public record EventDetails(String description, Instant timestamp) {}
   ```

4. **Enhanced Generics:**
   ```java
   public interface MatchFactory<T extends Match> {
       Match.Builder<T> createMatchBuilder(Team<?> homeTeam, Team<?> awayTeam);
   }
   ```

5. **Method References:**
   ```java
   matches.stream()
       .filter(Match::isActive)
       .map(Match::getSummary)
       .collect(Collectors.toList());
   ```

## Common Issues & Solutions

### Issue: Match Won't Start
**Symptom:** `startMatch()` returns false
**Cause:** Semaphore limit reached (max 5 concurrent starts)
**Solution:** Ensure matches are properly finished to release semaphore

```java
// Always pair start with finish
scoreboard.startMatch(homeTeam, awayTeam);
// ... operations ...
scoreboard.finishMatch(match); // Releases semaphore permit
```

### Issue: State Transition Fails
**Symptom:** State doesn't change as expected
**Cause:** Invalid state transition attempted
**Solution:** Check valid transitions in `MatchStateManager.java`

```java
// Valid transitions only:
// NOT_STARTED → IN_PROGRESS
// IN_PROGRESS → IN_PAUSE or FINISHED
// IN_PAUSE → IN_PROGRESS or FINISHED
```

### Issue: ConcurrentModificationException
**Symptom:** Exception when iterating matches
**Cause:** Modifying collection during iteration
**Solution:** Use concurrent collections or defensive copies

```java
// Bad
for (Match m : matches) {
    matches.remove(m); // ConcurrentModificationException
}

// Good
List<Match> copy = new ArrayList<>(matches);
for (Match m : copy) {
    matches.remove(m);
}

// Better: Use ConcurrentHashMap or stream operations
```

### Issue: Scoring Strategy Not Applied
**Symptom:** Scores not updating correctly
**Cause:** Mismatched strategy mode and strategy object
**Solution:** Ensure consistency between mode and strategy

```java
// Ensure mode matches strategy
match = builder
    .scoringStrategyMode(ScoringStrategyMode.CLASSIC) // Mode
    .scoringStrategy(ScoringStrategy.forFootballNormalTime()) // Strategy
    .build();

// Or for functional approach
match = builder
    .scoringStrategyMode(ScoringStrategyMode.FUNCTIONAL1) // Mode
    .scoringStrategyFunctional1(ScoringStrategiesFunctional1.footballNormalTimeScoringStrategy)
    .build();
```

### Issue: Tests Fail Intermittently
**Symptom:** Concurrency tests pass sometimes, fail others
**Cause:** Race conditions or timing issues
**Solution:** Use proper synchronization in tests

```java
// Use CountDownLatch for coordination
CountDownLatch latch = new CountDownLatch(threadCount);
// Use awaitTermination for executor cleanup
executor.shutdown();
executor.awaitTermination(5, TimeUnit.SECONDS);
```

## Best Practices for AI Assistants

### When Modifying Code

1. **Always Read First:**
   - Read the relevant file completely before editing
   - Understand the context and surrounding code
   - Check for related test files

2. **Maintain Patterns:**
   - Follow existing design patterns in the package
   - Don't introduce new patterns without justification
   - Keep consistency with naming and structure

3. **Thread Safety:**
   - Assume multi-threaded environment
   - Use locks appropriately (always in try-finally)
   - Prefer concurrent collections
   - Test concurrency scenarios

4. **Test Coverage:**
   - Add tests for new functionality
   - Update existing tests when modifying behavior
   - Include edge cases and error scenarios
   - Add concurrency tests for shared state

5. **Documentation:**
   - Update javadoc comments
   - Keep README.md in sync
   - Document non-obvious design decisions
   - Explain complex algorithms

### When Adding Features

1. **Design Pattern Selection:**
   - Consult Pattern Decision Matrix above
   - Prefer composition over inheritance
   - Use interfaces for abstraction
   - Keep single responsibility

2. **Package Placement:**
   - New scoring logic → `strategy/` or `strategy_functional*/`
   - New match type → `match/`
   - New state → `state/`
   - New storage backend → `storage/`
   - New command → `command/`

3. **Backward Compatibility:**
   - Don't break existing API contracts
   - Deprecate before removing
   - Provide migration path
   - Update version appropriately

4. **Performance Considerations:**
   - Profile before optimizing
   - Prefer O(1) or O(log n) algorithms
   - Use EnumMap for enum-based dispatch
   - Minimize lock contention

### When Debugging

1. **Check These First:**
   - State transitions valid? (See MatchStateManager)
   - Semaphore permits available? (Max 5 concurrent starts)
   - Locks properly released? (Check finally blocks)
   - Strategy mode matches strategy object?

2. **Logging:**
   - Logger available: `Logger.getLogger(ClassName.class.getName())`
   - Log level: INFO for normal operations, FINE for debug
   - Don't log in tight loops (performance)

3. **Testing:**
   - Run relevant test class first
   - Check concurrency tests if thread-related
   - Use debugger with breakpoints
   - Add temporary logging if needed

## File Reference Guide

### Most Frequently Modified Files

1. **match/Match.java** - Core match logic, builder pattern
2. **scoring/Scoreboard.java** - Singleton, concurrency control
3. **state/MatchStateManager.java** - State transitions
4. **strategy/*.java** - Scoring strategies (if adding new rules)
5. **abstract_factory/*Factory.java** - When adding new sport types

### Files You Likely Won't Modify

1. **timing/TimedMatch.java** - Stable interface
2. **storage/MatchStorage.java** - Stable interface
3. **observer/Observer.java** - Standard observer interface
4. **command/MatchCommand.java** - Standard command interface

### Configuration Files

1. **pom.xml** - Maven dependencies, compiler settings (Java 22)
2. **run.sh / run.bat** - Build automation with SDKMAN
3. **mvnw / mvnw.cmd** - Maven wrapper scripts

## Dependencies

### Production (None - Zero external runtime dependencies)
- Pure Java 22 standard library
- java.util.concurrent.* for concurrency
- java.util.stream.* for functional operations
- java.time.* for date/time handling

### Test Dependencies
- **JUnit 4.11** - Testing framework
- **Mockito 3.6.0** - Mocking framework

## Version History

- **v0.4.0** (Current) - Advanced synchronization, basketball support, multiple strategy implementations
- Earlier versions - See git history

## Quick Start for New Contributors

1. **Clone and Build:**
   ```bash
   git clone https://github.com/CesarChaMal/sportRadarExercise.git
   cd sportRadarExercise
   ./run.sh  # Or run.bat on Windows
   ```

2. **Explore Key Files:**
   - Start with `README.md` for user-facing documentation
   - Read this `CLAUDE.md` for development guidance
   - Review `src/main/java/com/sportradar/exercise/match/Match.java`
   - Check `src/test/java/com/sportradar/exercise/IntegrationTest.java`

3. **Run Tests:**
   ```bash
   ./mvnw test
   ```

4. **Make Changes:**
   - Follow TDD: Write test first
   - Implement feature
   - Ensure all tests pass
   - Follow existing patterns and conventions

5. **Commit:**
   ```bash
   git add .
   git commit -m "Descriptive commit message"
   git push origin <branch-name>
   ```

## Summary

This is a well-architected, production-quality library demonstrating:
- **12 design patterns** (Singleton, Builder, Factory, Strategy, State, Command, Observer, Decorator, etc.)
- **SOLID principles** throughout
- **Thread-safe concurrency** with multiple synchronization mechanisms
- **Modern Java 22 features** (pattern matching, records, switch expressions, generics)
- **Comprehensive testing** (29 test files covering unit, integration, concurrency)
- **Multiple strategy implementations** (Classic OOP, Functional BiConsumer, Functional Enum)
- **Extensible architecture** for adding new sports, strategies, and features

When in doubt, follow the existing patterns, maintain thread safety, and add comprehensive tests.
