package com.emissor.listeners;

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
public class CartaoReprovadoListener {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = "reprovado-emissor-ms-queue")
    public void receiveMessage(@Payload String mensagem) {
        send(mensagem);
    }

    @Async
    public void send(String to) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(construirCartaoReprovadoEmail(), true);
            helper.setTo(to);
            helper.setSubject("Sinto muito.");
            helper.setFrom("dev.mateuscunha@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }

    private String construirCartaoReprovadoEmail(){
        StringBuilder builder = new StringBuilder();

        builder.append("<!DOCTYPE html>\n" +
                "<html lang=\"pt-BR\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Desculpas pela Não Aprovação de Crédito</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f9f9f9;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background-color: #fff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "            max-width: 500px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .header {\n" +
                "            font-size: 24px;\n" +
                "            margin-bottom: 20px;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .message {\n" +
                "            font-size: 16px;\n" +
                "            margin-bottom: 20px;\n" +
                "            color: #666;\n" +
                "        }\n" +
                "        .button {\n" +
                "            padding: 10px 20px;\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff;\n" +
                "            border: none;\n" +
                "            border-radius: 4px;\n" +
                "            text-decoration: none;\n" +
                "            font-size: 16px;\n" +
                "        }\n" +
                "        .button:hover {\n" +
                "            background-color: #0056b3;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">Lamentamos</div>\n" +
                "        <div class=\"message\">\n" +
                "            Infelizmente, seu pedido de crédito não foi aprovado no momento.\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n");

        return builder.toString();
    }
}
