<%@ page import="java.util.List" %>
<%@ page import="com.globaltrade.entity.InventoryItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% List<InventoryItem> items=(List<InventoryItem>)request.getAttribute("items"); long low=0; if(items!=null) for(InventoryItem i:items) if(i.getQuantityOnHand()<=i.getReorderLevel()) low++; %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Inventory | GlobalTrade
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
                            Inventory
                        </h1>
                        <p>
                            Manage stock levels and reorder quantities
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
                    <% if(request.getAttribute("inventoryError")!=null){ %>
                        <div class="info-banner" style="background:#fdebed">
                            <strong>
                                <%=request.getAttribute("inventoryError")%>
                            </strong>
                        </div>
                    <% } %>
                    <% if("1".equals(request.getParameter("updated"))){ %>
                        <div class="info-banner">
                            <strong>
                                Inventory replenished successfully.
                            </strong>
                            <p>
                                Low-stock alert state was recalculated in the same backend transaction.
                            </p>
                        </div>
                    <% } %>
                    <% if("1".equals(request.getParameter("reconciled"))){ %>
                        <div class="info-banner">
                            <strong>
                                Physical inventory count reconciled successfully.
                            </strong>
                            <p>
                                This update was completed through the bean-managed transaction reconciliation service.
                            </p>
                        </div>
                    <% } %>
                    <div class="stats-grid">
                        <div class="stat-card">
                            <span class="stat-label">
                                Total Items
                            </span>
                            <div class="stat-value">
                                <%=items==null?0:items.size()%>
                            </div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">
                                Low Stock Items
                            </span>
                            <div class="stat-value">
                                <%=low%>
                            </div>
                        </div>
                    </div>
                    <div class="toolbar">
                        <div class="toolbar-left">
                            <div class="search-box">
                                <svg>
                                    <use href="${pageContext.request.contextPath}/assets/icons.svg#search">
                                    </use>
                                </svg>
                                <input type="search" data-table-search="inventoryTable" placeholder="Search by SKU, item or vendorr...">
                            </div>
                        </div>
                        <div class="toolbar-right">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/inventory/new">
                                <svg>
                                    <use href="${pageContext.request.contextPath}/assets/icons.svg#plus">
                                    </use>
                                </svg>
                                Add Inventory Item
                            </a>
                        </div>
                    </div>
                    <section class="card">
                        <div class="card-header">
                            <div>
                                <h3>
                                    Inventory List
                                </h3>
                                <p>
                                    View current stock levels and update item quantities..
                                </p>
                            </div>
                        </div>
                        <div class="table-wrap">
                            <table id="inventoryTable">
                                <thead>
                                    <tr>
                                        <th>
                                            SKU / Item
                                        </th>
                                        <th>
                                            Vendor
                                        </th>
                                        <th>
                                            On Hand
                                        </th>
                                        <th>
                                            Reorder
                                        </th>
                                        <th>
                                            Status
                                        </th>
                                        <th>
                                            Add Stock
                                        </th>
                                        <th>
                                            Physical Count (BMT)
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if(items!=null&&!items.isEmpty()){for(InventoryItem i:items){boolean isLow=i.getQuantityOnHand()<=i.getReorderLevel(); %>
                                            <tr>
                                                <td>
                                                    <div class="cell-main">
                                                        <%=i.getSku()%>
                                                    </div>
                                                    <div class="cell-sub">
                                                        <%=i.getItemName()%>
                                                        •
                                                        <%=i.getUnit()%>
                                                    </div>
                                                </td>
                                                <td>
                                                    <%=i.getVendor()==null?"-":i.getVendor().getVendorName()%>
                                                </td>
                                                <td>
                                                    <strong>
                                                        <%=i.getQuantityOnHand()%>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <%=i.getReorderLevel()%>
                                                </td>
                                                <td>
                                                    <span class="badge <%=isLow?"red":"green"%>">
                                                        <%=isLow?"Low Stock":"In Stock"%>
                                                    </span>
                                                </td>
                                                <td>
                                                    <form class="inline-stock-form" method="post" action="${pageContext.request.contextPath}/inventory/replenish">
                                                        <input type="hidden" name="itemId" value="<%=i.getId()%>">
                                                        <input class="form-control" name="quantity" type="number" min="1" value="10" aria-label="Replenishment quantity">
                                                        <button class="btn btn-outline" type="submit">
                                                            Add Stock
                                                        </button>
                                                    </form>
                                                </td>
                                                <td>
                                                    <form class="inline-stock-form" method="post" action="${pageContext.request.contextPath}/inventory/reconcile">
                                                        <input type="hidden" name="itemId" value="<%=i.getId()%>">
                                                        <input class="form-control" name="physicalCount" type="number" min="0" value="<%=i.getQuantityOnHand()%>" aria-label="Physical inventory count">
                                                        <button class="btn btn-outline" type="submit">
                                                            Reconcile
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                    <% }}else{ %>
                                        <tr>
                                            <td colspan="7" class="empty-note">
                                                No inventory items saved yet.
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
