Vue.component('card', {
    template: '#cardTemplate',
    mounted: function() {
        
    },
    props: {
        data: {
            type: Object,
            required: true,
            default: {}
        }
    },
    methods: {
      updateCards: function(response){
        this.allCards = response.data;
      }
    }
})
