#include "function.h"
#include <stdio.h>

Function::Function() {
	calls = bias = 0;
}

double Function::value_at(double x) {
	std::vector<double> values; 
	values.push_back(x);
	return this->value_at(values);
}

double Function::value_at(std::vector<double> &x) {
	calls++;
	std::vector<double> v(x.size());
	for (int i = 0; i < x.size(); i++) {
		v[i] = x[i] + bias;
	}
	return this->value(v);
}

int Function::get_call_count() {
	return calls;
}

void Function::reset_counter() {
	calls = 0;
}

double Function::get_bias() {
	return bias;
}
