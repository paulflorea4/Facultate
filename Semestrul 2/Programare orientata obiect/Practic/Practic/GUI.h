#pragma once
#include "Service.h"
#include <qboxlayout.h>
#include <qpushbutton.h>
#include <qmessagebox.h>
#include <qlineedit.h>
#include <qtablewidget.h>
#include <qgridlayout.h>
#include <qformlayout.h>
#include "QtWidgets/qwidget.h"

class Fereastra :public QWidget, public Observer {
	Q_OBJECT
private:
	Service& service;
	Parcare p;
	QGridLayout* grid = new QGridLayout;
	QVector<QVector<QPushButton*>> butoane;
	QVBoxLayout* layoutMain = new QVBoxLayout;

	void initGUI() {
		setLayout(layoutMain);
	}
	void loadData() {
		if (grid != nullptr) {
			QLayoutItem* item;
			while ((item = grid->takeAt(0)) != nullptr) {
				delete item->widget();
				delete item;
			}
			delete grid;
			grid = nullptr;
		}

		grid = new QGridLayout;
		butoane.clear();
		butoane.resize(p.getLinii());
		string stare = p.getStare();
		int poz = 0;
		for (int i = 0; i < p.getLinii(); i++) {
			butoane[i].resize(p.getColoane());
			for (int j = 0; j < p.getColoane(); j++) {
				QPushButton* btn = new QPushButton();
				if (stare[poz] == 'X')
					btn->setText(QString::fromStdString("X"));
				else
					btn->setText(QString::fromStdString("-"));
				
				btn->setFixedSize(40, 40);
				grid->addWidget(btn, i, j);
				butoane[i][j] = btn;

				QObject::connect(butoane[i][j], &QPushButton::clicked, [=]() {
					string stareNoua = stare;
					if (stare[poz] == 'X') {
						stareNoua[poz] = '-';
						service.update(p.getAdresa(), p.getLinii(), p.getColoane(), stareNoua);
						p.setStare(stareNoua);
						butoane[i][j]->setText(QString::fromStdString("-"));
					}
					else
					{
						stareNoua[poz] = 'X';
						service.update(p.getAdresa(), p.getLinii(), p.getColoane(), stareNoua);
						butoane[i][j]->setText(QString::fromStdString("X"));
						p.setStare(stareNoua);
					}
					});
				poz++;
			}
		}
		layoutMain->addLayout(grid);
	}
public:
	Fereastra(Service& service, Parcare& p) :service{ service }, p{ p } {
		initGUI();
		loadData();
		service.addObserver(this);
	}

	void update() override {
		loadData();
	}
};

class GUI :public QWidget,public Observer{
	Q_OBJECT
private:
	Service& service;
	QLineEdit* adresa = new QLineEdit;
	QLineEdit* linii = new QLineEdit;
	QLineEdit* coloane = new QLineEdit;
	QLineEdit* stare = new QLineEdit;
	QPushButton* add = new QPushButton{ "Adauga" };
	QPushButton* modifica = new QPushButton{ "Modifica" };
	QPushButton* completeazaStare = new QPushButton{ "Completeaza Stare" };
	QPushButton* deschide = new QPushButton{ "Deschide fereastra" };
	QTableWidget* table = new QTableWidget{};

	void initGUI() {
		QVBoxLayout* layoutMain = new QVBoxLayout;
		setLayout(layoutMain);
		layoutMain->addWidget(table);
		table->setMinimumSize(450, 400);
		auto layoutDate = new QFormLayout;
		layoutDate->addRow("Adresa", adresa);
		layoutDate->addRow("Linii", linii);
		layoutDate->addRow("Coloane", coloane);
		layoutDate->addRow("Stare", stare);
		layoutMain->addLayout(layoutDate);
		QHBoxLayout* layoutButoane = new QHBoxLayout;
		layoutButoane->addWidget(add);
		layoutButoane->addWidget(modifica);
		layoutButoane->addWidget(completeazaStare);
		layoutMain->addLayout(layoutButoane);
		layoutMain->addWidget(deschide);
		table->setSelectionMode(QAbstractItemView::SingleSelection);
	}
	void loadData() {
		table->clear();
		table->setRowCount(service.getAll().size());
		table->setColumnCount(4);

		QStringList l = { "Adresa", "Linii", "Coloane", "Stare" };
		table->setHorizontalHeaderLabels(l);

		int row = 0;
		for (const auto& p : service.getAll()) {
			QTableWidgetItem* adresa = new QTableWidgetItem{};
			adresa->setText(QString::fromStdString(p.getAdresa()));

			QTableWidgetItem* linii = new QTableWidgetItem{};
			linii->setText(QString::number(p.getLinii()));

			QTableWidgetItem* coloane = new QTableWidgetItem{};
			coloane->setText(QString::number(p.getColoane()));

			QTableWidgetItem* stare = new QTableWidgetItem{};
			stare->setText(QString::fromStdString(p.getStare()));

			table->setItem(row, 0, adresa);
			table->setItem(row, 1, linii);
			table->setItem(row, 2, coloane);
			table->setItem(row, 3, stare);

		row++;
		}
	}
	void initConnect() {
		QObject::connect(add, &QPushButton::clicked, [&]() {
			service.add(adresa->text().toStdString(), linii->text().toInt(), coloane->text().toInt(), stare->text().toStdString());
			loadData();
			});

		QObject::connect(modifica, &QPushButton::clicked, [&]() {
			if (service.update(adresa->text().toStdString(), linii->text().toInt(), coloane->text().toInt(), stare->text().toStdString()) == 0)
				loadData();
			else
				QMessageBox::warning(this, "Eroare!", "Date invalide!");
			});

		QObject::connect(completeazaStare, &QPushButton::clicked, [&]() {
			string stareRandom = "";
			int liniiR = linii->text().toInt();
			int coloaneR = coloane->text().toInt();
			for (int i = 0; i < liniiR * coloaneR; i++) {
				int x = rand() % 10;
				if (x % 2 == 0)
					stareRandom += "X";
				else
					stareRandom += "-";
			}
			stare->setText(QString::fromStdString(stareRandom));
			});

		QObject::connect(deschide, &QPushButton::clicked, [&]() {
			if (table->selectionModel()->hasSelection()) {
				int index = table->selectionModel()->currentIndex().row();
				Parcare p = service.getAll().at(index);
				Fereastra* f1=new Fereastra{ service,p };
				Fereastra* f2=new Fereastra{ service,p };
				f1->show();
				f2->show();
			}
			});
	}

public:
	GUI(Service& service) :service{ service }{
		initGUI();
		loadData();
		initConnect();
		service.addObserver(this);
	}

	void update() override {
		loadData();
	}
};

