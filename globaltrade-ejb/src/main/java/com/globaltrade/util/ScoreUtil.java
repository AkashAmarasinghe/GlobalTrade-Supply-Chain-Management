package com.globaltrade.util;
import java.math.BigDecimal;
import java.math.RoundingMode;
public final class ScoreUtil {
    private ScoreUtil() {
    }
    public static BigDecimal average(BigDecimal delivery,BigDecimal quality,BigDecimal compliance) {
        if(delivery==null||quality==null||compliance==null) throw new IllegalArgumentException("Scores cannot be null");
        return delivery.add(quality).add(compliance).divide(BigDecimal.valueOf(3),2,RoundingMode.HALF_UP);
    }
}
