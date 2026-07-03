#pragma once
#include "Activity.h"
#include <QAbstractListModel>
using std::vector;

string activityToString(const Activity& activity);

class ActivityListModel :public QAbstractListModel {
	Q_OBJECT
private:
	vector<Activity> activities;
public:
	ActivityListModel(QObject* parent = nullptr) :QAbstractListModel(parent) {}

	void setActivities(const vector<Activity>& activities) {
		emit layoutAboutToBeChanged();
		this->activities = activities;
		emit layoutChanged();
	}

	const Activity& getActivity(int index) {
		return activities[index];
	}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override {
		return static_cast<int>(activities.size());
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override {
		if (!index.isValid() || index.row() > activities.size())
			return {};

		const Activity& activity = activities[index.row()];

		if (role == Qt::DisplayRole) {
			return QString::fromStdString(activityToString(activity));
		}

		return {};
	}
};