package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet("/http/*")
public class HttpServletExample extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Rota inválida");
            return;
        }

        switch (path) {
            case "/headers":
                handleHeaders(request, response);
                break;
            case "/session":
                handleSession(request, response);
                break;
            case "/status":
                handleStatus(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Rota GET não encontrada");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if ("/request-body".equals(path)) {
            handleRequestBody(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Rota POST não encontrada");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if ("/methods".equals(path)) {
            handleMethods(response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Rota OPTIONS não encontrada");
        }
    }

    private void handleHeaders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println("=== Cabeçalhos da Requisição ===");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            response.getWriter().println(name + ": " + request.getHeader(name));
        }

        response.getWriter().println("\n=== Informações Adicionais ===");
        response.getWriter().println("IP do Usuário: " + request.getRemoteAddr());
        response.getWriter().println("Versão HTTP: " + request.getProtocol());
    }


    private void handleRequestBody(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        BufferedReader reader = request.getReader();
        String line;

        response.getWriter().println("=== Corpo da Requisição ===");
        while ((line = reader.readLine()) != null) {
            response.getWriter().println(line);
        }
    }

    private void handleSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        String user = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("usuario".equals(c.getName())) {
                    user = c.getValue();
                    break;
                }
            }
        }

        if (user == null) {
            user = "X";
            Cookie cookie = new Cookie("usuario", user);
            cookie.setMaxAge(60 * 60 * 24); // 1 dia
            response.addCookie(cookie);
            response.getWriter().println("Bem-vindo, novo usuário " + user + "!");
        } else {
            response.getWriter().println("Bem-vindo de volta, usuário " + user + "!");
        }
    }

    private void handleStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String codeStr = request.getParameter("code");
        int code = 200;

        try {
            code = Integer.parseInt(codeStr);
        } catch (Exception e) {
            response.setStatus(400);
            response.getWriter().println("Código inválido.");
            return;
        }

        response.setStatus(code);
        response.setContentType("text/plain");
        response.getWriter().println("Código de status HTTP enviado: " + code);
    }

    private void handleMethods(HttpServletResponse response) throws IOException {
        response.setHeader("Allow", "GET, POST, PUT, DELETE, OPTIONS");
        response.setContentType("text/plain");
        response.getWriter().println("Métodos suportados: GET, POST, PUT, DELETE, OPTIONS");
    }
}
