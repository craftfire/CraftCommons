package com.craftfire.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.craftfire.commons.enums.Encryption;

public class TestEncryption {
    @Test
    public final void testPHPass() {
        final String correct = "test12345";
        final String wrong = "test12346";
        final String hash = "$P$9IQRaTwmfeRo7ud9Fh4E2PdI0S3r.L0";
        final String correctpl = "ąćęłóńśźż";
        final String wrongpl = "acelonszz";
        final String hashpl = "$P$ByXWbzDvVAJ0jxvNp5sv4xYPIRXJJ1.";
        String hashed = CraftCommons.encrypt(Encryption.PHPASS, correct);
        assertEquals("Correct password doesn't match generated hash", hashed,
                CraftCommons.encrypt(Encryption.PHPASS, correct, hashed));
        assertFalse("Wrong password matches generated hash", CraftCommons
                .encrypt(Encryption.PHPASS, wrong, hashed).equals(hashed));
        assertEquals(
                "Correct password doesn't match hash from original PHPass",
                hash, CraftCommons.encrypt(Encryption.PHPASS, correct, hash));
        assertFalse("Wrong password matches hash from phpass", CraftCommons
                .encrypt(Encryption.PHPASS, wrong, hash).equals(hash));
        assertEquals(
                "Correct password with polish characters doesn't match hash from original PHPass",
                hashpl,
                CraftCommons.encrypt(Encryption.PHPASS, correctpl, hashpl));
        assertFalse(
                "Wrong password matches  hash from oroginal PHPass (original password contained polish characters)",
                CraftCommons.encrypt(Encryption.PHPASS, wrongpl, hashpl)
                        .equals(hashpl));
    }

}
