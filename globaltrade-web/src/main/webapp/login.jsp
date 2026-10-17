<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>
            Sign In | GlobalTrade
        </title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
    </head>
    <body>
        <div class="login-page">
            <section class="login-visual">
                <div class="login-brand">
                    <div class="brand-mark">
                        <span class="brand-orbit">
                        </span>
                        <span class="brand-core">
                            GT
                        </span>
                    </div>
                    <div>
                        <div class="brand-name">
                            GlobalTrade
                        </div>
                        <div class="brand-subtitle">
                            Logistics Corporation
                        </div>
                    </div>
                </div>
                <div class="login-message">
                    <div class="hero-kicker">
                        SUPPLY CHAIN MANAGEMENT SYSTEM
                    </div>
                    <h1>
                        Manage your supply chain in one place.
                    </h1>
                    <p>
                        Manage shipments, inventory, customs and vendors through a single system.
                    </p>
                    <div class="login-feature-grid">
                        <div class="login-feature">
                            <strong>
                                System Monitoring
                            </strong>
                            Track shipment, inventory and customs updates.
                        </div>
                        <div class="login-feature">
                            <strong>
                                User Access
                            </strong>
                            Access is provided based on user roles.
                        </div>
                        <div class="login-feature">
                            <strong>
                                Activity Tracking
                            </strong>
                            Keep track of important system activities.
                        </div>
                        <div class="login-feature">
                            <strong>
                                Reliable Operations
                            </strong>
                            Helps keep data consistent when errors occur.
                        </div>
                    </div>
                </div>
                <div class="login-footer">
                    GlobalTrade Supply Chain Management System
                </div>
            </section>
            <section class="login-form-side">
                <div class="login-card">
                    <div class="hero-kicker" style="color:#0f5bd7">
                        STAFF LOGIN
                    </div>
                    <h2>
                        Welcome to GlobalTrade
                    </h2>
                    <p>
                        Sign in with your username and password.
                    </p>
                    <% if(request.getAttribute("loginError")!=null){ %>
                        <div class="info-banner" style="background:#fdebed;color:#8f2835">
                            <strong>
                                <%= request.getAttribute("loginError") %>
                            </strong>
                        </div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="form-group">
                            <label for="username">
                                Username
                            </label>
                            <div class="input-wrap">
                                <svg>
                                    <use href="${pageContext.request.contextPath}/assets/icons.svg#users">
                                    </use>
                                </svg>
                                <input class="form-control" id="username" name="username" type="text" placeholder="Enter username" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="password">
                                Password
                            </label>
                            <div class="input-wrap">
                                <svg>
                                    <use href="${pageContext.request.contextPath}/assets/icons.svg#lock">
                                    </use>
                                </svg>
                                <input class="form-control" id="password" name="password" type="password" placeholder="Enter password" required>
                                <button type="button" id="passwordToggle" class="icon-btn" style="position:absolute;right:4px;top:3px;border:0;background:transparent">
                                    <svg>
                                        <use href="${pageContext.request.contextPath}/assets/icons.svg#eye">
                                        </use>
                                    </svg>
                                </button>
                            </div>
                        </div>
                        <button class="btn btn-primary login-submit" type="submit">
                            Sign In
                        </button>
                        <div class="login-note">
                            <svg>
                                <use href="${pageContext.request.contextPath}/assets/icons.svg#shield">
                                </use>
                            </svg>
                            <span>
                                Signin here and Manage your supply chain in one place
                            </span>
                        </div>
                    </form>
                </div>
            </section>
        </div>
        <script src="${pageContext.request.contextPath}/assets/app.js">
        </script>
    </body>
</html>
