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
         //alert(this.$http.get(url+'/view/maker/'+this.data.attributes.maker).primKey);
         var checkme = this.$http.get(url+'/view/maker/'+this.data.attributes.maker);        
          this.$http.get(url+'/view/maker/'+this.data.attributes.maker).then((response) => {
          alert(response);
            }, (response) => {
                alert('Error');
            });
         },
         listKeys : function(obj){
     	   var keys = [];
     	   for(var key in obj){
     	      keys.push(key);
     	   }
     	   return keys;
     	},
      hide: function(response) {
         this.visible=false;
         } 
      }      
   })
