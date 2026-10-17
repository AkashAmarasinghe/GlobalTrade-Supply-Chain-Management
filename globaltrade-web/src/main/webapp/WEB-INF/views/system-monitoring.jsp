<%@ page import="java.util.List" %>
<%@ page import="com.globaltrade.dto.DashboardStats" %>
<%@ page import="com.globaltrade.entity.SystemAlert" %>
<%@ page import="com.globaltrade.entity.AuditLog" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% DashboardStats st=(DashboardStats)request.getAttribute("stats"); List<SystemAlert> alerts=(List<SystemAlert>)request.getAttribute("alerts"); List<AuditLog> logs=(List<AuditLog>)request.getAttribute("logs"); %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            System Monitoring | GlobalTrade
        </title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
    </head>
    <body>
        <div class="app-shell">
            <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>
            <main class="main">
                <header class="topbar">
                    <div class="topbar-left">
                        <button class="menu-btn" id="menuBtn" aria-label="Open navigation">
                            <svg>
                                <use href="${pageContext.request.contextPath}/assets/icons.svg#menu">
                                </use>
                            </svg>
                        </button>
                        <div class="page-heading">
                            <h1>
                                System Monitoring
                            </h1>

                        </div>
                    </div>
                    <div class="user-chip">
                        <div class="avatar">
                            GT
                        </div>
                        <div class="user-meta">
                            <strong>
                                <%=session.getAttribute("fullName")%>
                            </strong>
                            <span>
                                <%=session.getAttribute("role")%>
                            </span>
                        </div>
                    </div>
                </header>
                <div class="content">

                    <div class="stats-grid">
                        <div class="stat-card">
                            <span class="stat-label">
                                Open Alerts
                            </span>
                            <div class="stat-value">
                                <%=st==null?0:st.getOpenAlerts()%>
                            </div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">
                                Active Shipments
                            </span>
                            <div class="stat-value">
                                <%=st==null?0:st.getActiveShipments()%>
                            </div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">
                                Low Stock Items
                            </span>
                            <div class="stat-value">
                                <%=st==null?0:st.getLowStockItems()%>
                            </div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">
                                Customs Deadlines
                            </span>
                            <div class="stat-value">
                                <%=st==null?0:st.getCustomsDeadlines()%>
                            </div>
                        </div>
                    </div>
                    <div class="grid-2">
                        <section class="card">
                            <div class="card-header">
                                <div>
                                    <h3>
                                        Current Alerts
                                    </h3>
                                    <p>
                                        Timer and transaction-generated events
                                    </p>
                                </div>
                            </div>
                            <div class="card-body">
                                <div class="alert-list">
                                    <%if(alerts!=null&&!alerts.isEmpty()){for(SystemAlert a:alerts){%>
                                            <div class="alert-item">
                                                <div class="alert-icon <%="HIGH".equals(a.getSeverity())?"red":"blue"%>">
                                                    <svg>
                                                        <use href="${pageContext.request.contextPath}/assets/icons.svg#alert">
                                                        </use>
                                                    </svg>
                                                </div>
                                                <div class="alert-copy">
                                                    <strong>
                                                        <%=a.getTitle()%>
                                                    </strong>
                                                    <p>
                                                        <%=a.getMessage()%>
                                                    </p>
                                                    <div class="alert-meta">
                                                        <span>
                                                            <%=a.getSeverity()%>
                                                        </span>
                                                        <span>
                                                            <%=a.getCategory()%>
                                                        </span>
                                                        <span>
                                                            <%=a.getReferenceType()==null?"SYSTEM":a.getReferenceType()+(a.getReferenceId()==null?"":" #"+a.getReferenceId())%>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                    <%}}else{%>
                                        <div class="empty-note">
                                            No open alerts.
                                        </div>
                                    <%}%>
                                </div>
                            </div>
                        </section>
                        <section class="card">
                            <div class="card-header">
                                <div>
                                    <h3>
                                        Recent Business Calls
                                    </h3>
                                    <p>
                                        AuditInterceptor output from the database
                                    </p>
                                </div>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>
                                                Action
                                            </th>
                                            <th>
                                                Result
                                            </th>
                                            <th>
                                                Time
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%if(logs!=null&&!logs.isEmpty()){for(AuditLog a:logs){%>
                                                <tr>
                                                    <td>
                                                        <%=a.getAction()%>
                                                    </td>
                                                    <td>
                                                        <span class="badge <%=a.isSuccess()?"green":"red"%>">
                                                            <%=a.isSuccess()?"Success":"Failed"%>
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <%=a.getExecutionTimeMs()%>
                                                        ms
                                                    </td>
                                                </tr>
                                        <%}}else{%>
                                            <tr>
                                                <td colspan="3" class="empty-note">
                                                    No audit calls recorded yet.
                                                </td>
                                            </tr>
                                        <%}%>
                                    </tbody>
                                </table>
                            </div>
                        </section>
                    </div>

                </div>
            </main>
        </div>
        <script src="${pageContext.request.contextPath}/assets/app.js">
        </script>
    </body>
</html>
