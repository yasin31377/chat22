package com.example.chat2.service;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.Socket;

@Service
public class Client {
    private static Socket socket;
    private static BufferedWriter bufferedWriter;
    private static BufferedReader bufferedReader;
    private static String username;
    private static String msgFromGroupChat=null;

    public static String getMsgFromGroupChat() {
        return msgFromGroupChat;
    }

    public static void setMsgFromGroupChat(String msgFromGroupChat) {
        Client.msgFromGroupChat = msgFromGroupChat;
    }

    public Client() {
    }

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sentMessage1(String user) {
        try {
            username = user;
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public static void sentMessage(String messageToSend) {
        try {
            bufferedWriter.write(username + ":  " + messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public static void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null)
                bufferedReader.close();
            if (bufferedWriter != null)
                bufferedWriter.close();
            if (socket != null)
                socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void chat(String username) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.sentMessage1(username);
        client.listenForMessage();
    }

}


