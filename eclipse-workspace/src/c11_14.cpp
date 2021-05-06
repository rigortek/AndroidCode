/*
 * c1114.cpp
 *
 *  Created on: 2021年5月5日
 *      Author: robin
 */

#include <iostream>
#include "c11_14.h"

c11_14::c11_14(int value) {
	// TODO Auto-generated constructor stub
	dataMember = value;
	std::cout<< "constructor " << this << ", " << dataMember << std::endl;

}

c11_14::~c11_14() {
	// TODO Auto-generated destructor stub
	std::cout<< "destructor " << this << ", " << dataMember << std::endl;
}

c11_14::c11_14(const c11_14 &other) {
	// TODO Auto-generated constructor stub

}

c11_14::c11_14(c11_14 &&other) {
	// TODO Auto-generated constructor stub
	std::cout<< "move constructor " << this << std::endl;
}

//c11_14& c11_14::operator=(const c11_14 &other) {
//	// TODO Auto-generated method stub
//
//}
//
//c11_14& c11_14::operator=(c11_14 &&other) {
//	// TODO Auto-generated method stub
//
//}

c11_14 c11_14::get() {
	c11_14 tmp2(2);
	std::cout << "tmp2: " << &tmp2 << std::endl;
	return tmp2;
}

