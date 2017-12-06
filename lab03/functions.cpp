#include <cmath>
#include <assert.h>
#include <stdlib.h>
#include "functions.h"

#include <stdio.h>

double Function1::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return 100 * pow(x2 - x1 * x1, 2) + pow(1 - x1, 2);
}

std::vector<double> Function1::gradient_at(std::vector<double> &x) {
	assert(x.size() == 2);
	std::vector<double> result(2);

	double x1 = x[0], x2 = x[1];
	result[0] = -400 * (x2 - x1 * x1) * x1 - 2 * (1 - x1);
	result[1] = 200 * (x2 - x1 * x1);

	return result;
}

std::vector< std::vector<double> > Function1::hessian_at(std::vector<double> x) {
	assert(x.size() == 2);
	std::vector< std::vector<double> > h(2);

	double x1 = x[0], x2 = x[1];

	h[0].push_back(-400 * (x2 - 3 * x1 * x1) + 2);
	h[0].push_back(-400 * x1);

	h[1].push_back(-400 * x1);
	h[1].push_back(200);

	return h;
}

double Function2::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return pow(x1 - 4, 2) + 4 * pow(x2 - 2, 2);
}

std::vector<double> Function2::gradient_at(std::vector<double> &x) {
	assert(x.size() == 2);
	std::vector<double> result(2);

	double x1 = x[0], x2 = x[1];
	result[0] = 2 * (x1 - 4);
	result[1] = 8 * (x2 - 2);

	return result;
}

std::vector< std::vector<double> > Function2::hessian_at(std::vector<double> x) {
	assert(x.size() == 2);
	std::vector< std::vector<double> > h(2);

	double x1 = x[0], x2 = x[1];

	h[0].push_back(2);
	h[0].push_back(0);

	h[1].push_back(0);
	h[1].push_back(8);

	return h;
}

double Function3::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return pow(x1 - 2, 2) + pow(x2 + 3, 2);
}

std::vector<double> Function3::gradient_at(std::vector<double> &x) {
	assert(x.size() == 2);
	std::vector<double> result(2);

	double x1 = x[0], x2 = x[1];
	result[0] = 2 * (x1 - 2);
	result[1] = 2 * (x2 + 3);

	return result;
}

std::vector< std::vector<double> > Function3::hessian_at(std::vector<double> x) {
	assert(x.size() == 2);
	std::vector< std::vector<double> > h(2);

	double x1 = x[0], x2 = x[1];

	h[0].push_back(2);
	h[0].push_back(0);

	h[1].push_back(0);
	h[1].push_back(2);

	return h;
}

double Function4::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];

	return pow(x1 - 3, 2) + x2 * x2;
}

std::vector<double> Function4::gradient_at(std::vector<double> &x) {
	assert(x.size() == 2);
	std::vector<double> result(2);

	double x1 = x[0], x2 = x[1];
	result[0] = 2 * (x1 - 3);
	result[1] = 2 * x2;

	return result;
}

std::vector< std::vector<double> > Function4::hessian_at(std::vector<double> x) {
	assert(x.size() == 2);
	std::vector< std::vector<double> > h(2);

	double x1 = x[0], x2 = x[1];

	h[0].push_back(2);
	h[0].push_back(0);

	h[1].push_back(0);
	h[1].push_back(2);

	return h;
}

double LimitFunction1::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return x2 - x1;
}

std::vector<double> LimitFunction1::gradient_at(std::vector<double> &x) {
	return std::vector<double>(0);
}

std::vector< std::vector<double> > LimitFunction1::hessian_at(std::vector<double> x) {
	return std::vector< std::vector<double> >(0);
}

double LimitFunction2::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return 2 - x1;
}

std::vector<double> LimitFunction2::gradient_at(std::vector<double> &x) {
	return std::vector<double>(0);
}

std::vector< std::vector<double> > LimitFunction2::hessian_at(std::vector<double> x) {
	return std::vector< std::vector<double> >(0);
}

double LimitFunction3::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return 3 - x1 - x2;
}

std::vector<double> LimitFunction3::gradient_at(std::vector<double> &x) {
	return std::vector<double>(0);
}

std::vector< std::vector<double> > LimitFunction3::hessian_at(std::vector<double> x) {
	return std::vector< std::vector<double> >(0);
}

double LimitFunction4::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return 3 + 1.5 * x1 - x2;
}

std::vector<double> LimitFunction4::gradient_at(std::vector<double> &x) {
	return std::vector<double>(0);
}

std::vector< std::vector<double> > LimitFunction4::hessian_at(std::vector<double> x) {
	return std::vector< std::vector<double> >(0);
}

double LimitFunction5::value(std::vector<double> &x) {
	assert(x.size() == 2);
	double x1 = x[0], x2 = x[1];
	return x2 - 1;
}

std::vector<double> LimitFunction5::gradient_at(std::vector<double> &x) {
	return std::vector<double>(0);
}

std::vector< std::vector<double> > LimitFunction5::hessian_at(std::vector<double> x) {
	return std::vector< std::vector<double> >(0);
}