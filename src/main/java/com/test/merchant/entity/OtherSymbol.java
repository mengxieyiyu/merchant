package com.test.merchant.entity;

import java.io.Serializable;

/**
 * set new string as symbol by yourself
 */
public class OtherSymbol implements Serializable {
    /**
     * MMICMDLV
     */
    private String value;
    /**
     * pish glob
     */
    private String symbolStr;

    public OtherSymbol(String value,String symbolStr){
        this.value = value;
        this.symbolStr = symbolStr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSymbolStr() {
        return symbolStr;
    }

    public void setSymbolStr(String symbolStr) {
        this.symbolStr = symbolStr;
    }
}
