/**
 * MailSender.java
 * appEducacional
 * 03/05/2014 12:59:17
 * Copyright David Romero Alcaide
 * com.app.infrastructureLayer
 */
package com.app.infrastructure;

/**
 * Imports
 */
import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.app.infrastructure.exceptions.GeneralException;

/**
 * @author David Romero Alcaide
 * 
 */
public class CustomMailSender {
	private JavaMailSender mailSender;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendMail(String subject, String content, String from,
			String to, File file) throws GeneralException {

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			helper.addAttachment(file.getName(), file);
		} catch (MessagingException e) {
			throw new GeneralException(e);
		}

		mailSender.send(message);
	}

	public void sendMail(String subject, String content, String from, String to) throws GeneralException{

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
		} catch (MessagingException e) {
			throw new GeneralException(e);
		}

		mailSender.send(message);
	}
}
