// half of the imageChoice component: there is no way to layout a single 
// component both within the form grid and within an overlay
Vue.component('imagechoiceoverlay', {
    template: '<div v-if="visible" class="c-overlay"><div class="o-modal"><div class="c-card">' +
        '<header class="c-card__header c-card__item--divider"><h2 class="c-heading">{{question.text}}</h2></header>' +
        '<div class="c-card__body">' +
        '<div v-for="item in imageUrls" style="display: inline-block">' +
        '<label v-bind:for="item.name"><img v-bind:src="item.image" v-bind:width="question.width" v-bind:height="question.height"/></label><br/>' +
        '<input type="radio" name="imageChoice" v-bind:value="item.name" v-bind:id="item.name" v-model="question.value"/>' +
        '<label v-bind:for="item.name">{{item.name}}</label></div><div class="newline"/>' +
        '<input type="button" value="OK" v-on:click="close" class="c-button c-button--brand"/></div>' +
        '</div></div></div>',
    mounted: function() {
        CollectionEvents.imageChoiceOpened.on(this.open)
    },
    props: {
        question: {
            type: Object,
            default: {}
        },
        imageUrls: {
            type: Array,
            default: []
        },
        visible: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        open: function(question) {
            this.question = question;
            this.imageUrls = [];
            var that = this;
            Object.keys(this.question.urls).forEach(function add(key) {
                that.imageUrls.push({
                    name: key,
                    image: url + "/download/" + that.question.urls[key]
                })
            });
            this.visible = true;
        },
        close: function() {
            this.visible = false;
            CollectionEvents.valueChanged.send({
                key: this.question.paramName,
                value: this.question.value
            });
        }
    }
})
