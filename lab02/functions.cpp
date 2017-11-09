#include <cmath>
#include <assert.h>
#include <stdlib.h>
#include "functions.h"

#include <stdio.h>

double Function1D::value(std::vector<double> &x) {
	assert(x.size() == 1);
	std::vector<double> x1 = x0;
	x1[pos] += x[0];
	return f.value_at(x1);
}

double Function1::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return 100 * pow(x2 - x1 * x1, 2) + pow(1 - x1, 2);
}

double Function2::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return pow(x1 - 4, 2) + 4 * pow(x2 - 2, 2);
}

double Function3::value(std::vector<double> &x) {
	double val = 0;
	for (int i = 0; i < x.size(); i++) {
		val += pow(x[i] - i, 2);
	}
	return val;
}

double Function4::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return abs((x1 - x2) * (x1 + x2)) + sqrt(x1 * x1 + x2 * x2);
}

double Function5::value(std::vector<double> &x) {
	double sum_squares = 0;
	for (int i = 0; i < x.size(); i++) {
		sum_squares += x[i] * x[i]; 
	}
	return 0.5 + (sin(sum_squares) * sin(sum_squares) - 0.5) / pow(1 + 0.001 * sum_squares, 2);
}