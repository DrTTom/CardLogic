Vue.component('questions', {
    template: '#questionsTemplate',
    mounted: function() {
        CardEvents.cardsLoaded.on(this.updateQuestions);
        CardEvents.questionsLoaded.on(this.updateQuestions);
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
        answerQuestion: function(event) {
            if (actorUser == true) {
                actorUser = false;
                if (event.which == 13 || event.keyCode == 13 || event.type == 'change') {
                    CardEvents.answerQuestion.send(this.allQuestions);
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
        store: function(event) {
            var newDeck = {
                "type": "deck",
                attributes: {}
            };
            for (i = 0; i < this.allQuestions.length; i++) {
                newDeck.attributes[this.allQuestions[i].paramName] = this.allQuestions[i].value;
            }
            this.$http.post(url + '/submit', newDeck).then((response) => {
                var result = response.body;
                console.log(result.done);
                console.log(result.questions[0].problem);

                if (!result.done) {
                    CardEvents.questionsLoaded.send(response.body);
                }
            });
        }
    }
})
