/*
 * c1114.cpp
 *
 *  Created on: 2021年5月5日
 *      Author: robin
 */

#include <iostream>
#include <string.h>
#include "c11_14.h"

c11_14::c11_14()
: dataMember(-1), pBuffer(NULL) {

}
c11_14::c11_14(int value, char* pValue) {
	// TODO Auto-generated constructor stub
	dataMember = value;
	pBuffer = new char[strlen(pValue) + 1];
	memset(pBuffer, 0, strlen(pValue));
	strncpy(pBuffer, pValue, strlen(pValue));
	std::cout<< "constructor " << this << ", " << dataMember << ", " << pBuffer << std::endl;
}
//10.27.65.12

c11_14::~c11_14() {
	// TODO Auto-generated destructor stub
	std::cout<< "destructor " << this << ", " << dataMember << ", " << pBuffer << std::endl;
	if (NULL != pBuffer) {
		delete pBuffer;
		pBuffer = NULL;
	}
}

c11_14::c11_14(const c11_14 &other) {
	// TODO Auto-generated constructor stub
	dataMember = other.dataMember;
	pBuffer = new char[strlen(other.pBuffer) + 1];
	memset(pBuffer, 0, strlen(other.pBuffer));
	strncpy(pBuffer, other.pBuffer, strlen(other.pBuffer));
	std::cout<< "copy constructor " << this << ", " << dataMember << ", " << pBuffer << std::endl;
}

//c11_14::c11_14(c11_14 &&other) {
//	// TODO Auto-generated constructor stub
//	dataMember = other.dataMember;
//	std::cout<< "move constructor " << this << std::endl;
//}

c11_14& c11_14::operator=(const c11_14 &other) {
	// TODO Auto-generated method stub
#if 1
	dataMember = other.dataMember;
	pBuffer = new char[strlen(other.pBuffer) + 1];
	memset(pBuffer, 0, strlen(other.pBuffer));
	strncpy(pBuffer, other.pBuffer, strlen(other.pBuffer));
#else
//	dataMember = other.dataMember;
//	pBuffer = other.pBuffer;
#endif
	std::cout<< "Assign operator= " << this << ", " << dataMember << ", " << pBuffer << std::endl;
}

//c11_14& c11_14::operator=(c11_14 &&other) {
//	// TODO Auto-generated method stub
//
//}

c11_14 c11_14::get() {
	c11_14 tmp2(2, "second");
	std::cout << "tmp2: " << &tmp2 << std::endl;
	return tmp2;
}

