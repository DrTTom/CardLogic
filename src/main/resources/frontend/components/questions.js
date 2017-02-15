Vue.component('questions', {
    template: '#questionsTemplate',
    mounted: function () {
        CardEvents.cardsLoaded.on(this.updateQuestions);
        CardEvents.questionsLoaded.on(this.updateQuestions);
    },
    props: {
        allQuestions: {
            type:Array,
            required:false,
            default:[]
        },
        questionGroup: {
            type:Array,
            required:false,
            default:[]
        },
        questionindex: {
            type: String,
            required: true,
            default:0
        },
        answer: {
            type: String,
            required: false,
            default: ''
        },
        currentQuestion: {
            type: Object,
            required: false,
            default: {}
        }

    },
    methods: {
        updateQuestions: function (response) {
            this.allQuestions= response.questions;
            this.questionGroup=[];
            var that=this;
            this.allQuestions.forEach(function(q){
            	 if (q.form=='identify')
            		 {
            	that.questionGroup.push(q);
            		 }
            });
            console.log(this.allQuestions.length)
            this.answer = '';
            console.log("========== " + this.questionindex)
            // this.currentQuestion = response.questions.length > 0 ? response.questions[this.questionindex] : '';
        },
        answerQuestion: function (event) {
        	if (event.which == 13 || event.keyCode == 13 || event.type == 'change') {
                //this.currentQuestion.answer = this.answer;
                CardEvents.answerQuestion.send(this.allQuestions);
            }
           // if (event.which == 0 || event.keyCode == 9)  for tab
        },
       getKeys : function(obj){
        	   var keys = [];
        	   for(var key in obj){
        	      keys.push(key);
        	   }
        	   return keys;
        	}
    }
})
