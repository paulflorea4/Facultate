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

#include "Cart.h"
#include "ActivityTableModel.h"
#include "Service.h"
#include "Observable.h"
#include <vector>
#include <string>

using std::vector;
using std::string;

class CartCrudGUI :public QWidget, Observer {
public:
    CartCrudGUI(Cart& cart) :cart{ cart } {
        cart.addObserver(this);
        initGUI();
        initConnect();
    }

    void update() override {
        loadData(cart.getAll());
    }
private:
    Cart& cart;
    QPushButton* addCartButton = new QPushButton{ "Adauga" };
    QPushButton* generateCartButton = new QPushButton{ "Genereaza" };
    QPushButton* emptyCartButton = new QPushButton{ "Goleste" };
    QPushButton* exportCartButton = new QPushButton{ "Salveaza" };
    QLineEdit* editTitle = new QLineEdit;
    QLineEdit* editGenerate = new QLineEdit;
    QLineEdit* editExport = new QLineEdit;

    QTableView* tableView = new QTableView{};
    ActivityTableModel* tableModel = new ActivityTableModel{};

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
            loadData(cart.getAll());
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
            loadData(cart.getAll());
            });

        QObject::connect(emptyCartButton, &QPushButton::clicked, [&]() {
            cart.empty();
            loadData(cart.getAll());
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
            });
    }

    void loadData(const vector<Activity>& activities) {
        tableModel->setActivities(activities);
    }

    void initGUI() {
        QHBoxLayout* mainCartLayout = new QHBoxLayout{};
        setLayout(mainCartLayout);
        resize(QSize{ 600, 300 });
        QVBoxLayout* leftBox = new QVBoxLayout{};
        QVBoxLayout* rightBox = new QVBoxLayout{};
        mainCartLayout->addLayout(leftBox);
        mainCartLayout->addLayout(rightBox);

        leftBox->addWidget(tableView);
        tableView->setModel(tableModel);

        auto titleRow = new QFormLayout{};
        titleRow->addRow("Titlu", editTitle);
        rightBox->addLayout(titleRow);
        rightBox->addWidget(addCartButton);
        rightBox->addWidget(emptyCartButton);
        QHBoxLayout* generateLayout = new QHBoxLayout{};
        QHBoxLayout* exportLayout = new QHBoxLayout{};
        rightBox->addLayout(generateLayout);
        rightBox->addLayout(exportLayout);
        rightBox->addStretch();
        generateLayout->addWidget(editGenerate);
        generateLayout->addWidget(generateCartButton);
        exportLayout->addWidget(editExport);
        exportLayout->addWidget(exportCartButton);

        setWindowTitle(QString::fromStdString("CosCrudGUI"));
    }
};