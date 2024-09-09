import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String severity = getSeverity(argv);
            String message = getMessage(argv);

            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
        }
    }

    private static String getSeverity(String[] argv) {
        if (argv.length < 1) {
            return "info"; // Valor padrão
        }
        return argv[0]; // O primeiro argumento é a severidade
    }

    private static String getMessage(String[] argv) {
        if (argv.length < 2) {
            return "Walter Filho"; // Mensagem padrão
        }
        return String.join(" ", java.util.Arrays.copyOfRange(argv, 1, argv.length)); // Junta todos os argumentos restantes como mensagem
    }
}
