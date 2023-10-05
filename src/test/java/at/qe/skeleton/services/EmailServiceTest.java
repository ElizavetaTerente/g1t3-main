package at.qe.skeleton.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;


public class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendSimpleEmail() {
        String toEmail = "test@example.com";
        String subject = "Test Email";
        String body = "This is a test email.";

        emailService.sendSimpleEmail(toEmail, subject, body);

        // Verify that the send method on the mailSender object was called
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

}
