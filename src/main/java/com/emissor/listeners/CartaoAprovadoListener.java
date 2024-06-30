package com.emissor.listeners;

import com.emissor.domain.CartaoEmissorMensagem;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class CartaoAprovadoListener {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = "aprovado-emissor-ms-queue")
    public void receiveMessage(@Payload CartaoEmissorMensagem mensagem) {
        send(mensagem.getEmail(), mensagem);
    }

    @Async
    public void send(String to, CartaoEmissorMensagem mensagem) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(construirCartaoEmail(mensagem), true);
            helper.setTo(to);
            helper.setSubject("\uD83D\uDC4F Parabéns, você acabou de ganhar seu cartao de credito! \uD83D\uDC4F");
            helper.setFrom("dev.mateuscunha@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }

    private String construirCartaoEmail(CartaoEmissorMensagem mensagem){
        StringBuilder builder = new StringBuilder();

        builder.append("<!DOCTYPE html>\n" +
                "<html lang=\"pt-BR\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "      background-color: #f4f4f4;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "    .card-container {\n" +
                "      background-color: #ffffff;\n" +
                "      margin: 50px auto;\n" +
                "      padding: 20px;\n" +
                "      border-radius: 8px;\n" +
                "      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "      max-width: 400px;\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "    .card-header {\n" +
                "      font-size: 24px;\n" +
                "      font-weight: bold;\n" +
                "      color: #333333;\n" +
                "      margin-bottom: 20px;\n" +
                "    }\n" +
                "    .card-details {\n" +
                "      font-size: 18px;\n" +
                "      color: #555555;\n" +
                "      margin-bottom: 10px;\n" +
                "    }\n" +
                "    .card-number {\n" +
                "      font-size: 20px;\n" +
                "      font-weight: bold;\n" +
                "      color: #000000;\n" +
                "      letter-spacing: 2px;\n" +
                "      margin-bottom: 15px;\n" +
                "    }\n" +
                "    .card-info {\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "      margin-bottom: 10px;\n" +
                "    }\n" +
                "    .card-info div {\n" +
                "      width: 45%;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "    .card-footer {\n" +
                "      font-size: 16px;\n" +
                "      color: #888888;\n" +
                "      margin-top: 20px;\n" +
                "    }\n" +
                "  </style>\n" +
                "  <title>Cartão Online</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"card-container\">\n" +
                "  <div class=\"card-header\">Informações do Cartão</div>\n" +
                "  <div class=\"card-details\">Nome: João Silva</div>\n" +
                "  <div class=\"card-number\"> " + mensagem.getNumero() + "</div>\n" +
                "  <div class=\"card-info\">\n" +
                "    <div>Validade: " + mensagem.getDataExpiracao() + "</div>\n" +
                "    <div>CVV: " + mensagem.getCvv() + "</div>\n" +
                "  </div>\n" +
                "  <div class=\"card-footer\">Aproveite seu cartao online.</div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n");

        return builder.toString();
    }
}
