<%@ page import="java.util.List" %>
<%@ page import="com.globaltrade.entity.Vendor" %>
<%@ page import="com.globaltrade.entity.InventoryItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% List<Vendor> vendors=(List<Vendor>)request.getAttribute("vendors"); List<InventoryItem> inventory=(List<InventoryItem>)request.getAttribute("inventory"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Create Shipment | GlobalTrade
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
                            Create Shipment
                        </h1>
                        <p>
                            Transaction-safe shipment and inventory allocation
                        </p>
                    </div>
                </header>
                <div class="content">
                    
                    <% if(request.getAttribute("formError")!=null){ %>
                        <div class="info-banner" style="background:#fdebed">
                            <strong>
                                <%= request.getAttribute("formError") %>
                            </strong>
                        </div>
                    <% } %>
                    <form class="form-card" action="${pageContext.request.contextPath}/shipments/new" method="post">
                        <div class="form-section">
                            <h3>
                                Shipment Information
                            </h3>
                            <div class="form-grid three">
                                <div class="form-group">
                                    <label>
                                        Shipment Number
                                    </label>
                                    <input class="form-control" name="shipmentNumber" placeholder="GT-SHP-0001" required>
                                </div>
                                <div class="form-group">
                                    <label>
                                        Vendor
                                    </label>
                                    <select class="form-control" name="vendorId">
                                        <option value="">
                                            Select vendor
                                        </option>
                                        <% if(vendors!=null)for(Vendor v:vendors){%>
                                            <option value="<%=v.getId()%>">
                                                <%=v.getVendorName()%>
                                            </option>
                                        <%}%>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>
                                        Carrier
                                    </label>
                                    <input class="form-control" name="carrierName" required>
                                </div>
                                <div class="form-group">
                                    <label>
                                        Tracking Reference
                                    </label>
                                    <input class="form-control" name="trackingReference" required>
                                </div>
                                <div class="form-group">
                                    <label>
                                        Origin Country
                                    </label>
                                    <input class="form-control" name="originCountry" required>
                                </div>
                                <div class="form-group">
                                    <label>
                                        Destination Country
                                    </label>
                                    <input class="form-control" name="destinationCountry" required>
                                </div>
                            </div>
                        </div>
                        <div class="form-section">
                            <h3>
                                Schedule
                            </h3>
                            <div class="form-grid three">
                                <div class="form-group">
                                    <label>
                                        Departure
                                    </label>
                                    <input class="form-control" name="departureDate" type="datetime-local">
                                </div>
                                <div class="form-group">
                                    <label>
                                        Expected Arrival
                                    </label>
                                    <input class="form-control" name="expectedArrivalDate" type="datetime-local">
                                </div>
                                <div class="form-group">
                                    <label>
                                        Status
                                    </label>
                                    <select class="form-control" name="status">
                                        <option>
                                            CREATED
                                        </option>
                                        <option>
                                            BOOKED
                                        </option>
                                        <option>
                                            IN_TRANSIT
                                        </option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="form-section">
                            <h3>
                                Shipment Item
                            </h3>
                            <div class="form-grid">
                                <div class="form-group">
                                    <label>
                                        Inventory Item
                                    </label>
                                    <select class="form-control" name="itemId">
                                        <option value="">
                                            No item allocation
                                        </option>
                                        <% if(inventory!=null)for(InventoryItem i:inventory){%>
                                            <option value="<%=i.getId()%>">
                                                <%=i.getSku()%>
                                                -
                                                <%=i.getItemName()%>
                                                (available
                                                <%=i.getQuantityOnHand()%>
                                                )
                                            </option>
                                        <%}%>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>
                                        Quantity
                                    </label>
                                    <input class="form-control" name="quantity" type="number" min="0" value="0">
                                </div>
                            </div>
                        </div>
                        <div class="actions-row">
                            <a class="btn btn-outline" href="${pageContext.request.contextPath}/shipments">
                                Cancel
                            </a>
                            <button class="btn btn-primary" type="submit">
                                Create Shipment
                            </button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
        <script src="${pageContext.request.contextPath}/assets/app.js">
        </script>
    </body>
</html>
