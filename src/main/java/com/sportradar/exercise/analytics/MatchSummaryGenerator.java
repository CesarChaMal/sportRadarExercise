package com.sportradar.exercise.analytics;

import com.sportradar.exercise.match.Match;
import com.sportradar.exercise.match.MatchInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MatchSummaryGenerator {
    public List<MatchInterface> generateSummary(List<MatchInterface> matches) {
        var sortedMatches = matches.stream()
                .sorted(Comparator.comparingInt(MatchInterface::getTotalScore).reversed()
                        .thenComparing(MatchInterface::getCreationTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return Collections.unmodifiableList(sortedMatches);
    }
}
