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
        groupVisible: {
            type:Array,
            required:false,
            default:[ true, false, false, false, false, false]
        },
        questionGroups: {
            type:Array,
            required:false,
            default:[]
        },
        actorUser: {
            type: Boolean,
            required: false,
            default: false
            },
    },
    methods: {
        updateQuestions: function (response) {
            this.allQuestions= response.questions;
            this.questionGroups=[];
            var currentGroup= { name:'', elements:[] };
            for (i=0; i< this.allQuestions.length; i++) {
               if (this.allQuestions[i].form!=currentGroup.name) {
                  currentGroup= { name:this.allQuestions[i].form, elements:[] };
                  this.questionGroups.push(currentGroup);
                  }
               currentGroup.elements.push(this.allQuestions[i]);	
               }
            },
        answerQuestion: function (event) {
            if (actorUser==true) {
               actorUser=false;               
               if (event.which == 13 || event.keyCode == 13 || event.type == 'change') {
                  CardEvents.answerQuestion.send(this.allQuestions);
                  }
               }
            // if (event.which == 0 || event.keyCode == 9)  for tab
            },
        enableUpdate: function(event) {
           actorUser=true;
           }, 
        next: function(index) {
        	this.groupVisible[index]=false;
        	this.groupVisible[index+1]=true;
        	this.groupVisible.push(true); // total nonsense but must make Vue notice the change
        	this.groupVisible.pop();
              },
       store: function(event) {
          var newDeck= {"type": "deck", attributes: {}};
          for (i=0; i< this.allQuestions.length; i++)
          {
          newDeck.attributes[this.allQuestions[i].paramName]= this.allQuestions[i].value; 
          }          
          this.$http.post(url+'/submit', newDeck).then( (response) => {
             console.log("should have sent");
             });
          }
    }
})
