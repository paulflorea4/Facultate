#include "Activity.h"

int Activity::getId() const {
	return id;
}

string Activity::getTitle() const {
	return title;
}

string Activity::getDescription() const {
	return description;
}

string Activity::getType() const {
	return type;
}

int Activity::getDuration() const {
	return duration;
}

void Activity::setTitle(const string newTitle) {
	this->title = newTitle;
}

void Activity::setDescription(const string newDescription) {
	this->description = newDescription;
}

void Activity::setType(const string newType) {
	this->type = newType;
}

void Activity::setDuration(const int newDuration) {
	this->duration = newDuration;
}

bool Activity::compareByTitle(const Activity& a, const Activity& b) {
	return a.getTitle() < b.getTitle();
}

bool Activity::compareByDescription(const Activity& a, const Activity& b) {
	return a.getDescription() < b.getDescription();
}

bool Activity::compareByTypeAndDuration(const Activity& a, const Activity& b) {
	if (a.getType() == b.getType())
		return a.getDuration() < b.getDuration();
	return a.getType() < b.getType();
}
