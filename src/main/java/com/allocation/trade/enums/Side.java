package com.allocation.trade.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum Side {
    BUY,
    SELL;

    @JsonCreator
    public static Side fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Side.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
