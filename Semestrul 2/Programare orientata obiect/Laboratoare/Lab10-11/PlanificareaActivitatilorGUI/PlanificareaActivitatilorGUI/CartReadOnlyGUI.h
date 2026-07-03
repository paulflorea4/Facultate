#pragma once
#include <QtWidgets/qwidget.h>
#include <QtWidgets/qboxlayout.h>
#include "ActivityList.h"
#include <qpainter.h>

class CartReadOnlyGUI :public QWidget, Observer {
public:
    CartReadOnlyGUI(ActivityList& cart) :cart{ cart } {
        cart.addObserver(this);
        initGUI();
    }

    void update() override {
        repaint();
    }
protected:
    void paintEvent(QPaintEvent* ev) override {
        Q_UNUSED(ev);
        QPainter painter(this);

        const int shapeWidth = 20;
        const int shapeHeight = 20;

        const auto& items = cart.getAll();

        for (size_t i = 0; i < items.size(); i++) {
            int x = rand() % (width() - shapeWidth);
            int y = rand() % (height() - shapeHeight);

            drawRandomShape(painter, x, y, shapeWidth, shapeHeight);
        }
    }

    void drawRandomShape(QPainter& painter, int x, int y, int width, int height) {
        int shapeType = rand() % 4;

        switch (shapeType) {
        case 0:
            painter.drawRect(x, y, width, height);
            break;
        case 1:
            painter.drawEllipse(x, y, width, height);
            break;
        case 2:
        {
            QPolygon triangle;
            triangle << QPoint(x, y + height)
                << QPoint(x, y)
                << QPoint(x + width, y + height);
            painter.drawPolygon(triangle);
            break;
        }
        case 3:
        {
            QPolygon diamond;
            diamond << QPoint(x + width / 2, y)
                << QPoint(x + width, y + height / 2)
                << QPoint(x + width / 2, y + height)
                << QPoint(x, y + height / 2);
            painter.drawPolygon(diamond);
            break;
        }
        }
    }
private:
    ActivityList& cart;

    void initGUI() {
        QHBoxLayout* mainCartLayout = new QHBoxLayout;
        setLayout(mainCartLayout);
        resize(QSize{ 400,300 });
        setWindowTitle("CartReadOnlyGUI");
    }
};