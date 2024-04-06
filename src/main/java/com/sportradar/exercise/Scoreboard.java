package com.sportradar.exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class Scoreboard {
    private final List<Match> matches;

    public Scoreboard() {
        this.matches = new ArrayList<>();
    }

    public void startMatch(String homeTeam, String awayTeam) {
        out.printf("Starting match between %s and %s%n", homeTeam, awayTeam);
        matches.add(new Match(homeTeam, awayTeam));
    }

    public List<Match> getSummary() {
        return  Collections.unmodifiableList(matches);
    }
}
