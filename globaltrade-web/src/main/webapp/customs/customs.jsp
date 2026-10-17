<%@ page import="java.util.List" %>
<%@ page import="com.globaltrade.entity.CustomsDocument" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% List<CustomsDocument> docs=(List<CustomsDocument>)request.getAttribute("documents"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Customs | GlobalTrade
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
                            Customs Documentation
                        </h1>
                        <p>
                            Manage trade documents, due dates and reminders
                        </p>
                    </div>
                </header>
                <div class="content">
                    <div class="toolbar">
                        <div>
                        </div>
                        <div class="toolbar-right">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/customs/new">
                                <svg>
                                    <use href="${pageContext.request.contextPath}/assets/icons.svg#plus">
                                    </use>
                                </svg>
                                + New Document
                            </a>
                        </div>
                    </div>
                    <section class="card">
                        <div class="card-header">
                            <div>
                                <h3>
                                    Trade Document Records
                                </h3>
                                <p>
                                    Document reminders are scheduled based on their due dates
                                </p>
                            </div>
                        </div>
                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>
                                            DOCUMENT ID
                                        </th>
                                        <th>
                                            SHIPMENT ID
                                        </th>
                                        <th>
                                            DOCUMENT TYPE
                                        </th>
                                        <th>
                                            DUE DATE
                                        </th>
                                        <th>
                                            CURRENT STATUS
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if(docs!=null&&!docs.isEmpty()){for(CustomsDocument d:docs){%>
                                            <tr>
                                                <td>
                                                    <div class="cell-main">
                                                        <%=d.getDocumentNumber()==null?"-":d.getDocumentNumber()%>
                                                    </div>
                                                </td>
                                                <td>
                                                    <%=d.getShipment().getShipmentNumber()%>
                                                </td>
                                                <td>
                                                    <%=d.getDocumentType()%>
                                                </td>
                                                <td>
                                                    <%=d.getDueDate()==null?"-":d.getDueDate()%>
                                                </td>
                                                <td>
                                                    <span class="badge amber">
                                                        <%=d.getStatus()%>
                                                    </span>
                                                </td>
                                            </tr>
                                    <%}}else{%>
                                        <tr>
                                            <td colspan="5" class="empty-note">
                                                No customs documents saved yet.
                                            </td>
                                        </tr>
                                    <%}%>
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
