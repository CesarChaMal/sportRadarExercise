package com.sportradar.exercise.match;

import com.sportradar.exercise.scoring.Scorable;
import com.sportradar.exercise.state.StateManageable;
import com.sportradar.exercise.strategy.MatchStrategy;
import com.sportradar.exercise.timing.TimedMatch;

public interface MatchInterface extends MatchInfo, Scorable, StateManageable, StateManagement, TimedMatch, MatchStrategy {
}
