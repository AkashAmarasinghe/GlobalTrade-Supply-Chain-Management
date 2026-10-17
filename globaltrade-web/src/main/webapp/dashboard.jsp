<%@ page import="com.globaltrade.dto.DashboardStats" %>
<%@ page import="java.util.List" %>
<%@ page import="com.globaltrade.entity.Shipment" %>
<%@ page import="com.globaltrade.entity.SystemAlert" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% DashboardStats st=(DashboardStats)request.getAttribute("stats"); List<Shipment> priority=(List<Shipment>)request.getAttribute("priorityShipments"); List<SystemAlert> alerts=(List<SystemAlert>)request.getAttribute("alerts"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Dashboard | GlobalTrade
        </title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
    </head>
    <body data-context-path="<%=request.getContextPath()%>">
        <div class="app-shell">
            <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>
            <main class="main">
                <header class="topbar">
                    <div class="page-heading">
                        <h1>
                          Dashboard
                        </h1>
                        <p>
                            Track shipments, inventory and customs updates
                        </p>
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
                    <div class="hero-strip">
                        <div class="hero-copy">
                            <div class="hero-kicker">
                                GLOBALTRADE OPERATIONS
                            </div>
                            <h2>
                                Supply Chain Overview
                            </h2>
                            <p>
                                View current shipments, stock levels, customs deadlines and vendor performance.
                            </p>
                        </div>
                        <div class="hero-actions">
                            <a class="btn btn-light" href="${pageContext.request.contextPath}/shipments/new">
                                New Shipment
                            </a>
                            <a class="btn btn-outline" style="background:rgba(255,255,255,.1);color:white" href="${pageContext.request.contextPath}/monitoring">
                                System Monitoring
                            </a>
                        </div>
                    </div>
                    <div class="stats-grid">
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
                                Customs Deadlines (48h)
                            </span>
                            <div class="stat-value">
                                <%=st==null?0:st.getCustomsDeadlines()%>
                            </div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">
                                Vendor Performance
                            </span>
                            <div class="stat-value">
                                <%=st==null?"0.0":String.format("%.1f",st.getAverageVendorScore())%>
                                %
                            </div>
                        </div>
                    </div>
                    <div class="grid-2">
                        <section class="card">
                            <div class="card-header">
                                <div>
                                    <h3>
                                        Active Shipments
                                    </h3>
                                    <p>
                                        Current shipment status
                                    </p>
                                </div>
                                <a class="link" href="${pageContext.request.contextPath}/shipments">
                                    View all
                                </a>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>
                                                Shipment
                                            </th>
                                            <th>
                                                Route
                                            </th>
                                            <th>
                                                Carrier
                                            </th>
                                            <th>
                                                Status
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%if(priority!=null&&!priority.isEmpty()){for(Shipment s:priority){%>
                                                <tr>
                                                    <td>
                                                        <div class="cell-main">
                                                            <%=s.getShipmentNumber()%>
                                                        </div>
                                                        <div class="cell-sub">
                                                            <%=s.getVendor()==null?"-":s.getVendor().getVendorName()%>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <%=s.getOriginCountry()%>
                                                        →
                                                        <%=s.getDestinationCountry()%>
                                                    </td>
                                                    <td>
                                                        <%=s.getCarrierName()%>
                                                    </td>
                                                    <td>
                                                        <span class="badge blue">
                                                            <%=s.getStatus()%>
                                                        </span>
                                                    </td>
                                                </tr>
                                        <%}}else{%>
                                            <tr>
                                                <td colspan="4" class="empty-note">
                                                    No priority shipments yet.
                                                </td>
                                            </tr>
                                        <%}%>
                                    </tbody>
                                </table>
                            </div>
                        </section>
                        <section class="card">
                            <div class="card-header">
                                <div>
                                    <h3>
                                        System Alerts
                                    </h3>
                                    <p>
                                        Latest shipment, customs and inventory alerts.
                                    </p>
                                </div>
                                <span class="badge blue">
                                    Live
                                </span>
                            </div>
                            <div class="card-body">
                                <div class="alert-list" id="liveAlertList" data-alert-feed="true">
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
                    </div>
                </div>
            </main>
        </div>
        <script src="${pageContext.request.contextPath}/assets/app.js">
        </script>
    </body>
</html>
