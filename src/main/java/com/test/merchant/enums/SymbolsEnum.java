package com.test.merchant.enums;

/**
 * symbol enum
 */
public enum SymbolsEnum {
    /**
     * symbol: I
     */
    I(1, "I"),
    /**
     * symbol: V
     */
    V(5, "V"),
    /**
     * symbol: X
     */
    X(10, "X"),
    /**
     * symbol: L
     */
    L(50, "L"),
    /**
     * symbol: C
     */
    C(100, "C"),
    /**
     * symbol: D
     */
    D(500, "D"),
    /**
     * symbol: M
     */
    M(1000, "M");

    private Integer value;
    private String symbol;

    SymbolsEnum(Integer value, String symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    /**
     * get value of symbol
     *
     * @param symbol
     * @return
     */
    public static Integer getValue(String symbol) {
        for (SymbolsEnum c : SymbolsEnum.values()) {
            if (c.symbol.equals(symbol))
                return c.value;
        }

        return 0;
    }

    public Integer getValue() {
        return value;
    }

    public String getSymbol() {
        return symbol;
    }
}
