package org.example.demohibernate.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demohibernate.dto.UserDTO;
import org.example.demohibernate.service.UserService;
import org.example.demohibernate.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/users/*")
public class UserServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Create user
        String json = readRequestBody(req);
        UserDTO userDTO = JsonUtil.fromJson(json);
        UserDTO createdUser = userService.createUser(userDTO);
        resp.setContentType("application/json");
        resp.getWriter().write(JsonUtil.toJson(createdUser));
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");

        if (pathInfo == null || pathInfo.equals("/")) {
            // List all users
            List<UserDTO> users = userService.findAllUsers();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < users.size(); i++) {
                json.append(JsonUtil.toJson(users.get(i)));
                if (i < users.size() - 1) json.append(",");
            }
            json.append("]");
            resp.getWriter().write(json.toString());
        } else {
            // Get user by ID
            Long id = Long.parseLong(pathInfo.substring(1));
            UserDTO user = userService.findUser(id);
            if (user != null) {
                resp.getWriter().write(JsonUtil.toJson(user));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Update user
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            Long id = Long.parseLong(pathInfo.substring(1));
            String json = readRequestBody(req);
            UserDTO userDTO = JsonUtil.fromJson(json);
            userDTO.setId(id);
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            if (updatedUser != null) {
                resp.setContentType("application/json");
                resp.getWriter().write(JsonUtil.toJson(updatedUser));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Delete user
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            Long id = Long.parseLong(pathInfo.substring(1));
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        userService.close();
    }

    private String readRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
}