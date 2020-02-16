/**
 * Performs a GET request with given path.
 * @param path
 * @param consumer is called with parsed response object
 * @param errorhandler optional, is called in case of error with error object, by default error is logged
 */
function getJson(path, consumer, errorhandler) {
	fetch(path)
		.then(response => response.json()).then(consumer)
		.catch(errorhandler === undefined ? err => console.log(err) : errorhandler);
}

/**
 * Performs a POST request with given path.
 * @param path
 * @param body must accept JSON content
 * @param consumer is called with parsed response object
 * @param errorhandler optional, is called in case of error with error object, by default error is logged
 */
function postObject(path, data, consumer, errorhandler) {
	fetch(path,
		{
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json'
			},
			method: "POST",
			body: JSON.stringify(data)
		})
		.then(response => response.json()).then(consumer)
		.catch(errorhandler === undefined ? err => console.log(err) : errorhandler);
}
