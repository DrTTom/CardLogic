Vue.component('card', {
    template: '#cardTemplate',
    mounted: function() {
        
    },
    props: {
    	data: {
            type: Object,
            required: true,
            default: {}
        },
        detaillevel: {
        type: String,
        required: false,
        default: 'high'
    }
    },
    methods: {
      updateCards: function(response){
        this.allCards = response.data;
      }
    }
})
