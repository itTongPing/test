package com.aukey.report.service;

import org.springframework.http.ResponseEntity;

import com.aukey.report.domain.Email;


public interface SendEmailService {

    public ResponseEntity<Email> sendSimpleMail(Email email);
        

    public ResponseEntity<Email> attachments(Email email) throws Exception;
    
    public ResponseEntity<Email> attachments(Email email,String ext) throws Exception;
}