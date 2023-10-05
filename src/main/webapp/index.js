angular.module('app',[]).controller('mainView',function ($scope,$http) {

    const contextPath = 'http://localhost:8080';

    //sorting in tables

    //sorted by parameter pr . Initially pr = '' (empty) ,so we see values in the table how they are
    //if the "^" button is clicked then corresponding sort function run (because we can sort several columns)
    //and the sorting parameter in html is set. Button change from "^" to "⌄". and corresponding to column
    //boolean variables is changing its value (to track which sort we need to do next : in desc order or asc)
    $scope.pr = '';
    let name = false;
    let description = false;
    let amountOfNewCards = false;
    let amountOfCardsToRepeat = false;

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

    $scope.sortAmountOfNewCards = function() {
        if(amountOfNewCards){
            $scope.pr = '-amountOfNewCards';
            document.getElementById("sort_amountOfNewCards").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'amountOfNewCards';
            document.getElementById("sort_amountOfNewCards").innerHTML = '^';
        }
        amountOfNewCards = !amountOfNewCards;
    }

    $scope.sortAmountOfCardsToRepeat = function() {
        if(amountOfCardsToRepeat){
            $scope.pr = '-amountOfCardsToRepeat';
            document.getElementById("sort_amountOfCardsToRepeat").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'amountOfCardsToRepeat';
            document.getElementById("sort_amountOfCardsToRepeat").innerHTML = '^';
        }
        amountOfCardsToRepeat = !amountOfCardsToRepeat;
    }
    //

    //function to print decks of the user in html table
    $scope.loadDecks = function () {
        $http.get(contextPath + '/decks')
            .then(function (response){
                $scope.decksList = response.data;
            });
    }

    //function to check roles of user.
    //return values from backend:
    //1 = student
    //2 = student + admin
    //3 = admin

    //by default "admin mode" button is not shown
    let adminModeButton = document.getElementById("adminModeButton");
    adminModeButton.style.display = "none";

    $scope.checkRoles = function () {
        $http.get(contextPath + '/checkRoles')
            .then(function (response){
                $scope.roleIndex = response.data;
                if($scope.roleIndex === 2) {
                    //if user is STUDENT + ADMIN give his opportunity to change between student mode and admin mode
                    //concretly here show button to go to admin mode
                    //(and from admin/index "student mode" button is shown)
                    adminModeButton.style.display = "block";
                }
            });
    }

    $scope.addNewDeck = function() {
        document.getElementById('click').click();
        $http.post(contextPath + '/decks/' + $scope.newDeck.name + '/' + $scope.newDeck.description);
        //update decks to show changes
        window.location.reload();
    }

    //go to detailed view  of a deck with this id when corresponding button is clicked
    $scope.detailedView = function (deckId) {
        $http.get(contextPath + '/isDeckBelongsToUser/' + deckId)
            .then(function (response){
                if (response.data){
                    window.location.href = "http://localhost:8080/student/detailedViewOfADeck.html?id="+deckId + "&public=false";
                }else{
                    window.location.href = "http://localhost:8080/student/detailedViewOfADeck.html?id="+deckId + "&public=true";
                }
            });
    }

    //study deck with this id when corresponding button is clicked
    $scope.studyMode = function (deckId) {
        window.location.href = "http://localhost:8080/student/studyMode.html?id="+deckId;
    }

    //when page is load
    $scope.checkRoles(); //check if its student+admin to show corresponding functionality
    $scope.loadDecks(); //show users decks in table
    //to enamble closing add new deck form by clicking "close button"
    $scope.closeDropdown = function() {
        $('#collapseExample').collapse('hide');
    }
    //


});