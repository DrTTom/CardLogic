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
        detaillevel: {
        type: String,
        required: false,
        default: 'lowDetail'
        }
    },
    methods: {
      updateCards: function(response){
        this.allCards = response.matches;
        this.numberMatching = response.numberMatching;
        this.numberPossible = response.numberPossible;
        if (this.numberPossible<24)
        {
         this.detaillevel='highDetail';
         } else {
         this.detallevel='lowDetail';
         }
      }
    }
})
