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
            Add Customs Document | GlobalTrade
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
                            Add Customs Document
                        </h1>
                        <p>
                            Create compliance documentation and a persistent deadline reminder
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
                    <form class="form-card" action="${pageContext.request.contextPath}/customs" method="post">
                        <div class="form-grid">
                            <div class="form-group">
                                <label>
                                    Shipment
                                </label>
                                <select class="form-control" name="shipmentId" required>
                                    <option value="">
                                        Select shipment
                                    </option>
                                    <% if(shipments!=null) for(Shipment s:shipments){ %>
                                        <option value="<%=s.getId()%>">
                                            <%=s.getShipmentNumber()%>
                                        </option>
                                    <% } %>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>
                                    Document Type
                                </label>
                                <input class="form-control" name="documentType" placeholder="Export Declaration" required>
                            </div>
                            <div class="form-group">
                                <label>
                                    Document Number
                                </label>
                                <input class="form-control" name="documentNumber">
                            </div>
                            <div class="form-group">
                                <label>
                                    Due Date
                                </label>
                                <input class="form-control" name="dueDate" type="datetime-local" required>
                            </div>
                            <div class="form-group" style="grid-column:1/-1">
                                <label>
                                    Notes
                                </label>
                                <textarea class="form-control" name="notes">
                                </textarea>
                            </div>
                        </div>
                        <div class="actions-row">
                            <a class="btn btn-outline" href="${pageContext.request.contextPath}/customs">
                                Cancel
                            </a>
                            <button class="btn btn-primary" type="submit">
                                Save Document
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
