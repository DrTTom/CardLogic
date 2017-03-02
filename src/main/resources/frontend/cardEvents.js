var CardEvents = (function() {
    
    var bus = new Vue(); // eigene Applikation erreichbar aus allen Schichten

    var createEvent = function(name){
      return {
        send: function(payload){
          bus.$emit(name, typeof payload == 'undefined' ? {} : payload)
        },
        on: function(callback){
          bus.$on(name,callback);
        }
      }
    }

    return {
      getCards: createEvent('getCards'),
      cardsLoaded: createEvent('cardsLoaded'),
      answerQuestion: createEvent('answerQuestion'),
      removeAnsweredQuestion : createEvent('removeAnsweredQuestion'),
      questionsLoaded : createEvent('questionsLoaded'),
      showDeck : createEvent('showDeck')
    };

})();
