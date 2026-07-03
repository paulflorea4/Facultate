#pragma once
#include <QAbstractTableModel>
#include "Activity.h"
using namespace std;

#pragma once

class ActivityTableModel : public QAbstractTableModel
{
	Q_OBJECT
private:
	vector<Activity> activities;
public:
	ActivityTableModel(QObject* parent = nullptr) :
		QAbstractTableModel(parent) {
	}

	void setActivities(const vector<Activity>& activities) {
		emit layoutAboutToBeChanged();
		this->activities = activities;
		emit layoutChanged();
	}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override {
		return static_cast<int>(activities.size());
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override {
		return 4;
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override {
		if (role == Qt::DisplayRole) {
			const Activity& activity = activities[index.row()];
			switch (index.column()) {
			case 0: return QString::fromStdString(activity.getTitle());
			case 1: return QString::fromStdString(activity.getDescription());
			case 2: return QString::fromStdString(activity.getType());
			case 3: return QString::number(activity.getDuration());
			}
		}
		return {};
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override {
		if (role == Qt::DisplayRole && orientation == Qt::Horizontal) {
			switch (section) {
			case 0: return "Titlu";
			case 1: return "Descriere";
			case 2: return "Tip";
			case 3: return "Durata";
			}
		}
		return {};
	}
};

