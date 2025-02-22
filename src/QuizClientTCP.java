import java.io.*;
import java.net.*;
import java.util.Scanner;

public class QuizClientTCP {

    public void stream() throws Exception {
        // Connect to the server on port 2025
        Socket client = new Socket("192.168.2.169", 2025);

        // Create input and output streams
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        // Create a scanner for user input
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Receive the question from the server
            String question = in.readUTF();
            if (question.equals("Quiz finalizado! Obrigado por participar:D")) {
                System.out.println(question);
                break; // Exit the loop if the quiz is finished
            }

            // Display the question and get the answer from the user
            System.out.println("-> " + question);
            System.out.print("Resposta sem acentuacao: ");
            String answer = scanner.nextLine();

            // Send the answer back to the server
            out.writeUTF(answer);

            // Receive the response from the server
            String response = in.readUTF();
            System.out.println("Resposta do server: " + response);
        }

        // Close streams and socket
        in.close();
        out.close();
        client.close();
        scanner.close();
    }

    public static void main(String[] args) throws Exception {
        QuizClientTCP client = new QuizClientTCP();
        client.stream();
    }
}