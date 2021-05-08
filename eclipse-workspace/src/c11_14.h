/*
 * c1114.h
 *
 *  Created on: 2021年5月5日
 *      Author: robin
 */

#ifndef C11_14_H_
#define C11_14_H_

// CDT下载URL
//https://www.eclipse.org/cdt/downloads.php

class c11_14 {
public:
	c11_14();
	c11_14(int value, char* pValue);
	virtual ~c11_14();
	c11_14(const c11_14 &other);
//	c11_14(c11_14 &&other);  // move拷贝构造函数
	c11_14& operator=(const c11_14 &other);
//	c11_14& operator=(c11_14 &&other); // move赋值构造函数

	c11_14 get();

private:
	int dataMember;

	char* pBuffer;
};

#endif /* C11_14_H_ */
