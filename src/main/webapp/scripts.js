const apiUrl = 'http://localhost:8080/demo_hibernate_war_exploded/api/users';
console.log("script load")
// Fetch and display all users
function loadUsers() {
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch users');
            return response.json();
        })
        .then(users => {
            const tbody = $('#userTableBody');
            tbody.empty();
            users.forEach(user => {
                tbody.append(`
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>
                            <button class="btn btn-sm btn-warning edit-user" data-id="${user.id}" data-name="${user.name}" data-email="${user.email}">Edit</button>
                            <button class="btn btn-sm btn-danger delete-user" data-id="${user.id}">Delete</button>
                        </td>
                    </tr>
                `);
            });
        })
        .catch(error => {
            showError('Error loading users: ' + error.message);
        });
}

// Handle form submission (Create/Update)
$('#userForm').on('submit', function (e) {
    e.preventDefault();
    const id = $('#userId').val();
    const user = {
        name: $('#name').val(),
        email: $('#email').val()
    };
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${apiUrl}/${id}` : apiUrl;

    fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (!response.ok) throw new Error(`Failed to ${id ? 'update' : 'create'} user`);
            return response.json();
        })
        .then(() => {
            resetForm();
            loadUsers();
        })
        .catch(error => {
            showError(`Error ${id ? 'updating' : 'creating'} user: ${error.message}`);
        });
});

// Edit user (populate form)
function editUser(id, name, email) {
    $('#userId').val(id);
    $('#name').val(name);
    $('#email').val(email);
    $('#formTitle').text('Update User');
    $('#submitButton').text('Update');
    $('#cancelButton').show();
}

// Delete user
function deleteUser(id) {
    if (confirm('Are you sure you want to delete this user?')) {
        fetch(`${apiUrl}/${id}`, { method: 'DELETE' })
            .then(response => {
                if (!response.ok) throw new Error('Failed to delete user');
                loadUsers();
            })
            .catch(error => {
                showError('Error deleting user: ' + error.message);
            });
    }
}

// Reset form
function resetForm() {
    $('#userForm')[0].reset();
    $('#userId').val('');
    $('#formTitle').text('Create User');
    $('#submitButton').text('Create');
    $('#cancelButton').hide();
    $('#errorMessage').text('');
}

// Show error message
function showError(message) {
    $('#errorMessage').text(message);
}

// Event delegation for edit and delete buttons
$('#userTableBody').on('click', '.edit-user', function () {
    const id = $(this).data('id');
    const name = $(this).data('name');
    const email = $(this).data('email');
    editUser(id, name, email);
});

$('#userTableBody').on('click', '.delete-user', function () {
    const id = $(this).data('id');
    deleteUser(id);
});

// Cancel edit
$('#cancelButton').on('click', resetForm);

// Load users on page load
$(document).ready(loadUsers);