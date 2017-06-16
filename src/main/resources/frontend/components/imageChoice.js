Vue.component('imagechoice', {
    template: '<div v-if="visible"><div v-for="item in antivue.values" class="chooseMe">'
    	+'<img v-bind:src="item.image"/> <br>{{item.name}}'+
    	' <input type="radio" v-bind:name="question.paramName" v-bind:value="item.name"/></div></div>',
    mounted: function() {
    	   this.populate();
        },
    props: {
        question:{
        	type: Object,
        	default: {}
        },
    	antivue: {
    		type: Object,
    		default: {values: []}
    	},
        visible:{
        	type:Boolean,
        	default: true
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