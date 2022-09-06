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
        System.out.println("Cliente Ativo!");

        String msg_digitada;
        String msg_recebida;
        String nome_cliente;
        String assunto;

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Informe o nome do cliente: ");
        nome_cliente = teclado.readLine();

        Socket socket = new Socket("localhost", 8657);
        System.out.println(nome_cliente + " entrou no chat!");

        BufferedReader entrada_servidor = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );

        Thread thread = new Cliente(socket);
        thread.start();

        DataOutputStream saida_servidor = new DataOutputStream(cliente.getOutputStream());

        saida_servidor.writeBytes(nome_cliente + '\n');

        System.out.println("Informe o assunto desejado: \n1)Economia \n2)Entretenimento \n3)Tecnologia");
        assunto = teclado.readLine();

        saida_servidor.writeBytes(assunto + '\n');

        while (true) {
            msg_digitada = teclado.readLine();

            if (msg_digitada.startsWith("fim")) {
                break;
            }

            saida_servidor.writeBytes(msg_digitada + '\n');

            msg_recebida = entrada_servidor.readLine();

            System.out.println("Servidor: " + msg_recebida);
        }

        cliente.close();
        System.out.println(nome_cliente + " saiu do chat!");
    }

    public void run() {

    }
}
