/*
 * This file is part of CraftCommons.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.craftfire.commons.util.MailSender;

public class MailTest_Manual {
    static final String newline = System.getProperty("line.separator");

    /**
     * @param args
     * @throws MessagingException
     */
    public static void main(String[] args) throws MessagingException {
        MailSender sender;
        String from, to, host, user, pass;
        int port;
        boolean ssl;
        from = ask("Sender email", "dev@craftfire.com");
        host = ask("Host", "localhost");
        port = Integer.parseInt(ask("Port", "25"));
        to = ask("Target email", "");
        sender = new MailSender(new InternetAddress(from), host, port);
        user = ask("Username", "");
        pass = ask("Passwword", "");
        ssl = ask("Use SSL", "false").equals("true");
        sender.setUsername(user);
        sender.setPassword(pass);
        sender.setAuth(true);
        sender.setSSL(ssl);
        MimeMessage msg = sender.createMessage();
        msg.setSubject("Testing");
        msg.setRecipients(RecipientType.TO, to);
        msg.setText("This is CraftCommons mail sending test.");
        sender.send(msg);
    }

    public static String ask(String name, String defaultvalue) {
        String line = null;
        boolean valid = false;
        try {
            System.out.println(newline + name + ": ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            line = br.readLine();
            if (line != null && !line.isEmpty()) {
                return line;
            } else {
                System.out.println(defaultvalue);
                return defaultvalue;
            }
        } catch (IOException ioe) {
            System.out.println("IO exception = " + ioe);
        }
        return null;
    }
}
