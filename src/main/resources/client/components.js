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
 * A container which can be vertically collapsed
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
        cancel.onclick = (() => {
            $('#' + refId + '_overlay').style.display = 'none';
            $('#' + refId + '_body').innerHTML = '';
        });
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
 * A text input. TODO: make this a base class for different types of questions.
 */
class MyText extends MyCustomElement {
    createContent(refId) {
        let div = buildChildNode(this, 'div').class('question').get();
        buildChildNode(div, 'label').for(refId + '_input');
        buildChildNode(div, 'input').type('text').id(refId + '_input');
        buildChildNode(div, 'label');
        buildChildNode(div, 'label').class('warning').id(refId + '_msg');
    }

    input() {
        return $('#' + this.getRefId() + '_input', this);
    }

    load(data) {
        this.setAttribute("param", data.paramName);
        $('label', this).innerText = data.text;
        $('input', this).value = data.value;
        if (data.message) {
            $('#' + this.getRefId() + '_msg', this).innerText = data.message;
        }
        $('input', this).title = data.helptext;
        this.setAttribute('data-key', data.paramName);
    }

    store() {
        return $('input', this).value;
    }
}


class SearchView extends MyCustomElement {
    createContent(refId) {
        let div = buildChildNode(this, 'div').get();
        buildChildNode(div, 'div').id(refId + '_questions').get();
        buildChildNode(div, 'div').class('cardscontainer').id(refId + '_results');
    }

    load(data) {
        const refId = this.getRefId();
        const selectorPrefix = '#' + refId + '_';
        const questionsDiv = $(selectorPrefix + 'questions');
        questionsDiv.innerHTML = '';
        let lastGroupTitle = '';
        let group;
        data.questions.forEach(q => {
            if (lastGroupTitle !== q.form) {
                lastGroupTitle = q.form;
                group = buildChildNode(questionsDiv, 'vertical-accordion').get();
                group.title().innerHTML = lastGroupTitle;
            }
            if (q.options) registerI18n(q.paramName, q.options);
            let element = buildChildNode(group.content(), "my-text").get();
            element.load(q);
            element.input().addEventListener("change", () => this.update(refId));
        });
        const tagName = supportedTiles[data.type][0];
        const resultsDiv = $(selectorPrefix + 'results');
        resultsDiv.innerHTML = '';
        data.matches.forEach(d =>
            buildChildNode(resultsDiv, tagName).get().load(d));
    }

    update(refId) {
        let url = '/collected/deck/search';
        let separator = '?';
        $$('[data-key]', this).forEach(x => {
            const value = x.store();
            if (value !== null && value !== '' && value !== '(Keine Angabe)') {
                url = url + separator + x.getAttribute('data-key') + '=' + encodeURIComponent(value);
            }
        });
        console.log(url);
        getJson(url, x => this.load(x));
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
elements.define("modal-dialog", Dialog);
elements.define("vertical-accordion", VerticalAccordion);
