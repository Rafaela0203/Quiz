import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class QuizServerTCP {

    private List<String> quizQuestions;
    private int currentQuestionIndex;

    public QuizServerTCP() {
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
        // Create a server socket on port 2025
        ServerSocket serverSocket = new ServerSocket(2025);
        System.out.println("Server espereando conexao...");

        // Accept a connection from a client
        Socket socket = serverSocket.accept();
        System.out.println("Conexao recebida de " + socket.getInetAddress().getHostName());

        // Create input and output streams
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Communicate with the client
        while (currentQuestionIndex < quizQuestions.size()) {
            // Send the current question
            String question = quizQuestions.get(currentQuestionIndex);
            out.writeUTF(question);
            currentQuestionIndex++;

            // Wait for the client's answer
            String answer = in.readUTF();
            String correctAnswer = getCorrectAnswer(question);
            String response = (correctAnswer != null && correctAnswer.equalsIgnoreCase(answer)) ? "Certo!" : "Errado!";
            out.writeUTF(response);
        }

        // Notify the client that the quiz is finished
        out.writeUTF("Quiz finalizado! Obrigado por participar:D");

        // Close streams and socket
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
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
        QuizServerTCP server = new QuizServerTCP();
        server.stream();
    }
}