package com.oleksandr.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String timezone = request.getParameter("timezone");
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        if (timezone != null && !timezone.isEmpty()) {
            dateFormat.setTimeZone(parseTimeZone(timezone));
        } else {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            timezone = "UTC";
        }
        String formattedTime = dateFormat.format(currentTime);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Current time</title></head>");
        out.println("<body>");
        out.println("<h1>Current time (" + timezone + ")</h1>");
        out.println("<p>" + formattedTime + "</p>");
        out.println("</body>");
        out.println("</html>");
    }
    private TimeZone parseTimeZone(String timezoneParam) {
        try {
            int offsetHours = Integer.parseInt(timezoneParam.replace("UTC", "").replace("+", "").trim());
            return TimeZone.getTimeZone("GMT" + (offsetHours >= 0 ? "+" : "") + offsetHours);
        } catch (NumberFormatException e) {
            return TimeZone.getTimeZone("UTC");
        }
    }
}

