Vue.component('imageupload', {
    template: '<div v-on:drop="dropZoneDrop" v-on:dragover="dropZoneOver" v-on:click="upload">' +
        '<input type="file" style="display:none" v-on:change="echo"/> Drop image or click here' +
        '<img v-bind:src="currentImage"/></div>',
    mounted: function() {
    	if (this.question.value!= "")
    		{
        this.currentImage = url + '/view/'+this.question.value;
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
        echo: function(e) {
            console.log("echo");
        },
        upload: function(e) {
            console.log(e.target);
            e.target.children[0].click();
        },
        dropZoneOver: function(e) {
        	// if this method is not defined, the drop event will never be obtained.
            if (e.preventDefault) e.preventDefault();
            if (e.stopPropagation) e.stopPropagation();
            e.dataTransfer.dropEffect = "copy";
        },
        dropZoneDrop: function(e) {
            if (e.preventDefault) e.preventDefault();
            if (e.stopPropagation) e.stopPropagation();
            var fileList = e.dataTransfer.files;
            if (fileList.length > 0) {
                var data = new FormData();
                data.append('file', fileList[0]);
                var extension = fileList[0].name;
                extension = extension.substr(extension.lastIndexOf(".")+1);
                var ref = this.question.proposedRef+extension;
                var that = this;
                this.$http.post(url + '/upload/' + ref, data).then((response) => {
                	that.question.value=ref;
                	CollectionEvents.valueChanged.send({
                        key: that.question.paramName,
                        value: ref
                    });
                }, (error) => {
                    console.log(error)
                })
            }
        }
    }
})