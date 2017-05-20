package com.blito.services;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.blito.enums.Response;
import com.blito.models.User;
import com.blito.resourceUtil.ResourceUtil;

@Service
public class MailService {
	private final Logger log = LoggerFactory.getLogger(MailService.class);
	
	@Value(value = "${api.base.url}")
	String baseUrl;
	
	@Value(value = "${serverAddress}")
	String serverAddress;

	@Autowired
	private JavaMailSenderImpl javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	public void sendActivationEmail(User user) {
        log.debug("Sending user activation e-mail to '{}'", user.getEmail());
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        context.setVariable("serverAddress",serverAddress);
        String content = templateEngine.process("activationEmail", context);
        System.out.println(content);
        sendEmail(user.getEmail(), content);
    }
	
	public void sendPasswordResetEmail(User user)
	{
		log.debug("Sending user reset password e-mail to '{}'", user.getEmail());
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("baseUrl", baseUrl);
		context.setVariable("serverAddress", serverAddress);
		String content = templateEngine.process("resetPasswordEmail", context);
		sendEmail(user.getEmail(), content);
	}

	public void sendEmail(String to,  String content) {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, CharEncoding.UTF_8);
			message.setTo(to);
			message.setSubject(ResourceUtil.getMessage(Response.ACTIVATE_ACCOUNT_EMAIL));
			message.setText(content, true);
			javaMailSender.send(mimeMessage);
			log.debug("Sent e-mail to User '{}'", to);
		} catch (Exception e) {
			log.warn("E-mail could not be sent to user '{}'", to, e);
		}
	}
}
