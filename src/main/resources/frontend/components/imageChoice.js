Vue.component('imagechoice', {
    template: '<span><input type="button" v-on:click="openChoice" v-bind:value="question.value"/></span>',
    mounted: function() {
        this.populate();
        CollectionEvents.valueChanged.on(this.update);
    },
    props: {
        question: {
            type: Object,
            default: {}
        },
        data: {
            type: Object,
            default: {
                values: [],
                key: "",
                selected: ""
            }
        },
    },
    methods: {
        populate: function() {
            this.data = {
                values: []
            };
            var that = this;
            Object.keys(this.question.urls).forEach(function add(key) {
                that.data.values.push({
                    name: key,
                    image: url + "/download/" + that.question.urls[key]
                })
            });
            this.data.key = this.question.paramName;
            this.data.selected = this.question.value;
        },
        openChoice: function() {
            CollectionEvents.imageChoiceOpened.send(this.data);
        },
        update: function(entry) {
            if (this.question.key == entry.key) {
                this.question.value = entry.value;
                console.log(entry.value);
            }
        }
    }
})
