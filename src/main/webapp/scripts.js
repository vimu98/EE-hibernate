const apiUrl = 'http://localhost:8080/demo_hibernate_war_exploded/api/users';

// 1. Load users and display in table
function loadUsers() {
    fetch(apiUrl)
        .then(res => res.json())
        .then(users => {
            const tbody = document.getElementById('userTableBody');
            tbody.innerHTML = '';
            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>
                        <button class="btn btn-sm btn-warning" onclick="editUser(${user.id}, '${user.name}', '${user.email}')">Edit</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.id})">Delete</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(err => showError('Error loading users: ' + err.message));
}

// 2. Create or Update user
function createOrUpdateUser(event) {
    event.preventDefault();
    const id = document.getElementById('userId').value;
    const name = document.getElementById('name').value.trim();
    const email = document.getElementById('email').value.trim();
    const user = { name, email };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${apiUrl}/${id}` : apiUrl;

    fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
    })
        .then(res => {
            if (!res.ok) throw new Error(`Failed to ${id ? 'update' : 'create'} user`);
            return res.json();
        })
        .then(() => {
            resetForm();
            loadUsers();
        })
        .catch(err => showError(`Error ${id ? 'updating' : 'creating'} user: ` + err.message));
}

// 3. Edit user (populate form)
function editUser(id, name, email) {
    document.getElementById('userId').value = id;
    document.getElementById('name').value = name;
    document.getElementById('email').value = email;
    document.getElementById('formTitle').textContent = 'Update User';
    document.getElementById('submitButton').textContent = 'Update';
    document.getElementById('cancelButton').style.display = 'inline-block';
}

// 4. Delete user
function deleteUser(id) {
    if (confirm('Are you sure you want to delete this user?')) {
        fetch(`${apiUrl}/${id}`, { method: 'DELETE' })
            .then(res => {
                if (!res.ok) throw new Error('Failed to delete user');
                loadUsers();
            })
            .catch(err => showError('Error deleting user: ' + err.message));
    }
}

// Helpers
function resetForm() {
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('formTitle').textContent = 'Create User';
    document.getElementById('submitButton').textContent = 'Create';
    document.getElementById('cancelButton').style.display = 'none';
    document.getElementById('errorMessage').textContent = '';
}

function showError(message) {
    document.getElementById('errorMessage').textContent = message;
}

// Event listeners
document.getElementById('userForm').addEventListener('submit', createOrUpdateUser);
document.getElementById('cancelButton').addEventListener('click', resetForm);
document.addEventListener('DOMContentLoaded', loadUsers);
