//============================================================================
// Name        : c11_14.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>

#include "c11_14.h"

using namespace std;

int main() {
	c11_14 tmp1(1);
	cout << "tmp1: " << &tmp1 << endl;

//	c11_14 tmp3 = tmp1.get();
	c11_14 tmp3(move(tmp1));
	cout << "tmp3: " << &tmp3 << endl;

	return 0;
}
