Vue.component('imagechoice', {
    template: '<div><ul><li v-for="text in antivue.values"><img v-bind:src="text.image"/><input type="radio" v-bind:name="name"/></li></ul></div>',
    mounted: function() {
    	   this.populate();
        },
    props: {
    	name: {
    		type: String,
    		default: "changeMe"
    	},
        question:{
        	type: Object,
        	default: {}
        },
    	antivue: {
    		type: Object,
    		default: {values: []}
    	}
    	    },
    methods: {
    	populate: function()
    	{
    		this.antivue={values: []};
    		var that=this;
    		Object.keys(this.question.urls).forEach( function add(key)
    		{
    			that.antivue.values.push( { name: key, image: "http://localhost:4567/download/"+that.question.urls[key], radio:false})
    		});		
    	}
    }
})