var url = 'http://localhost:4567';
var currentType = 'deck';
var searchMode = 'search';

var app = new Vue({
    el: '#app',
    mounted: function() {
        CollectionEvents.answersChanged.on(this.executeSearch);
        this.executeSearch([]);
    },
    methods: {
        executeSearch: function(questions) {
            var query = url + '/' + searchMode + '/' + currentType + this.extractQueryParams(questions);
            this.$http.get(query).then((response) => {
                CollectionEvents.searchUpdated.send(response.body);
            }, (response) => {
                alert("Problem communicating with " + url + "\nResponse is:\n" + response.body);
            });
        },
        extractQueryParams: function(questions) {
            var queryParams = '';
            questions.forEach(function(answer, index) {
                if (answer.value != '') {
                    if (queryParams) {
                        queryParams += '&';
                    } else {
                        queryParams = '?';
                    }
                    queryParams += answer.paramName + "=" + answer.value;
                }
            });
            return queryParams;
        },
        listKeys: function(obj) {
            var keys = [];
            for (var key in obj) {
                keys.push(key);
            }
            return keys;
        }
    }
})
