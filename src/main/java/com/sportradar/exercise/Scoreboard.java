package com.sportradar.exercise;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.*;

public class Scoreboard {
    private List<Match> matches;

    public Scoreboard() {
        this.matches = new ArrayList<>();
    }

    public void startMatch(String homeTeam, String awayTeam) {
        out.printf("Starting match between %s and %s%n", homeTeam, awayTeam);
        matches.add(new Match(homeTeam, awayTeam));
    }
}
