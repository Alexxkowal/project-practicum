package ru.tbank.practicum.models.enums;

import lombok.Getter;

@Getter
public enum WeatherCategory {
    THUNDERSTORM(200, 299),
    DRIZZLE(300, 399),
    RAIN(500, 599),
    SNOW(600, 699),
    ATMOSPHERE(700, 799),
    CLEAR(800, 800),
    CLOUDS(801, 804),
    UNKNOWN(0, 0);

    private int from;
    private int to;

    WeatherCategory(int from, int to) {}

    public static WeatherCategory fromCode(int code) {
        for (WeatherCategory category : values()) {
            if (code >= category.from && code <= category.to) {
                return category;
            }
        }
        return UNKNOWN;
    }
}
