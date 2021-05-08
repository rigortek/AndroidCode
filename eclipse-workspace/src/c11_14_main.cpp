//============================================================================
// Name        : c11_14.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include <utility>
#include <bitset>

#include "c11_14.h"

using namespace std;


// C++11 变参模板(variadic templates)
//重载的递归终止函数
void printX() {
	cout << "no arguments" << endl;
}

template<typename T,typename...Types>
void printX(const T& firstArg, const Types&...args) {
	cout << firstArg << endl;
	printX(args...);
}

template<typename...Types>
void printY(const Types&...args) {
	/***/
	cout << sizeof...(args) << endl;
}

int main() {
#if 0
	c11_14 tmp1(1, "first");
	cout << "tmp1: " << &tmp1 << endl;

//	c11_14 tmp3 = tmp1.get();
	c11_14 tmp3(move(tmp1));
	cout << "tmp3: " << &tmp3 << endl;
#endif

#if 0
	c11_14* pInstance = new c11_14(100, "string 100");
	c11_14 pInstance2(2, "string 200");
	pInstance2 = *pInstance;

	delete pInstance;
#endif

//	printX(7.5, "hello", bitset<16>(377), 42);
	printY(7.5, "hello", bitset<16>(377), 42);

	return 0;
}
