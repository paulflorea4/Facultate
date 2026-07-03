#pragma once
#include <string>
using std::string;

class DTO {
private:
	string title;
	int frequency;
public:
	DTO() : title(""), frequency(0) {}

	DTO(string& title) :title{ title }, frequency{ 1 } {};

	const string& getTitle() const {
		return title;
	}

	void incrementFrequency() {
		frequency++;
	}

	int getFrequency() const {
		return frequency;
	}
};