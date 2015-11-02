package flextrade.flexvision.fx.nio;

import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public void start() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("localhost", 9091));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            listen();
        } catch (IOException e) {
            log.error("Failed to open selector ", e);
        }
    }

    private void listen() {
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();

                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();

                    if (selectionKey.isAcceptable()) {
                        handleAccept(selectionKey);
                    }

                    if (selectionKey.isReadable()) {
                        handleRead(selectionKey);
                    }
                }
            } catch (IOException e) {
                log.error("Failed to listen ", e);
            }
        }
    }

    private void handleRead(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(16 * 1000);
        int numRead = channel.read(byteBuffer);

        log.debug("Read {} remaining is {}",  numRead, byteBuffer.remaining());

        if (numRead == -1) {
            return;
        }
        byteBuffer.flip();

        byte[] data = new byte[numRead];
        byteBuffer.get(data, 0, numRead);

        String receivedString = new String(data);
        log.debug("Received string is {} in HEX string is {}", receivedString, Hex.encodeHexString(receivedString.getBytes()));
    }

    private void handleAccept(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = ((ServerSocketChannel) selectionKey.channel()).accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);

        channel.write(ByteBuffer.wrap("你是歌手吗？ \r\n".getBytes()));
    }

    public static void main(String... args) {
        Server server = new Server();
        server.start();
    }
}
