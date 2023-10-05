angular.module('studyMode',[]).controller('smc',function ($scope,$http) {

    const contextPath = 'http://localhost:8080';

    //get deckId parameter from url
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id')

    //load first card for study(and initialize iterator in backend)
    $scope.loadCardToStudy = function () {
        $http.get(contextPath + '/cardToStudy/' + id)
            .then(function (response){
                $scope.cardDto = response.data;
                if($scope.cardDto == null){
                    //if no cards to study print corresponding message and hide study form, interrupt process
                    document.getElementById('modeBody').style.display="none";
                    document.getElementById("end").innerHTML = "Congrats! Thats it for today:)"
                    document.getElementById('repeat_cards').innerHTML = "cards to repeat: 0";
                    document.getElementById('new_cards').innerHTML = "new cards: 0";
                    return;
                }
                //print # cards to repeat and # new cards
                document.getElementById('repeat_cards').innerHTML = "cards to repeat: " + $scope.cardDto.cardsToRepeat;
                document.getElementById('new_cards').innerHTML = "new cards: " + $scope.cardDto.newCards;
                //put question and solution of a card to "study card"
                document.getElementById('question-text').innerHTML = $scope.cardDto.question;
                document.getElementById('solution-text').innerHTML = $scope.cardDto.solution;
            });
    }

    //send evaluation of  a card to back end and recieve next one
    $scope.rateCardAndNextCard = function(score) {
        //flip card to question
        document.getElementById('click').click();
        $http.post(contextPath + '/rateCard/' + id + '/' + $scope.cardDto.cardId + '/' + score)
            .then(function (response){
                $scope.cardDto = response.data;
                if($scope.cardDto == null){
                    //if no cards to study print corresponding message and hide study form, interrupt process
                    document.getElementById('modeBody').style.display="none";
                    document.getElementById("end").innerHTML = "Congrats! Thats it for today:)"
                    document.getElementById('repeat_cards').innerHTML = "cards to repeat: 0";
                    document.getElementById('new_cards').innerHTML = "new cards: 0";
                    return;
                }
                //print # cards to repeat and # new cards
                document.getElementById('repeat_cards').innerHTML = "cards to repeat: " + $scope.cardDto.cardsToRepeat; //!
                document.getElementById('new_cards').innerHTML = "new cards: " + $scope.cardDto.newCards; //!
                //put question and solution of a card to "study card"
                document.getElementById('question-text').innerHTML = $scope.cardDto.question;
                document.getElementById('solution-text').innerHTML = $scope.cardDto.solution;
            });
    }

    //when page is load
    $scope.loadCardToStudy(); //load public decks to show in table

});