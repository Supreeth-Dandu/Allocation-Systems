package com.allocation.trade.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum TradeStatus {
    NEW,
    AMENDED,
    CANCELLED,
    COMPLETED;

    @JsonCreator
    public static TradeStatus fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return TradeStatus.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
