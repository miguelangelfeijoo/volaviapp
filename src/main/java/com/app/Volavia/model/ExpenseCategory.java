package com.app.Volavia.model;

public enum ExpenseCategory {
    FLIGHT("Vuelo"),
    HOTELS("Alojamiento"),
    TRANSPORT("Transporte"),
    OTHER("Gastos Varios");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 