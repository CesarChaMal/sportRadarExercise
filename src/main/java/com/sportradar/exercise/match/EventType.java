package com.sportradar.exercise.match;

public enum EventType {

    GOAL("Goal", Sport.FOOTBALL),
    YELLOW_CARD("Yellow Card", Sport.FOOTBALL),
    RED_CARD("Red Card", Sport.FOOTBALL),
    PENALTY("Penalty", Sport.FOOTBALL),
    OFFSIDE("Offside", Sport.FOOTBALL),

    POINTS_SCORED("Points Scored", Sport.BASKETBALL),
    FOUL("Foul", Sport.BASKETBALL),
    TIMEOUT("Timeout", Sport.BASKETBALL),
    THREE_POINT_SCORE("Three-Point Score", Sport.BASKETBALL),
    FREE_THROW("Free Throw", Sport.BASKETBALL),

    MATCH_NO_STARTED("Match No Started", null),
    MATCH_STARTED("Match Started", null),
    MATCH_FINISHED("Match Finished", null),
    MATCH_PAUSED("Match Pause", null),
    MATCH_RESUMED("Match Resumed", null),
    SCORE_UPDATE("Score Update", null),
    SUBSTITUTION("Substitution", null),
    UNKNOWN("Unknown",null);

    private final String description;
    private final Sport sport;

    EventType(String description, Sport sport) {
        this.description = description;
        this.sport = sport;
    }

    public String getDescription() {
        return description;
    }

    public Sport getSport() {
        return sport;
    }
}

enum Sport {
    FOOTBALL,
    BASKETBALL
}
