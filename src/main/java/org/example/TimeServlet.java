package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    private static final String DEFAULT_TIMEZONE = "UTC";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String timeZone = req.getParameter("timezone");
        if(timeZone == null || timeZone.isEmpty()){
            timeZone = DEFAULT_TIMEZONE;
        }

        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(timeZone));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String modifiedDateTime = localDateTime.format(dateTimeFormatter);

        out.write("<p>${dateTime}</p>"
                .replace("${dateTime}", modifiedDateTime + " " + timeZone));
        out.close();
    }
}
