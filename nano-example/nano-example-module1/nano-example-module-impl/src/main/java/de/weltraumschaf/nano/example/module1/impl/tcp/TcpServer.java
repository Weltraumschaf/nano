package de.weltraumschaf.nano.example.module1.impl.tcp;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.example.module1.api.tcp.TcpServiceConfiguration;
import de.weltraumschaf.nano.example.module1.api.tcp.TcpServiceHandler;
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
 * Simple NIO based TCP server.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class TcpServer {
    private static final Logger LOG = LoggerFactory.getLogger(TcpServer.class);
    private static final int BUFFER_SIZE = 1_024;
    private final TcpServiceConfiguration configuration;
    private final TcpServiceHandler handler;
    private Selector selector;
    private ServerSocket server;
    private ServerSocketChannel channel;
    /**
     * Volatile because must be recognized over multiple threads.
     */
    private volatile boolean listening;

    /**
     * Dedicated constructor.
     *
     * @param configuration not {@code null}
     * @param handler       not {@code null}
     */
    TcpServer(final TcpServiceConfiguration configuration, final TcpServiceHandler handler) {
        super();
        this.configuration = Validate.notNull(configuration, "configuration");
        this.handler = Validate.notNull(handler, "handler");
    }

    /**
     * Strat the server non-blocking in own thread.
     */
    void start() {
        LOG.debug("Starting TCP server on port {} ...", configuration.getPort());

        try {
            selector = Selector.open();
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (final IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        listening = true;
        server = channel.socket();

        try {
            server.bind(new InetSocketAddress(InetAddress.getByName(configuration.getHostname()), configuration.getPort()));
        } catch (final IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        new Thread(this::serve).start();
        LOG.debug("TCP server started at port {} ...", configuration.getPort());
    }

    /**
     * Stops the server.
     */
    void stop() {
        LOG.debug("Stopping TCP server on port {} ...", configuration.getPort());
        listening = false;
        closeSilently(server);
        closeSilently(channel);
        closeSilently(selector);
        LOG.debug("TCP server stopped on port {}.", configuration.getPort());
    }

    private void serve() {
        try {
            while (listening) {
                // This blocks until there are keys for ready channels.
                final int numberOfKeys = selector.select();

                if (numberOfKeys == 0) {
                    // No keys selected: nothing to do.
                    continue;
                }

                querySelectorKeys();
            }
        } catch (final IOException e) {
            LOG.error("The echo server on port {} terminated because of an exception: {}",
                configuration.getPort(), e.getMessage(), e);
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
                final byte[] request = drainConnection(connection);
                final byte[] response = handler.handle(request);
                sendResponse(connection, response);

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
