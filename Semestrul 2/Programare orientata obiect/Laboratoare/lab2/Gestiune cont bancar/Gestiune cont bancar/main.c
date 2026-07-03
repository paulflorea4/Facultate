#include <stdio.h>
#include "Repository.h"
#include "Service.h"
#include "UI.h"
#include "tests.h"

int main() 
{
	runTests();

	TransactionsService service = createTransactionsService();
	runApp(&service);
	return 0;
}