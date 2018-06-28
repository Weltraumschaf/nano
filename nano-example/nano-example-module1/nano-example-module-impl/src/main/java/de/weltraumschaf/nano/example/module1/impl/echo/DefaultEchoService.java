package de.weltraumschaf.nano.example.module1.impl.echo;

import de.weltraumschaf.nano.api.ServiceContext;
import de.weltraumschaf.nano.example.module1.api.EchoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 *
 */
public final class DefaultEchoService implements EchoService {
    private static Logger LOG = LoggerFactory.getLogger(DefaultEchoService.class);
        private static final String EOL = "\r\n";
    private static final int BUFFER_SIZE = 1_024;

    private final int port = 4242;
    private Selector selector;
    private ServerSocket server;
    private ServerSocketChannel channel;
    private volatile boolean listening; // Volatile because must be recognized over multiple threads.

    @Override
    public void activate(final ServiceContext ctx) {
        LOG.debug("Activate echo server ...");
        try {
            selector = Selector.open();

            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);

            server = channel.socket();
            server.bind(new InetSocketAddress(InetAddress.getByName("localhost"), port));
            LOG.debug("Echo server activated.");
        } catch (final IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void start() {
        LOG.debug("Starting instance server at port {0}...", port);
        listening = true;
        new Thread(this::serveUnchecked).start();
    }

    @Override
    public void stop() {
        LOG.debug("Stopping echo server...");
        listening = false;
    }

    @Override
    public void deactivate() {
        LOG.debug("Deactivating echo server...");
        closeSilently(server);
        closeSilently(channel);
        closeSilently(selector);
        LOG.debug("Echo server deactivated.");
    }

    private void serveUnchecked() {
        try {
            serve();
        } catch (final IOException e) {
            LOG.error("The echo server terminated because of an exception: {}", e.getMessage(), e);
        } finally {
            // If you land here, either there was an Exception(stopping is appropriate) or the server was stopped already.
            // Stopping it again is save.
            stop();
            deactivate();
        }
    }

    private void serve() throws IOException {
        while (listening) {
            // This blocks until there are keys for ready channels.
            final int numberOfKeys = selector.select();

            if (numberOfKeys == 0) {
                // No keys selected: nothing to do.
                continue;
            }

            querySelectorKeys();
        }
    }

    private void querySelectorKeys() throws IOException {
        final Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

        while (keys.hasNext()) {
            final SelectionKey key = keys.next();

            if (key.isAcceptable()) {
                acceptNewConnection(key);
            }

            if (key.isReadable()) {
                final SocketChannel connection = (SocketChannel) key.channel();
                sendResponse(connection, drainConnection(connection));
                connection.close();
            }

            // Remove key from selected set: it's been handled.
            keys.remove();
        }
    }

    private void acceptNewConnection(final SelectionKey key) throws IOException {
        final ServerSocketChannel socket = (ServerSocketChannel) key.channel();
        final SocketChannel connection = socket.accept();

        if (connection == null) { // May happen, ignoring.
            return;
        }

        connection.configureBlocking(false);
        // Register for reading the data of the newly accepted connection.
        connection.register(selector, SelectionKey.OP_READ);
    }

    private byte[] drainConnection(final SocketChannel connection) throws IOException {
        final ByteBuffer input = ByteBuffer.allocateDirect(BUFFER_SIZE);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        while (connection.read(input) > 0) {
            input.flip(); // Make buffer readable
            final byte[] bytes = new byte[input.remaining()];
            input.get(bytes, 0, input.remaining());
            output.write(bytes);
            input.clear();
        }

        return output.toByteArray();
    }

    private void sendResponse(final SocketChannel socketChannel, final byte[] data) throws IOException {
        socketChannel.write(ByteBuffer.wrap(data));
    }

    private static void closeSilently(final Closeable toClose) {
        if (null != toClose) {
            try {
                toClose.close();
            } catch (final IOException | ClosedSelectorException e) {
                LOG.warn("Problem closing resource: {}", e.getMessage(), e);
            }
        }
    }

}
