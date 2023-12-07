package org.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    private static final String DEFAULT_TIMEZONE = "UTC";
    private TemplateEngine engine;

    @Override
    public void init() {
        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();

        resolver.setPrefix("C:/Users/Abramiuk/Documents/GitHub/GoIT_dev_homework_9/src/main/resources/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        Map<String, Object> params = new LinkedHashMap<>();
        String timezone = req.getParameter("timezone");

        if (timezone == null) {
            if(req.getCookies() == null){
                timezone = DEFAULT_TIMEZONE;
            } else {
                timezone = getTimezoneValue(req.getCookies());
            }
        }
        resp.addCookie(new Cookie("lastTimezone", timezone));

        params.put("time" + timezone, getCurrentTime(timezone));
        Context context = new Context(req.getLocale(), Map.of("queryTime", params));
        engine.process("timeApp", context, resp.getWriter());
        resp.getWriter().close();
    }


    private String getTimezoneValue(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("lastTimezone")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String getCurrentTime(String timezone){
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(timezone));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(dateTimeFormatter) + " " + timezone;
    }
}
