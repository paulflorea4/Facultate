#define _CRTDBG_MAP_ALLOC 
#include <stdio.h>
#include "Repository.h"
#include "Service.h"
#include "UI.h"
#include "Tests.h"
#include <crtdbg.h>

int main()
{
	runTests();

	runApp();
	_CrtDumpMemoryLeaks();

	if (_CrtDumpMemoryLeaks()) {
		printf("Memory leaks found!\n");
	}
	else {
		printf("No memory leaks found!\n");
	}
	return 0;
}