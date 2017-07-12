Vue.component('imageupload', {
    template: '<div v-on:drop="uploadByDrop" v-on:dragover="dropZoneOver" v-on:click="inputFile">' +
        '<input type="file" style="display:none" v-on:change="uploadByInput"/> Drop image or click here' +
        '<img v-if="currentImage" v-bind:src="currentImage"/></div>',
    mounted: function() {
    	console.log("mounted for question with value "+this.question.value);
    	if (this.question.value!= "")
    		{
        this.currentImage = url + '/download/'+this.question.value;
     }
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
        uploadByInput: function(e) {
        	var file = e.target.files[0];
        	this.doUpload(file);
        },
        doUpload: function(file)
        {
        	var data = new FormData();
            data.append('file', file);
            var extension = file.name;
            extension = extension.substr(extension.lastIndexOf(".")+1);
            var ref = this.question.proposedRef+extension;
            var that = this;
            this.$http.post(url + '/upload/' + ref, data).then((response) => {
            	that.question.value=ref;
            	this.currentImage= url + '/download/'+this.question.value;
            	CollectionEvents.valueChanged.send({
                    key: that.question.paramName,
                    value: ref
                });
            }, (error) => {
                console.log(error)
            });
        },
        inputFile: function(e) {
        	console.log(e.target.children.length);
            e.target.children[0].click();
        },
        dropZoneOver: function(e) {
        	// if this method is not defined, the drop event will never be obtained.
            if (e.preventDefault) e.preventDefault();
            if (e.stopPropagation) e.stopPropagation();
            e.dataTransfer.dropEffect = "copy";
        },
        uploadByDrop: function(e) {
            if (e.preventDefault) e.preventDefault();
            if (e.stopPropagation) e.stopPropagation();
            var fileList = e.dataTransfer.files;
            if (fileList.length > 0) {
            	this.doUpload(fileList[0]);
            }
        }
    }
})