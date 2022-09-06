import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente extends Thread {
    private Socket cliente;

    public Cliente(Socket socket) {
        cliente = socket;
    }

    public static void main(String[] args) throws IOException {
        String msg_recebida;
        String nome_cliente;
        String assunto;

        Socket cliente = new Socket("localhost", 8657);

        System.out.println("Cliente Ativo!");

        DataOutputStream saida_servidor =
            new DataOutputStream(cliente.getOutputStream()
        );
        BufferedReader entrada_servidor = new BufferedReader(
            new InputStreamReader(cliente.getInputStream())
        );
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Informe o nome do cliente: ");
        nome_cliente = teclado.readLine();
        saida_servidor.writeBytes(nome_cliente + '\n');

        msg_recebida = entrada_servidor.readLine();
        System.out.println("Servidor: " + msg_recebida);

        System.out.println("Informe o assunto desejado: \n1)Economia \n2)Entretenimento \n3)Tecnologia");
        assunto = teclado.readLine();

        saida_servidor.writeBytes(assunto + '\n');

        msg_recebida = entrada_servidor.readLine();
        System.out.println(msg_recebida);

        Thread thread = new Cliente(cliente);
        thread.start();

        while (true) {
            msg_recebida = entrada_servidor.readLine();

            System.out.println(msg_recebida);
        }
    }

    public void run() {
        String msg_digitada;

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        try {
            DataOutputStream saida_servidor =
                new DataOutputStream(cliente.getOutputStream()
            );

            while (true) {
                msg_digitada = teclado.readLine();

                if (msg_digitada.startsWith("fim")) {
                    break;
                }

                saida_servidor.writeBytes(msg_digitada + '\n');
            }

            cliente.close();
            System.out.println("Conexão encerrada!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
