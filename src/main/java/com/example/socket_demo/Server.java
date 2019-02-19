package com.example.socket_demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Server {

    private ServerSocket mServerSocket;
    private Socket mSocket;

    public void run() {
        try {
            mServerSocket = new ServerSocket(5555);
            System.out.println("서버 시작!!!");
            // 스레드가 멈춰 있고

            // 연결 요청이 들어오면 연결
            mSocket = mServerSocket.accept();
            System.out.println("클라이언트와 연결 됨");

            DataInputStream dis = new DataInputStream(mSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(mSocket.getOutputStream());

            while(true) {
                var arr = new ArrayList<Byte>();
                var s = Stream.iterate(arr, a -> {
                        try { a.add(dis.readByte()); } catch (IOException e) { e.printStackTrace(); }
                        return a;
                    })
                    .takeWhile(a -> !hasLineFeed(a))
                    .reduce((first, second) -> second)
                    .get();

                dos.write(toPrimitiveArray(s));
                dos.flush();
            }

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try { mSocket.close(); } catch (IOException e) { e.printStackTrace(); }
            try { mServerSocket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private boolean hasLineFeed(List<Byte> a) {
        var size = a.size();
        return size > 0 && a.get(size - 1) == '\n';
    }

    private byte[] toPrimitiveArray(List<Byte> oBytes) {
        byte[] bytes = new byte[oBytes.size()];
        for(int i = 0; i < oBytes.size(); i++){
            bytes[i] = oBytes.get(i);
        }
        return bytes;
    }
}
