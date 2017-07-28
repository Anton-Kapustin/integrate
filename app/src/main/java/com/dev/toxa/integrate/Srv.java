//package com.dev.toxa.integrate;
//
//import java.io.IOException;
//import java.net.SocketException;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class Srv {
//    public int port;
//    public Srv() {
//        port = 8030;
//    }
//
//    public static void main(String[] args) throws IOException {
//        boolean connection = false;
//        ServerConnect server = null;
//        String cmd = null;
//        Srv srv = new Srv();
//        List<String> message = new ArrayList<>();
//        int count = 0;
//        while (true) {
//            try {
//                server = new ServerConnect(srv.port);
//                if (server.connect()) {
//                    connection = true;
//                } else connection = false;
//                server.start_input_stream();
//                server.start_output_stream();
//
//                while (server.connection) {
//                    System.out.println("Получение комманды");
//                    cmd = server.receive();
//                    System.out.println("Получено");
//
//                    Commands command = new Commands();
//                    message = command.Command(cmd);
//                    if (message.get(0).contains("exit")) {
//                        server.close();
//                        System.out.println("Close");
//                        break;
//                    }
//                    server.send(message);
//                }
//            } catch (SocketException e) {
//                server.close();
//                connection = false;
//
//                System.err.println("Connection close.");
//                System.err.println("Wait for connection.");
//
//            }
//            server.close();
//            count++;
//            System.out.println("Соединение разорвано (" + count + ")");
//        }
//
//
//    }
//}