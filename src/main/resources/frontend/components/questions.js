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
        questionGroups: {
            type:Array,
            required:false,
            default:[]
        },
        questionGroup: {
            type:Array,
            required:false,
            default:[]
        },
        currentQuestion: {
            type: Object,
            required: false,
            default: {}
        },
        actorUser: {
         type: Boolean,
         required: false,
         default: false
         }
    },
    methods: {
        updateQuestions: function (response) {
            this.allQuestions= response.questions;
            this.questionGroup=[];
            this.questionGroups=[];
            var currentGroup= { name:'', elements:[] };
            for (i=0; i< this.allQuestions.length; i++)
            {
            	if (this.allQuestions[i].form!=currentGroup.name)
            	{
             	currentGroup= { name:this.allQuestions[i].form, elements:[] };
            	this.questionGroups.push(currentGroup);
            	 console.log("pushed group");
             	}
             currentGroup.elements.push(this.allQuestions[i]);	
             }
            var that=this;
            this.allQuestions.forEach(function(q){
            	 if (q.form=='identify')
            		 {
            	that.questionGroup.push(q);
            		 }
            });
        },
        answerQuestion: function (event) {
            if (actorUser==true)
            {
            actorUser=false;        
        	if (event.which == 13 || event.keyCode == 13 || event.type == 'change') {
        	console.log("will re-send answers");
                CardEvents.answerQuestion.send(this.allQuestions);
            }
            }
           // if (event.which == 0 || event.keyCode == 9)  for tab
        },
       enableUpdate: function(event)
       {
          actorUser=true;
          console.log("User interaction detected");
       },
       example: function(event) 
       {
    	   var dummy={"x": "hallo", "y": "tt"};
    	   this.$http.post(url+'/submit', dummy).then( (response) => {
          	 console.log("should have sent");
            } );
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
