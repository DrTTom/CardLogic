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
 * A dialog which can be closed
 */
class Dialog extends MyCustomElement {

    createContent(refId) { // no content by default }
    
	/**
	 * shows the dialog
	 */
	show() {
	    let pane = buildChildNode(document.body, 'div').class('modal-overlay').get();
	    let frame = buildChildNode(pane,'div').class('modal-frame').get();
	    let headerLine = buildChildNode(frame, 'div').class('modal-header').get();
	    let header = buildChildNode(headerLine, 'div').get();
	    let cancel = buildChildNode(header, 'div').class('cancel-button').get();
	    cancel.innerHTML="X";
	    cancel.onclick=this.hide;	    
	}
	
	/**
	 * hides the dialog
	 */
	hide () {
	    document.body.removeChild($('[class=modal-overlay]'));
	}
}

/**
 * A text input.
 */
class MyText extends MyCustomElement {
    createContent(refId) {
        let div = buildChildNode(this, 'div').class('question').get();
        buildChildNode(div, 'label').class('left').for(refId + '_input');
        buildChildNode(div, 'input').type('text').class('right').id(refId + '_input');
        buildChildNode(div, 'label').class('warning').id(refId + '_msg');
    }

    load(data) {
        $('label', this).innerText = data.question;
        $('input', this).value = data.value;
        $('#' + this.getRefId() + '_msg', this).innerText = data.message;
        $('input', this).title = data.tooltip;
    }

    store() {
        return $('input', this).value;
    }
}

/**
 * A drop down list.
 * @type {CustomElementRegistry}
 */
class DropDownList extends HTMLElement {
    connectedCallback() {
        const inpId = this.getAttribute('id') + '_inp';
        let div = buildChildNode(this, 'div').class('question').get();
        buildChildNode(div, 'label').class('left').for(inpId);
        buildChildNode(div, 'select').type('text').class('right').id(inpId);
        buildChildNode(div, 'label').class('warning').id(this.getAttribute('refId') + '_msg');
    }

    load(data) {
        $('label', this).innerText = data.question;
        $('input', this).value = data.value;
        $('#' + this.getAttribute('id') + '_msg', this).innerText = data.message;
        $('input', this).title = data.tooltip;
    }

    store() {
        return $('input', this).value;
    }
}


class SearchView extends MyCustomElement {
    createContent(refId) {
        let div = buildChildNode(this, 'div').get();
        buildChildNode(div, 'div').class('cardscontainer').id(refId + '_results');
    }

    load(data) {
        const tagName = supportedTiles[data.type][0];
        data.questions.forEach(q => {
            if (q.options) registerI18n(q.paramName, q.options)
        });
        data.matches.forEach(d =>
            buildChildNode($('#' + this.getAttribute('refId') + '_results'), tagName).get().load(d));
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
elements.define("my-text", MyText);
elements.define("search-view", SearchView);
elements.define("dialog-view", Dialog);
