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
#include "SharedPtrSample.h"

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

int sharedPtrTest() {
	  shared_ptr<A> a(new A(1));
	  shared_ptr<A> b(a), c(a), d(a);

	  cout << "a: " << a->a << "\tb: " << b->a
	     << "\tc: " << c->a << "\td: " << d->a << endl;

	  // reset之后，不会改变a,b,c的值
	  d.reset(new A(10));

	  cout << "a: " << a->a << "\tb: " << b->a
	     << "\tc: " << c->a << "\td: " << d->a << endl;

	  return 0;
}

void testCyclicSharedPtr() {
//	FirstClass* pFirst = new FirstClass(1);
//	SecondClass* pSecond = new SecondClass(2);

	// 如果直接传递参数值1，某些平台上会触发编译错误-》 error: no matching function for call to 'make_shared'
	// std::shared_ptr<FirstClass> first = std::make_shared<FirstClass>(1);
	int firstValue = 1;
	std::shared_ptr<FirstClass> first = std::make_shared<FirstClass>(firstValue);
	cout << "----------------------->1" << endl;
	// std::shared_ptr<SecondClass> second = std::make_shared<SecondClass>(2);  // error: no matching function for call to 'make_shared'
	int secondValue = 2;
	std::shared_ptr<SecondClass> second = std::make_shared<SecondClass>(secondValue);
	cout << "----------------------->2" << endl;

	first->mSecondClassPtr = second;
	cout << "----------------------->3" << endl;
	second->mFirstClassPtr = first;
	cout << "----------------------->4" << endl;
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

#if 0
//	printX(7.5, "hello", bitset<16>(377), 42);
	printY(7.5, "hello", bitset<16>(377), 42);
#endif

//	sharedPtrTest();

	testCyclicSharedPtr();

	return 0;
}
