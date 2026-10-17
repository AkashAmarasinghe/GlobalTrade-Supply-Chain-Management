<%@ page import="java.util.List" %>
<%@ page import="com.globaltrade.entity.Shipment" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% List<Shipment> shipments=(List<Shipment>)request.getAttribute("shipments"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Shipments | GlobalTrade
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
                            Shipments
                        </h1>
                        <p>
                            Track shipments, carriers and delivery status
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
                                <input type="search" data-table-search="shipmentsTable" placeholder="Search by shipment, vendor or carrier...">
                            </div>
                        </div>
                        <div class="toolbar-right">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/shipments/new">
                                <svg>
                                    <use href="${pageContext.request.contextPath}/assets/icons.svg#plus">
                                    </use>
                                </svg>
                                Create Shipment
                            </a>
                        </div>
                    </div>
                    <section class="card">
                        <div class="card-header">
                            <div>
                                <h3>
                                    Shipment List
                                </h3>
                                <p>
                                    View and manage all current shipments
                                </p>
                            </div>
                        </div>
                        <div class="table-wrap">
                            <table id="shipmentsTable">
                                <thead>
                                    <tr>
                                        <th>
                                            Shipment
                                        </th>
                                        <th>
                                            Vendor
                                        </th>
                                        <th>
                                            Route
                                        </th>
                                        <th>
                                            Carrier
                                        </th>
                                        <th>
                                            ETA
                                        </th>
                                        <th>
                                            Status
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if(shipments!=null&&!shipments.isEmpty()){for(Shipment s:shipments){ %>
                                            <tr>
                                                <td>
                                                    <div class="cell-main">
                                                        <%=s.getShipmentNumber()%>
                                                    </div>
                                                    <div class="cell-sub">
                                                        <%=s.getTrackingReference()==null?"-":s.getTrackingReference()%>
                                                    </div>
                                                </td>
                                                <td>
                                                    <%=s.getVendor()==null?"-":s.getVendor().getVendorName()%>
                                                </td>
                                                <td>
                                                    <%=s.getOriginCountry()%>
                                                    →
                                                    <%=s.getDestinationCountry()%>
                                                </td>
                                                <td>
                                                    <%=s.getCarrierName()==null?"-":s.getCarrierName()%>
                                                </td>
                                                <td>
                                                    <%=s.getExpectedArrivalDate()==null?"-":s.getExpectedArrivalDate().toLocalDate()%>
                                                </td>
                                                <td>
                                                    <span class="badge blue">
                                                        <%=s.getStatus()%>
                                                    </span>
                                                </td>
                                            </tr>
                                    <% }}else{ %>
                                        <tr>
                                            <td colspan="6" class="empty-note">
                                                No shipments saved yet.
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
