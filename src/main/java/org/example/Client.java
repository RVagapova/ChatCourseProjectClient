package org.example;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client extends Thread {
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final Scanner scanner = new Scanner(System.in);

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

                String outMsg = scanner.nextLine();
                if (name == null) {
                    while (outMsg.isEmpty()) {
                        System.out.println("You should complete your nickname");
                        outMsg = scanner.nextLine();
                    }
                    //ввод и отправка имени клиента
                    name = outMsg;
                    writer.write(name + "\n");
                    writer.flush();
                } else {
                    //отправка сообщения
                    writer.write(outMsg + "\n");
                    writer.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
