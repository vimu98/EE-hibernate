<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Management</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <style>
        body { padding: 20px; }
        .form-container { max-width: 500px; margin-bottom: 20px; }
        .table-container { max-width: 800px; }
        .error { color: red; }
    </style>
</head>
<body>
<div class="container">
    <h1 class="mb-4">User Management</h1>

    <!-- Form for Creating/Updating Users -->
    <div class="form-container">
        <h3 id="formTitle">Create User</h3>
        <form id="userForm">
            <input type="hidden" id="userId">
            <div class="mb-3">
                <label for="name" class="form-label">Name</label>
                <input type="text" class="form-control" id="name" required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" required>
            </div>
            <button type="submit" class="btn btn-primary" id="submitButton">Create</button>
            <button type="button" class="btn btn-secondary" id="cancelButton" style="display: none;">Cancel</button>
        </form>
        <div id="errorMessage" class="error mt-2"></div>
    </div>

    <!-- Table for Displaying Users -->
    <div class="table-container">
        <h3>Users</h3>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody id="userTableBody">
            <!-- Populated by JavaScript -->
            </tbody>
        </table>
    </div>
</div>

<!-- Bootstrap JS and jQuery -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="scripts.js"></script>
</body>
</html>