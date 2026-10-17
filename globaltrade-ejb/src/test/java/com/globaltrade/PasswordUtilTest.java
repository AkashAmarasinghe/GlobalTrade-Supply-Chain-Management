package com.globaltrade;

import com.globaltrade.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {

    @Test
    void hashesAndMatchesPasswordUsingSaltedPbkdf2() {
        String first = PasswordUtil.hash("Admin@123");
        String second = PasswordUtil.hash("Admin@123");

        assertTrue(first.startsWith("pbkdf2$"));
        assertTrue(PasswordUtil.matches("Admin@123", first));
        assertFalse(PasswordUtil.matches("wrong", first));
        assertNotEquals(first, second);
    }

    @Test
    void stillMatchesExistingSha256HashesDuringDatabaseMigration() {
        String legacy = "e86f78a8a3caf0b60d8e74e5942aa6d86dc150cd3c03338aef25b7d2d7e3acc7";
        assertTrue(PasswordUtil.matches("Admin@123", legacy));
        assertFalse(PasswordUtil.matches("wrong", legacy));
    }
}
