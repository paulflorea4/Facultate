#pragma once
#include "CartCrudGUI.h"
#include "CartReadOnlyGUI.h"

class ActivityGUI :public QWidget {
public:
    ActivityGUI(Service& service, ActivityList& cart) :service{ service }, cart{ cart } {
        initGUI();
        loadData();
        initConnect();
    }
private:
    Service& service;
    ActivityList& cart;

    vector<Activity> activities;
    vector<Activity> cartActivities;
    QListWidget* list = new QListWidget;

    QLineEdit* editId = new QLineEdit;
    QLineEdit* editTitle = new QLineEdit;
    QLineEdit* editDescription = new QLineEdit;
    QLineEdit* editType = new QLineEdit;
    QLineEdit* editDuration = new QLineEdit;

    QLineEdit* editGenerate = new QLineEdit;
    QLineEdit* editExport = new QLineEdit;

    QPushButton* addButton = new QPushButton{ "Adauga" };
    QPushButton* deleteButton = new QPushButton{ "Sterge" };
    QPushButton* updateButton = new QPushButton{ "Modifica" };
    QPushButton* findButton = new QPushButton{ "Cauta" };
    QPushButton* filterButton = new QPushButton{ "Filtrare" };
    QPushButton* sortButton = new QPushButton{ "Sortare" };
    QPushButton* undoButton = new QPushButton{ "Undo" };
    QPushButton* raportButton = new QPushButton{ "Raport" };

    QPushButton* addCartButton = new QPushButton{ "Adauga" };
    QPushButton* generateCartButton = new QPushButton{ "Genereaza" };
    QPushButton* emptyCartButton = new QPushButton{ "Goleste" };
    QPushButton* exportCartButton = new QPushButton{ "Salveaza" };
    

    CartCrudGUI* cartCrudWidget;
    QPushButton* cartCrudButton = new QPushButton{ "CosCrudGUI" };

    CartReadOnlyGUI* cartReadOnlyWidget;
    QPushButton* cartReadOnlyButton = new QPushButton{ "CartReadOnlyGUI" };


    void initConnect() {
        QObject::connect(addButton, &QPushButton::clicked, [&]() {
            auto id = editId->text().toInt();
            auto title = editTitle->text().toStdString();
            auto description = editDescription->text().toStdString();
            auto type = editType->text().toStdString();
            auto duration = editDuration->text().toInt();

            try {
                service.addActivity(id, title, description, type, duration);
            }
            catch (Exception& errorMessage) {
                QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
                return;
            }

            editId->clear();
            editTitle->clear();
            editDescription->clear();
            editType->clear();
            editDuration->clear();
            loadData();
            });

        QObject::connect(updateButton, &QPushButton::clicked, [&]() {
            auto id = editId->text().toInt();
            auto title = editTitle->text().toStdString();
            auto description = editDescription->text().toStdString();
            auto type = editType->text().toStdString();
            auto duration = editDuration->text().toInt();

            try {
                service.updateActivity(id, title, description, type, duration);
            }
            catch (Exception& errorMessage) {
                QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
                return;
            }

            editId->clear();
            editTitle->clear();
            editDescription->clear();
            editType->clear();
            editDuration->clear();
            loadData();
            });

        QObject::connect(deleteButton, &QPushButton::clicked, [&]() {
            auto id = editId->text().toInt();

            try {
                service.deleteActivity(id);
            }
            catch (Exception& errorMessage) {
                QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
                return;
            }

            editId->clear();
            editTitle->clear();
            editDescription->clear();
            editType->clear();
            editDuration->clear();
            loadData();
            });


        QObject::connect(findButton, &QPushButton::clicked, [&]() {
            auto id = editId->text().toInt();

            try {
                auto activity = service.findActivity(id);
                editTitle->setText(QString::fromStdString(activity.getTitle()));
                editDescription->setText(QString::fromStdString(activity.getDescription()));
                editType->setText(QString::fromStdString(activity.getType()));
                editDuration->setText(QString::fromStdString(std::to_string(activity.getDuration())));

            }
            catch (Exception& errorMessage) {
                QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
                return;
            }
            loadData();
            });

        QObject::connect(undoButton, &QPushButton::clicked, [&]() {
            try {
                service.undo();
            }
            catch (Exception& errorMessage) {
                QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
                return;
            }

            editId->clear();
            editTitle->clear();
            editDescription->clear();
            editType->clear();
            editDuration->clear();
            loadData();
            });

        QObject::connect(filterButton, &QPushButton::clicked, [&]() {
            QDialog* dialog = new QDialog(this);
            dialog->setWindowTitle("Filtrare");
            QHBoxLayout* dialogLayout = new QHBoxLayout(dialog);
            QPushButton* filterButtonDialog = new QPushButton{ "Filtrare" };

            QVBoxLayout* leftBox = new QVBoxLayout(dialog);
            QVBoxLayout* rightBox = new QVBoxLayout(dialog);

            QLineEdit* editFilter = new QLineEdit(dialog);
            QComboBox* filterOptions = new QComboBox(dialog);
            filterOptions->addItem("Descriere");
            filterOptions->addItem("Tip");
            QListWidget* filteredActivities = new QListWidget(dialog);
            QPushButton* okButton = new QPushButton("OK");

            QObject::connect(okButton, &QPushButton::clicked, dialog, &QDialog::accept);

            leftBox->addWidget(filteredActivities);
            rightBox->addWidget(filterOptions);
            rightBox->addWidget(editFilter);
            rightBox->addWidget(filterButtonDialog);
            rightBox->addWidget(okButton);
            dialogLayout->addLayout(leftBox);
            dialogLayout->addLayout(rightBox);
            vector<string> fields = { "description","type" };

            QObject::connect(filterButtonDialog, &QPushButton::clicked, [=]() {
                auto field = fields[filterOptions->currentIndex()];
                auto value = editFilter->text().toStdString();
                try {
                    auto activities = service.filterBy(field, value);
                    filteredActivities->clear();
                    for (const auto& activity : activities) {
                        QString item = QString::fromStdString(std::to_string(activity.getId()) + "," + activity.getTitle() + "," +
                            activity.getDescription() + "," + activity.getType() + "," + std::to_string(activity.getDuration()));
                        filteredActivities->addItem(item);
                    }
                }
                catch (Exception& errorMessage) {
                    QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
                }
                });
            dialog->show();
            });

        QObject::connect(sortButton, &QPushButton::clicked, [&]() {
            QDialog* dialog = new QDialog(this);
            dialog->setWindowTitle("Sortare");
            QHBoxLayout* dialogLayout = new QHBoxLayout(dialog);
            QPushButton* sortButtonDialog = new QPushButton{ "Sortare" };

            QVBoxLayout* leftBox = new QVBoxLayout(dialog);
            QVBoxLayout* rightBox = new QVBoxLayout(dialog);

            QComboBox* sortOptions = new QComboBox(dialog);
            sortOptions->addItem("Titlu");
            sortOptions->addItem("Descriere");
            sortOptions->addItem("Tip si durata");
            QListWidget* sortedActivities = new QListWidget(dialog);
            QPushButton* okButton = new QPushButton("OK");

            QObject::connect(okButton, &QPushButton::clicked, dialog, &QDialog::accept);

            leftBox->addWidget(sortedActivities);
            rightBox->addWidget(sortOptions);
            rightBox->addWidget(sortButtonDialog);
            rightBox->addWidget(okButton);
            dialogLayout->addLayout(leftBox);
            dialogLayout->addLayout(rightBox);

            QObject::connect(sortButtonDialog, &QPushButton::clicked, [=]() {
                try {
                    vector<Activity> result;
                    switch (sortOptions->currentIndex()) {
                    case 0:
                        result = service.sortBy(&Activity::compareByTitle);
                        break;
                    case 1:
                        result = service.sortBy(&Activity::compareByDescription);
                        break;
                    case 2:
                        result = service.sortBy(&Activity::compareByTypeAndDuration);
                        break;
                    }
                    sortedActivities->clear();
                    for (const auto& activity : result) {
                        QString item = QString::fromStdString(std::to_string(activity.getId()) + "," + activity.getTitle() + "," +
                            activity.getDescription() + "," + activity.getType() + "," + std::to_string(activity.getDuration()));
                        sortedActivities->addItem(item);
                    }
                }
                catch (Exception& errorMessage) {
                    QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
                }
                });
            dialog->show();
            loadData();
            });

        QObject::connect(raportButton, &QPushButton::clicked, [&]() {
            QDialog* dialog = new QDialog(this);
            dialog->setWindowTitle("Raport");
            QVBoxLayout* layout = new QVBoxLayout(dialog);
            QListWidget* raportList = new QListWidget(dialog);
            layout->addWidget(raportList);
            try {
                auto raports = service.createMap();
                for (const auto& raport : raports) {
                    QString item = QString::fromStdString(raport.second.getTitle() + " : " + std::to_string(raport.second.getFrequency()));
                    raportList->addItem(item);
                }
            }
            catch (Exception& errorMessage) {
                QMessageBox::warning(this, "Atentie", QString::fromStdString(errorMessage.what()));
            }
            dialog->exec();
            });

        QObject::connect(cartCrudButton, &QPushButton::clicked, [&]() {
            cartCrudWidget->show();
            });

        QObject::connect(cartReadOnlyButton, &QPushButton::clicked, [&]() {
            cartReadOnlyWidget->show();
            });

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
        list->clear();
        activities = service.getAll();

        for (const auto& activity : activities) {
            QWidget* itemWidget = new QWidget(list);
            QHBoxLayout* layout = new QHBoxLayout(itemWidget);

            QString text = QString::fromStdString(std::to_string(activity.getId()) + " | " + activity.getTitle() + " | " +
                activity.getDescription() + " | " + std::to_string(activity.getDuration()));
            QLabel* label = new QLabel(text);
            QPushButton* button = new QPushButton("Tip");
            button->setFixedSize(80, 20);
            QString type = QString::fromStdString(activity.getType());

            QObject::connect(button, &QPushButton::clicked, [type]() {
                QMessageBox::information(nullptr, "Tipul activitatii", "Tipul:" + type);
                });

            layout->addWidget(label);
            layout->addWidget(button);
            layout->setContentsMargins(4, 2, 4, 2);

            QListWidgetItem* item = new QListWidgetItem(list);
            item->setSizeHint(itemWidget->sizeHint());
            item->setData(Qt::UserRole, QString::fromStdString(activity.getTitle()));
            list->addItem(item);
            list->setItemWidget(item, itemWidget);
        }
    }

    void initGUI() {
        QHBoxLayout* mainBoxLayout = new QHBoxLayout{};
        setLayout(mainBoxLayout);
        resize(QSize{ 1000, 500 });
        QVBoxLayout* leftBox = new QVBoxLayout{};

        leftBox->addWidget(list);

        QVBoxLayout* rightBox = new QVBoxLayout{};

        auto informationLayout = new QFormLayout;
        informationLayout->addRow("Id", editId);
        informationLayout->addRow("Titlu", editTitle);
        informationLayout->addRow("Descriere", editDescription);
        informationLayout->addRow("Tip", editType);
        informationLayout->addRow("Durata", editDuration);

        QHBoxLayout* operationBox1 = new QHBoxLayout{};
        operationBox1->addWidget(addButton);
        operationBox1->addWidget(updateButton);
        operationBox1->addWidget(deleteButton);
        operationBox1->addWidget(undoButton);

        QHBoxLayout* operationBox2 = new QHBoxLayout{};
        operationBox2->addWidget(findButton);
        operationBox2->addWidget(filterButton);
        operationBox2->addWidget(sortButton);
        operationBox2->addWidget(raportButton);

        QVBoxLayout* operationBox3 = new QVBoxLayout{};
        operationBox3->addWidget(addCartButton);
        operationBox3->addWidget(emptyCartButton);
        QHBoxLayout* generateLayout = new QHBoxLayout{};
        QHBoxLayout* exportLayout = new QHBoxLayout{};
        operationBox3->addLayout(generateLayout);
        operationBox3->addLayout(exportLayout);
        generateLayout->addWidget(editGenerate);
        generateLayout->addWidget(generateCartButton);
        exportLayout->addWidget(editExport);
        exportLayout->addWidget(exportCartButton);

        QVBoxLayout* CartCrudLayout = new QVBoxLayout{};
        CartCrudLayout->addWidget(cartCrudButton,0,Qt::AlignRight);
        cartCrudButton->setMinimumSize(140, 40);
        cartCrudButton->setMaximumSize(170, 80);

        QVBoxLayout* CartReadOnlyLayout = new QVBoxLayout{};
        CartReadOnlyLayout->addWidget(cartReadOnlyButton, 0, Qt::AlignRight);
        cartReadOnlyButton->setMinimumSize(140, 40);
        cartReadOnlyButton->setMaximumSize(170, 80);

        rightBox->addLayout(informationLayout);
        rightBox->addLayout(operationBox1);
        rightBox->addLayout(operationBox2);
        rightBox->addWidget(new QLabel("Cos de activitati"),0,Qt::AlignCenter);
        rightBox->addLayout(operationBox3);
        rightBox->addStretch();
        rightBox->addLayout(CartCrudLayout);
        rightBox->addLayout(CartReadOnlyLayout);

        mainBoxLayout->addLayout(leftBox);
        mainBoxLayout->addLayout(rightBox);

        cartCrudWidget = new CartCrudGUI{ cart };
        cartReadOnlyWidget = new CartReadOnlyGUI{ cart };
    }
};
