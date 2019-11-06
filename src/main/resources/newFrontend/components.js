/**
 * A text input.
 */
class MyText extends HTMLElement {
    connectedCallback() {
        const inpId= this.getAttribute('id')+'_inp';
        let div = buildChildNode(this, 'div').class('question').get();
        buildChildNode(div, 'label').class('left').for(inpId);
        buildChildNode(div, 'input').type('text').class('right').id(inpId);
        buildChildNode(div, 'label').class('warning').id(this.getAttribute('id')+'_msg');
    }

    load(data)
    {
        $('label', this).innerText=data.question;
        $('input', this).value=data.value;
        $('#'+this.getAttribute('id')+'_msg', this).innerText=data.message;
        $('input', this).title=data.tooltip;
    }

    store()
    {
        return $('input', this).value;
    }
}

/**
 * A drop down list.
 * @type {CustomElementRegistry}
 */
class DropDownList extends HTMLElement {
    connectedCallback() {
        const inpId= this.getAttribute('id')+'_inp';
        let div = buildChildNode(this, 'div').class('question').get();
        buildChildNode(div, 'label').class('left').for(inpId);
        buildChildNode(div, 'select').type('text').class('right').id(inpId);
        buildChildNode(div, 'label').class('warning').id(this.getAttribute('id')+'_msg');
    }

    load(data)
    {
        $('label', this).innerText=data.question;
        $('input', this).value=data.value;
        $('#'+this.getAttribute('id')+'_msg', this).innerText=data.message;
        $('input', this).title=data.tooltip;
    }

    store()
    {
        return $('input', this).value;
    }
}



const elements = window.customElements ? window.customElements: customElements;
elements.define("my-text", MyText);
