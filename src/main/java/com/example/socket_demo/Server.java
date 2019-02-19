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

            mSocket = mServerSocket.accept();
            System.out.println("클라이언트와 연결 됨");

            DataInputStream dis = new DataInputStream(mSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(mSocket.getOutputStream());

            while (true) {
                echo(dis, dos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { mSocket.close(); } catch (IOException e) { e.printStackTrace(); }
            try { mServerSocket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void echo(DataInputStream dis, DataOutputStream dos) throws IOException {
        var arr = new ArrayList<Byte>();
        while (!hasLineFeed(arr)) {
            try { arr.add(dis.readByte()); } catch (IOException e) { e.printStackTrace(); }
        }

        dos.write(toPrimitiveArray(arr));
        dos.flush();
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
