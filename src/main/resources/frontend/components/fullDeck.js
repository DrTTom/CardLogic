Vue.component('deck_full', {
   template: '#deck_fullTemplate',
   mounted: function() {
      CardEvents.showDeck.on(this.updateDeckView);
      },
   props: {
      data: {
         type: Object,
         required: false,
         default: {}
         },
      visible: {
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
      pattern: {
         type: Object,
         required: false,
         default: {}
         },
      },
   methods: {
      updateDeckView: function(payload) {
         this.data= payload;
         this.visible=true;
         this.getAuxObject('maker', this.data.attributes.maker);
      	 this.getAuxObject('makerSign', this.data.attributes.makerSign);
         this.getAuxObject('pattern', this.data.attributes.pattern);
         this.getAuxObject('taxStamp', this.data.attributes.taxStamp);
         },
         getAuxObject: function(key, value) {
       	  if (typeof value == "undefined" || value=='') {
       		this[key] ={}
                 } 
              else {
            	  var that=this;
                 this.$http.get(url+'/view/'+key+'/'+value).then((response) => {
                	 that[key] = response.body;
                	 console.log("should have got value "+ key);
                	 console.log(that[key]);
                    }, (response) => {
                    alert('Error');
                    });
                 }
         },

      hide: function(response) {
         this.visible=false;
         } 
      }
   })
