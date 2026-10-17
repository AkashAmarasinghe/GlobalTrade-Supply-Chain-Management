package com.globaltrade;
import com.globaltrade.util.ScoreUtil;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
class ScoreUtilTest {
    @Test void calculatesAverageScore(){assertEquals(new BigDecimal("90.00"),ScoreUtil.average(new BigDecimal("90"),new BigDecimal("85"),new BigDecimal("95")));}
}
