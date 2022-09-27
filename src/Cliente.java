package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Cliente {
    public static void main(String[] args) throws IOException {
        String msg_digitada; // mensagem digitada
        String msg_recebida; // mensagem recebida

        // cria o stream do teclado
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        // Declara Socket cliente
        DatagramSocket clientSocket = new DatagramSocket();

        // Declara strings de bytes para envio e recebimento de mensagens
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        // Solicita mensagem do teclado
        System.out.println("Digite uma mensagem: ");
        while(true){
            msg_digitada = teclado.readLine();
            sendData = msg_digitada.getBytes();

            // Cria pacote de dados a ser enviado para o servidor
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 7999);
            if (msg_digitada.startsWith("fim")) {
                break;
            }
            // Envia o pacote
            clientSocket.send(sendPacket);

            // Cria pacote de dados a ser recebido do servidor
            DatagramPacket receivePacket =
                    new DatagramPacket(receiveData, receiveData.length);

            // Recebe pacote do servidor
            clientSocket.receive(receivePacket);

            // Separa somente o dado recebido
            msg_recebida = new String(receivePacket.getData());

            // Imprime na console o dado recebido
            System.out.println("Servidor: " + msg_recebida);

        }
        // Fecha o cliente
        clientSocket.close();

    }

}
