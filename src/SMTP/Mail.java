package SMTP;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Mail {

    Session newSession = null;
    MimeMessage mimeMessage = null;
    public static void main(String[] args) throws MessagingException, IOException {
//        Mail mail = new Mail();
//        mail.setupServerProperties();
//        mail.draftEmail("alexobz09@gmail.com","hi alex");
//        mail.sendEmail();
    }

    public Mail(){

    }

    public void sendEmail() throws MessagingException {
        String fromUser = "alexobz01@gmail.com";
        String fromUserPassword = "gdqjalpudtihjibk";
        String emailHost = "smtp.gmail.com";
        Transport transport = newSession.getTransport("smtp");
        transport.connect(emailHost,fromUser,fromUserPassword);
        transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        transport.close();


    }

    public MimeMessage draftEmail(String recipient, String emailBody, String attachmentFilePath) throws MessagingException, AddressException, IOException {
        String[] emailRecipients = {recipient};
        String emailSubject = "Test Email";
        mimeMessage = new MimeMessage(newSession);

        for(int i = 0; i < emailRecipients.length; i++){
            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(emailRecipients[i])));
        }
        mimeMessage.setSubject(emailSubject);


        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailBody,"text/html;charset=UTF-8");

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        attachmentBodyPart.attachFile(new File(String.valueOf(attachmentFilePath)));

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);

        multipart.addBodyPart(attachmentBodyPart);

        mimeMessage.setContent(multipart);
        return mimeMessage;

    }

    public MimeMessage draftEmail(String recipient, String emailBody) throws MessagingException, AddressException, IOException {
        String[] emailRecipients = {recipient};
        String emailSubject = "Test Email";
        mimeMessage = new MimeMessage(newSession);

        for(int i = 0; i < emailRecipients.length; i++){
            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(emailRecipients[i])));
        }
        mimeMessage.setSubject(emailSubject);


        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailBody,"text/html;charset=UTF-8");


        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);


        mimeMessage.setContent(multipart);
        return mimeMessage;

    }

    public void setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");
        newSession = Session.getDefaultInstance(properties,null);
    }

}