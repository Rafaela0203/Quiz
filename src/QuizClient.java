import java.net.*;
import java.util.Scanner;

public class QuizClient {

    DatagramPacket in;
    DatagramPacket out;
    DatagramSocket connection;
    Scanner scanner;

    public QuizClient() {
        scanner = new Scanner(System.in);
    }

    public void stream() throws Exception {
        connection = new DatagramSocket();
        InetAddress addr = InetAddress.getByName("127.0.0.1");

        while (true) {
            // Request the next question
            String request = "NEXT";
            byte[] buffer = request.getBytes();
            out = new DatagramPacket(buffer, buffer.length, addr, 2025);
            connection.send(out);

            // Receive the question
            in = new DatagramPacket(new byte[512], 512);
            connection.receive(in);
            String question = new String(in.getData()).trim();
            System.out.println("-> " + question);

            // Check if the quiz is finished
            if (question.equals("Quiz finalizado! Obrigado por participar:D")) {
                break; // Exit the loop if the quiz is finished
            }

            // Get the answer from the user
            System.out.print("Resposta sem acentuacao: ");
            String answer = scanner.nextLine();

            // Send the answer back to the server
            String messageToSend = question + ";" + answer;
            buffer = messageToSend.getBytes();
            out = new DatagramPacket(buffer, buffer.length, addr, 2025);
            connection.send(out);

            // Receive the response
            in = new DatagramPacket(new byte [512], 512);
            connection.receive(in);
            String response = new String(in.getData()).trim();
            System.out.println("Resposta do server: " + response);
        }

        // Close the connection
        connection.close();
        scanner.close();
    }

    public static void main(String[] args) throws Exception {
        QuizClient client = new QuizClient();
        client.stream();
    }
}