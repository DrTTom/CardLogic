Vue.component('imagechoice', {
    template: '<span><input type="button" v-on:click="openChoice" v-bind:value="question.value"/>'
              + '<img v-if="currentImage!=null" v-bind:src="currentImage" width="auto" v-bind:height="40"/>'
               +'</span>',
    mounted: function() {
        CollectionEvents.valueChanged.on(this.update);
        this.currentImage=this.question.value;
    },
    props: {
        question: {
            type: Object,
            default: {}
        },
        currentImage: {
        	type: String,
        	default: null
        }
    },
    methods: {
        openChoice: function() {
            CollectionEvents.imageChoiceOpened.send(this.question);
        },
        update: function(entry) {
            if (this.question.paramName == entry.key) {
                this.currentImage=url + "/download/"+this.question.urls[this.question.value];
            }
        }
    }
})
