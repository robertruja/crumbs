package org.crumbs.http.mvc.http.impl.sun;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbInit;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.annotation.Property;
import org.crumbs.core.logging.Logger;
import org.crumbs.http.mvc.exception.CrumbsMVCInitException;
import org.crumbs.http.mvc.http.CrumbsMVCDispatcher;
import org.crumbs.http.mvc.http.Request;
import org.crumbs.http.mvc.http.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

@Crumb
public class Server {

    private static final String LOCALHOST = "127.0.0.1";
    // max socket connections that ServerSocket can accept
    private static final int BACKLOG = 1000;

    private Logger log = Logger.getLogger(Server.class);

    @Property("crumbs.mvc.server.enabled")
    private Boolean serverEnabled = true;

    @Property("crumbs.mvc.server.host")
    private String host;
    @Property("crumbs.mvc.server.port")
    private Integer port;

    @CrumbRef
    private CrumbsMVCDispatcher dispatcher;

    private HttpServer server;

    @CrumbInit
    public void start() {
        if (serverEnabled) {
            if (host == null) {
                host = LOCALHOST;
            }
            if (port == null) {
                throw new CrumbsMVCInitException("Port must be specified as property in config file" +
                        " as crumbs.mvc.server.port");
            }
            try {
                server = HttpServer.create(new InetSocketAddress(host, port), BACKLOG);
                server.setExecutor(Executors.newCachedThreadPool());
                server.createContext("/", new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        Request request = RequestImpl.from(exchange);
                        Response response = new ResponseImpl(exchange);
                        dispatcher.handle(request, response);
                        response.flush();
                    }
                });
                server.start();
            } catch (IOException e) {
                throw new CrumbsMVCInitException("Could not start server due to exception", e);
            }
            log.info("Started Crumbs MVC server on host {} and port {}", host, port);
        }
    }

    public void stop() {
        log.info("Stopping Crumbs MVC server...");
        server.stop(1);
        log.info("Server stopped");
    }

    public void setServerEnabled(Boolean serverEnabled) {
        this.serverEnabled = serverEnabled;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
