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
        }
    },
    methods: {
      updateCards: function(response){
        this.allCards = response.matches;
        alert('got cards '+ response.matches.length)
      }
    }
})
