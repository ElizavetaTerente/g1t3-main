angular.module('appAdmin',[]).controller('mainViewAdmin',function ($scope,$http) {

    const contextPath = 'http://localhost:8080';

    //sorting in tables

    //sorted by parameter pr . Initially pr = '' (empty) ,so we see values in the table how they are
    //if the "^" button is clicked then corresponding sort function run (because we can sort several columns)
    //and the sorting parameter in html is set. Button change from "^" to "⌄". and corresponding to column
    //boolean variables is changing its value (to track which sort we need to do next : in desc order or asc)
    $scope.pr = '';
    let username = false;
    let lastName = false;
    let firstName = false;

    $scope.sortUsername = function() {
        if(username){
            $scope.pr = '-username';
            document.getElementById("sort_username").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'username';
            document.getElementById("sort_username").innerHTML = '^';
        }
        username = !username;
    }

    $scope.sortLastName = function() {
        if(lastName){
            $scope.pr = '-lastName';
            document.getElementById("sort_lastName").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'lastName';
            document.getElementById("sort_lastName").innerHTML = '^';
        }
        lastName = !lastName;
    }

    $scope.sortFirstName = function() {
        if(firstName){
            $scope.pr = '-firstName';
            document.getElementById("sort_firstName").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'firstName';
            document.getElementById("sort_firstName").innerHTML = '^';
        }
        firstName = !firstName;
    }

    //function to check roles of user.
    //return values from backend:
    //1 = student
    //2 = student + admin
    //3 = admin

    //by default "student mode" button is not shown
    let studentModeButton = document.getElementById("studentModeButton");
    studentModeButton.style.display = "none";

    $scope.checkRoles = function () {
        $http.get(contextPath + '/checkRoles')
            .then(function (response) {
                $scope.roleIndex = response.data;
                if ($scope.roleIndex === 2) {
                    //if user is STUDENT + ADMIN give his opportunity to change between student mode and admin mode
                    //concretly here show button to go to student mode
                    //(and from student/index "admin mode" button is shown)
                    studentModeButton.style.display = "block";
                }
            });
    }
//function to print users in html table
    $scope.loadUsers = function () {
        $http.get(contextPath + '/admin/loadUsers')
            .then(function (response) {
                $scope.usersList = response.data;

            });
    }

    //checking by adding new user if such a username already exists,
    //because its unique index in out database
    //in case exists - corresponding message is shown
    $scope.checkIfUserWithSuchANameAlreadyExists = function (wantedUsername) {
        $scope.usersList.forEach(function (user) {
            if (user.username === wantedUsername) {
                alert("such Username already exists." +
                    "Change it please to to able to add new user");
                //interrupt adding user process
                bool = true;
            }
        });
    }

    //variables shows if username of the user which admin want to add in invalid
    //defaultly false;
    let bool = false;
    $scope.addNewUser = function () {

        //if username is invalid set bool to true;
        $scope.checkIfUserWithSuchANameAlreadyExists($scope.newUser.username);
        //if username is invalid we interrupt adding new user process
        if (bool) {
            return;
        }
        //by adding new user at leat one role need to be specified
        if ($scope.newUser.studentRole !== true && $scope.newUser.adminRole !== true) {
            alert("check at least one role");
            return;
        }
        //adding roles in array to pass to backend
        let roles = [];

        if ($scope.newUser.studentRole === true) {
            roles.push("STUDENT");
        }
        if ($scope.newUser.adminRole === true) {
            roles.push("ADMIN");
        }

        $http.post(contextPath + '/admin/newUser/' + $scope.newUser.username + '/' + $scope.newUser.password + '/' + roles + '/' + $scope.newUser.firstName + '/' + $scope.newUser.lastName + '/' + $scope.newUser.email);
                //reload info to see changes
        window.location.reload();
                //closing "add new user" form after submit
        document.getElementById("addNewUserForm").reset();
        document.getElementById('addUserButton').click();
    }

    $scope.deleteUser = function (username) {
        $http.delete(contextPath + '/admin/deleteUser/' + username);
        alert("User " + username + " deleted!");
        window.location.reload();
    }

    //next 2 functions is edit-save row process (of user table)
    $scope.editUser = function (username, index) {
        //take all the data from user_table with tag "input" in html
        let inputs = document.getElementById('users_table').rows[index].getElementsByTagName('input');

        //to identify row which admin wants to edit to every data(input type in this case because editable)
        //i add index with column name and index of the row which i recieved from html function call
        inputs[1].setAttribute("id", "role1_" + index);
        inputs[2].setAttribute("id", "role2_" + index);
        inputs[3].setAttribute("id", "firstName_" + index);
        inputs[4].setAttribute("id", "lastName_" + index);

        //making all data in row editable
        document.getElementById("role1_" + index).removeAttribute("disabled");
        document.getElementById("role2_" + index).removeAttribute("disabled");

        document.getElementById("firstName_" + index).removeAttribute("style");
        document.getElementById("firstName_" + index).removeAttribute("readonly");

        document.getElementById("lastName_" + index).removeAttribute("style");
        document.getElementById("lastName_" + index).removeAttribute("readonly");

        //load all the buttons from this row in array
        let buttons = document.getElementById('users_table').rows[index].getElementsByTagName('button');
        //set id to buttons in this row to identify them
        buttons[0].setAttribute("id", "save_button_" + index);
        buttons[1].setAttribute("id", "edit_button_" + index);
        //hide "edit" button and show "save" button
        document.getElementById("edit_button_" + index).style.display = "none";
        document.getElementById("save_button_" + index).style.display = "block";
    }

    $scope.saveUser = function (username, index) {
        //saving new values of a row
        let role1 = document.getElementById("role1_" + index).checked;
        let role2 = document.getElementById("role2_" + index).checked;
        let firstName = document.getElementById("firstName_" + index).value;
        let lastName = document.getElementById("lastName_" + index).value;

        //if no roles checked print message and interrupt process
        if (role1 !== true && role2 !== true) {
            alert("check at least one role");
            return;
        }

        //making all data not editable again
        document.getElementById("role1_" + index).disabled = true;
        document.getElementById("role2_" + index).disabled = true;

        document.getElementById("firstName_" + index).setAttribute("style", "border:none;background: transparent");
        document.getElementById("firstName_" + index).setAttribute("readonly", "true");

        document.getElementById("lastName_" + index).setAttribute("style", "border:none;background: transparent");
        document.getElementById("lastName_" + index).setAttribute("readonly", "true");

        //load all buttons in this row info
        //hide "save" button and show again "edit" button
        document.getElementById("edit_button_" + index).style.display = "inline-block";
        document.getElementById("save_button_" + index).style.display = "none";

        //put roles in array to pass them to backend
        let roles = [];

        if (role1 === true) {
            roles.push("STUDENT");
        }
        if (role2 === true) {
            roles.push("ADMIN");
        }
        //pass edited info to backend
        $http.post(contextPath + '/editUser/' + username +'/'+ roles +'/'+ firstName + '/' + lastName);
    }

    //to visualise user roles in CheckBoxe for table
    $scope.checkedFunc = function (roles, index) {
        let inputs = document.getElementById('users_table').rows[index].getElementsByTagName('input');
        if (roles.includes("STUDENT")) {
            inputs[1].checked = true;
        }
        if (roles.includes("ADMIN")) {
            inputs[2].checked = true;
        }
    }

    //when page is load
    $scope.checkRoles();//check if its student+admin to show corresponding functionality
    $scope.loadUsers();//show users in table
    //to enamble closing add new deck form by clicking "close button"
    $scope.closeDropdown = function() {
        $('#collapseExample').hide();
    }
});
