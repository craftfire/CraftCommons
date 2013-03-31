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

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.craftfire.commons.analytics.AnalyticsException;
import com.craftfire.commons.analytics.AnalyticsManager;
import com.craftfire.commons.encryption.*;

public final class CraftCommons {
    private CraftCommons() {
        try {
            new AnalyticsManager("http://stats.craftfire.com/", "CraftCommons", "1.0.0").submit();
        } catch (MalformedURLException ignore) {
        } catch (IOException e) {
            //TODO: logging
        } catch (AnalyticsException e) {
            //TODO: logging
        }
    }

    public static String getName() {
        return "CraftCommons";
    }

    public static String getVersion() {
        return "1.0.0";
    }

    public static String long2ip(Long i) {
        return ((i >> 24) & 0xFF) + "." +
               ((i >> 16) & 0xFF) + "." +
               ((i >> 8) & 0xFF) + "." +
               (i & 0xFF);
    }

    public static Long ip2long(String ip) {
        String[] ipArray = ip.split("\\.");
        long num = 0;
        for (int i = 0; i < ipArray.length; i++) {
            int power = 3 - i;
            num += ((Integer.parseInt(ipArray[i]) % 256 * Math.pow(256, power)));
        }
        return num;
    }

    public static boolean hasClass(String classString) {
        try {
            Class.forName(classString);
        } catch (ClassNotFoundException ignore) {
            return false;
        }
        return true;
    }

    @Deprecated
    public static boolean inVersionRange(String lastversion, String compare) {
        if (lastversion.equalsIgnoreCase(compare)) {
            return true;
        }
        String s1 = normalisedVersion(compare);
        String s2 = normalisedVersion(lastversion);
        int cmp = s1.compareTo(s2);
        return cmp < 0;
    }

    public static String encrypt(Encryption encryption, Object object) {
        return encrypt(encryption, object, null, 0);
    }
    public static String encrypt(Encryption encryption, Object object, String salt) {
        return encrypt(encryption, object, salt, 0);
    }

    public static String encrypt(Encryption encryption, Object object, String salt, int iterationCount) {
        try {
            String string = (String) object;
            MessageDigest md = null;
            String newSalt = salt;
            if (encryption.equals(Encryption.MD5)) {
                md = MessageDigest.getInstance("MD5");
            } else if (encryption.equals(Encryption.SHA1)) {
                md = MessageDigest.getInstance("SHA-1");
            } else if (encryption.equals(Encryption.SHA256)) {
                md = MessageDigest.getInstance("SHA-256");
            } else if (encryption.equals(Encryption.SHA512)) {
                md = MessageDigest.getInstance("SHA-512");
            } else if (encryption.equals(Encryption.CRC32)) {
                Checksum checksum = new CRC32();
                checksum.update(string.getBytes(), 0, string.getBytes().length);
                return Long.toString(checksum.getValue());
            } else if (encryption.equals(Encryption.WHIRLPOOL)) {
                Whirlpool w = new Whirlpool();
                byte[] digest = new byte[Whirlpool.DIGESTBYTES];
                w.NESSIEinit();
                w.NESSIEadd(string);
                w.NESSIEfinalize(digest);
                return Whirlpool.display(digest);
            } else if (encryption.equals(Encryption.PHPASS_P) || encryption.equals(Encryption.PHPASS_H)) {
                PHPass phpass;
                if (iterationCount == 0) {
                    phpass = new PHPass(encryption, 8);
                } else {
                    phpass = new PHPass(encryption, iterationCount);
                }
                if (newSalt == null || newSalt.isEmpty()) {
                    newSalt = phpass.gensalt();
                }
                String hash = phpass.crypt(string, newSalt);
                if (hash.length() == 34) {
                    return hash;
                }
                return "*";
            } else if (encryption.equals(Encryption.BLOWFISH)) {
                if (newSalt == null || newSalt.isEmpty()) {
                    if (iterationCount == 0) {
                        newSalt = BCrypt.gensalt(8);
                    } else {
                        newSalt = BCrypt.gensalt(iterationCount);
                    }
                }
                return BCrypt.hashpw(string, newSalt);
            }
            if (md != null) {
                md.update(string.getBytes("ISO-8859-1"), 0, string.length());
                byte[] hash = md.digest();
                return EncryptionUtil.bytesTohex(hash);
            } else {
                return null;
            }
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static Encryption unixHashIdentify(String hash){
        if (hash.startsWith("$1$") || hash.startsWith("$md5$")) {
            return Encryption.MD5;
        } else if (hash.startsWith("$2")) {
            return Encryption.BLOWFISH;
        } else if (hash.startsWith("$5$")) {
            return Encryption.SHA256;
        } else if (hash.startsWith("$6$")) {
            return Encryption.SHA512;
        } else if (hash.startsWith("$P$")) {
            return Encryption.PHPASS_P;
        } else if (hash.startsWith("$H$")) {
            return Encryption.PHPASS_H;
        }
        return null;
    }

    @Deprecated
    public static String normalisedVersion(String version) {
        return normalisedVersion(version, ".", 4);
    }

    @Deprecated
    public static String normalisedVersion(String version, String sep, int maxWidth) {
        String[] split = Pattern.compile(sep, Pattern.LITERAL).split(version);
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(String.format("%" + maxWidth + 's', s));
        }
        return sb.toString();
    }

    /**
     * @param urlstring The url of the image.
     * @return Image object.
     */
    public static Image urlToImage(String urlstring) {
        try {
            URL url = new URL(urlstring);
            return Toolkit.getDefaultToolkit().createImage(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

    public static String convertHexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        for ( int i=0; i<hex.length()-1; i+=2 ) {
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char)decimal);
        }
        return sb.toString();
    }

    public static String convertStringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString(chars[i]));
        }
        return hex.toString();
    }

    @Deprecated
    public static String forumCache(String cache, String player, int userid, String nummember, String activemembers,
                                    String newusername, String newuserid, String extrausername, String lastvalue) {
        StringTokenizer st = new StringTokenizer(cache, ":");
        int i = 0;
        List<String> array = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            array.add(st.nextToken() + ":");
        }
        StringBuffer newcache = new StringBuffer();
        while (array.size() > i) {
            if (array.get(i).equals("\"" + nummember + "\";i:") && nummember != null) {
                String temp = array.get(i + 1);
                temp = removeChar(temp, '"');
                temp = removeChar(temp, ':');
                temp = removeChar(temp, 's');
                temp = removeChar(temp, ';');
                temp = temp.trim();
                int tempnum = Integer.parseInt(temp) + 1;
                if (lastvalue.equalsIgnoreCase(nummember)) {
                    temp = tempnum + ";}";
                } else {
                    temp = tempnum + ";s:";
                }
                array.set(i + 1, temp);
            } else if (array.get(i).equals("\"" + newusername + "\";s:") && newusername != null) {
                array.set(i + 1, player.length() + ":");
                if (lastvalue.equalsIgnoreCase(newusername)) {
                    array.set(i + 2, "\"" + player + "\"" + ";}");
                } else {
                    array.set(i + 2, "\"" + player + "\"" + ";s" + ":");
                }
            } else if (array.get(i).equals("\"" + extrausername + "\";s:") && extrausername != null) {
                array.set(i + 1, player.length() + ":");
                if (lastvalue.equalsIgnoreCase(extrausername)) {
                    array.set(i + 2, "\"" + player + "\"" + ";}");
                } else {
                    array.set(i + 2, "\"" + player + "\"" + ";s" + ":");
                }
            } else if (array.get(i).equals("\"" + activemembers + "\";s:") && activemembers != null) {
                String temp = array.get(i + 2);
                temp = removeChar(temp, '"');
                temp = removeChar(temp, ':');
                temp = removeChar(temp, 's');
                temp = removeChar(temp, ';');
                temp = temp.trim();
                int tempnum = Integer.parseInt(temp) + 1;
                String templength = "" + tempnum;
                if (lastvalue.equalsIgnoreCase(activemembers)) {
                    temp = "\"" + tempnum + "\"" + ";}";
                } else {
                    temp = "\"" + tempnum + "\"" + ";s:";
                }
                array.set(i + 1, templength.length() + ":");
                array.set(i + 2, temp);
            } else if (array.get(i).equals("\"" + newuserid + "\";s:") && newuserid != null) {
                String dupe = "" + userid;
                array.set(i + 1, dupe.length() + ":");
                if (lastvalue.equalsIgnoreCase(newuserid)) {
                    array.set(i + 2, "\"" + userid + "\"" + ";}");
                } else {
                    array.set(i + 2, "\"" + userid + "\"" + ";s:");
                }
            }
            newcache.append(array.get(i));
            i++;
        }
        return newcache.toString();
    }

    @Deprecated
    public static String forumCacheValue(String cache, String value) {
        StringTokenizer st = new StringTokenizer(cache, ":");
        int i = 0;
        List<String> array = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            array.add(st.nextToken() + ":");
        }
        while (array.size() > i) {
            if (array.get(i).equals("\"" + value + "\";s:") && value != null) {
                String temp = array.get(i + 2);
                temp = removeChar(temp, '"');
                temp = removeChar(temp, ':');
                temp = removeChar(temp, 's');
                temp = removeChar(temp, ';');
                temp = temp.trim();
                return temp;
            }
            i++;
        }
        return null;
    }

    public static String removeChar(String s, char c) {
        StringBuilder r = new StringBuilder(s.length());
        r.setLength(s.length());
        int current = 0;
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c) {
                r.setCharAt(current++, cur);
            }
        }
        return r.toString();
    }

    /**
     * Unserialize object serialized with php serialization.
     *
     * @param  serialized  string to unserialize
     * @return             unserialized data - Integer, Double, String,
     *                     Boolean, Map<Object,Object> or PhpObject
     */
    public static Object phpUnserialize(String serialized) {
        SerializedPhpParser parser = new SerializedPhpParser(serialized);
        return parser.parse();
    }

    /**
     * Serialize object with php serialization.
     *
     * @param  value  object to serialize
     * @return        object serialized to String.
     */
    public static String phpSerialize(Object value) {
        return PhpSerializer.serialize(value);
    }
}
