var answeredQuestions = [];
var url = 'http://localhost:4567';
var app = new Vue({
    el: '#app',
    mounted: function() {
        CardEvents.getCards.on(this.getCards);
        CardEvents.answerQuestion.on(this.answerQuestion);
        this.getQuestions();
    },
    methods: {
        answerQuestion: function(updatedQuestions) {
            answeredQuestions = updatedQuestions;
            this.getCards();
        },
        buildQueryParamsByAnsweredQuestions: function() {
            var queryParams = '';
            answeredQuestions.forEach(function(answer, index) {
                if (answer.value != '') {
                    if (queryParams) {
                        queryParams += '&';
                    } else {
                        queryParams = '?';
                    }
                    queryParams += answer.paramName + "=" + answer.value;
                }
            });
            return queryParams;
        },
        listKeys: function(obj) {
            var keys = [];
            for (var key in obj) {
                keys.push(key);
            }
            return keys;
        },
        getQuestions: function() {
            this.$http.get(url + '/search/deck').then((response) => {
                CardEvents.questionsLoaded.send(response.body);
                CardEvents.cardsLoaded.send(response.body);
            }, (response) => {
                alert('Error');
            });
        },
        getCards: function() {
            var queryParams = this.buildQueryParamsByAnsweredQuestions();
            this.$http.get(url + '/search/deck' + queryParams).then((response) => {
                CardEvents.cardsLoaded.send(response.body);
            }, (response) => {
                // error callback
            });
        }
    }
})