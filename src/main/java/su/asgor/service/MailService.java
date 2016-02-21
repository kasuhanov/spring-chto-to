package su.asgor.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Autowired
    private JavaMailSender javaMailSender;
	public void sendConfirmRegistration(String email, String verificationUrl) {
        send(email, "Подтверждение регистарции", "Чтобы подтвердить регистрацию перейдите по ссылке: "+verificationUrl);
    }
	public void sendPasswordRecovery(String email, String url){
		send(email, "Восстановление пароля", "Чтобы востановить пароль перейдите по ссылке: " + url);
	}
	private void send(String email, String subject, String text) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(email);
            helper.setFrom("test-zakupki@mail.ru");
            helper.setSubject(subject);
            helper.setText(text);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {}
        javaMailSender.send(mail);
    }
}
