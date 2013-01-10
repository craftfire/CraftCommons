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
