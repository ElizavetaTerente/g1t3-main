angular.module('detailedView',[]).controller('dvc',function ($scope,$http) {

    const contextPath = 'http://localhost:8080';

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id')

    //sorting in tables

    //sorted by parameter pr . Initially pr = '' (empty) ,so we see values in the table how they are
    //if the "^" button is clicked then corresponding sort function run (because we can sort several columns)
    //and the sorting parameter in html is set. Button change from "^" to "⌄". and corresponding to column
    //boolean variables is changing its value (to track which sort we need to do next : in desc order or asc)
    $scope.pr = '';
    let question = false;
    let solution = false;

    $scope.sortQuestion = function() {
        if(question){
            $scope.pr = '-question';
            document.getElementById("sort_question").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'question';
            document.getElementById("sort_question").innerHTML = '^';
        }
        question = !question;
    }

    $scope.sortSolution = function() {
        if(solution){
            $scope.pr = '-solution';
            document.getElementById("sort_solution").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'solution';
            document.getElementById("sort_solution").innerHTML = '^';
        }
        solution = !solution;
    }
    //

    $scope.loadDeckDetails = function () {
        $http.get(contextPath + '/checkIfReverted/' + id)
            .then(function (response){
                $scope.reverted = response.data;
                if($scope.reverted){
                    //if deck is reverted(in inverse mode) i request back end to send me reverted cards
                    $http.get(contextPath + '/student/RevertedCardsOfADeck/' + id)
                        .then(function (response) {
                            $scope.cardsInDeck = response.data;
                        });

                }else{ //if no, i request usual cards
                    $http.get(contextPath + '/student/cardsOfADeck/' + id)
                        .then(function (response) {
                            $scope.cardsInDeck = response.data;
                        });
                }
            });

        //request to recieve deck name and description to show it to user
        $http.get(contextPath + '/student/deckDetails/' + id)
            .then(function (response){
                $scope.deck = response.data;
            });
    }


    $scope.publishDeck = function(id) {
        //check if deck is already public
        //in this case show corresponding message
        $http.get(contextPath + '/isDeckPublic/' + id)
            .then(function (response){
                if(response.data) {
                    alert("Deck is already published");
                    return;
                }else{ //in case no - publish deck and show corresponding message
                    $http.post(contextPath + '/student/publishDeck/' + id);
                            alert("Deck is published:)");
                }
            });
    }

    $scope.deleteDeck = function(id) {
        $http.delete(contextPath + '/student/deleteDeck/' + id);
        alert("Deck is deleted!");
        //after deleting deck redirection to student main page (because admin has no deck -> cannot delete)
        window.location.href = "http://localhost:8080/student/index.html";
    }

    $scope.addNewCard = function(deckId){
        //close form after submit
        document.getElementById('click').click();
        //post request with new card data to add it
        $http.post(contextPath + '/addNewCard/' + deckId + '/' + $scope.newCard.question +'/'+ $scope.newCard.solution);
        //reload info to see changes
        $scope.loadDeckDetails();
        //to enable close button inside a form (add new card form)
        $('#collapseExample').show();
    }

    $scope.deleteCardFromDeck = function(cardId){
        $http.delete(contextPath + '/deleteCardFromDeck/' + cardId );
        //reload info to see changes
        $scope.loadDeckDetails();
        window.location.reload();
    }

    //next 2 functions is edit-save deck process
    $scope.edit_deck = function(){
        //make all data editable
        document.getElementById("deck_name").removeAttribute("style");
        document.getElementById("deck_name").removeAttribute("readonly");

        document.getElementById("deck_description").removeAttribute("style");
        document.getElementById("deck_description").removeAttribute("readonly");
        //hide "edit" button and show "save" button
        document.getElementById("edit_button").style.display= "none";
        document.getElementById("save_button").style.display = "block";

    }

    $scope.save_changes = function (deckId){
        //make all data uneditable again
        document.getElementById("deck_name").setAttribute("style","border:none;background: transparent");
        document.getElementById("deck_name").setAttribute("readonly","true");

        document.getElementById("deck_description").setAttribute("style","border:none;background: transparent");
        document.getElementById("deck_description").setAttribute("readonly","true");

        document.getElementById("edit_button").style.display= "inline-block";
        document.getElementById("save_button").style.display = "none";
        //taking new values
        let deckName = document.getElementById("deck_name").value;
        let deckDesc = document.getElementById("deck_description").value;
        //send post request with new data
        $http.post(contextPath + '/editDeck/' + deckId +'/'+ deckName +'/'+ deckDesc)

    }
    //next 2 functions is edit-save row process (of a card table)
    $scope.edit_card = function(index){
        //take all the data from user_table with tag "input" in html
        let inputs = document.getElementById('cards_table').rows[index].getElementsByTagName('input');
        inputs[0].setAttribute("id","card_question_" + index);
        inputs[1].setAttribute("id","card_solution_" + index);

        //to identify row which user wants to edit to every data(input type in this case because editable)
        //i add index with column name and index of the row which i recieved from html function call

        document.getElementById("card_question_" + index).removeAttribute("style");
        document.getElementById("card_question_" + index).removeAttribute("readonly");

        document.getElementById("card_solution_" + index).removeAttribute("style");
        document.getElementById("card_solution_" + index).removeAttribute("readonly");
        //load all the buttons from this row in array
        let buttons = document.getElementById('cards_table').rows[index].getElementsByTagName('button');
        //set id to buttons in this row to identify them
        buttons[0].setAttribute("id","save_card_button_" + index);
        buttons[1].setAttribute("id","edit_card_button_" + index);
        //hide "edit" button and show "save" button
        document.getElementById("edit_card_button_" + index).style.display= "none";
        document.getElementById("save_card_button_" + index).style.display = "block";

    }
    //saving new values of a row
    $scope.save_edited_card = function (cardId,index){
        //making all data not editable again
        document.getElementById("card_question_" + index).setAttribute("style","border:none;background: transparent");
        document.getElementById("card_question_" + index).setAttribute("readonly","true");

        document.getElementById("card_solution_" + index).setAttribute("style","border:none;background: transparent");
        document.getElementById("card_solution_" + index).setAttribute("readonly","true");

        document.getElementById("edit_card_button_" + index).style.display= "inline-block";
        document.getElementById("save_card_button_" + index).style.display = "none";
        //saving new values of a row
        let question = document.getElementById("card_question_" + index).value;
        let solution = document.getElementById("card_solution_" + index).value;
        //pass edited info to backend
        $http.post(contextPath + '/editCard/' + id +'/' + cardId +'/'+ question +'/'+ solution);
    }

    //function to revert deck
    $scope.inverse = function(deckId){
        $http.post(contextPath + '/inverseDeck/' + deckId);
        //reload data to see changes
        window.location.reload();
    }


    //function to check if deck is reverted
    $scope.checkIfReverted = function(){
        $http.get(contextPath + '/checkIfReverted/' + id)
            .then(function (response){
                $scope.reverted = response.data;
                return $scope.reverted;
            });
    }

    //function to check if url parameter "public" (true or false) matching reality
    //(because user can edit it in url and recieve functionality he don't have to)
    //if its public person cannot edit it
    const isPublic = urlParams.get('public')
    $scope.checkPermissions = function () {
        $http.get(contextPath + '/isDeckPublic/' + id)
            .then(function (response) {
                if (response.data && isPublic === "false") {//if deck is public but in url is "false"
                    $http.get(contextPath + '/isDeckBelongsToUser/' + id) //AND it its not user's deck
                        .then(function (response){
                            if(!response.data) {
                                //redirect to error page
                                window.location.href = "http://localhost:8080/error/403.html";
                            }
                        });
                }
            });
    }




    //when page is load
    $scope.loadDeckDetails();//show deck data in table
    //to enamble closing add new deck form by clicking "close button"
    $scope.checkPermissions(); //for additional finctionality (if deck if public person cannot edit it)
    $scope.closeDropdown = function() {
        $('#collapseExample').collapse('hide');
    }


});