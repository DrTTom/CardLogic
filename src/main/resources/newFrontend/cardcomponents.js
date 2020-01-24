class CardMaker extends MyCustomElement {
    createContent(refId) {
        let div = buildChildNode(this, 'div').class('card').get();
        buildChildNode(div, 'h4');
        buildChildNode(div, 'label').id(refId + "_place");
        buildChildNode(div, 'label').id(refId + "_time");
        buildChildNode(div, 'img').class('topright');
        buildChildNode(div, 'p').class('scroll3lines');
    }

    load(data) {
        $('h4', this).innerHTML = data.attributes.name;
        $('h4', this).title = data.primKey;
        const idPrefix = '#' + this.getRefId();
        $(idPrefix + '_time').innerHTML = data.attributes.from + ' - ' + data.attributes.to;
        $(idPrefix + '_place').innerHTML = data.attributes.place + ' ';
        $('p', this).innerHTML = data.attributes.remark;
        $('img', this).setAttribute('src', 'TODO');
    }
}

class DeckBig extends MyCustomElement {
    createContent(refId) {
        let div = buildChildNode(this, 'div').class('card').get();
        const left = buildChildNode(div, 'header').attribute("class", "leftofimage").get();
        buildChildNode(left, 'h4');
        buildChildNode(left, 'label').id(refId + "_maker");
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_date");
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_numbercards");
        buildChildNode(div, 'img').attribute("class", "defaultimage");
        buildChildNode(div, 'p').class('scroll3lines');
    }

    load(data) {
        $('h4', this).innerHTML = data.primKey + '. ' + data.attributes.name;
        const idPrefix = '#' + this.getRefId();
        $(idPrefix + '_maker').innerHTML = i18n('maker', data.attributes.maker);
        $(idPrefix + '_date').innerHTML =this.timeString(data.attributes);
        $(idPrefix + '_numbercards').innerHTML = data.attributes.numberCards + " Blatt, " + data.attributes.format;
        $('p', this).innerHTML = data.attributes.remark;
        $('img', this).setAttribute('src', '/download/' + data.attributes.image);
    }

    timeString(attrs) {
        let to = attrs.printedLatest;
        let from = attrs.printedEarliest;
        if (to) {
            return to == from ? to : (from ? 'zwischen ' + from + ' und ' : 'spätestens ') + to;
        }
        return from ? 'frühestens ' + from : 'keine Zeitangabe';
    }
}

supportedTiles.deck = ['deck-big', 'deck-medium', "deck-small"];
supportedTiles.maker = ['card-maker'];

elements.define("card-maker", CardMaker);
elements.define("deck-big", DeckBig);
