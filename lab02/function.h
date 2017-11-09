#ifndef __FUNCTION_H_INCLUDED__
#define __FUNCTION_H_INCLUDED__  

#include <vector>

class Function {
private:
	int calls;
	double bias;
	virtual double value(std::vector<double> &x) = 0;
public:
	Function();
	Function(double _bias) : bias(_bias), calls(0) {};
	double value_at(std::vector<double> &x);
	double value_at(double x);
	void reset_counter();
	int get_call_count();
	double get_bias();
};

#endif 