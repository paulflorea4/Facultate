#pragma once
#include <QtWidgets/qwidget.h>
#include <QtWidgets/QApplication>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qformlayout.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qlistwidget.h>
#include <QtWidgets/qmessagebox.h>
#include <QtWidgets/qcombobox.h>
#include <QtWidgets/qtablewidget.h>
#include <QtWidgets/qheaderview.h>

#include "ActivityList.h"
#include "Service.h"
#include "Observable.h"
#include <vector>
#include <string>

using std::vector;
using std::string;

class CartCrudGUI :public QWidget, Observer {
public:
    CartCrudGUI(ActivityList& cart) :cart{ cart } {
        cart.addObserver(this);
        initGUI();
        initConnect();
    }

    void update() override {
        loadData();
    }
private:
    ActivityList& cart;
    QTableWidget* cartTable = new QTableWidget{ 0,4 };
    QPushButton* addCartButton = new QPushButton{ "Adauga" };
    QPushButton* generateCartButton = new QPushButton{ "Genereaza" };
    QPushButton* emptyCartButton = new QPushButton{ "Goleste" };
    QPushButton* exportCartButton = new QPushButton{ "Salveaza" };
    QLineEdit* editTitle = new QLineEdit;
    QLineEdit* editGenerate = new QLineEdit;
    QLineEdit* editExport = new QLineEdit;

    void initConnect() {
        QObject::connect(addCartButton, &QPushButton::clicked, [&]() {
            string title = editTitle->text().toStdString();
            try {
                cart.add(title);
            }
            catch (Exception& errorMessage) {
                QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
            }
            editTitle->clear();
            loadData();
            });

        QObject::connect(generateCartButton, &QPushButton::clicked, [&]() {
            int number = editGenerate->text().toInt();
            try {
                cart.generateActivities(number);
            }
            catch (Exception& errorMessage) {
                QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
            }
            editGenerate->clear();
            loadData();
            });

        QObject::connect(emptyCartButton, &QPushButton::clicked, [&]() {
            cart.empty();
            loadData();
            QMessageBox::information(this, "Info", "Cosul a fost golit!");
            });

        QObject::connect(exportCartButton, &QPushButton::clicked, [&]() {
            string title = editExport->text().toStdString();
            try {
                cart.exportCSV(title);
            }
            catch (Exception& errorMessage) {
                QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
            }
            editExport->clear();
            loadData();
            });
    }

    void loadData() {
        cartTable->clearContents();
        cartTable->setRowCount(0);

        cartTable->setHorizontalHeaderLabels({ "Titlu","Descriere","Tip","Durata" });
        auto cartActivities = cart.getAll();
        for (const auto& activity : cartActivities) {
            int row = cartTable->rowCount();
            cartTable->insertRow(row);

            cartTable->setItem(row, 0, new QTableWidgetItem(QString::fromStdString(activity.getTitle())));
            cartTable->setItem(row, 1, new QTableWidgetItem(QString::fromStdString(activity.getDescription())));
            cartTable->setItem(row, 2, new QTableWidgetItem(QString::fromStdString(activity.getType())));
            cartTable->setItem(row, 3, new QTableWidgetItem(QString::number(activity.getDuration())));
        }
    }

    void initGUI() {
        QHBoxLayout* mainCartLayout = new QHBoxLayout{};
        setLayout(mainCartLayout);
        resize(QSize{ 600, 300 });
        QVBoxLayout* leftBox = new QVBoxLayout{};
        QVBoxLayout* rightBox = new QVBoxLayout{};
        mainCartLayout->addLayout(leftBox);
        mainCartLayout->addLayout(rightBox);

        cartTable->setHorizontalHeaderLabels({ "Titlu", "Descriere", "Tip", "Durata" });
        cartTable->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
        leftBox->addWidget(cartTable);

        auto titleRow = new QFormLayout{};
        titleRow->addRow("Titlu", editTitle);
        rightBox->addLayout(titleRow);
        rightBox->addWidget(addCartButton);
        rightBox->addWidget(emptyCartButton);
        QHBoxLayout* generateLayout = new QHBoxLayout{};
        QHBoxLayout* exportLayout = new QHBoxLayout{};
        rightBox->addLayout(generateLayout);
        rightBox->addLayout(exportLayout);
        generateLayout->addWidget(editGenerate);
        generateLayout->addWidget(generateCartButton);
        exportLayout->addWidget(editExport);
        exportLayout->addWidget(exportCartButton);

        setWindowTitle(QString::fromStdString("CosCrudGUI"));
    }
};