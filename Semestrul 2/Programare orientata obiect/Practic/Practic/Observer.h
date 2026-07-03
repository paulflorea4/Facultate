#pragma once
#include <vector>
using std::vector;

class Observer {
public:
	virtual void update() = 0;
};

class Observable {
private:
	vector<Observer*> obs;
public:
	void addObserver(Observer* o) {
		obs.push_back(o);
	}

	void notify() {
		for (const auto& o : obs) {
			o->update();
		}
	}
};