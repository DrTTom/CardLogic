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
      },
   methods: {
      updateDeckView: function(payload) {
         this.data= payload;
         this.visible=true;
         if (this.data.attributes.maker == undefined || this.data.attributes.maker=='') {
             this.maker= {};
             } 
          else {
             this.$http.get(url+'/view/maker/'+this.data.attributes.maker).then((response) => {
                this.maker=response.body;
                }, (response) => {
                alert('Error');
                });
             }         
         if (this.data.attributes.taxStamp == undefined || this.data.attributes.taxStamp=='') {
             this.taxStamp= {};
             } 
          else {
        	  console.log("will look at tax stamp " +url+'/view/taxStamp/'+this.data.attributes.taxStamp);
             this.$http.get(url+'/view/taxStamp/'+this.data.attributes.taxStamp).then((response) => {
            	console.log("got" +response.body);
                this.taxStamp=response.body;
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
