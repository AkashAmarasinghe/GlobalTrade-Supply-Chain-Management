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
            Add Inventory | GlobalTrade
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
                            Add Inventory Item
                        </h1>
                        <p>
                            Create a monitored stock item and reorder threshold
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
                    <form class="form-card" action="${pageContext.request.contextPath}/inventory" method="post">
                        <div class="form-grid three">
                            <div class="form-group">
                                <label>
                                    SKU
                                </label>
                                <input class="form-control" name="sku" required>
                            </div>
                            <div class="form-group">
                                <label>
                                    Item Name
                                </label>
                                <input class="form-control" name="itemName" required>
                            </div>
                            <div class="form-group">
                                <label>
                                    Vendor
                                </label>
                                <select class="form-control" name="vendorId">
                                    <option value="">
                                        No vendor
                                    </option>
                                    <% if(vendors!=null) for(Vendor v:vendors){ %>
                                        <option value="<%=v.getId()%>">
                                            <%=v.getVendorName()%>
                                        </option>
                                    <% } %>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>
                                    Quantity On Hand
                                </label>
                                <input class="form-control" name="quantity" type="number" min="0" value="0" required>
                            </div>
                            <div class="form-group">
                                <label>
                                    Reorder Level
                                </label>
                                <input class="form-control" name="reorderLevel" type="number" min="0" value="10" required>
                            </div>
                            <div class="form-group">
                                <label>
                                    Unit
                                </label>
                                <input class="form-control" name="unit" value="units">
                            </div>
                            <div class="form-group" style="grid-column:1/-1">
                                <label>
                                    Description
                                </label>
                                <textarea class="form-control" name="description">
                                </textarea>
                            </div>
                        </div>
                        <div class="actions-row">
                            <a class="btn btn-outline" href="${pageContext.request.contextPath}/inventory">
                                Cancel
                            </a>
                            <button class="btn btn-primary" type="submit">
                                Save Inventory Item
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
