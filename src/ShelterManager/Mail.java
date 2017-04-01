package ShelterManager;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class Mail {

    static void send(){

        final String username = " --- wpisz adres email ---";
        final String password = " --- wpisz swoje haslo ---";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sebastianwijas@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("sebastianwijas@gmail.com"));
            message.setSubject("Manager schroniska - alert");
            message.setText("Poziom wolnych miejsc w schronisku jest mniejszy od 5!");
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }
}
