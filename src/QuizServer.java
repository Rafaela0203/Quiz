import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class QuizServer {

    DatagramPacket in;
    DatagramPacket out;
    DatagramSocket connection;
    List<String> quizQuestions;
    int currentQuestionIndex;

    public QuizServer() {
        // Initialize quiz questions
        quizQuestions = new ArrayList<>();
        quizQuestions.add("Qual o maior planeta do Sistema Solar?");
        quizQuestions.add("Qual é o planeta mais quente do nosso sistema solar?");
        quizQuestions.add("Qual é o planeta mais próximo do Sol?");
        quizQuestions.add("Qual planeta possui o maior número de luas conhecidas?");
        quizQuestions.add("Qual planeta possui uma atmosfera composta principalmente por dióxido de carbono?");
        currentQuestionIndex = 0;
    }

    public void stream() throws Exception {
        connection = new DatagramSocket(2025);
        System.out.println("Server do Quiz esta rodando...");

        while (true) {
            // Receive a datagram
            in = new DatagramPacket(new byte[512], 512);
            connection.receive(in);
            String receivedMessage = new String(in.getData()).trim();
            System.out.println("Server recebeu: " + receivedMessage);

            // Check if the client is ready for the next question
            if (receivedMessage.equalsIgnoreCase("NEXT")) {
                if (currentQuestionIndex < quizQuestions.size()) {
                    String question = quizQuestions.get(currentQuestionIndex);
                    byte[] buffer = question.getBytes();
                    out = new DatagramPacket(buffer, buffer.length, in.getAddress(), in.getPort());
                    connection.send(out);
                    currentQuestionIndex++;
                } else {
                    // No more questions
                    String endMessage = "Quiz finalizado! Obrigado por participar:D";
                    byte[] buffer = endMessage.getBytes();
                    out = new DatagramPacket(buffer, buffer.length, in.getAddress(), in.getPort());
                    connection.send(out);
                    break; // Exit the loop after sending the final message
                }
            } else {
                // Check the answer
                String[] parts = receivedMessage.split(";");
                if (parts.length == 2) {
                    String question = parts[0];
                    String answer = parts[1];
                    String correctAnswer = getCorrectAnswer(question);
                    String response = (correctAnswer != null && correctAnswer.equalsIgnoreCase(answer)) ? "Certo!" : "Errado!";
                    byte[] buffer = response.getBytes();
                    out = new DatagramPacket(buffer, buffer.length, in.getAddress(), in.getPort());
                    connection.send(out);
                }
            }
        }

        // Close the connection
        connection.close();
    }

    private String getCorrectAnswer(String question) {
        switch (question) {
            case "Qual o maior planeta do Sistema Solar?":
                return "Jupiter";
            case "Qual é o planeta mais quente do nosso sistema solar?":
                return "Venus";
            case "Qual é o planeta mais próximo do Sol?":
                return "Mercurio";
            case "Qual planeta possui o maior número de luas conhecidas?":
                return "Saturno";
            case "Qual planeta possui uma atmosfera composta principalmente por dióxido de carbono?":
                return "Venus";
            default:
                return null;
        }
    }

    public static void main(String[] args) throws Exception {
        QuizServer server = new QuizServer();
        server.stream();
    }
}