#pragma once
#include "Activity.h"
#include "Repository.h"
#include <functional>

class UndoAction {
public:
	virtual void doUndo() = 0;
	virtual ~UndoAction() = default;
};

class UndoAdd :public UndoAction {
private:
	Activity activityToDelete;
	Repository& repository;
public:
	UndoAdd(const Activity activity, Repository& repository) :activityToDelete{ activity }, repository{ repository }{}
	void doUndo() override {
		repository.remove(activityToDelete.getId());
	}
};

class UndoDelete :public UndoAction {
private:
	Activity activityToAdd;
	Repository& repository;
public:
	UndoDelete(const Activity activity, Repository& repository) :activityToAdd{ activity }, repository{ repository } {}
	void doUndo() override {
		repository.store(activityToAdd);
	}
};

class UndoUpdate :public UndoAction {
private:
	Activity activityToUpdate;
	Repository& repository;
public:
	UndoUpdate(const Activity activity, Repository& repository) :activityToUpdate{ activity }, repository{ repository } {}
	void doUndo() override {
		repository.update(activityToUpdate);
	}
};