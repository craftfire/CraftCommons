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
package com.craftfire.commons.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class PHPass {
    static String itoa64 =
            "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    int iteration_count_log2;
    SecureRandom random_gen;

    public PHPass(int iteration_count_log2) {
        if (iteration_count_log2 < 4 || iteration_count_log2 > 31) {
            iteration_count_log2 = 8;
        }

        this.iteration_count_log2 = iteration_count_log2;
        this.random_gen = new SecureRandom();
    }

    private String encode64(byte[] src, int count) {
        int i, value;
        String output = "";
        i = 0;

        if (src.length < count) {
            byte[] t = new byte[count];
            System.arraycopy(src, 0, t, 0, src.length);
            Arrays.fill(t, src.length, count - 1, (byte) 0);
        }

        do {
            value = src[i]  + (src[i] < 0 ? 256 : 0);
            ++i;
            output += itoa64.charAt(value & 63);
            if (i < count) {
                value |= (src[i] + (src[i] < 0 ? 256 : 0)) << 8;
            }
            output += itoa64.charAt((value >> 6) & 63);
            if (i++ >= count) {
                break;
            }
            if (i < count) {
                value |= (src[i] + (src[i] < 0 ? 256 : 0)) << 16;
            }
            output += itoa64.charAt((value >> 12) & 63);
            if (i++ >= count) {
                break;
            }
            output += itoa64.charAt((value >> 18) & 63);
        } while (i < count);

        return output;
    }

    private String crypt_private(String password, String setting) {
        String output = "*0";
        if (((setting.length() < 2 )? setting : setting.substring(0, 2))
                .equalsIgnoreCase(output)) {
            output = "*1";
        }

        String id = (setting.length() < 3) ? setting : setting.substring(0, 3);
        if (!(id.equals("$P$") || id.equals("$H$"))) {
            return output;
        }

        int count_log2 = itoa64.indexOf(setting.charAt(3));
        if (count_log2 < 7 || count_log2 > 30) {
            return output;
        }

        int count = 1 << count_log2;
        String salt = setting.substring(4, 4+8);
        if (salt.length() != 8) {
            return output;
        }

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return output;
        }

        byte[] pass = EncryptionUtil.stringToUtf8(password);
        byte[] hash = md.digest(EncryptionUtil.stringToUtf8(salt + password));
        do {
            byte[] t = new byte[hash.length + pass.length];
            System.arraycopy(hash, 0, t, 0, hash.length);
            System.arraycopy(pass, 0, t, hash.length, pass.length);
            hash = md.digest(t);
        } while (--count > 0);

        output = setting.substring(0, 12);
        output += this.encode64(hash, 16);

        return output;
    }

    private String gensalt_private(byte[] input) {
        String output = "$P$";
        output += itoa64.charAt(Math.min(this.iteration_count_log2 + 5, 30));
        output += this.encode64(input, 6);
        return output;
    }

    public String crypt(String password, String setting) {
        return this.crypt_private(password, setting);
    }

    public String gensalt(String input) {
        return this.gensalt_private(EncryptionUtil.stringToUtf8(input));
    }

    public String gensalt(byte[] input) {
        return this.gensalt_private(input);
    }

    public String gensalt() {
        byte random[] = new byte[6];
        this.random_gen.nextBytes(random);
        return this.gensalt(random);
    }

    public Random getRandomGen() {
        return this.random_gen;
    }
}
