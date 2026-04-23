package ru.tbank.practicum.models.enums;

import lombok.Getter;

@Getter
public enum WeatherCategory {
    THUNDERSTORM,
    DRIZZLE,
    RAIN,
    SNOW,
    ATMOSPHERE,
    CLEAR,
    CLOUDS,
    UNKNOWN;

    public static WeatherCategory fromCode(int code) {
        if (code == 800) return CLEAR;
        if (code >= 801 && code <= 804) return CLOUDS;
        return switch (code / 100) {
            case 2 -> THUNDERSTORM;
            case 3 -> DRIZZLE;
            case 5 -> RAIN;
            case 6 -> SNOW;
            case 7 -> ATMOSPHERE;
            default -> UNKNOWN;
        };
    }
}
