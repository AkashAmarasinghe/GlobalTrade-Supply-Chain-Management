<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, List<String>> permissions = (Map<String, List<String>>) request.getAttribute("permissions");
    Number roleCount = (Number) request.getAttribute("databaseRoleCount");
    Number activeUsers = (Number) request.getAttribute("activeUserCount");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Access Control | GlobalTrade</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>
    <main class="main">
        <header class="topbar">
            <div class="topbar-left">
                <button class="menu-btn" id="menuBtn" aria-label="Open navigation">
                    <svg><use href="${pageContext.request.contextPath}/assets/icons.svg#menu"></use></svg>
                </button>
                <div class="page-heading">
                    <h1>Access Control</h1>
                    <p>Role-based permissions used by the application authorization </p>
                </div>
            </div>
            <div class="user-chip">
                <div class="avatar">GT</div>
                <div class="user-meta">
                    <strong><%=session.getAttribute("fullName")%></strong>
                    <span><%=session.getAttribute("role")%></span>
                </div>
            </div>
        </header>

        <div class="content">


            <div class="stats-grid">
                <div class="stat-card">
                    <span class="stat-label">Configured Roles</span>
                    <div class="stat-value"><%=roleCount == null ? 0 : roleCount%></div>
                </div>
                <div class="stat-card">
                    <span class="stat-label">Active Users</span>
                    <div class="stat-value"><%=activeUsers == null ? 0 : activeUsers%></div>
                </div>
            </div>

            <section class="card">
                <div class="card-header">
                    <div>
                        <h3>Role Permission Matrix</h3>
                        <p>Permissions loaded from application roles stored in the SCM database.</p>
                    </div>
                </div>
                <div class="card-body">
                    <div class="role-grid">
                        <% if (permissions != null && !permissions.isEmpty()) {
                            for (Map.Entry<String, List<String>> entry : permissions.entrySet()) { %>
                            <div class="role-card">
                                <div class="role-head">
                                    <span class="role-title"><%=entry.getKey()%></span>
                                    <span class="badge green">ACTIVE</span>
                                </div>
                                <div class="permission-list">
                                    <% for (String permission : entry.getValue()) { %>
                                        <span class="permission"><%=permission%></span>
                                    <% } %>
                                </div>
                            </div>
                        <%  }
                           } else { %>
                            <div class="empty-note">No roles are available in the database.</div>
                        <% } %>
                    </div>
                </div>
            </section>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/app.js"></script>
</body>
</html>
