package com.jj.tomcat.util.net;

/**
 * 定义每个Socket发生的事件，这些事件需要容器进一步处理。通常，这些事件由Socket实现触发，但它们也可能由容器触发。
 *
 */
public enum SocketEvent {
    /**
     * Data is available to be read.
     */
    OPEN_READ,

    /**
     * The socket is ready to be written to.
     */
    OPEN_WRITE,

    /**
     * The associated Connector/Endpoint is stopping and the connection/socket
     * needs to be closed cleanly.
     */
    STOP,

    /**
     * A timeout has occurred and the connection needs to be closed cleanly.
     * Currently this is only used by the Servlet 3.0 async processing.
     */
    TIMEOUT,

    /**
     * The client has disconnected.
     */
    DISCONNECT,

    /**
     * An error has occurred on a non-container thread and processing needs to
     * return to the container for any necessary clean-up. Examples of where
     * this is used include:
     * <ul>
     * <li>by NIO2 to signal the failure of a completion handler</li>
     * <li>by the container to signal an I/O error on a non-container thread
     *     during Servlet 3.0 asynchronous processing.</li>
     * </ul>
     */
    ERROR,

    /**
     * A client attempted to establish a connection but failed. Examples of
     * where this is used include:
     * <ul>
     * <li>TLS handshake failures</li>
     * </ul>
     */
    CONNECT_FAIL
}
