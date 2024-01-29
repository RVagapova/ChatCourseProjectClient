package org.example;

import java.io.BufferedReader;
import java.io.IOException;

import static org.example.Client.logger;

public class ListenClient extends Thread {
    private final BufferedReader reader;

    public ListenClient(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String input = reader.readLine();
                System.out.println(input);
                logger.info("Message received from server: " + input);
            } catch (IOException e) {
                logger.warn("Connection with server lost");
                throw new RuntimeException(e);
            }
        }
    }
}
