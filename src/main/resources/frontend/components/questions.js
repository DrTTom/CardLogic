Vue.component('questions', {
    template: '#questionsTemplate',
    mounted: function() {
    	CollectionEvents.searchUpdated.on(this.updateQuestions);
    	CollectionEvents.valueChanged.on(this.valueChanged);
    },
    props: {
        allQuestions: {
            type: Array,
            default: []
        },
        groupVisible: {
            type: Array,
            default: [true, false, false, false, false, false]
        },
        questionGroups: {
            type: Array,
            default: []
        },
        actorUser: {
            type: Boolean,
            default: false
        },
        nextAction: 
        	{
        	type: String, 
        	default: "check"
        	},
    },
    methods: {
        updateQuestions: function(response) {
            this.allQuestions = response.questions;
            this.questionGroups = [];
            var currentGroup = {
                name: '',
                elements: []
            };
            for (i = 0; i < this.allQuestions.length; i++) {
                if (this.allQuestions[i].form != currentGroup.name) {
                    currentGroup = {
                        name: this.allQuestions[i].form,
                        elements: []
                    };
                    this.questionGroups.push(currentGroup);
                }
                currentGroup.elements.push(this.allQuestions[i]);
            }
        },
        valueChanged: function(event)
        {
           for (i = 0; i < this.allQuestions.length; i++) {
        	   console.log(this.allQuestions[i].paramName + "--> "+event.key)
        	   if (this.allQuestions[i].paramName==event.key)
        		   {
        		   this.allQuestions[i].value=event.value;
        		   CollectionEvents.answersChanged.send(this.allQuestions);
        		   return;
        		   }
           }
        },
        answerQuestion: function(event) {
            if (actorUser == true) {
                actorUser = false;
                if (event.which == 13 || event.keyCode == 13 || event.type == 'change') {
                	 CollectionEvents.answersChanged.send(this.allQuestions);
                }
            }
        },
        enableUpdate: function(event) {
            actorUser = true;
        },
        next: function(index) {
            this.groupVisible[index] = false;
            this.groupVisible[index + 1] = true;
            this.groupVisible.push(true); // total nonsense but must make Vue notice the change
            this.groupVisible.pop();
        },
        check: function(event) 
        {
            this.nextAction="submit";
            searchMode="check";
            CollectionEvents.answersChanged.send(this.allQuestions);
        },
        uploadFile: function(event)
        {
        	console.log(event.target.files);
        	var data = new FormData();
        	 data.set('file', event.target.files[0]);
        	 this.$http.post('/upload/image', data).then((response) => {
        	  // result
        	 }, (error) => { console.log (error)})
        },
        
        store: function(event) {
            var newDeck = {
                "type": "deck",
                attributes: {}
            };
            for (i = 0; i < this.allQuestions.length; i++) {
                newDeck.attributes[this.allQuestions[i].paramName] = this.allQuestions[i].value;
            }
            alert("if ready, add POST request here!");
//            this.$http.post(url + '/submit', newDeck).then((response) => {
//                var result = response.body;
//                console.log(result.done);
//                console.log(result.questions[0].problem);
//
//                if (!result.done) {
//                    CollectionEvents.questionsLoaded.send(response.body);
//                }
//            });
        }
    }
})
