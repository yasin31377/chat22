package com.example.chat2;

import java.io.IOException;
import java.util.Objects;

import com.example.chat2.service.Client;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static java.util.Objects.*;

@Component
public class SocketTextHandler extends TextWebSocketHandler {
    @Autowired
    Client client;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        String payload = message.getPayload();
        JSONObject jsonObject = new JSONObject(payload);
        try {
            client.chat((String) jsonObject.get("username"));
            jsonObject.remove("username");
        } catch (IOException | JSONException e) {
            try {
                client.sentMessage((String) jsonObject.get("text"));
                jsonObject.remove("text");
            } catch (JSONException ex) {
                System.out.println("text");            }
        }
            new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    String msg = client.getMsgFromGroupChat();
                                    if (msg != null) {
                                        (session).sendMessage(new TextMessage(msg));
                                        client.setMsgFromGroupChat(null);
                                    }
                                } catch (IOException exception) {
                                    System.out.println("");
                                }
                            }
                        }     }).start();

        }
    }



