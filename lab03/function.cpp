#include "function.h"
#include <stdio.h>
#include <math.h>
#include <limits>

const double INF = std::numeric_limits<double>::max();

Function::Function() {
	calls = 0;
	bias = 0;
	gradient_calls = 0;
	hessian_calls = 0;
}

double Function::value_at(double x) {
	std::vector<double> values; 
	values.push_back(x);
	return this->value_at(values);
}

double Function::value_at(std::vector<double> x) {
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
	calls = 0; gradient_calls = 0;
}

void Function::gradient_called() {
	gradient_calls += 1;
}

int Function::get_gradient_calls() {
	return gradient_calls;
}

void Function::hessian_called() {
	hessian_calls += 1;
}

int Function::get_hessian_calls() {
	return hessian_calls;
}

double Function::get_bias() {
	return bias;
}

double Function::limit_value_at(std::vector<double> &x, double r, std::vector<Function*> &g, std::vector<Function*> &h) {
	double g_vals_sum = 0, g_fn_val;

	for (int i = 0; i < g.size(); i++) {
		g_fn_val = g[i]->value_at(x);
		if (g_fn_val <= 0) return INF;
		g_vals_sum += log(g_fn_val);
	}

	double h_squared_sum = 0, h_val;

	for (int i = 0; i < h.size(); i++) {
		h_val = h[i]->value_at(x);
		h_squared_sum += h_val * h_val;
	}

	return this->value_at(x) - r * g_vals_sum + (1.0 / r) * h_squared_sum;
}

