#define _CRTDBG_MAP_MALLOC
#include <iostream>
#include "Tests.h"
#include "UI.h"
using namespace std;

int main() {
	runAllTests();
	{
		RepositoryFile repository{ "activities.txt" };
		RepositoryRandom repositoryRandom;

		ActivityList list;
		Service service{ repository ,list};
		/*try {
			Service serviceRandom{ repositoryRandom,list };

			UI ui{ serviceRandom };
			ui.runUI();
		}
		catch (RepositoryRandomException& e) {
			cout << e.what()<<"\n\n";
		}*/
		
		UI ui{ service };
		ui.runUI();
	}
	if (_CrtDumpMemoryLeaks())
		cout << "Memory leak";
	else
		cout << "No memory leak";
	return 0;
}