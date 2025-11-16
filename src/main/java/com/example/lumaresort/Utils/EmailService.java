package com.example.lumaresort.Utils;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {

    private final String remitente;
    private final String password;
    private final Session session;

    // Constructor (configura remitente y servidor SMTP)
    public EmailService(String remitente, String password) {
        this.remitente = remitente;
        this.password = password;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        this.session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });
    }

    // Función básica: enviar correo con texto plano
    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
            System.out.println("Correo enviado a " + destinatario);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Función para enviar correo con HTML
    public void enviarCorreoHTML(String destinatario, String asunto, String html) {
        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setContent(html, "text/html; charset=utf-8");

            Transport.send(mensaje);
            System.out.println("Correo HTML enviado a " + destinatario);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}


/* POSIBLE LLAMADO DE LAS FUNCIONES
 * 
 * 
 *         EmailService emailService = new EmailService(
                "tucorreo@gmail.com",
                "tu_contraseña_de_aplicacion"
        );

        // Enviar texto plano
        emailService.enviarCorreo(
                "destinatario@correo.com",
                "Prueba con texto",
                "Hola, este es un correo de prueba desde Java."
        );

        // Enviar HTML
        emailService.enviarCorreoHTML(
                "destinatario@correo.com",
                "Prueba con HTML",
                "<h1 style='color:blue'>Hola</h1><p>Este es un correo con <b>HTML</b></p>"
        );
    }

    
        
 */
