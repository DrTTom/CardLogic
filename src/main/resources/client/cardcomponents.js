class DefaultTile extends MyCustomElement {
	createContent(refId) {
		let div = buildChildNode(this, 'div').class('c-frame').get();
		buildChildNode(div, 'div').class('c-header').id(refId + "_header");
		buildChildNode(div, 'div').class('c-body').id(refId + "_content");
	}
	load(data) {
		const idPrefix = '#' + this.getRefId();
		let header = $(idPrefix + '_header');
		header.innerHTML = data.attributes.name;
		header.title = data.primKey;
		this.fillContent($(idPrefix + '_content'), data);
		$('div', this).onclick = () => this.showFull(data.type, data.primKey);
	}
	fillContent(node, data) {
		node.innerHTML = JSON.stringify(data.attributes);
	}
	showFull(type, key) {
		getJson('collected/' + type + '/key/' + key, data => {
			let fullView = supportedFullViews[type] ? supportedFullViews[type].apply(): new FullView();
			$('modal-dialog').show(key + '. ' + data.attributes.name, fullView);
			fullView.load(data, this.fillContent);
		});
	}
}


class CardMaker extends DefaultTile {
	createContent(refId) {
		super.createContent(refId);
		$('div', this).classList.add('c-medium');
	}

	fillContent(node, data) {
		buildChildNode(node, 'div').get().innerHTML = data.attributes.place;
		buildChildNode(node, 'div').get().innerHTML = data.attributes.from + ' - ' + data.attributes.to;
		buildChildNode(node, 'p').class('scroll5lines').get().innerHTML = data.attributes.remark;
	}
}

class SimpleTile extends DefaultTile {
	createContent(refId) {
		super.createContent(refId);
		$('div', this).classList.add('c-small');
	}
}

class DeckSmall extends DefaultTile {
	createContent(refId) {
		super.createContent(refId);
		$('div', this).classList.add('c-small');
	}
	fillContent(node, data) {
		buildChildNode(node, 'div').get().innerHTML = i18n('maker', data.attributes.maker);
		buildChildNode(node, 'div').get().innerHTML = DeckSmall.timeString(data.attributes);
		let text = data.attributes.remark ? data.attributes.remark : i18n('pattern', data.attributes.pattern);
		buildChildNode(node, 'p').class('scroll2lines').get().innerHTML = text;
	}

	static timeString(attrs) {
		let to = attrs.printedLatest;
		let from = attrs.printedEarliest;
		if (to) {
			return to == from ? to : (from ? 'zwischen ' + from + ' und ' : 'spätestens ') + to;
		}
		return from ? 'frühestens ' + from : 'keine Zeitangabe';
	}

}

class DeckMedium extends DefaultTile {
	createContent(refId) {
		super.createContent(refId);
		$('div', this).classList.add('c-medium');
	}
	fillContent(node, data) {
		buildChildNode(node, 'img').class('image-small').attribute('src', '/download/' + data.attributes.image);
		buildChildNode(node, 'div').text(i18n('maker', data.attributes.maker));
		buildChildNode(node, 'div').text(DeckSmall.timeString(data.attributes));
		buildChildNode(node, 'div').class('separated').text(data.attributes.numberCards + ' Blatt, ' + data.attributes.format);
		let text = data.attributes.remark ? data.attributes.remark : i18n('pattern', data.attributes.pattern);
		buildChildNode(node, 'p').class('scroll3lines separated clear').text(text);
	}
}


class DeckBig extends DefaultTile {
	createContent(refId) {
		super.createContent(refId);
		$('div', this).classList.add('c-big');
	}
	fillContent(node, data) {
		buildChildNode(node, 'img').class('image-default').attribute('src', '/download/' + data.attributes.image);
		buildChildNode(node, 'div').text(i18n('maker', data.attributes.maker))
		buildChildNode(node, 'div').text(DeckSmall.timeString(data.attributes));
		buildChildNode(node, 'div').class('separated').text(data.attributes.numberCards + ' Blatt, ' + data.attributes.format);
		let text = 'erworben '+data.attributes.bought+', '+i18n('condition', data.attributes.condition);
		buildChildNode(node, 'div').class('separated').text(text);
		text = data.attributes.remark ? data.attributes.remark : i18n('pattern', data.attributes.pattern);
		buildChildNode(node, 'p').class('scroll3lines separated clear').text(text);		
	}
}

class FullView extends MyCustomElement {
	createContent(refId) {
		buildChildNode(this, 'div').id(refId+"_content").get();
		buildChildNode(this, 'div').class('button').text('Löschen').id(refId+"_delete");
		buildChildNode(this, 'div').class('button').text('In Eingabe übernehmen').id(refId+"_edit");		
		}
		
		/**
		 * buildContent is short cut in case full view and tile have identic content. Otherwise, class must impement this method itself.
		 */		
		load(data, buildContent)
		{
			buildContent($('#'+this.getRefId()+'_content'), data);
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

supportedTiles.deck = ['deck-big', 'deck-medium', 'deck-small'];
supportedTiles.maker = ['card-maker'];
supportedTiles.makerSign = ['simple-tile'];
supportedTiles.taxStamp = ['simple-tile'];
supportedTiles.pattern = ['simple-tile'];

let supportedFullViews = {};
supportedFullViews.deck = () => new FullDeck();
// supportedFullViews.maker = () => new CardMaker();
// supportedFullViews.makerSign = () => new SimpleTile();
// supportedFullViews.taxStamp = () => new SimpleTile();
// supportedFullViews.pattern = () => new SimpleTile();


elements.define("card-maker", CardMaker);
elements.define("deck-big", DeckBig);
elements.define("deck-medium", DeckMedium);
elements.define("deck-small", DeckSmall);
elements.define("simple-tile", SimpleTile);
elements.define("deck-full", FullDeck);
elements.define("default-tile", DefaultTile);
elements.define("default-full", FullView);
