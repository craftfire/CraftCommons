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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;

import com.craftfire.commons.util.MailSender;

public class TestMailSender {
    MailSender sender;

    @Before
    public void init() throws AddressException {
        this.sender = new MailSender("dev@craftfire.com", "localhost", 25);
    }

    @Test
    public void testSettings() throws AddressException {
        assertEquals("dev@craftfire.com", this.sender.getSender());
        assertEquals(new InternetAddress("dev@craftfire.com"), this.sender.getSenderAddress());
        this.sender.setSender("test@craftfire.com");
        assertEquals("test@craftfire.com", this.sender.getSender());
        assertEquals(new InternetAddress("test@craftfire.com"), this.sender.getSenderAddress());
        this.sender.setSenderAddress(new InternetAddress("bob@craftfire.com"));
        assertEquals("bob@craftfire.com", this.sender.getSender());
        assertEquals(new InternetAddress("bob@craftfire.com"), this.sender.getSenderAddress());

        assertEquals("localhost", this.sender.getHost());
        this.sender.setHost("smtp.craftfire.com");
        assertEquals("smtp.craftfire.com", this.sender.getHost());

        assertEquals(25, this.sender.getPort());
        this.sender.setPort(225);
        assertEquals(225, this.sender.getPort());

        this.sender.setUsername("bob");
        assertEquals("bob", this.sender.getUsername());

        this.sender.setPassword("shh...");
        assertEquals("shh...", this.sender.getPassword());

        this.sender.setAuth(true);
        assertTrue(this.sender.isAuth());
        this.sender.setAuth(false);
        assertFalse(this.sender.isAuth());

        this.sender.setSSL(true);
        assertTrue(this.sender.isSSL());
        this.sender.setSSL(false);
        assertFalse(this.sender.isSSL());
    }

    @Test
    public void testSession() {
        Session s = this.sender.getSession();
        assertEquals("25", s.getProperty("mail.smtp.port"));
        assertEquals("localhost", s.getProperty("mail.smtp.host"));
        assertEquals(String.valueOf(this.sender.isAuth()), s.getProperty("mail.smtp.auth"));
        this.sender.setSSL(true);
        this.sender.setHost("smtp.gmail.com");
        this.sender.setPort(465);
        this.sender.setUsername("bob");
        this.sender.setPassword("boo");
        this.sender.setAuth(true);
        s = this.sender.getSession();
        assertEquals("465", s.getProperty("mail.smtp.port"));
        assertEquals("smtp.gmail.com", s.getProperty("mail.smtps.host"));
        assertEquals(String.valueOf(this.sender.isAuth()), s.getProperty("mail.smtps.auth"));
        assertEquals("javax.net.ssl.SSLSocketFactory", s.getProperty("mail.smtp.socketFactory.class"));
        assertEquals("465", s.getProperty("mail.smtp.socketFactory.port"));
        assertEquals("false", s.getProperty("mail.smtp.socketFactory.fallback"));
    }

    @Test
    public void testCreateMessage() throws MessagingException {
        MimeMessage msg = this.sender.createMessage();
        assertEquals(this.sender.getSenderAddress(), msg.getFrom()[0]);
    }
}
