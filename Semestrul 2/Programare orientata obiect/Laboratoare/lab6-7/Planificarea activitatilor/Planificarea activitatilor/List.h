#pragma once

#include "Iterator.h"

template <typename TElem>
class Iterator;

template <typename TElem>
class List {
private:
	int length, capacity;
	TElem* elements;

	//Functia de asigurare a capacitatii vectorului
	void ensureCapacity();
public:
	List() :length{ 0 }, capacity{ 2 }, elements{ new TElem[capacity] } {}

	void push_back(const TElem& elem);

	int size() const {
		return length;
	}

	void erase(const int& index);

	TElem& operator[](const int& index) {
		return elements[index];
	}

	const TElem& operator[](const int& index) const{
		return elements[index];
	}

	List& operator=(const List& other)
	{
		if (this != &other)
		{
			delete[] elements;

			capacity = other.capacity;
			length = other.length;

			elements = new TElem[capacity];

			if (length > 0 && other.elements != nullptr) {
				for (int i = 0; i < length; i++) {
					elements[i] = other.elements[i];
				}
			}
		}
		//std::cout << "S-a atribuit lista" << std::endl;
		return *this;
	}


	List(const List& other);

	~List() {
		delete[] elements;
	}

	friend class Iterator<TElem>;
	Iterator<TElem> begin();
	Iterator<TElem> end();
	Iterator<TElem> begin() const;
	Iterator<TElem> end() const;
};

template <typename TElem>
void List<TElem>::ensureCapacity()
{
	if (length == capacity)
	{
		TElem* newList = new TElem[capacity * 2];
		for (int i = 0; i < length; i++)
			newList[i] = elements[i];
		delete[] elements;
		elements = newList;
		capacity *= 2;
	}
}

template <typename TElem>
void List<TElem>::push_back(const TElem& element) {
	ensureCapacity();
	elements[length++] = element;
}

template<typename TElem>
inline void List<TElem>::erase(const int& index)
{	
	for (int i = index; i < length-1; i++)
		elements[i] = elements[i + 1];
	length--;
}

template <typename TElem>
List<TElem>::List(const List& other) : length{ other.length }, capacity{ other.capacity }, elements{ new TElem[other.capacity] } {
    for (int i = 0; i < length; i++) {
		elements[i] = other.elements[i];
    }
	//std::cout << "S-a copiat lista" << std::endl;
}

template <typename TElem>
Iterator<TElem> List<TElem>::begin() {
	return Iterator<TElem>(*this);
}

template <typename TElem>
Iterator<TElem> List<TElem>::end() {
	return Iterator<TElem>(*this, length);
}

template <typename TElem>
Iterator<TElem> List<TElem>::begin() const {
	return Iterator<TElem>(*this);
}

template <typename TElem>
Iterator<TElem> List<TElem>::end() const {
	return Iterator<TElem>(*this, length);
}

