#ifndef __FUNCTIONS_H_INCLUDED__
#define __FUNCTIONS_H_INCLUDED__ 

#include "function.h"

class Function1D : public Function {
private:
	Function &f;
	std::vector<double> x0;
	int pos;
public:
	Function1D(std::vector<double> _x, int _pos, Function &_f) : Function(0), x0(_x), pos(_pos), f(_f) {};
	double value(std::vector<double> &x);
};

class Function1 : public Function {
public:
	Function1(double _bias = 0) : Function(_bias) {};
	double value(std::vector<double> &x);
};

class Function2 : public Function {
public:
	Function2(double _bias = 0) : Function(_bias) {};
	double value(std::vector<double> &x);
};

class Function3 : public Function {
public:
	Function3(double _bias = 0) : Function(_bias) {};	
	double value(std::vector<double> &x);
};

class Function4 : public Function {
public:
	Function4(double _bias = 0) : Function(_bias) {};
	double value(std::vector<double> &x);
};

class Function5 : public Function {
public:
	Function5(double _bias = 0) : Function(_bias) {};
	double value(std::vector<double> &x);
};

#endif