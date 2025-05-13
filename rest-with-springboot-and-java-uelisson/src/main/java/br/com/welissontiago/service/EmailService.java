package br.com.welissontiago.service;

import br.com.welissontiago.config.EmailConfig;
import br.com.welissontiago.dto.v1.request.EmailDTO;
import br.com.welissontiago.mail.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(EmailDTO emailDTO) {
        emailSender
                .to(emailDTO.getTo())
                .withSubject(emailDTO.getSubject())
                .withMessage(emailDTO.getBody())
                .send(emailConfig);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {
        File tempFile = null;
        try {
            EmailDTO emailDTO = new ObjectMapper().readValue(emailRequestJson, EmailDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);
            emailSender
                    .to(emailDTO.getTo())
                    .withSubject(emailDTO.getSubject())
                    .withMessage(emailDTO.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfig);
        } catch (Exception e) {
            throw new RuntimeException("Error sending email with file",e);
        } finally {
            if(tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }
}
