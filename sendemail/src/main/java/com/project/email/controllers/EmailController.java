/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.email.controllers;


import com.project.email.dtos.EmailDto;
import com.project.email.models.EmailModel;
import com.project.email.services.EmailService;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author suzy
 */
@RestController
@RequestMapping({"/api/email"})
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping({"/sending-email"})
    public ResponseEntity<EmailModel> sendingEmail(@RequestBody @Valid EmailDto emailDto) throws MessagingException {
        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDto, emailModel);
        this.emailService.sendEmail(emailModel);
        return new ResponseEntity(emailModel, HttpStatus.CREATED);
    }

    @GetMapping({"/emails"})
    public ResponseEntity<Page<EmailModel>> getAllEmails(@PageableDefault(page = 0, size = 2, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity(this.emailService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping({"/emails/{id}"})
    public ResponseEntity<Object> getOneEmail(@PathVariable("id") String emailId) {
        Optional<EmailModel> emailModelOptional = this.emailService.findById(emailId);
        if (!emailModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(emailModelOptional.get());
    }
}
