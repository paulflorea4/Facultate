/********************************************************************************
** Form generated from reading UI file 'PlanificareaActivitatilorGUI.ui'
**
** Created by: Qt User Interface Compiler version 6.9.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_PLANIFICAREAACTIVITATILORGUI_H
#define UI_PLANIFICAREAACTIVITATILORGUI_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_PlanificareaActivitatilorGUIClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *PlanificareaActivitatilorGUIClass)
    {
        if (PlanificareaActivitatilorGUIClass->objectName().isEmpty())
            PlanificareaActivitatilorGUIClass->setObjectName("PlanificareaActivitatilorGUIClass");
        PlanificareaActivitatilorGUIClass->resize(600, 400);
        menuBar = new QMenuBar(PlanificareaActivitatilorGUIClass);
        menuBar->setObjectName("menuBar");
        PlanificareaActivitatilorGUIClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(PlanificareaActivitatilorGUIClass);
        mainToolBar->setObjectName("mainToolBar");
        PlanificareaActivitatilorGUIClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(PlanificareaActivitatilorGUIClass);
        centralWidget->setObjectName("centralWidget");
        PlanificareaActivitatilorGUIClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(PlanificareaActivitatilorGUIClass);
        statusBar->setObjectName("statusBar");
        PlanificareaActivitatilorGUIClass->setStatusBar(statusBar);

        retranslateUi(PlanificareaActivitatilorGUIClass);

        QMetaObject::connectSlotsByName(PlanificareaActivitatilorGUIClass);
    } // setupUi

    void retranslateUi(QMainWindow *PlanificareaActivitatilorGUIClass)
    {
        PlanificareaActivitatilorGUIClass->setWindowTitle(QCoreApplication::translate("PlanificareaActivitatilorGUIClass", "PlanificareaActivitatilorGUI", nullptr));
    } // retranslateUi

};

namespace Ui {
    class PlanificareaActivitatilorGUIClass: public Ui_PlanificareaActivitatilorGUIClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_PLANIFICAREAACTIVITATILORGUI_H
