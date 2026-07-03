#include "Practic.h"
#include <QtWidgets/QApplication>
#include "Teste.h"
#include "GUI.h"
int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    
    Repository repository{ "parcari.txt" };
    Service service{ repository };
    GUI window{ service };
    ruleazaTeste();
    window.show();
    return app.exec();
}
