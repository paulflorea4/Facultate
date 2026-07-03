#define _CRTDBG_MAP_ALLOC 
#include <stdio.h>
#include "tests.h"
#include "ui.h"
#include <crtdbg.h>


int main() {
	test_all();
	run();
	if (_CrtDumpMemoryLeaks()) {
		printf("Memory leaks found!\n");
	}
	else {
		printf("No memory leaks found!\n");
	}
}