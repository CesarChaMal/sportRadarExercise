# Live Football Scoreboard Library

## Overview

This Live Football Scoreboard Library is designed to simulate real-time tracking of football matches. It provides functionalities to start new matches, update ongoing match scores, finish matches, and generate a summary of matches ordered by their total score and start times. The current version of the library is v0.1.0.

## Version

The current version of the Live Football Scoreboard Library is **v0.1.0**. This initial version includes all basic functionalities required for tracking football matches, including starting matches, updating scores, finishing matches, and retrieving match summaries in a sorted order.

## Features

- **Start a Match**: Initiate a match between two teams.
- **Update Score**: Dynamically update the score of ongoing matches.
- **Finish Match**: Conclude a match, removing it from the active scoreboard.
- **Get Summary**: Retrieve a list of active matches, sorted by total score and creation time for matches with equal scores.

## Utilization of Functional Programming

This library incorporates functional programming principles to enhance readability, maintainability, and code efficiency:

- **Comparator Chains**: Utilizes `Comparator` chains for sorting matches, leveraging lambda expressions and method references for concise and readable sorting logic.
- **Optional**: Employs `Optional` for safe retrieval of matches, minimizing the risk of `NullPointerException` and simplifying conditional logic.
- **Stream API**: Leverages the Stream API for filtering and collecting matches, showcasing the power of streams in processing collections.

## Development Approach

- **Test-Driven Development (TDD)**: Development began with writing tests for each functionality, ensuring each piece of code is properly tested before implementation.
- **Clean Code**: Effort was made to write readable, simple, and refactored code to enhance maintainability and understandability.

## How to Use

Here's a quick start guide:

```java
Scoreboard scoreboard = new Scoreboard();

// Start new matches
scoreboard.startMatch("Home Team A", "Away Team A");
scoreboard.startMatch("Home Team B", "Away Team B");

// Update scores
Match matchA = scoreboard.getMatch("Home Team A", "Away Team A");
if (matchA != null) {
    scoreboard.updateScore(matchA, 1, 0);
}

// Finish a match
Match matchB = scoreboard.getMatch("Home Team B", "Away Team B");
if (matchB != null) {
    scoreboard.finishMatch(matchB);
}

// Get and print summary
List<Match> summary = scoreboard.getSummary();
summary.forEach(System.out::println);
