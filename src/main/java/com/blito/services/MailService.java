package com.blito.services;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.SendingEmailException;
import com.blito.models.User;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.services.util.HtmlRenderer;

@Service
public class MailService {
	private final Logger log = LoggerFactory.getLogger(MailService.class);
	
	@Value(value = "${api.base.url}")
	String baseUrl;
	
	@Value(value = "${serverAddress}")
	String serverAddress;
	
	@Value("${spring.mail.username}")
	String mailFromAddress;

	@Autowired
	private JavaMailSenderImpl javaMailSender;

	@Autowired
	private HtmlRenderer htmlRenderer;

	public void sendActivationEmail(User user) {
        log.debug("Sending user activation e-mail to '{}'", user.getEmail());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        map.put("baseUrl", baseUrl);
        map.put("serverAddress",serverAddress);
        String content = htmlRenderer.renderHtml("accountVerification", map);
        System.out.println(content);
        sendEmail(user.getEmail(), content,ResourceUtil.getMessage(Response.ACTIVATE_ACCOUNT_EMAIL));
    }
	
	public void sendPasswordResetEmail(User user)
	{
		log.debug("Sending user reset password e-mail to '{}'", user.getEmail());
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        map.put("baseUrl", baseUrl);
        map.put("serverAddress",serverAddress);
        String content = htmlRenderer.renderHtml("forgetPassword", map);
        sendEmail(user.getEmail(), content,ResourceUtil.getMessage(Response.ACTIVATE_ACCOUNT_EMAIL));
	}

	public void sendEmail(String to,  String content,String subject) {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, CharEncoding.UTF_8);
			message.setFrom(mailFromAddress);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(content, true);
			javaMailSender.send(mimeMessage);
			log.debug("Sent e-mail to User '{}'", to);
		} catch (Exception e) {
			log.error("E-mail could not be sent to user '{}'", to, e);
			throw new SendingEmailException(ResourceUtil.getMessage(Response.SENDING_EMAIL_ERROR));
		}
	}
}
