import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class Servidor extends Thread {
    private final Socket conexao;

    private static final Vector<DataOutputStream> vetSaidaEconomia = new Vector<>();
    private static final Vector<DataOutputStream> vetSaidaEntretenimento = new Vector<>();
    private static final Vector<DataOutputStream> vetSaidaTecnologia = new Vector<>();

    public Servidor(Socket socket) {
        conexao = socket;
    }

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(8657);

            while(true) {
                System.out.println("Esperando cliente se conectar...");

                Socket conexao = servidor.accept();

                Thread thread = new Servidor(conexao);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String dataHora() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    public void run() {
        String msg_recebida;
        String msg_enviada;
        String nome_cliente;
        String assunto;

        try {

            DataOutputStream saida_cliente = new DataOutputStream(conexao.getOutputStream());
            BufferedReader entrada_cliente = new BufferedReader(
                new InputStreamReader(conexao.getInputStream())
            );

            nome_cliente = entrada_cliente.readLine();
            System.out.println("Cliente " + nome_cliente + " conectado!");

            saida_cliente.writeBytes("### Seja bem-vindo " + nome_cliente + " !" + "\n");

            assunto = entrada_cliente.readLine();

            Vector<DataOutputStream> todosClientes = null;

            switch (assunto) {
                case "1":
                    vetSaidaEconomia.add(saida_cliente);
                    todosClientes = vetSaidaEconomia;
                    assunto = "Economia";
                    break;
                case "2":
                    vetSaidaEntretenimento.add(saida_cliente);
                    todosClientes = vetSaidaEntretenimento;
                    assunto = "Entretenimento";
                    break;
                default:
                    vetSaidaTecnologia.add(saida_cliente);
                    todosClientes = vetSaidaTecnologia;
                    assunto = "Tecnologia";
                    break;
            }

            for (int i = 0; i < todosClientes.size(); i++) {
                todosClientes.get(i).writeBytes("Servidor: " + nome_cliente + " entrou no chat de " + assunto + " - " + dataHora() + "\n");
            }

            msg_recebida = entrada_cliente.readLine();

            while (
                msg_recebida != null
                && !(msg_recebida.trim().equals(""))
                && !(msg_recebida.startsWith("fim"))
            ) {
                System.out.println(nome_cliente + ": " + msg_recebida);

                msg_enviada = assunto + ": " + msg_recebida + " (" + dataHora() + ")" + '\n';

                for (int i = 0; i < todosClientes.size(); i++) {
                    if (todosClientes.get(i) != saida_cliente) {
                        todosClientes.get(i).writeBytes(msg_enviada);
                    }
                }

                msg_recebida = entrada_cliente.readLine();
            }

            System.out.println(nome_cliente + " desconectou!");
            conexao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
