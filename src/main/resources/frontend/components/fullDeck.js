Vue.component('full_deck', {
    template: '#full_deckTemplate',
    mounted: function() {
    	CardEvents.showDeck.on(this.updateDeckView);
    },
    props: {
    	data: {
            type: Object,
            required: false,
            default: {}
        },
        showfully: {
            type: Boolean,
            required: false,
            default: false
        },
    	maker: {
            type: Object,
            required: false,
            default: {}
        },
    	makerSign: {
            type: Object,
            required: false,
            default: {}
        },
    	taxStamp: {
            type: Object,
            required: false,
            default: {}
        },
    },
    methods: {
    	updateDeckView: function(payload)
    	{
    		this.data= payload;
    		this.showfully=true;
    	},
    hideOverlay: function(response){
        this.showfully=false;
      }
    }
})
