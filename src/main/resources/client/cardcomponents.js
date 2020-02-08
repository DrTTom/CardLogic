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

class SimpleTile extends MyCustomElement {
    createContent(refId) {
        let div = buildChildNode(this, 'div').class('card').get();
        buildChildNode(div, 'h4');
    }

    load(data) {
        $('h4', this).innerHTML = data.attributes.name;
        $('h4', this).title = data.primKey;
    }
}


class DeckBig extends MyCustomElement {
    createContent(refId) {
        let div = buildChildNode(this, 'div').class('card').get();
        const left = buildChildNode(div, 'div').attribute("class", "leftofimage").get();
        let header = buildChildNode(left, 'h4').get();
        buildChildNode(header, 'span').id(refId + "_key");
        buildChildNode(header, 'span').id(refId + "_title");
        buildChildNode(left, 'label').id(refId + "_maker");
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_date");
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_numbercards");
        buildChildNode(div, 'img').attribute("class", "defaultimage");
        buildChildNode(div, 'p').class('scroll3lines');
        div.onclick = () => this.showFull(refId);
    }

    load(data) {
        const idPrefix = '#' + this.getRefId();
        $(idPrefix + '_key').innerHTML = data.primKey;
        $(idPrefix + '_title').innerHTML = '. ' + data.attributes.name;
        $(idPrefix + '_maker').innerHTML = i18n('maker', data.attributes.maker);
        $(idPrefix + '_date').innerHTML = this.timeString(data.attributes);
        $(idPrefix + '_numbercards').innerHTML = data.attributes.numberCards + " Blatt, " + data.attributes.format;
        $('p', this).innerHTML = data.attributes.remark;
        $('img', this).setAttribute('src', '/download/' + data.attributes.image);
    }

    showFull(refId) {
        let key = $('#' + refId + '_key').innerHTML;
        getJson('collected/deck/key/' + key, deck => {
            let fullDeck = new FullDeck();
            $('modal-dialog').show(key + '. ' + deck.attributes.name, fullDeck);
            fullDeck.load(deck);
        });
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

class FullDeck extends MyCustomElement {
    createContent(refId) {
        const left = buildChildNode(this, 'div').attribute("class", "leftofimage").get();
        buildChildNode(left, 'label').id(refId + "_maker");
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_date").class('sepAfter');
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_numbercards");
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_specialMeasure").class('sepAfter');
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_suits");
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_pattern");
        buildChildNode(left, 'br');
        buildChildNode(left, 'label').id(refId + "_index");
        buildChildNode(this, 'img').attribute("class", "bigimage");
        buildChildNode(this, 'p').class('scroll6lines');
        buildChildNode(this, 'label').id(refId + "_condition");
        buildChildNode(this, 'br');
        buildChildNode(this, 'button').id(refId + '_button').get().innerHTML = 'Daten übernehmen';
    }

    load(data) {
        const idPrefix = '#' + this.getRefId();
        let attrs = data.attributes;
        $(idPrefix + '_maker').innerHTML = i18n('maker', attrs.maker);
        $(idPrefix + '_date').innerHTML = this.timeString(attrs);
        $(idPrefix + '_numbercards').innerHTML = data.attributes.numberCards + " Blatt, " + data.attributes.format;
        $('p', this).innerHTML = data.attributes.remark;
        $('img', this).setAttribute('src', '/download/' + data.attributes.image);
        $(idPrefix + '_condition').innerHTML = 'erworben ' + attrs.bought + ', ' + i18n('condition', attrs.condition) + ', befindet sich in ' + attrs.location;
        $(idPrefix + '_suits').innerHTML = 'Farbzeichen ' + i18n('suits', attrs.suits);
        $(idPrefix + '_pattern').innerHTML = 'Bild: ' + i18n('pattern', attrs.pattern);
        $(idPrefix + '_index').innerHTML = ' Index ' + attrs.numIndex + ' mal ' + attrs.index;
        $(idPrefix + '_button').onclick = () => $('search-view').update(attrs);
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
supportedTiles.makerSign = ['simple-tile'];
supportedTiles.taxStamp = ['simple-tile'];
supportedTiles.pattern = ['simple-tile'];

elements.define("card-maker", CardMaker);
elements.define("deck-big", DeckBig);
elements.define("simple-tile", SimpleTile);
elements.define("deck-full", FullDeck);
