let internalIdSource = 0;

function createId() {
    return 'id'+internalIdSource++;
}

/**
 * A text input.
 */
class MyText extends HTMLElement {
    connectedCallback() {
        const refId = createId();
        this.setAttribute('refId', refId);
        let div = buildChildNode(this, 'div').class('question').get();
        buildChildNode(div, 'label').class('left').for(refId+'_input');
        buildChildNode(div, 'input').type('text').class('right').id(refId+'_input');
        buildChildNode(div, 'label').class('warning').id(refId + '_msg');
    }

    load(data) {
        $('label', this).innerText = data.question;
        $('input', this).value = data.value;
        $('#'+this.getAttribute('refId')+'_msg', this).innerText = data.message;
        $('input', this).title = data.tooltip;
    }

    store() {
        return $('input', this).value;
    }
}

class CardMaker extends HTMLElement {
    connectedCallback() {
        let div = buildChildNode(this, 'div').class('card').get();
        const refId = createId();
        this.setAttribute('refId', refId);
        buildChildNode(div, 'h4');
        buildChildNode(div, 'label').id(refId+"_place");
        buildChildNode(div, 'label').id(refId+"_time");
        buildChildNode(div, 'img');
        buildChildNode(div, 'p');
    }

    load(data) {
        $('h4', this).innerHTML=data.attributes.name;
        $('h4', this).title=data.primKey;
        const idPrefix = '#' + this.getAttribute('refId');
        $(idPrefix+'_time').innerHTML=data.attributes.from+' - '+data.attributes.to;
        $(idPrefix+'_place').innerHTML=data.attributes.place+' ';
        $('p', this).innerHTML=data.attributes.remark;
        $('img', this).setAttribute('src', 'TODO');
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


const elements = window.customElements ? window.customElements : customElements;
elements.define("my-text", MyText);
elements.define("card-maker", CardMaker);
