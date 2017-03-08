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
             this.$http.get(url+'/view/taxStamp/'+this.data.attributes.taxStamp).then((response) => {
                this.taxStamp=response.body;
                }, (response) => {
                alert('Error');
                });
             }
         if (this.data.attributes.makerSign == undefined || this.data.attributes.makerSign=='') {
             this.makerSign= {};
             } 
          else {
             this.$http.get(url+'/view/makerSign/'+this.data.attributes.makerSign).then((response) => {
                this.makerSign=response.body;
                }, (response) => {
                alert('Error');
                });
             }
         if (this.data.attributes.pattern == undefined || this.data.attributes.pattern=='') {
             this.makerSign= {};
             } 
          else {
             this.$http.get(url+'/view/pattern/'+this.data.attributes.pattern).then((response) => {
                this.pattern=response.body;
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
