#include "PlanificareaActivitatilorGUI.h"
#include <QtGlobal>
#include <vector>
#include <string>
#include "ActivityGUI.h"
#define Q_CHECK_MEMORY_LEAKS

using std::vector;
using std::string;

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Repository repository;
    RepositoryFile repositoryFile{ "activities.txt" };
    Service service{ repositoryFile };
    ActivityList activityList{ service };
    ActivityGUI gui{ service ,activityList};
    gui.show();
    Q_CHECK_MEMORY_LEAKS;
    return a.exec();
}
