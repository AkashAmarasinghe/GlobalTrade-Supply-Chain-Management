<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Add Vendor | GlobalTrade
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
                            Register Vendor
                        </h1>
                        <p>
                            Validated supplier master-data registration
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
                    <form class="form-card" action="${pageContext.request.contextPath}/vendors" method="post">
                        <div class="form-grid three">
                            <div class="form-group">
                                <label>
                                    Vendor Code
                                </label>
                                <input class="form-control" name="vendorCode" placeholder="VND-0001" required>
                            </div>
                            <div class="form-group">
                                <label>
                                    Vendor Name
                                </label>
                                <input class="form-control" name="vendorName" required>
                            </div>
                            <div class="form-group">
                                <label>
                                    Country
                                </label>
                                <input class="form-control" name="country">
                            </div>
                            <div class="form-group">
                                <label>
                                    Contact Person
                                </label>
                                <input class="form-control" name="contactPerson">
                            </div>
                            <div class="form-group">
                                <label>
                                    Email
                                </label>
                                <input class="form-control" name="email" type="email">
                            </div>
                            <div class="form-group">
                                <label>
                                    Phone
                                </label>
                                <input class="form-control" name="phone">
                            </div>
                        </div>
                        <div class="actions-row">
                            <a class="btn btn-outline" href="${pageContext.request.contextPath}/vendors">
                                Cancel
                            </a>
                            <button class="btn btn-primary" type="submit">
                                Save Vendor
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
