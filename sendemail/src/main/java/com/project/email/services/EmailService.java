/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.email.services;

/**
 *
 * @author suzy
 */
import com.project.email.models.EmailModel;
import com.project.email.repositories.EmailRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Service
public class EmailService {

    private static TemplateEngine templateEngine;
    private static Context context;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    private JavaMailSender emailSender;

    public EmailModel sendEmail(EmailModel emailModel) throws MessagingException {
        emailModel.setSendDateEmail(LocalDateTime.now());

        try {
            String htmlTemplate = "templates/template";
            initializeTemplateEngine();
            context.setVariable("sender", emailModel.getEmailFrom());
            context.setVariable("content", emailModel.getText());
            context.setVariable("subject", emailModel.getSubject());
            context.setVariable("mailTo", emailModel.getEmailTo());
            String mailBody = templateEngine.process(htmlTemplate, (IContext) context);

            MimeMessage message = this.emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, 3, StandardCharsets.UTF_8.name());

            helper.setTo(emailModel.getEmailTo());
            helper.setSubject(emailModel.getSubject());
            helper.setText(mailBody, true);
            this.emailSender.send(message);

            return (EmailModel) this.emailRepository.save(emailModel);
        } catch (MailException e) {
            return (EmailModel) this.emailRepository.save(emailModel);
        } finally {
            Exception exception = null;
        }
    }

    private void initializeTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver((ITemplateResolver) resolver);
        context = new Context(Locale.US);
    }

    public Page<EmailModel> findAll(Pageable pageable) {
        return this.emailRepository.findAll(pageable);
    }

    public Optional<EmailModel> findById(String emailId) {
        return this.emailRepository.findById(emailId);
    }
}
