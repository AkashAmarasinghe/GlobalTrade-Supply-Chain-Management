<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.globaltrade.entity.Vendor" %>
<%@ page import="com.globaltrade.entity.VendorPerformance" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
List<Vendor> vendors=(List<Vendor>)request.getAttribute("vendors");
List<VendorPerformance> evaluations=(List<VendorPerformance>)request.getAttribute("evaluations");
double avg=0; int count=0; int review=0; Vendor top=null;
if(vendors!=null){
for(Vendor v:vendors){
if(v.getPerformanceScore()!=null){
double score=v.getPerformanceScore().doubleValue();
avg+=score; count++;
if(score<70) review++;
if(top==null || score>top.getPerformanceScore().doubleValue()) top=v;
}
}
}
if(count>0) avg/=count;
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Vendor Performance | GlobalTrade
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
                                Vendor Performance
                            </h1>
                            <p>
                                Database-backed supplier performance visibility and scheduled threshold monitoring
                            </p>
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
                                Average Score
                            </span>
                            <div class="stat-value">
                                <%=String.format("%.1f",avg)%>
                            </div>
                            <div class="stat-foot">
                                Across saved vendors
                            </div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">
                                Top Performer
                            </span>
                            <div class="stat-value" style="font-size:18px">
                                <%=top==null?"-":top.getVendorName()%>
                            </div>
                            <div class="stat-foot">
                                <%=top==null?"No vendor data":"Score "+top.getPerformanceScore()%>
                            </div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">
                                Review Required
                            </span>
                            <div class="stat-value">
                                <%=review%>
                            </div>
                            <div class="stat-foot">
                                Score below 70
                            </div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">
                                Evaluations
                            </span>
                            <div class="stat-value">
                                <%=evaluations==null?0:evaluations.size()%>
                            </div>
                            <div class="stat-foot">
                                Recent saved assessments
                            </div>
                        </div>
                    </div>
                    <% if(request.getAttribute("formError")!=null){ %>
                        <div class="info-banner" style="background:#fdebed">
                            <strong><%=request.getAttribute("formError")%></strong>
                        </div>
                    <% } %>
                    <% if(request.getAttribute("formSuccess")!=null){ %>
                        <div class="info-banner" style="background:#eaf8ef">
                            <strong><%=request.getAttribute("formSuccess")%></strong>
                        </div>
                    <% } %>
                    <section class="card">
                        <div class="card-header">
                            <div>
                                <h3>Record Vendor Evaluation</h3>
                                <p>Save delivery, quality and trade-compliance assessment scores</p>
                            </div>
                        </div>
                        <form action="${pageContext.request.contextPath}/vendor-performance" method="post">
                            <div class="form-grid three">
                                <div class="form-group">
                                    <label>Vendor</label>
                                    <select class="form-control" name="vendorId" required>
                                        <option value="">Select vendor</option>
                                        <% if(vendors!=null){for(Vendor v:vendors){ %>
                                            <option value="<%=v.getId()%>"><%=v.getVendorCode()%> - <%=v.getVendorName()%></option>
                                        <% }} %>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Delivery Score</label>
                                    <input class="form-control" type="number" name="deliveryScore" min="0" max="100" step="0.01" required>
                                </div>
                                <div class="form-group">
                                    <label>Quality Score</label>
                                    <input class="form-control" type="number" name="qualityScore" min="0" max="100" step="0.01" required>
                                </div>
                                <div class="form-group">
                                    <label>Compliance Score</label>
                                    <input class="form-control" type="number" name="complianceScore" min="0" max="100" step="0.01" required>
                                </div>
                                <div class="form-group" style="grid-column:span 2">
                                    <label>Remarks</label>
                                    <input class="form-control" name="remarks" maxlength="500">
                                </div>
                            </div>
                            <div class="actions-row">
                                <button class="btn btn-primary" type="submit">Save Evaluation</button>
                            </div>
                        </form>
                    </section>
                    <section class="card" style="margin-top:18px">
                        <div class="card-header">
                            <div>
                                <h3>
                                    Current Vendor Scores
                                </h3>
                                <p>
                                    Loaded from the vendors table through VendorBean
                                </p>
                            </div>
                        </div>
                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>
                                            Vendor
                                        </th>
                                        <th>
                                            Country
                                        </th>
                                        <th>
                                            Current Score
                                        </th>
                                        <th>
                                            Status
                                        </th>
                                        <th>
                                            Review State
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if(vendors!=null&&!vendors.isEmpty()){for(Vendor v:vendors){double score=v.getPerformanceScore()==null?0:v.getPerformanceScore().doubleValue(); %>
                                            <tr>
                                                <td>
                                                    <div class="cell-main">
                                                        <%=v.getVendorName()%>
                                                    </div>
                                                    <div class="cell-sub">
                                                        <%=v.getVendorCode()%>
                                                    </div>
                                                </td>
                                                <td>
                                                    <%=v.getCountry()==null?"-":v.getCountry()%>
                                                </td>
                                                <td>
                                                    <strong>
                                                        <%=v.getPerformanceScore()==null?BigDecimal.ZERO:v.getPerformanceScore()%>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <span class="badge green">
                                                        <%=v.getStatus()%>
                                                    </span>
                                                </td>
                                                <td>
                                                    <span class="badge <%=score<70?"amber":"green"%>">
                                                        <%=score<70?"Review":"Healthy"%>
                                                    </span>
                                                </td>
                                            </tr>
                                    <% }}else{ %>
                                        <tr>
                                            <td colspan="5" class="empty-note">
                                                No vendors saved yet.
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </section>
                    <section class="card" style="margin-top:18px">
                        <div class="card-header">
                            <div>
                                <h3>
                                    Recent Detailed Evaluations
                                </h3>
                                <p>
                                    Delivery, quality and compliance scores recorded by the backend service
                                </p>
                            </div>
                        </div>
                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>
                                            Date
                                        </th>
                                        <th>
                                            Vendor
                                        </th>
                                        <th>
                                            Delivery
                                        </th>
                                        <th>
                                            Quality
                                        </th>
                                        <th>
                                            Compliance
                                        </th>
                                        <th>
                                            Overall
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if(evaluations!=null&&!evaluations.isEmpty()){for(VendorPerformance p:evaluations){ %>
                                            <tr>
                                                <td>
                                                    <%=p.getEvaluationDate()%>
                                                </td>
                                                <td>
                                                    <%=p.getVendor().getVendorName()%>
                                                </td>
                                                <td>
                                                    <%=p.getDeliveryScore()%>
                                                </td>
                                                <td>
                                                    <%=p.getQualityScore()%>
                                                </td>
                                                <td>
                                                    <%=p.getComplianceScore()%>
                                                </td>
                                                <td>
                                                    <strong>
                                                        <%=p.getOverallScore()%>
                                                    </strong>
                                                </td>
                                            </tr>
                                    <% }}else{ %>
                                        <tr>
                                            <td colspan="6" class="empty-note">
                                                No detailed vendor evaluations have been recorded yet. Current vendor scores are shown above.
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
        <script src="${pageContext.request.contextPath}/assets/app.js">
        </script>
    </body>
</html>
