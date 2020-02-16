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
		buildChildNode(this, 'div').id(refId + '_content').class('accordion-panel');
		title.onclick = () => {
			title.classList.toggle('active');
			let panel = $('#' + refId + "_content");
			if (panel.style.maxHeight) {
				panel.style.maxHeight = null;
			} else {
				panel.style.maxHeight = panel.scrollHeight + "px";
			}
		};
	}

	content() {
		return $('#' + this.getRefId() + '_content');
	}

	title() {
		return $('#' + this.getRefId() + '_title');
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
 * Select element supporting only text. 
 */
class TextChoice extends InputElement {
	createInputElement(id) {
		return buildNode('select').id(id).get();
	}

	loadInput(data) {
		const select = this.input();
		Object.entries(data.options).forEach(e => buildChildNode(select, 'option').attribute('value', e[0]).get().innerHTML = e[1]);
		select.value = data.value;
		select.title = data.helptext;
	}
}

/**
 * Select element providing additional info about selected element.
 */
class ObjectChoice extends InputElement {
	createInputElement(id) {
		let span = buildNode('span').class("flex-row").get();
		buildChildNode(span, 'select').class("flex-grows").id(id);
		buildChildNode(span, 'div').id(this.getRefId() + '_more').class('button').get().innerHTML = '?';
		return span;
	}

	loadInput(data) {
		const select = this.input();
		Object.entries(data.options).forEach(e => buildChildNode(select, 'option').attribute('value', e[0]).get().innerHTML = e[1]);
		select.value = data.value;
		select.title = data.helptext;
	}
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
		let clear = buildChildNode(control, 'button').get();
		clear.innerHTML = 'Eingaben löschen';
		clear.onclick= ()=>this.update({});
		let create = buildChildNode(control, 'button').get();
		create.innerHTML = 'Neu Anlegen';
		create.onclick = () => this.createObject();
		let update = buildChildNode(control, 'button').attribute('disabled', 'true').get();
		update.innerHTML = 'Aktualisieren';
		buildChildNode(control, 'input').attribute('type', 'checkbox').get();
		buildChildNode(control, 'span').get().innerHTML = 'Fehler ignorieren';

		buildChildNode(div, 'div').class('cardscontainer').id(refId + '_results');
	}

	load(data, keepQuestionGroups = false) {
		const selectorPrefix = '#' + this.getRefId() + '_';
		this.setAttribute('type', data.type);

		this.loadQuestions(data.questions, $(selectorPrefix + 'questions', this), keepQuestionGroups);

		$(selectorPrefix + 'stats').innerHTML = data.numberPossible + " von " + data.numberTotal + " passend, " + data.numberMatching + " wahrscheinlich";
		const tags = supportedTiles[data.type];
		const tagName = tags[Math.min(Math.floor(Math.log(data.numberPossible) / 2.1), tags.length - 1)];
		const resultsDiv = $(selectorPrefix + 'results');
		resultsDiv.innerHTML = '';
		data.matches.forEach(d =>
			buildChildNode(resultsDiv, tagName).get().load(d));
	}

	loadQuestions(questions, targetNode, keepGroups) {
		if (keepGroups) {
			$$('vertical-accordion', targetNode).forEach(a => a.content().innerHTML = '');
		} else {
			targetNode.innerHTML = '';
		}
		questions.forEach(q => {
			let groupId = q.form.replace(/ /g, '_');
			let group = $('#' + groupId, targetNode);
			if (!group) {
				group = buildChildNode(targetNode, 'vertical-accordion').id(groupId).get();
				group.title().innerHTML = q.form;
			}
			if (q.options) registerI18n(q.paramName, q.options);
			const questionType = elements.get(q.type) === undefined ? 'text-input' : q.type;
			let element = buildChildNode(group.content(), questionType).get();
			element.load(q);
			element.input().addEventListener("change", () => this.updateFromQuestions());
		});
	}

	updateFromQuestions() {
		let data = {};
		$$('[data-key]', this).forEach(x => data[x.getAttribute('data-key')] = x.store());
		this.update(data);
	}

	update(data) {
		let params = Object.entries(data).filter(e => e[1] !== null && e[1] !== '' && e[1] !== '(Keine Angabe)')
			.map(e => e[0] + '=' + encodeURIComponent(e[1])).join('&');
		let url = '/collected/' + this.getAttribute('type') + '/search' + (params === '' ? '' : '?' + params);
		getJson(url, x => this.load(x, true));
	}

	createObject() {
		let data = {};
		$$('[data-key]', this).forEach(x => data[x.getAttribute('data-key')] = x.store());
		postObject('/collected/' + this.getAttribute('type'), data, r => {
			alert(i18n('messages', r.message));
			if (r.questions) { this.loadQuestions(r.questions, $('#'+this.getRefId() + '_questions', this), true); }
		});
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
	var items = Array.prototype.slice.call(container.children);
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

const elements = window.customElements ? window.customElements : customElements;
elements.define("text-input", TextInput);
elements.define("text-choice", TextChoice);
elements.define("object-choice", ObjectChoice);
elements.define("image-choice", ImageChoice);
elements.define("search-view", SearchView);
elements.define("modal-dialog", Dialog);
elements.define("vertical-accordion", VerticalAccordion);
elements.define("tab-row", TabRow);
