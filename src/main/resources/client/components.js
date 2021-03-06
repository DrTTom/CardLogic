let internalIdSource = 0;

/**
 * Base class for custom components, defines a unique id and calls a createContent method if necessary.
 */
class MyCustomElement extends HTMLElement {

	connectedCallback() {
		if (!this.getRefId()) {
			const refId = 'id' + internalIdSource++;
			this.setAttribute('refId', refId);
			this.createContent(refId);
		}
	}

	getRefId() {
		return this.getAttribute('refId');
	}

	createContent(refId) {
		console.log("createContent must be defined by each component")
	}
}

/**
 * A container which can be vertically collapsed. Ever seen a real accordion?
 */
class VerticalAccordion extends MyCustomElement {

	createContent(refId) {
		let title = buildChildNode(this, 'label').id(refId + '_title').class('accordion-title').get();
		let panel = buildChildNode(this, 'div').id(refId + '_content').class('accordion-panel').get();
		title.onclick = () =>
			panel.style.maxHeight = title.classList.toggle('active') ? panel.scrollHeight + "px" : null;
	}

	content() {
		return $('#' + this.getRefId() + '_content');
	}

	title() {
		return $('#' + this.getRefId() + '_title');
	}

	isExpanded() {
		return this.title().classList.contains('active');
	}

    /**
     * note that expand also corrects the height if content changed.
     */
	expand() {
		let t = this.title();
		if (!t.classList.contains('active')) t.classList.add('active');
		let c = this.content();
		c.style.maxHeight = c.scrollHeight + "px";
	}

	collapse() {
		this.title().classList.remove('active');
		this.content().style.maxHeight = null;
	}
}

/**
 * A row of tabs where the action for activating a tab can be chosen freely.
 */
class TabRow extends HTMLElement {
	connectedCallback() {
		buildChildNode(this, 'div').class('tab-row');
	}

	add(text, activate) {
		let parent = $('div', this);
		let tab = buildChildNode(parent, 'span').text(text).get();
		tab.onclick = () => {
			if (!tab.classList.contains('selected')) {
				$$('span', parent).forEach(e => e.classList.remove('selected'));
				tab.classList.add('selected');
				activate();
			}
		};
		return tab;
	}
}

/**
 * A dialog with header and body which can be closed
 */
class Dialog extends MyCustomElement {

	createContent(refId) {
		let pane = buildChildNode(this, 'div').class('modal-overlay').id(refId + '_overlay').get();
		let frame = buildChildNode(pane, 'div').class('c-frame modal-frame').get();
		let headerLine = buildChildNode(frame, 'div').class('c-header').get();
		let cancel = buildChildNode(headerLine, 'span').class('closebutton').get();
		buildChildNode(headerLine, 'div').id(refId + '_title');
		buildChildNode(frame, 'div').class('c-body').id(refId + '_body');
		cancel.innerHTML = "&times;";
		cancel.onclick = () => {
			$('#' + refId + '_overlay').style.display = 'none';
			$('#' + refId + '_body').innerHTML = '';
		};
	}

    /**
     * shows the dialog
     */
	show(title, body) {
		let refPrefix = '#' + this.getRefId();
		$(refPrefix + '_title').innerHTML = title;
		$(refPrefix + '_overlay').style.display = 'flex';
		if (typeof (body) === 'string') {
			$(refPrefix + '_body').innerHTML = body;
		} else if (typeof (body) === 'object') {
			$(refPrefix + '_body').innerHTML = '';
			$(refPrefix + '_body').appendChild(body);
		}
	}

    /**
     * hides the dialog
     */
	hide() {
		$('#' + this.getRefId() + '_overlay').style.display = 'none';
	}
}

/**
 * A basic input element. To avoid dummy methods, this is a text input.
 */
class InputElement extends MyCustomElement {
	createContent(refId) {
		let div = buildChildNode(this, 'div').class('question').get();
		buildChildNode(div, 'label').for(refId + '_input');
		div.appendChild(this.createInputElement(refId + '_input'));
		buildChildNode(div, 'label');
		buildChildNode(div, 'label').class('warning').id(refId + '_msg');
	}

	input() {
		return $('#' + this.getRefId() + '_input', this);
	}

	load(data) {
		this.setAttribute("param", data.paramName);
		$('label', this).innerText = data.text;
		this.loadInput(data);
		if (data.problem) {
			$('#' + this.getRefId() + '_msg', this).innerText = data.problem;
		}
		// TODO: eins davon raus!
		this.setAttribute('data-key', data.paramName);
	}

	store() {
		return this.input().value;
	}

	createInputElement(id) {
		return buildNode('input').type('text').id(id).get();
	}

	loadInput(data) {
		this.input().value = data.value;
		this.input().title = data.helptext;
	}
}

/**
 * A text input.
 */
class TextInput extends InputElement {
	// everything default
}

/**
 * A text input.
 */
class BigTextInput extends InputElement {
	createInputElement(id) {
		return buildNode('textarea').id(id).get();
	}

	loadInput(data) {
		super.loadInput(data);
		this.input().setAttribute('rows', data.rows);
	}
}

class FileInput extends InputElement {
	createInputElement(id) {
		let form = buildNode('form').attribute('enctype', 'multipart/form-data').get();
		buildChildNode(form, 'img').attribute('title', 'Bild hochlanden').attribute('alt', 'Bild').attribute('height', '80').attribute('src', 'no_image.png');
		buildChildNode(form, 'input').type('text').id(id).attribute('style', 'display:none');
		buildChildNode(form, 'input').attribute('name', 'file').type('file').attribute('style', 'display:none');
		return form;
	}

	loadInput(data) {
		this.input().value = data.value;
		if (data.value) {
			$('img', this).setAttribute('src', '/download/' + data.value);
			$('img', this).setAttribute('title', data.value + ' ersetzen');
		}
		let uploadPath = '/file/' + data.contextType + '?primKey=' + data.contextKey;
		$('img', this).onclick = () => $('[type=file]', this).click();
		$('[type=file]', this).onchange = () => {
			fetch(uploadPath, { method: 'POST', body: new FormData($('form', this)) })
				.then(response => response.text())
				.then(text => {
					$('[type=text]', this).value = text;
					let originalFileName = /.*[\\\/](.*)$/.exec($('[type=file]', this).value)[1];
					$('img', this).setAttribute('src', '/download/' + text);
					$('img', this).setAttribute('title', text);
				});
		};
	}
}

class FileUpload extends HTMLElement {
	connectedCallback() {
		let form = BuildNode.tag('form').in(this).attribute('enctype', 'multipart/form-data').get();
		let inp = buildChildNode(form, 'input').attribute('name', 'file').type('file').attribute('style', 'display:none').get();
		inp.onchange = () =>
			fetch(this.getAttribute('url'), { method: 'POST', body: new FormData($('form', this)) })
				.then(response => response.text()).then(text => alert(text));
		BuildNode.div().in(form).text(this.getAttribute('text')).class('button').get().onclick = () => inp.click();
	}
}


/**
 * Select element supporting only text.
 */
class TextChoice extends InputElement {
	createInputElement(id) {
		return buildNode('select').id(id).get();
	}

	loadInput(data) {
		const select = this.input();
		Object.entries(data.options).forEach(e => buildChildNode(select, 'option').attribute('value', e[0]).text(e[1]));
		select.value = data.value;
		select.title = data.helptext;
	}
}

/**
 * Select element providing additional info about selected element.
 */
class ObjectChoice extends TextChoice {
	createInputElement(id) {
		let span = buildNode('span').class("flex-row").get();
		buildChildNode(span, 'select').class("flex-grows").id(id);
		buildChildNode(span, 'div').id(this.getRefId() + '_more').class('button').text('...');
		return span;
	}

	// TODO: add action to ... when loading
}

class ImageChoice extends ObjectChoice {
	// TODO: implement own view
}

class SearchView extends MyCustomElement {
	createContent(refId) {
		let div = buildChildNode(this, 'div').get();
		buildChildNode(div, 'div').id(refId + '_questions');

		let afterQuestions = buildChildNode(div, 'div').class('separator').get();
		buildChildNode(afterQuestions, 'label').id(refId + '_stats');
		let control = buildChildNode(afterQuestions, 'span').get();
		buildChildNode(control, 'button').text('Eingaben löschen').get().onclick = () => this.clear();
		buildChildNode(div, 'div').class('cardscontainer').id(refId + '_results');
	}

	load(data, keepQuestionGroups = false) {
		const selectorPrefix = '#' + this.getRefId() + '_';
		if (this.getAttribute('type') !== data.type) {
			this.setAttribute('type', data.type);
			this.clear();
		}

		this.loadQuestions(data.questions, $(selectorPrefix + 'questions', this), keepQuestionGroups);

		$(selectorPrefix + 'stats').innerHTML = data.numberPossible + " von " + data.numberTotal + " passend";
		const tags = supportedTiles[data.type];
		const tagName = tags[Math.min(Math.floor(Math.log(data.numberPossible) / 2.1), tags.length - 1)];
		const resultsDiv = $(selectorPrefix + 'results');
		resultsDiv.innerHTML = '';
		const showMatching = data.matches.length > 0 && data.matches[0].matchValue > 50;
		data.matches.forEach(d =>
			buildChildNode(resultsDiv, tagName).get().load(d, showMatching));
	}

	buildStoreControls(node, refId) {
		let line = BuildNode.div().class('question').in(node).get();
		BuildNode.div().in(line);
		let content = BuildNode.div().in(line).get();
		BuildNode.div().class('button').in(content).text('Neu Anlegen').get().onclick = () => this.createObject();
		const primKey = this.getAttribute('currentPrimkey');
		if (primKey) {
			BuildNode.div().class('button').in(content).text(primKey + ' Aktualisieren').get().onclick = () => this.updateObject();
		}
		BuildNode.input('checkbox').in(content).id(refId + '_lenient');
		BuildNode.tag('span').in(content).text('Fehler ignorieren');
	}

	loadQuestions(questions, targetNode, keepGroups) {
		if (keepGroups) {
			$$('vertical-accordion', targetNode).forEach(a => a.content().innerHTML = '');
		} else {
			targetNode.innerHTML = '';
		}
		let group;
		questions.forEach(q => {
			let groupId = q.form.replace(/ /g, '_');
			group = $('#' + groupId, targetNode);
			if (!group) {
				group = buildChildNode(targetNode, 'vertical-accordion').id(groupId).get();
				group.title().innerHTML = q.form;
			}
			if (q.options) registerI18n(q.paramName, q.options);
			const questionType = elements.get(q.type) === undefined ? 'text-input' : q.type;
			let element = buildChildNode(group.content(), questionType).get();
			element.load(q);
			const focusNext = (current) => {
				let next = current.nextSibling;
				if (next && typeof (next.input) === 'function') {
					next.input().focus();
				}
			};
			element.input().addEventListener("change", () => {
				focusNext(element);
				this.updateFromQuestions();
			});
			element.input().addEventListener("keydown", (e) => {
				if (e.keyCode === 13) {
					focusNext(element);
				}
			});
			element.input().onfocus = () => this.currentFocus = element.getAttribute('data-key');
			if (q.problem) {
				group.expand();
			}
		});
		this.buildStoreControls(group.content(), this.getRefId());
		if (this.currentFocus) {
			let question = $('[data-key=' + this.currentFocus + ']', this);
			if (question) {
				question.input().focus();
			}
		}
		$$('vertical-accordion', targetNode).forEach(a => {
			if (a.isExpanded()) {
				a.expand();
			}
		});
	}

	updateFromQuestions() {
		let data = {};
		$$('[data-key]', this).forEach(x => data[x.getAttribute('data-key')] = x.store());
		this.update(data);
	}

	update(data) {
		let primKey = this.getAttribute('currentPrimkey');
		if (primKey) {
			data.primKey = primKey;
		}
		let params = Object.entries(data).filter(e => e[1] !== null && e[1] !== '' && e[1] !== '(Keine Angabe)')
			.map(e => e[0] + '=' + encodeURIComponent(e[1])).join('&');
		if (this.getAttribute('creatingObject')) {
			params = params + '&strictCheck=true';
		}

		let url = '/collected/' + this.getAttribute('type') + '/search' + (params === '' ? '' : '?' + params);
		getJson(url, x => this.load(x, true));
	}

	setObjectToEdit(data) {
		this.setAttribute('currentPrimkey', data.primKey);
		this.update(data.attributes);
		if ($$('vertical-accordion', this).every(a => !a.isExpanded())) {
			$('vertical-accordion', this).expand();
		}
	}

	updateObject() {
		let data = {};
		$$('[data-key]', this).forEach(x => data[x.getAttribute('data-key')] = x.store());
		const path = '/collected/' + this.getAttribute('type') + '/key/' + this.getAttribute('currentPrimkey') + this.isLenient();
		putObject(path, data, r => this.handleSubmissionResponse(r));

	}

	createObject() {
		let data = {};
		$$('[data-key]', this).forEach(x => data[x.getAttribute('data-key')] = x.store());
		const path = '/collected/' + this.getAttribute('type') + this.isLenient();
		postObject(path, data, r => {
			this.handleSubmissionResponse(r);
			this.setAttribute('currentPrimkey', r.primaryKey);
		});
	}

	handleSubmissionResponse(resp) {
		this.setAttribute('creatingObject', 'true');
		alert(i18n('messages', resp.message));
		if (resp.questions) {
			this.loadQuestions(resp.questions, $('#' + this.getRefId() + '_questions', this), true);
		}
		if (resp.success) {
			this.updateFromQuestions();
		}
	}

	isLenient() {
		return $('#' + this.getRefId() + '_lenient', this).checked ? '?lenient=true' : '';
	}

	clear() {
		this.removeAttribute('currentPrimkey');
		this.removeAttribute('creatingObject');
		this.update({});
	}
}

class DefaultTile extends MyCustomElement {
	createContent(refId) {
		let div = buildChildNode(this, 'div').class('c-frame').get();
		buildChildNode(div, 'div').class('c-header').id(refId + "_header");
		buildChildNode(div, 'div').class('c-body').id(refId + "_content");
	}

	load(data, doShowMatching = true) {
		const idPrefix = '#' + this.getRefId();
		let header = $(idPrefix + '_header');
		header.innerHTML = data.attributes.name;
		header.title = data.primKey;
		if (doShowMatching && data.matchValue <= 50) {
			this.classList.add('indifferent');
		}
		this.fillContent($(idPrefix + '_content'), data);
		$('div', this).onclick = () => this.showFull(data.type, data.primKey);
	}

	fillContent(node, data) {
		node.innerHTML = JSON.stringify(data.attributes);
	}

	showFull(type, key) {
		getJson('collected/' + type + '/key/' + key, data => {
			let fullView = supportedFullViews[type] ? supportedFullViews[type].apply() : new FullView();
			$('modal-dialog').show(key + '. ' + data.attributes.name, fullView);
			fullView.load(data, this.fillContent);
		});
	}
}

class SimpleTile extends DefaultTile {
	createContent(refId) {
		super.createContent(refId);
		$('div', this).classList.add('c-small');
	}
}

class FullView extends MyCustomElement {
	createContent(refId) {
		buildChildNode(this, 'div').id(refId + "_content").get();
		buildChildNode(this, 'div').class('button').text('Löschen').id(refId + "_delete");
		buildChildNode(this, 'div').class('button').text('In Eingabe übernehmen').id(refId + "_edit");
	}

    /**
     * may pass fillContent method from another class in case full view and tile have identic content.
     */
	load(data, defaultFillContent) {
		const idPrefix = '#' + this.getRefId() + '_';
		let actualFillContent = this.fillContent ? this.fillContent : defaultFillContent;
		actualFillContent($(idPrefix + 'content'), data);
		$(idPrefix + 'edit').onclick = () => {
			$('search-view').setObjectToEdit(data);
			$('modal-dialog').hide();
		};
		$(idPrefix + 'delete').onclick = () => {
			if (confirm('Wirklich löschen?')) {
				deleteObject('/collected/' + data.type + '/key/' + data.primKey, r => {
					if (r.status === 200) {
						$('search-view').updateFromQuestions();
						$('modal-dialog').hide();
					}
				});
			}
		};
	}
}

let i18nData = {};

function registerI18n(param, mapping) {
	i18nData[param] = mapping;
}

function i18n(param, key) {
	let map = i18nData[param];
	if (map && map[key]) return map[key];
	return key;
}

/**
 * Sorts the children of specified content element by some property
 * @param pathToContentContainer specifies the parent of all elements to sort.
 * @param pathToSortableValue specifies the part within the sorted elements to sort by
 * @param reverse true to sort backwards
 */
function sortChildren(pathToContentContainer, pathToSortableValue, reverse) {
	const container = $(pathToContentContainer);
	let items = Array.prototype.slice.call(container.children);
	items.sort((a, b) => $(pathToSortableValue, a).innerHTML.localeCompare($(pathToSortableValue, b).innerHTML));
	if (reverse === true) {
		items.reverse();
	}
	container.innerHTML = "";
	items.forEach(node => container.appendChild(node));
}

/**
 * register tile components here: key data element type, values is array
 */
const supportedTiles = {};

/**
 * same for full view components, ommit registration to use tile as full view.
 */
let supportedFullViews = {};
const elements = window.customElements ? window.customElements : customElements;
elements.define("text-input", TextInput);
elements.define("bigtext-input", BigTextInput);
elements.define("text-choice", TextChoice);
elements.define("object-choice", ObjectChoice);
elements.define("image-choice", ImageChoice);
elements.define("search-view", SearchView);
elements.define("modal-dialog", Dialog);
elements.define("vertical-accordion", VerticalAccordion);
elements.define("tab-row", TabRow);
elements.define("simple-tile", SimpleTile);
elements.define("default-tile", DefaultTile);
elements.define("default-full", FullView);
elements.define("file-upload", FileInput);
elements.define("upload-file", FileUpload);
