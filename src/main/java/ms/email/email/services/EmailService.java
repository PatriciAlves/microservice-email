package ms.email.email.services;


import ms.email.email.enuns.StatusEmail;
import ms.email.email.models.EmailModel;
import ms.email.email.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    @Autowired
    EmailRepository repository;

    @Autowired
    private JavaMailSender emailSender;

    public EmailModel sendEmail(EmailModel emailModel) {
        emailModel.setSendDataEmail(LocalDateTime.now());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());

            emailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.SENT);

        }catch (MailException e){
            emailModel.setStatusEmail(StatusEmail.ERROR);

        }finally {
            return repository.save(emailModel);
        }
    }
    public Page<EmailModel> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }
}
