package br.com.welissontiago.controller;

import br.com.welissontiago.controller.docs.EmailControllerDocs;
import br.com.welissontiago.dto.v1.request.EmailDTO;
import br.com.welissontiago.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/email/v1")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService service;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) {
        service.sendSimpleEmail(emailDTO);
        return new ResponseEntity<>("email sent with success!", HttpStatus.OK);
    }

    @PostMapping(value = "/withAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendWithAttachment(@RequestParam("emailRequest") String emailRequest,
                                                     @RequestParam("attachment") MultipartFile attachment) {
        service.sendEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("email sent with success!", HttpStatus.OK);
    }
}
