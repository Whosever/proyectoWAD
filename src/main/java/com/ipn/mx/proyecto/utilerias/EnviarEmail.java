/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.proyecto.utilerias;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;

/**
 *
 * @author Alvaro Zúñiga
 */
@NoArgsConstructor
public class EnviarEmail {
    public void enviarCorreo(String destinatario, String asunto, String mensaje){
        try {
            Properties propiedades = new Properties();
            propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
            propiedades.setProperty("mail.smtp.starttls.enable", "true");
            propiedades.setProperty("mail.smtp.port", "587");
            propiedades.setProperty("mail.smtp.user", "alvarozunip@gmail.com");
            propiedades.setProperty("mail.smtp.auth", "true");
            //password: =m!zt3M3
            
            Session session;
            session = Session.getDefaultInstance(propiedades);
            MimeMessage elMensaje = new MimeMessage(session);
            
            elMensaje.setFrom(new InternetAddress(propiedades.getProperty("mail.smtp.user")));
            elMensaje.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destinatario));
            elMensaje.setSubject(asunto);
            elMensaje.setText(mensaje);
            
            Transport t = session.getTransport("smtp");
            t.connect(propiedades.getProperty("mail.smtp.user"),"=m!zt3M3");
            
            //Transport.send(elMensaje);
            
            t.sendMessage(elMensaje, elMensaje.getAllRecipients());
            t.close();
            
        } catch (AddressException ex) {
            Logger.getLogger(EnviarEmail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EnviarEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
