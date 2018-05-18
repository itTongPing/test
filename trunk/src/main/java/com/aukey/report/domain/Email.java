package com.aukey.report.domain;


public class Email {
    public String from;
    public String to;
    public String subject;
    public String text;
    public boolean status;
    private String fileName;
    
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Email [from=" + from + ", to=" + to + ", subject=" + subject + ", text=" + text + ", fileName=" + fileName + "]";
    }
    public Email() {

    }
    public Email(String from, String to, String subject, String text, String fileName) {
        super();
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.fileName = fileName;
    }
    public String getFileName()
    {
        return fileName;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    
}
