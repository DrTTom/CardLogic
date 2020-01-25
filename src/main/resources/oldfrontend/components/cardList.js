Vue.component('cardlist', {
    template: '#cardListTemplate',
    mounted: function() {
        CollectionEvents.searchUpdated.on(this.updateCards);
    },
    props: {
        allCards: {
            type: Array,
            default: []
        },
        numberMatching: {
            type: Number,
        },
        numberPossible: {
            type: Number,
        },
        itemClass: {
            type: String,
            default: 'smallTile'
        }
    },
    methods: {
        updateCards: function(response) {
            this.numberMatching = response.numberMatching;
            this.numberPossible = response.numberPossible;
            if (this.numberPossible < 24) {
                this.itemClass = 'bigTile';
            } else {
                this.itemClass = 'smallTile';
            }
            this.allCards = response.matches;
        }
    }
})