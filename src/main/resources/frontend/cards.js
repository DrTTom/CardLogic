var answeredQuestions=[];
var url='http://localhost:4567';
var app = new Vue({
    el: '#app',
    mounted: function() {
    	CardEvents.getCards.on(this.getCards);
    	CardEvents.answerQuestion.on(this.answerQuestion);
        CardEvents.removeAnsweredQuestion.on(this.removeAnsweredQuestion);
        this.getQuestions();
    },
    methods: {
        removeAnsweredQuestion: function(question){
            answeredQuestions.splice(answeredQuestions.indexOf(question),1);
            this.getCards();
        },
    	answerQuestion: function(answeredQuestion){
    		answeredQuestions.push(answeredQuestion);
    		this.getCards();
    	},
    	buildQueryParamsByAnsweredQuestions: function(){
    		var queryParams='';
    		answeredQuestions.forEach(function(answer,index){
    			if(index==0){
    				queryParams='?';
    			}
    			queryParams+=answer.attribute+"="+answer.answer;
    			if(index!=this.answeredQuestions.length-1){
    				queryParams+='&';
    			}
    		});
    		return queryParams;
    	},
        getQuestions: function(){
            this.$http.get(url+'/search/deck').then((response) => {
                CardEvents.questionsLoaded.send(response.body);
                CardEvents.cardsLoaded.send(response.body);
            }, (response) => {
                alert('Error');
            });
        },
        getCards: function() {     
        	alert('getCards called');
        	var queryParams = this.buildQueryParamsByAnsweredQuestions();   	
            this.$http.get(url+'/search/deck'+queryParams).then((response) => {
            	alert('should have asked server');
            	CardEvents.cardsLoaded.send(response.body);
            }, (response) => {
                // error callback
            });
        }
    }
})
