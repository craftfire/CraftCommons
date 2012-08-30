/*
 * This file is part of CraftCommons.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * CraftCommons is licensed under the GNU Lesser General Public License.
 *
 * CraftCommons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CraftCommons is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
