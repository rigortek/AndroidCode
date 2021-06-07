#ifndef SHARED_PTR_H_
#define SHARED_PTR_H_

#include <memory>
#include <iostream>

using namespace std;

struct A {
    int a;
    
	A(int a) : a(a) {
	    cout << "constructor: " << a << endl;
	}
	
	~A() {
		cout << "destructor: " << a << endl;
	}
};

#if 1

// 智能指针最大的一个陷阱是循环引用，循环引用会导致内存泄漏。解决方法是改为weak_ptr。
// 循环引用-> SecondClass
class SecondClass;

class FirstClass {
  public:
    FirstClass() {}
    
	explicit FirstClass(int value) : mValue(value) {
	    cout << "constructor: value " << value << ", " << this << endl;
	}
	
	virtual ~FirstClass() {
		cout << "destructor: value " << mValue << ", " << this << endl;
	}
	
  private:
    int mValue;
	
  public:
    // std::shared_ptr<SecondClass> mSecondClassPtr;
    std::weak_ptr<SecondClass> mSecondClassPtr;  // weak_ptr替代shared_ptr，以解决循环依赖导致无法释放问题
};
#endif

#if 1
// 循环引用-> FirstClass

// class FirstClass {};

class SecondClass {
  public:
    SecondClass() {};
    
	explicit SecondClass(int value) : mValue(value) {
	    cout << "constructor: value " << value << ", " << this << endl;
	}
	
	virtual ~SecondClass() {
		cout << "destructor: value " << mValue << ", " << this << endl;
	}

  private:
    int mValue;
    
  public:
    //std::shared_ptr<FirstClass> mFirstClassPtr;
    std::weak_ptr<FirstClass> mFirstClassPtr;
};
#endif

#endif
