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
	RepositoryAbstract& repository;
public:
	UndoAdd(const Activity activity, RepositoryAbstract& repository) :activityToDelete{ activity }, repository{ repository }{}
	void doUndo() override {
		repository.remove(activityToDelete.getId());
	}
};

class UndoDelete :public UndoAction {
private:
	Activity activityToAdd;
	RepositoryAbstract& repository;
public:
	UndoDelete(const Activity activity, RepositoryAbstract& repository) :activityToAdd{ activity }, repository{ repository } {}
	void doUndo() override {
		repository.store(activityToAdd);
	}
};

class UndoUpdate :public UndoAction {
private:
	Activity activityToUpdate;
	RepositoryAbstract& repository;
public:
	UndoUpdate(const Activity activity, RepositoryAbstract& repository) :activityToUpdate{ activity }, repository{ repository } {}
	void doUndo() override {
		repository.update(activityToUpdate);
	}
};