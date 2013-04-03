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
package com.craftfire.commons.util;

import java.security.Security;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

/**
 * A helper class for mail sending.
 */
public class MailSender {
    private String host, username, password;
    private InternetAddress senderEmail;
    private int port = 0;
    private boolean ssl = false;
    private boolean auth = false;
    private Session session;

    /**
     * Creates a new MailSender with given sender e-mail address and SMTP server
     * 
     * @param  senderEmail      e-mail address to use as the sender address
     * @param  host             hostname of the SMTP server
     * @param  port             port of the SMTP server
     * @throws AddressException if the sender e-mail isn't a valid e-mail address
     */
    public MailSender(String senderEmail, String host, int port) throws AddressException {
        this(new InternetAddress(senderEmail), host, port);
    }

    /**
     * Creates a new MailSender with given sender e-mail address and SMTP server
     * 
     * @param  senderEmail      e-mail address to use as the sender address
     * @param  host             hostname of the SMTP server
     * @param  port             port of the SMTP server
     */
    public MailSender(InternetAddress senderEmail, String host, int port) {
        this(senderEmail);
        this.host = host;
        this.port = port;
    }

    protected MailSender(InternetAddress senderEmail) {
        this.senderEmail = senderEmail;
    }

    /**
     * Returns the SMTP server hostname.
     * 
     * @return SMTP server hostname
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Sets the SMTP server hostname.
     * 
     * @param host  SMTP server hostname
     */
    public void setHost(String host) {
        this.host = host;
        this.session = null;
    }

    /**
     * Rerurns the SMTP server port.
     * 
     * @return SMTP server port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Sets the SMTP server port.
     * 
     * @param port  SMTP server port
     */
    public void setPort(int port) {
        this.port = port;
        this.session = null;
    }

    /**
     * Returns the username to used in SMTP authentication
     * 
     * @return  SMTP username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username to use in SMTP authentication
     * 
     * @param username  SMTP username
     */
    public void setUsername(String username) {
        this.username = username;
        this.session = null;
    }

    /**
     * Returns the password used in SMTP authentication
     * 
     * @return SMTP password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password to use in SMTP authentication
     * 
     * @param password  SMTP password
     */
    public void setPassword(String password) {
        this.password = password;
        this.session = null;
    }

    /**
     * Returns the e-mail address used as a sender address
     * 
     * @return sender e-mail address
     */
    public String getSender() {
        return this.senderEmail.toString();
    }

    /**
     * Sets the e-mail address to use as a sender address
     * 
     * @param senderEmail       sender e-mail address
     * @throws AddressException if the address isn't a valid e-mail address
     */
    public void setSender(String senderEmail) throws AddressException {
        this.senderEmail = new InternetAddress(senderEmail);
    }

    /**
     * Returns the e-mail address used as a sender address
     * 
     * @return sender e-mail address
     */
    public InternetAddress getSenderAddress() {
        return this.senderEmail;
    }

    /**
     * Sets the e-mail address to use as a sender address
     * 
     * @param senderEmail       sender e-mail address
     */
    public void setSenderAddress(InternetAddress senderEmail) {
        this.senderEmail = senderEmail;
    }

    /**
     * Checks if SMTP authorization is enabled
     * 
     * @return true if enabled, false if not
     */
    public boolean isAuth() {
        return this.auth;
    }

    /**
     * Sets if this sender should use SMTP authorization
     * 
     * @param auth  true if should, false if not
     */
    public void setAuth(boolean auth) {
        this.auth = auth;
        this.session = null;
    }

    /**
     * Checks if SSL is enabled
     * 
     * @return true if enabled, false if not
     */
    public boolean isSSL() {
        return this.ssl;
    }

    /**
     * Sets if this sender should use SSL
     * 
     * @param ssl  true if should, false if not
     */
    public void setSSL(boolean ssl) {
        this.ssl = ssl;
        this.session = null;
    }

    /**
     * Sends an e-mail message with SMTP settings configured in this sender.
     * 
     * @param message             the message to send
     * @throws MessagingException if a messaging error occurred
     */
    public void send(MimeMessage message) throws MessagingException {
        SMTPTransport t = (SMTPTransport) getSession().getTransport(this.ssl ? "smtps" : "smtp");
        try {
            if (this.auth) {
                t.connect(this.host, this.username, this.password);
            } else {
                t.connect(this.host, null, null);
            }
            t.sendMessage(message, message.getAllRecipients());
        } finally {
            t.close();
        }
    }

    /**
     * Returns a mail session with all the SMTP settings of this sender.
     * 
     * @return the mail session
     */
    public Session getSession() {
        if (this.session == null) {
            Properties props = System.getProperties();
            props.setProperty("mail.smtp.port", String.valueOf(this.port));
            if (this.ssl) {
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.setProperty("mail.smtp.socketFactory.fallback", "false");
                props.setProperty("mail.smtp.socketFactory.port", String.valueOf(this.port));
                props.setProperty("mail.smtps.host", this.host);
                props.setProperty("mail.smtps.auth", String.valueOf(this.auth));
            } else {
                props.setProperty("mail.smtp.host", this.host);
                props.setProperty("mail.smtp.auth", String.valueOf(this.auth));
            }
            props.put("mail.smtps.quitwait", "false");

            this.session = Session.getInstance(props, null);
        }
        return this.session;
    }

    /**
     * Creates a new e-mail message with sender address and all the SMTP settings configured in this sender.
     * 
     * @return                    a new message
     * @throws MessagingException if a messaging error occurred
     */
    public MimeMessage createMessage() throws MessagingException {
        MimeMessage message = new MimeMessage(getSession());
        message.setSender(this.senderEmail);
        return message;
    }

}
