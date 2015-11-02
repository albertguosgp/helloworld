package flextrade.flexvision.fx.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class Client {
    public static void main(String... args) {
        try (
                Socket echoSocket = new Socket("localhost", 9091);
                PrintWriter out =
                        new PrintWriter(new OutputStreamWriter(echoSocket.getOutputStream(), Charset.forName("UTF-8")), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))
        ) {
            while (true) {
                try {
                    Thread.sleep(3000);
                    out.print("1234567");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out.flush();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
