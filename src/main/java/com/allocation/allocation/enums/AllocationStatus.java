package com.allocation.allocation.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum AllocationStatus {
    PENDING,
    ACTIVE,
    CANCELLED,
    SETTLED,
    COMPLETED;

    @JsonCreator
    public static AllocationStatus fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return AllocationStatus.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
