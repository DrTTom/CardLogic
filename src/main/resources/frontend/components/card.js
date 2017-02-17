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
        default: 'highDetails'
    }, 
    showfully: {
        type: Boolean,
        required: false,
        default: false
    }
    },
    methods: {
      showFully: function(response){
        this.showfully=true;
      }, 
      hideOverlay: function(response){
        this.showfully=false;
      }
    }
})
