/**
 *  Finds an element within the DOM.
 * @param selector specifies the element to return
 * @param parent parent node to search in, optional.
 * @returns first found element
 */
function $(selector, parent) {
	return (typeof (parent) === 'undefined' ? document : parent).querySelector(selector);
}

/**
 * Returns array of found elements.
 * @param selector specifies the element to return
 * @param parent parent node to search in, optional.
 * @returns array of found elements
 */
function $$(selector, parent) {

	const list = (typeof (parent) === 'undefined' ? document : parent).querySelectorAll(selector);
	return Array.prototype.slice.call(list);
}

class BuildNode {
	constructor(name) {
		this.element = document.createElement(name);
	}

	static div() { return BuildNode.tag('div'); }
	static input(type) { return BuildNode.tag('input').attribute('type', type); }
	static tag(name) {
		return new BuildNode(name);
	}

	in(parent)
	{
		parent.appendChild(this.element);
		return this;	
	}
	
	appendTo(parent)
	{
		parent.appendChild(this.element);
		return element;
	}
	
	attribute(name, value) {
		this.element.setAttribute(name, value);
		return this;
	}
	
	text(value) {
		this.element.innerText = value;
		return this;
	}
	
	get() {
		return this.element;
	}

	id(value) { return this.attribute('id', value); }
	
}
/**
 * Returns a builder for a new node.
 * @param name tag name of new node.
 * @returns {InternalBuilder}
 */
function buildNode(name) {
	return new InternalBuilder(name);
}

/**
 * Returns a builder for new node which is already appended as child to parent node.
 * @param parent parent node
 * @param name tag name
 * @returns {InternalBuilder}
 */
function buildChildNode(parent, name) {
	let result = new InternalBuilder(name);
	parent.appendChild(result.get());
	return result;
}

class InternalBuilder {
	constructor(name) {
		this.element = document.createElement(name);
	}

	attribute(name, value) {
		this.element.setAttribute(name, value);
		return this;
	}

	text(value) {
		this.element.innerText = value;
		return this;
	}

	id(value) {
		return this.attribute('id', value);
	}

	class(value) {
		return this.attribute('class', value);
	}

	type(value) {
		return this.attribute('type', value);
	}

	value(value) {
		this.element.value = value;
		return this;
	}

	for(value) {
		return this.attribute('for', value);
	}

	get() {
		return this.element;
	}
}
