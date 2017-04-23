Vue.component('deck_tile', {
    template: '#deck_tileTemplate',
    props: {
        data: {
            type: Object,
            required: true,
        },
        size: {
            type: String,
            default: 'bigDetail'
        }
    },
    methods: {
        showFullDetails: function(response) {
            CardEvents.showDeck.send(this.data);
        }
    }
})
