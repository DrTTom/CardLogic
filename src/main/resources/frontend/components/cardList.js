Vue.component('cardlist', {
    template: '#cardListTemplate',
    mounted: function() {
        CardEvents.cardsLoaded.on(this.updateCards);
    },
    props: {
        allCards: {
        	type: Array,
            required: false,
            default: []
        },
        numberMatching: 
    	{
    	type: Number,
        required: true
    	},    
        numberPossible: 
    	{
    	type: Number,
        required: true
    	},
        itemClass: {
        type: String,
        required: false,
        default: 'smallTile'
        }
    },
    methods: {
      updateCards: function(response){
        this.numberMatching = response.numberMatching;
        this.numberPossible = response.numberPossible;
        if (this.numberPossible<24)
        {
         this.itemClass='bigTile';
         } else {
         this.itemClass='smallTile';
         }
         this.allCards = response.matches;
      }
    }
})
