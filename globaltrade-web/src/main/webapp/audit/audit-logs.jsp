<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.globaltrade.entity.AuditLog" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<AuditLog> logs = (List<AuditLog>) request.getAttribute("logs");
    DateTimeFormatter auditTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Audit Logs | GlobalTrade</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
    </head>
    <body>
        <div class="app-shell">
            <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>
            <main class="main">
                <header class="topbar">
                    <div class="page-heading">
                        <h1>Audit Logs</h1>
                        <p>System activity recorded automatically for important business actions</p>
                    </div>
                </header>
                <div class="content">
                    <section class="card">
                        <div class="card-header">
                            <div>
                                <h3>Audit Trail</h3>
                                <p>Create, update, approval and stock actions are recorded here</p>
                            </div>
                        </div>
                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Time</th>
                                        <th>Actor</th>
                                        <th>Action</th>
                                        <th>Target</th>
                                        <th>Result</th>
                                        <th>Execution</th>
                                        <th>Details</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (logs != null && !logs.isEmpty()) {
                                        for (AuditLog a : logs) { %>
                                            <tr>
                                                <td><%= a.getCreatedAt() == null ? "" : a.getCreatedAt().format(auditTimeFormat) %></td>
                                                <td><%= a.getActor() %></td>
                                                <td><%= a.getAction() %></td>
                                                <td>
                                                    <%= a.getEntityType() == null ? "-" : a.getEntityType() %>
                                                    <%= a.getEntityId() == null ? "" : " #" + a.getEntityId() %>
                                                </td>
                                                <td>
                                                    <span class="badge <%= a.isSuccess() ? "green" : "red" %>">
                                                        <%= a.isSuccess() ? "Success" : "Failed" %>
                                                    </span>
                                                </td>
                                                <td><%= a.getExecutionTimeMs() %> ms</td>
                                                <td><%= a.getDetails() == null ? "" : a.getDetails() %></td>
                                            </tr>
                                    <%  }
                                    } else { %>
                                        <tr>
                                            <td colspan="7" class="empty-note">
                                                No audit events yet. Complete a create, update, approval or stock action and refresh this page.
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </section>
                </div>
            </main>
        </div>
        <script src="${pageContext.request.contextPath}/assets/app.js"></script>
        <script>
            window.setTimeout(function () {
                window.location.reload();
            }, 10000);
        </script>
    </body>
</html>
