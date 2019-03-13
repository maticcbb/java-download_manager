package pl.sda.downloadmanager;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyEmbeddedServer {
    /**
     * Creates a configured server, that always returns the given response.
     *
     * @param content content of the response
     * @param port    a port that server will listen on
     * @return the configured server
     */
    public static Server prepareServerWithResponse(String content, int port) {
        Server server = new Server(port);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
                httpServletResponse.setContentType("text/plain");
                httpServletResponse.setStatus(200);
                httpServletResponse.getWriter().write(content);
                request.setHandled(true);
            }
        });
        return server;
    }
}
