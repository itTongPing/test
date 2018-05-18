package com.aukey.report.service.impl;

import com.aukey.report.service.SendEmailService;
import com.aukey.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.aukey.report.domain.Email;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

@Service
public class SendEmailServiceImpl implements SendEmailService
{
    @Autowired
    private JavaMailSender javaMailSender;

    public ResponseEntity<Email> sendSimpleMail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email.getFrom());
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getText());
        javaMailSender.send(message);
        email.setStatus(true);
        return new ResponseEntity<Email>(email, HttpStatus.OK);
    }

    public ResponseEntity<Email> attachments(Email email) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(email.getFrom());
        mimeMessageHelper.setTo(email.getTo());
        mimeMessageHelper.setSubject(email.getSubject());
        mimeMessageHelper.setText("<html><body>" + email.getText() + "</body></html>", true);
        FileSystemResource fileSystemResource = new FileSystemResource(new File(email.getFileName()));
        String title = email.getSubject();
        mimeMessageHelper.addAttachment((DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss"))+".xls", fileSystemResource);
        javaMailSender.send(mimeMessage);
        email.setStatus(true);
        return new ResponseEntity<Email>(email, HttpStatus.OK);
    }
    public ResponseEntity<Email> attachments(Email email, String ext) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(email.getFrom());
        mimeMessageHelper.setTo(email.getTo());
        mimeMessageHelper.setSubject(email.getSubject());
        mimeMessageHelper.setText("<html><body>" + email.getText() + "</body></html>", true);
        FileSystemResource fileSystemResource = new FileSystemResource(new File(email.getFileName()));
        String title = email.getSubject();
        mimeMessageHelper.addAttachment((DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss"))+ "." + ext, fileSystemResource);
        javaMailSender.send(mimeMessage);
        email.setStatus(true);
        return new ResponseEntity<Email>(email, HttpStatus.OK);
    }
}
