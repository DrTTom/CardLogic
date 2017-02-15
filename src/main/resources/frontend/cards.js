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
    	answerQuestion: function(updatedQuestions){    		
    		answeredQuestions = updatedQuestions;
    		this.getCards();
    	},
    	buildQueryParamsByAnsweredQuestions: function(){
    		var queryParams='';
        	alert("building params for " +this.listKeys(answeredQuestions))
    		answeredQuestions.forEach(function(answer,index){
    			if (answer.value != '')
    				{
    			if(index==0){
    				queryParams='?';
    			}
    			else 
    				{
    				queryParams+='&';
    				}    			
    			queryParams+=answer.paramName+"="+answer.value;
    		}
    		});
    		alert(queryParams);
    		return queryParams;
    	},
    	listKeys : function(obj){
     	   var keys = [];
     	   for(var key in obj){
     	      keys.push(key);
     	   }
     	   return keys;
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
        	var queryParams = this.buildQueryParamsByAnsweredQuestions();   	
            this.$http.get(url+'/search/deck'+queryParams).then((response) => {
            	CardEvents.cardsLoaded.send(response.body);
            }, (response) => {
                // error callback
            });
        }
    }
})
