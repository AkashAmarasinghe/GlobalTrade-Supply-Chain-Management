<%@ page import="java.util.List" %>
<%@ page import="com.globaltrade.entity.Vendor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% List<Vendor> vendors=(List<Vendor>)request.getAttribute("vendors"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Vendors | GlobalTrade
        </title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
    </head>
    <body>
        <div class="app-shell">
            <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>
            <main class="main">
                <header class="topbar">
                    <div class="page-heading">
                        <h1>
                            Vendors
                        </h1>
                        <p>
                            Vendor master data, validation state and performance assessment
                        </p>
                    </div>
                </header>
                <div class="content">
                    <div class="toolbar">
                        <div class="toolbar-left">
                            <div class="search-box">
                                <svg>
                                    <use href="${pageContext.request.contextPath}/assets/icons.svg#search">
                                    </use>
                                </svg>
                                <input type="search" data-table-search="vendorTable" placeholder="Search vendor, country, contact...">
                            </div>
                        </div>
                        <div class="toolbar-right">
                            <a class="btn btn-outline" href="${pageContext.request.contextPath}/vendor-performance">
                                Performance View
                            </a>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/vendors/new">
                                <svg>
                                    <use href="${pageContext.request.contextPath}/assets/icons.svg#plus">
                                    </use>
                                </svg>
                                Register Vendor
                            </a>
                        </div>
                    </div>
                    <section class="card">
                        <div class="card-header">
                            <div>
                                <h3>
                                    Vendor Register
                                </h3>
                                <p>
                                    Data loaded from MySQL through JPA + EJB
                                </p>
                            </div>
                        </div>
                        <div class="table-wrap">
                            <table id="vendorTable">
                                <thead>
                                    <tr>
                                        <th>
                                            Code / Vendor
                                        </th>
                                        <th>
                                            Country
                                        </th>
                                        <th>
                                            Contact
                                        </th>
                                        <th>
                                            Performance
                                        </th>
                                        <th>
                                            Status
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if(vendors!=null&&!vendors.isEmpty()){for(Vendor v:vendors){ %>
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
                                                    <%=v.getContactPerson()==null?"-":v.getContactPerson()%>
                                                    <br>
                                                    <span class="cell-sub">
                                                        <%=v.getEmail()==null?"":v.getEmail()%>
                                                    </span>
                                                </td>
                                                <td>
                                                    <strong>
                                                        <%=v.getPerformanceScore()%>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <span class="badge green">
                                                        <%=v.getStatus()%>
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
                </div>
            </main>
        </div>
        <script src="${pageContext.request.contextPath}/assets/app.js">
        </script>
    </body>
</html>
