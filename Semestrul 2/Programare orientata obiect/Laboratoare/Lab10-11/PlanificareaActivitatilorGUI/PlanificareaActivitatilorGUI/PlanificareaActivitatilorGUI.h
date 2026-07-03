#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_PlanificareaActivitatilorGUI.h"

class PlanificareaActivitatilorGUI : public QMainWindow
{
    Q_OBJECT

public:
    PlanificareaActivitatilorGUI(QWidget *parent = nullptr);
    ~PlanificareaActivitatilorGUI();

private:
    Ui::PlanificareaActivitatilorGUIClass ui;
};
