package com.sportradar.exercise.match;

public enum EventType {

    GOAL("Goal", Sport.FOOTBALL, -1),
    YELLOW_CARD("Yellow Card", Sport.FOOTBALL, -1),
    RED_CARD("Red Card", Sport.FOOTBALL, -1),
    PENALTY("Penalty", Sport.FOOTBALL, -1),
    OFFSIDE("Offside", Sport.FOOTBALL, -1),

    POINTS_SCORED("Points Scored", Sport.BASKETBALL, -1),
    FOUL("Foul", Sport.BASKETBALL, -1),
    TIMEOUT("Timeout", Sport.BASKETBALL, -1),
    THREE_POINT_SCORE("Three-Point Score", Sport.BASKETBALL, 3),
    TWO_POINT_SCORE("Two-Point Score", Sport.BASKETBALL, 2),
    FREE_THROW("Free Throw", Sport.BASKETBALL, 1),

    MATCH_NO_STARTED("Match No Started", null, -1),
    MATCH_STARTED("Match Started", null, -1),
    MATCH_FINISHED("Match Finished", null, -1),
    MATCH_PAUSED("Match Pause", null, -1),
    MATCH_RESUMED("Match Resumed", null, -1),
    SCORE_UPDATE("Score Update", null, -1),
    SUBSTITUTION("Substitution", null, -1),
    UNKNOWN("Unknown", null, -1);

    private final String description;
    private final Sport sport;
    private final int points;

    EventType(String description, Sport sport, int points) {
        this.description = description;
        this.sport = sport;
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public Sport getSport() {
        return sport;
    }

    public int getPoints() {
        return points;
    }

    public static EventType determineEventTypeByPoints(int points) {
        for (EventType type : values()) {
            if (type.points == points && type.sport == Sport.BASKETBALL) {
                return type;
            }
        }
        return UNKNOWN;
    }
}

enum Sport {
    FOOTBALL,
    BASKETBALL
}