package com.oleksandr.servlets;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.io.PrintWriter;
@WebFilter("/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            // Перехоплюємо запит та перевіряємо наявність та валідність параметру "timezone"
            String timezone = request.getParameter("timezone");
            if (timezone == null || isValidTimezone(timezone)) {
                // Якщо параметр "timezone" валідний, продовжуємо ланцюг фільтрів та обробку запиту
                chain.doFilter(request, response);
            } else {
                response.setStatus(400);
                String responseText = """
                                          <html>
                                               <body>
                                                    <center><h1>
                                                        "Invalid timezone UTC %s
                                                    </h1></center>
                                               </body>
                                          </html>""";

                try (PrintWriter writer = response.getWriter()) {
                    writer.write(responseText);
                }
            }
        }
    private boolean isValidTimezone(String timezone) {
        try {
            int currentGMT = Integer.parseInt(timezone.replaceAll("[^0-9-]", ""));
            int maxGMT = 12;
            int minGMT = -11;
            return currentGMT <= maxGMT && currentGMT >= minGMT;
        }catch (NumberFormatException e) {
            return false;
        }
    }
}
