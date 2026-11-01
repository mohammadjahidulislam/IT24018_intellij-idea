<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Map" %>
<%!
    private static String esc(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
%>
<%
    // সরাসরি index.jsp খুললে /prime এ পাঠিয়ে দেবে, যাতে ডাটা লোড হয়
    if (request.getAttribute("records") == null) {
        response.sendRedirect(request.getContextPath() + "/prime");
        return;
    }
    @SuppressWarnings("unchecked")
    Map<Integer, Integer> records = (Map<Integer, Integer>) request.getAttribute("records");
    String message = (String) request.getAttribute("message");
    String dbError = (String) request.getAttribute("dbError");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nth Prime</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { border-collapse: collapse; margin-top: 10px; }
        td, th { border: 1px solid #888; padding: 6px 16px; text-align: center; }
        th { background: #eee; }
        .msg { margin: 14px 0; font-weight: bold; color: #0a5; }
        .err { margin: 14px 0; color: #c00; }
    </style>
</head>
<body>
    <h1>Hello World!</h1>
    <h3>Welcome to My First Web Application</h3>

    <h2>Find Nth Prime Number</h2>
    <form method="post" action="<%= request.getContextPath() %>/prime">
        N: <input type="number" name="n" min="1" max="200000" required>
        <button type="submit">Find &amp; Save</button>
    </form>

    <% if (message != null) { %>
        <div class="msg"><%= esc(message) %></div>
    <% } %>
    <% if (dbError != null) { %>
        <div class="err">Database error: <%= esc(dbError) %></div>
    <% } %>

    <h3>Saved in database</h3>
    <table>
        <tr><th>N</th><th>Nth Prime</th></tr>
        <% for (Map.Entry<Integer, Integer> e : records.entrySet()) { %>
            <tr><td><%= e.getKey() %></td><td><%= e.getValue() %></td></tr>
        <% } %>
    </table>

    <p><a href="<%= request.getContextPath() %>/hello">Hello World Servlet</a></p>
</body>
</html>
