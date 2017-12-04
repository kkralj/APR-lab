#ifndef __FUNCTIONS_H_INCLUDED__
#define __FUNCTIONS_H_INCLUDED__ 

#include "function.h"

class Function1 : public Function {
public:
	Function1(double _bias = 0) : Function(_bias) {};
	double value(std::vector<double> &x);
	std::vector<double> gradient_at(std::vector<double> &x);
	std::vector< std::vector<double> > hessian_at(std::vector<double> x);
};

class Function2 : public Function {
public:
	Function2(double _bias = 0) : Function(_bias) {};
	double value(std::vector<double> &x);
	std::vector<double> gradient_at(std::vector<double> &x);
	std::vector< std::vector<double> > hessian_at(std::vector<double> x);
};

class Function3 : public Function {
public:
	Function3(double _bias = 0) : Function(_bias) {};	
	double value(std::vector<double> &x);
	std::vector<double> gradient_at(std::vector<double> &x);
	std::vector< std::vector<double> > hessian_at(std::vector<double> x);
};

class Function4 : public Function {
public:
	Function4(double _bias = 0) : Function(_bias) {};
	double value(std::vector<double> &x);
	std::vector<double> gradient_at(std::vector<double> &x);
	std::vector< std::vector<double> > hessian_at(std::vector<double> x);
};

#endif