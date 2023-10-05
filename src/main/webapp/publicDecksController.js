angular.module('publicDecks',[]).controller('pdc',function ($scope,$http) {

    const contextPath = 'http://localhost:8080';
    $scope.parameter = 1;

    //sorting in tables

    //sorted by parameter pr . Initially pr = '' (empty) ,so we see values in the table how they are
    //if the "^" button is clicked then corresponding sort function run (because we can sort several columns)
    //and the sorting parameter in html is set. Button change from "^" to "⌄". and corresponding to column
    //boolean variables is changing its value (to track which sort we need to do next : in desc order or asc)
    $scope.pr = '';
    let name = false;
    let description = false;

    $scope.sortName = function() {
        if(name){
            $scope.pr = '-name';
            document.getElementById("sort_name").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'name';
            document.getElementById("sort_name").innerHTML = '^';
        }
        name = !name;
    }

    $scope.sortDescription = function() {
        if(description){
            $scope.pr = '-description';
            document.getElementById("sort_description").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'description';
            document.getElementById("sort_description").innerHTML = '^';
        }
        description = !description;
    }

    //load public decks to show in table
    $scope.loadPublicDecks = function () {
        $http.get(contextPath + '/publicDecks')
            .then(function (response){
                $scope.publicDecksList = response.data;
            });
    }

    //show detailed view of a deck (without opportunity to edit anything )
    $scope.detailedView = function (deckId) {
        window.location.href = "http://localhost:8080/student/detailedViewOfADeck.html?id="+deckId + "&public=true";
    }

    //add public deck to user's deck
    $scope.addToMyDecks = function (deckId) {
        $http.post(contextPath + '/addToMyDecks/' + deckId)
            .then(function (response){
                $scope.code = response.data;
                //code == true : user already has this deck
                // code == false : deck can be added
                if($scope.code === false){
                    alert("It is your deck or u already added this deck");
                }else{
                    alert("successfully added");
                }
            });



    }

    //admin feature to lock deck
    $scope.blockDeck = function (deckId){
        $http.post(contextPath + '/admin/blockDeck/' + deckId);
        alert("deck blocked");
        window.location.reload();

    }

    //function to check roles of user.
    //return values from backend:
    //1 = student
    //2 = student + admin
    //3 = admin

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const admin = urlParams.get('admin')

    $scope.checkPermissions = function () {
        $http.get(contextPath + '/checkRoles')
            .then(function (response){
                $scope.roleIndex = response.data;
                //if student try to recieve admin functions by changing parameter "admin" in url or admin(only admin) try to recieve user functionality
                if(($scope.roleIndex === 1 && admin === "true") || ($scope.roleIndex === 3 && admin === "false")) {
                    //redirect to error page
                    window.location.href = "http://localhost:8080/error/403.html";
                }
            });
    }

    //when page is load
    $scope.loadPublicDecks(); //load public decks to show in table
    $scope.checkPermissions();
});