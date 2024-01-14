package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client extends Thread {
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final Scanner scanner = new Scanner(System.in);
    public static Logger logger = LoggerFactory.getLogger(Client.class);

    public Client() throws IOException {
        InputStream resources = getClass().getClassLoader().getResourceAsStream("properties.yaml");
        Properties properties = new Properties();
        properties.load(resources);
        if (resources != null) {
            resources.close();
        }
        InetAddress inetAddress = InetAddress.getByName(properties.getProperty("host"));
        Socket socket = new Socket(inetAddress, Integer.parseInt(properties.getProperty("port")));

        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        String name = null;
        while (true) {
            try {
                //сообщение от сервера
                String input = reader.readLine();
                System.out.println(input);
                logger.info("Message received from server: " + input);

                String outMsg = scanner.nextLine();
                if (name == null) {
                    while (outMsg.isEmpty()) {
                        logger.info("The name not specified");
                        System.out.println("You should complete your nickname");
                        outMsg = scanner.nextLine();
                    }
                    //ввод и отправка имени клиента
                    name = outMsg;
                    writer.write(name + "\n");
                    writer.flush();
                    logger.info("Nickname " + name + " send");
                } else {
                    //отправка сообщения
                    writer.write(outMsg + "\n");
                    writer.flush();
                    logger.info(name + " send message: " + outMsg);
                }
            } catch (IOException e) {
                logger.warn("Connection with server lost");
                throw new RuntimeException(e);
            }
        }
    }
}
