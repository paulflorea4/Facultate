#pragma once
#include <vector>
#include "Observer.h"
using std::vector;

class Observable {
private:
	vector<Observer*> observers;
public:
	void addObserver(Observer* observer) {
		observers.push_back(observer);
	}
protected:
	void notify() {
		for (auto observer : observers)
			observer->update();
	}
};