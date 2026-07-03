#pragma once

#include <string>
using std::string;

class Exception {
protected:
	string errorMessage;

public:
	explicit Exception(string message):errorMessage{message}{}

	virtual const char* what() const noexcept
	{
		return errorMessage.c_str();
	}

	virtual ~Exception() = default;
	
};