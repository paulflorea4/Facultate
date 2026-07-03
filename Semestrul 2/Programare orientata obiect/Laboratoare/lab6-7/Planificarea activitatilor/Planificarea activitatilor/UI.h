#pragma once
#include "Service.h"
#include "Exception.h"

class UI {
private:
	Service& activityService;

public:
	UI(Service& activityService) : activityService{ activityService } {};

	UI(const UI& other) = delete;

	void runUI();

	void printMenuUI() const;

	void addActivityUI();

	void deleteActivityUI();

	void searchActivityUI();

	void updateActivityUI();

	void printActivitiesUI();

	void filterActivitiesUI();

	void sortActivitiesUI();

	void printListMenuUI() const;

	void addActivityInListUI();

	void emptyListUI();

	void exportListUI() const;

	void generateActivitiesInListUI();

	void printActivityListUI() const;

	void manageListUI();

	void showRaportUI();

	void undoUI();
};