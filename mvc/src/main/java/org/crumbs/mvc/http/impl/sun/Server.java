package org.crumbs.mvc.http.impl.sun;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbInit;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.annotation.Property;
import org.crumbs.core.logging.Logger;
import org.crumbs.mvc.exception.CrumbsMVCInitException;
import org.crumbs.mvc.http.CrumbsMVCDispatcher;
import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

@Crumb
public class Server {

    private static final String LOCALHOST =  "127.0.0.1";
    // max socket connections that ServerSocket can accept
    private static final int BACKLOG = 1000;

    private Logger log = Logger.getLogger(Server.class);

    @Property("crumbs.mvc.server.host")
    private String host;
    @Property("crumbs.mvc.server.port")
    private Integer port;

    @CrumbRef
    private CrumbsMVCDispatcher dispatcher;

    private HttpServer server;

    @CrumbInit
    public void start() {
        if(host == null) {
            host = LOCALHOST;
        }
        if(port == null) {
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
                    try {
                        exchange.sendResponseHeaders(response.getStatus().getCode(), response.getBody().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBody());
                        os.close();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex.getMessage());
                    }
                }
            });
            server.start();
        } catch (IOException e) {
            throw new CrumbsMVCInitException("Could not start server due to exception", e);
        }
        log.info("Started Crumbs MVC server on host {} and port {}", host, port);
    }
}
