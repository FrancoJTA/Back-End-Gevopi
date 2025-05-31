package com.example.gevopi_back_end.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendFormularioEmail(String to, String subject, String linkFormulario) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; background-color: #f4f4f9; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 12px; box-shadow: 0 3px 10px rgba(0,0,0,0.1);">
                        <h2 style="color: #0077b6;">Formulario de Evaluación Post-Incendio</h2>
                        <p style="color: #333;">¡Hola voluntario!</p>
                        <p style="color: #555;">
                            Has sido invitado a completar tu formulario de evaluación post-incendio. Por favor, haz clic en el siguiente botón para acceder al formulario y completar tu información.
                        </p>

                        <div style="text-align: center; margin: 30px 0;">
                            <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="margin: auto;">
                                <tr>
                                    <td align="center" bgcolor="#00b4d8" style="border-radius: 8px;">
                                        <a href="%s" target="_blank" style="display: inline-block; padding: 14px 24px; font-size: 16px; color: #ffffff; text-decoration: none; border-radius: 8px; font-weight: bold;">
                                            Completar Formulario
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <p style="color: #555;">Es importante que completes esta evaluación para mantener actualizado tu estado físico y psicológico en nuestra base de datos.</p>

                        <p style="color: #333;">Gracias por tu compromiso.<br>El equipo de GEVOPI</p>

                        <hr style="border: none; border-top: 1px solid #ddd; margin: 30px 0;">
                        <p style="font-size: 12px; color: #999;">Este es un correo automatizado, por favor no respondas.</p>
                    </div>
                </body>
                </html>
                """.formatted(linkFormulario);

            helper.setText(htmlContent, true); // Habilita HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de formulario", e);
        }
    }

    public void sendPasswordEmail(String to, String subject, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background: #ffffff; padding: 30px; border-radius: 12px; box-shadow: 0 3px 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #0077b6;">Recuperación de Contraseña</h2>
                    <p style="color: #333;">Hola,</p>
                    <p style="color: #555;">
                        Has solicitado una nueva contraseña o se te ha asignado una temporal. Por favor, utiliza la siguiente contraseña para iniciar sesión:
                    </p>

                    <div style="text-align: center; margin: 30px 20px;">
                        <span style="display: inline-block; font-size: 24px; font-weight: bold; color: #ffffff; background-color: #0077b6; padding: 12px 24px; border-radius: 8px;">
                            %s
                        </span>
                    </div>

                    <p style="color: #555;">Por tu seguridad, te recomendamos cambiarla después de iniciar sesión.</p>

                    <p style="color: #333;">Gracias,<br>El equipo de GEVOPI</p>

                    <hr style="border: none; border-top: 1px solid #ddd; margin: 30px 0;">
                    <p style="font-size: 12px; color: #999;">Este es un correo automatizado, por favor no respondas.</p>
                </div>
            </body>
            </html>
            """.formatted(password);

            helper.setText(htmlContent, true); // Habilita HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de contraseña", e);
        }
    }

    public void sendAccountDeactivationEmail(String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Notificación de Desactivación de Cuenta");

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background: #ffffff; padding: 30px; border-radius: 12px; box-shadow: 0 3px 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #d62828;">Cuenta Desactivada</h2>
                    <p style="color: #333;">Hola,</p>
                    <p style="color: #555;">
                        Te informamos que tu cuenta ha sido <strong>desactivada</strong> por el equipo de GEVOPI.
                    </p>

                    <p style="color: #555;">
                        Si consideras que esto fue un error o tienes preguntas al respecto, por favor contacta con soporte para obtener más información.
                    </p>

                    <p style="color: #333;">Gracias por tu comprensión.<br>El equipo de GEVOPI</p>

                    <hr style="border: none; border-top: 1px solid #ddd; margin: 30px 0;">
                    <p style="font-size: 12px; color: #999;">Este es un correo automatizado, por favor no respondas.</p>
                </div>
            </body>
            </html>
            """;

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de desactivación de cuenta", e);
        }
    }

}