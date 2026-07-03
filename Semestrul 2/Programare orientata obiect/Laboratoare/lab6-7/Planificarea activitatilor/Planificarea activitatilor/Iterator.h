#pragma once

template <typename TElem>
class List;

template <typename TElem>
class Iterator {
	friend class List<TElem>;
private:
	const List<TElem>& list;
	int index = 0;

public:
	Iterator(const List<TElem>& list) noexcept;

	Iterator(const List<TElem>& list, int index) noexcept;

	bool valid() const;

	TElem& element() const;

	int getIndex() const;

	void next();

	TElem& operator*();

	Iterator& operator++();

	bool operator==(const Iterator& other) noexcept;

	bool operator!=(const Iterator& other) noexcept;
};

template<typename TElem>
Iterator<TElem>::Iterator(const List<TElem>& list) noexcept : list{list}{}

template<typename TElem>
Iterator<TElem>::Iterator(const List<TElem>& list, int index) noexcept : list{ list }, index{ index } {}

template<typename TElem>
bool Iterator<TElem>::valid() const {
	return index < list.length;
}

template<typename TElem>
TElem& Iterator<TElem>::element() const {
	return list.elements[index];
}

template<typename TElem>
int Iterator<TElem>::getIndex() const {
	return index;
}

template<typename TElem>
void Iterator<TElem>::next() {
	index++;
}

template<typename TElem>
TElem& Iterator<TElem>::operator*() {
	return element();
}

template<typename TElem>
Iterator<TElem>& Iterator<TElem>::operator++() {
	next();
	return *this;
}

template<typename TElem>
bool Iterator<TElem>::operator==(const Iterator& other) noexcept{
	return index == other.index;
}

template<typename TElem>
bool Iterator<TElem>::operator!=(const Iterator& other) noexcept {
	return !(*this == other);
}