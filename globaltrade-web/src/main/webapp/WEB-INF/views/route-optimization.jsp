<%@ page import="java.util.List" %>
<%@ page import="com.globaltrade.dto.RouteOption" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% List<RouteOption> routes=(List<RouteOption>)request.getAttribute("routes"); RouteOption selected=(routes==null||routes.isEmpty())?null:routes.get(0); %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Route Optimization | GlobalTrade
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
                                Route Selection
                            </h1>
                            <p>
                                Compare route options for operational planning
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

                    <div class="grid-equal">
                        <section class="form-card">
                            <div class="section-title">
                                <div>
                                    <h2>
                                        Route Search
                                    </h2>
                                    <p>
                                        Enter the starting point, destination and preferred optimization option
                                    </p>
                                </div>
                            </div>
                            <form action="${pageContext.request.contextPath}/route-optimization" method="post">
                                <div class="form-grid">
                                    <div class="form-group">
                                        <label>
                                            Starting Location
                                        </label>
                                        <input class="form-control" name="origin" value="<%=request.getAttribute("origin")%>" required>
                                    </div>
                                    <div class="form-group">
                                        <label>
                                            Destination
                                        </label>
                                        <input class="form-control" name="destination" value="<%=request.getAttribute("destination")%>" required>
                                    </div>
                                    <div class="form-group">
                                        <label>
                                            Transport Type
                                        </label>
                                        <select class="form-control" name="mode">
                                            <option <%= "Air Freight".equals(request.getAttribute("mode"))?"selected":"" %>
                                                >Air Freight
                                            </option>
                                            <option <%= "Sea Freight".equals(request.getAttribute("mode"))?"selected":"" %>
                                                >Sea Freight
                                            </option>
                                            <option <%= "Multimodal".equals(request.getAttribute("mode"))?"selected":"" %>
                                                >Multimodal
                                            </option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>
                                            Optimization Goal
                                        </label>
                                        <select class="form-control" name="goal">
                                            <option <%= "Fastest Route".equals(request.getAttribute("goal"))?"selected":"" %>
                                                >Fastest Route
                                            </option>
                                            <option <%= "Lowest Risk".equals(request.getAttribute("goal"))?"selected":"" %>
                                                >Lowest Risk
                                            </option>
                                            <option <%= "Balanced".equals(request.getAttribute("goal"))?"selected":"" %>
                                                >Balanced
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="actions-row">
                                    <button class="btn btn-primary" type="submit">
                                        Calculate Routes
                                    </button>
                                </div>
                            </form>
                        </section>
                        <section class="card">
                            <div class="card-header">
                                <div>
                                    <h3>
                                        Selected Route
                                    </h3>
                                    <p>
                                        Recommended backend result
                                    </p>
                                </div>
                                <span class="badge green">
                                    Best Match
                                </span>
                            </div>
                            <div class="card-body">
                                <%if(selected!=null){%>
                                    <div class="cell-main">
                                        <%=selected.getPath()%>
                                    </div>
                                    <div class="kpi-mini" style="margin-top:22px">
                                        <div class="kpi-mini-item">
                                            <span>
                                                Estimated Transit
                                            </span>
                                            <strong>
                                                <%=selected.getTransit()%>
                                            </strong>
                                        </div>
                                        <div class="kpi-mini-item">
                                            <span>
                                                Risk Level
                                            </span>
                                            <strong>
                                                <%=selected.getRisk()%>
                                            </strong>
                                        </div>
                                        <div class="kpi-mini-item">
                                            <span>
                                                Customs Complexity
                                            </span>
                                            <strong>
                                                <%=selected.getCustomsComplexity()%>
                                            </strong>
                                        </div>
                                    </div>
                                <%}else{%>
                                    <div class="empty-note">
                                        No route result.
                                    </div>
                                <%}%>
                            </div>
                        </section>
                    </div>
                    <section class="card">
                        <div class="card-header">
                            <div>
                                <h3>
                                    Alternative Routes
                                </h3>
                                <p>
                                    Calculated by RouteOptimizationBean
                                </p>
                            </div>
                        </div>
                        <div class="table-wrap">
                            <table>
                                <thead>
                                    <tr>
                                        <th>
                                            Option
                                        </th>
                                        <th>
                                            Path
                                        </th>
                                        <th>
                                            Transit
                                        </th>
                                        <th>
                                            Risk
                                        </th>
                                        <th>
                                            Customs Complexity
                                        </th>
                                        <th>
                                            Recommendation
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%if(routes!=null)for(RouteOption r:routes){%>
                                        <tr>
                                            <td>
                                                <strong>
                                                    <%=r.getName()%>
                                                </strong>
                                            </td>
                                            <td>
                                                <%=r.getPath()%>
                                            </td>
                                            <td>
                                                <%=r.getTransit()%>
                                            </td>
                                            <td>
                                                <span class="badge <%= "LOW".equals(r.getRisk())?"green":"amber" %>">
                                                    <%=r.getRisk()%>
                                                </span>
                                            </td>
                                            <td>
                                                <%=r.getCustomsComplexity()%>
                                            </td>
                                            <td>
                                                <span class="badge blue">
                                                    <%=r.getRecommendation()%>
                                                </span>
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
