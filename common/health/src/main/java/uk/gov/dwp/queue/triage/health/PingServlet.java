package uk.gov.dwp.queue.triage.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class PingServlet extends HttpServlet {

    private static Logger LOGGER = LoggerFactory.getLogger(PingServlet.class);

    private final PingResponseWriter responseWriter;

    public PingServlet(PingResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            responseWriter.write(resp.getWriter());
        } catch (IOException e) {
            LOGGER.error("Unable to get response writer", e);
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "An error occurred writing the response");
        }
    }
}
