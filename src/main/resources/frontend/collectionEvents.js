var CollectionEvents = (function() {

    var bus = new Vue(); // own Vue to enable access from all components

    var createEvent = function(name) {
        return {
            send: function(payload) {
                console.log("Event: "+name+"(" +typeof payload+")");
                bus.$emit(name, typeof payload == 'undefined' ? {} : payload)
            },
            on: function(callback) {
                bus.$on(name, callback);
            }
        }
    }

    return {
        // user input or reset of all answers: data is array of (potentially answered) questions 
        answersChanged: createEvent('answersChanged'),
        // search was executed: data is search result containing fount objects and updated questions
        searchUpdated: createEvent('searchUpdated'),

        // old events, refactor!
        showDeck: createEvent('showDeck')
    };

})();