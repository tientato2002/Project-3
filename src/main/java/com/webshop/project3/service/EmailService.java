package com.webshop.project3.service;

import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;


@Service
public class EmailService {

	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	SpringTemplateEngine templateEngine;

//	public void testEmail() {
//		String to = "trientran2722002@gmail.com";
//		String subject = "Trien ngu";
//		String body = "<h1>Trien ngu</h1>";
//
//		sendMail(to, subject, body);
//	}
//
//	public void sendBirthdayEmail(String to, String name) {
//		String subject = "Happy Birthday !" + name;
//		
//		Context context = new Context();
//		context.setVariable("name", name);
//		
//		String body = templateEngine.process("email/hpbd.html", context);
//
//		sendMail(to, subject, body);
//	}
	
	
	@Async
	public void sendMail(String to, String subject, String body) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		try {
			
			//load template email with content
			Context context = new Context();
			context.setVariable("name", body);
			String html = templateEngine.process("hi.html", context);
			//send email
			helper.setTo(to); //email gui toi
			helper.setSubject(subject);//noi dung
			helper.setText(html, true);// true : ho tro HTML
			helper.setFrom("manh4269@gmail.com");

			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	@Async
	public void sendMailwithFile(String to, String subject, String body) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		try {
			helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File attackment = new File("D:\\Desktop\\Code\\project3\\temp.xlsx");

		try {
			//load template email with content
			Context context = new Context();
			context.setVariable("name", body);
			String html = templateEngine.process("hi.html", context);
			//send email
			helper.setTo(to); //email gui toi
			helper.setSubject(subject);//noi dung
			helper.setText(html, true);// true : ho tro HTML
			helper.setFrom("manh4269@gmail.com");
			
			//them file neu dinh kem ton tai
			if(attackment != null) {
				System.err.println("da vao duoc day");
				String attachmentName = attackment.getName();
				helper.addAttachment(attachmentName,attackment);
				System.err.println("dong 94");
			}
			
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
