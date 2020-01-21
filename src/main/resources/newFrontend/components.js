let internalIdSource = 0;

function createId() {
    return 'id' + internalIdSource++;
}

/**
 * A text input.
 */
class MyText extends HTMLElement {
    connectedCallback() {
        const refId = createId();
        this.setAttribute('refId', refId);
        let div = buildChildNode(this, 'div').class('question').get();
        buildChildNode(div, 'label').class('left').for(refId + '_input');
        buildChildNode(div, 'input').type('text').class('right').id(refId + '_input');
        buildChildNode(div, 'label').class('warning').id(refId + '_msg');
    }

    load(data) {
        $('label', this).innerText = data.question;
        $('input', this).value = data.value;
        $('#' + this.getAttribute('refId') + '_msg', this).innerText = data.message;
        $('input', this).title = data.tooltip;
    }

    store() {
        return $('input', this).value;
    }
}

class SearchView extends HTMLElement {
    connectedCallback() {
        const refId = createId();
        this.setAttribute('refId', refId);
        let div = buildChildNode(this, 'div').get();
        buildChildNode(div, 'div').class('cardscontainer').id(refId + '_results');
    }

    load(data) {
        const tagName = supportedCards[data.type][0];
        data.matches.forEach(d =>
            buildChildNode($('#' + this.getAttribute('refId') + '_results'), tagName).get().load(d));
    }
}

class CardMaker extends HTMLElement {
    connectedCallback() {
        let div = buildChildNode(this, 'div').class('card').get();
        const refId = createId();
        this.setAttribute('refId', refId);
        buildChildNode(div, 'h4');
        buildChildNode(div, 'label').id(refId + "_place");
        buildChildNode(div, 'label').id(refId + "_time");
        buildChildNode(div, 'img').class('topright');
        buildChildNode(div, 'p').class('scroll3lines');
    }

    load(data) {
        $('h4', this).innerHTML = data.attributes.name;
        $('h4', this).title = data.primKey;
        const idPrefix = '#' + this.getAttribute('refId');
        $(idPrefix + '_time').innerHTML = data.attributes.from + ' - ' + data.attributes.to;
        $(idPrefix + '_place').innerHTML = data.attributes.place + ' ';
        $('p', this).innerHTML = data.attributes.remark;
        $('img', this).setAttribute('src', 'TODO');
    }
}

class DeckBig extends HTMLElement {

    connectedCallback() {
        let div = buildChildNode(this, 'div').class('card').get();
        const refId = createId();
        this.setAttribute('refId', refId);
        const left = buildChildNode(div, 'header').attribute("class", "leftofimage").get();
        buildChildNode(left, 'h4');
        buildChildNode(left, 'label').id(refId + "_maker");
        buildChildNode(left, 'label').id(refId + "_numbercards");
        buildChildNode(div, 'img').attribute("class", "defaultimage");
        buildChildNode(div, 'p').class('scroll3lines');
    }

    load(data) {
        $('h4', this).innerHTML = data.primKey + '. ' + data.attributes.name;
        const idPrefix = '#' + this.getAttribute('refId');
        $(idPrefix + '_maker').innerHTML = data.attributes.maker+ ' '+ this.timeString(data);
        $(idPrefix + '_numbercards').innerHTML = data.attributes.numberCards + " Blatt, "+data.attributes.format;
        $('p', this).innerHTML = data.attributes.remark;
        $('img', this).setAttribute('src', '/download/'+data.attributes.image);
    }
    
    timeString(data)
    {
    	return ' gedruckt etwa '+data.attributes.printedLatest;
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

const supportedCards =
    {
        'maker': ['card-maker'],
        'deck': ['deck-big', 'deck-medium', "deck-small"]
    }

const elements = window.customElements ? window.customElements : customElements;
elements.define("my-text", MyText);
elements.define("card-maker", CardMaker);
elements.define("search-view", SearchView);
elements.define("deck-big", DeckBig);
