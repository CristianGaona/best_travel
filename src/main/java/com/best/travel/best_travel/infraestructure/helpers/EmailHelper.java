package com.best.travel.best_travel.infraestructure.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailHelper {
    
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String springMailUsername;

    public EmailHelper(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String name, String product) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String htmlContent = this.readHTMLTemplate(name, product);
        try{
            mimeMessage.setFrom(new InternetAddress(springMailUsername));
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, to);
            mimeMessage.setContent(htmlContent, MediaType.TEXT_HTML_VALUE);
            mailSender.send(mimeMessage);
        }catch (MessagingException e) {
            log.error("Error creating email: {}", e.getMessage());
        }
    }

    public String readHTMLTemplate(String name, String product) {
        try(var lines = Files.lines(TEMPLATE_PATH)){
            var html = lines.collect(Collectors.joining(System.lineSeparator()));
            return html.replace("{name}", name)
                       .replace("{product}", product);
        }catch(IOException e){
            log.error("Cannot read HTML template: {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    private final Path TEMPLATE_PATH = Paths.get("src/main/resources/email/email_template.html");
}
