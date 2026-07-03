#pragma once
#include "Activity.h"
#include "Repository.h"
#include "Service.h"
#include <cassert>

void runDomainTests();

void runRepositoryTests();

void runRepositoryFileTests();

void runServiceTests();

void runAllTests();