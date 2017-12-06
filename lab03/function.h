#ifndef __FUNCTION_H_INCLUDED__
#define __FUNCTION_H_INCLUDED__  

#include <vector>

class Function {
private:
	int calls, gradient_calls, hessian_calls;
	double bias;
	virtual double value(std::vector<double> &x) = 0;
public:
	Function();
	Function(double _bias) : bias(_bias), calls(0), gradient_calls(0), hessian_calls(0) {};
	
	virtual std::vector<double> gradient_at(std::vector<double> &x) = 0;
	virtual std::vector< std::vector<double> > hessian_at(std::vector<double> x) = 0;
	
	double value_at(std::vector<double> x);
	double value_at(double x);
	virtual double limit_value_at(std::vector<double> &x, double t, std::vector<Function*> &g, std::vector<Function*> &h);
	
	int get_call_count();
	void reset_counter();
	
	void gradient_called();
	int get_gradient_calls();
	
	void hessian_called();
	int get_hessian_calls();
	
	double get_bias();
};

#endif 
