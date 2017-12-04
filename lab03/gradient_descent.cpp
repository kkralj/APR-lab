#include <iostream>
#include <math.h>
#include <assert.h>

#include "function.h"
#include "functions.h"

using namespace std;

const double K = 0.5 * (sqrt(5) - 1);

std::vector<double> get_vector_point(std::vector<double> &x0, std::vector<double> &grad, double lambda) {
	assert(x0.size() == grad.size());
	std::vector<double> x = x0;

	for (int i = 0; i < x.size(); i++) {
		x[i] += lambda * grad[i];
	}

	return x;
}

double golden_section_search_interval(std::vector<double> &point, std::vector<double> &grad, double a, double b, Function &f, double eps = 1e-6) {
	double c = b - K * (b - a);
	double d = a + K * (b - a);

	double fc = f.value_at(get_vector_point(point, grad, c));
	double fd = f.value_at(get_vector_point(point, grad, d));

	while (b - a > eps) {
		if (fc < fd) {
			b = d;
			d = c;
			c = b - K * (b - a);
			fd = fc;
			fc = f.value_at(get_vector_point(point, grad, c));
		} else {
			a = c;
			c = d;
			d = a + K * (b - a);
			fc = fd;
			fd = f.value_at(get_vector_point(point, grad, d));
		}
	}

	// printf("Found best value: %lf\n", (a + b) / 2);
	return (a + b) / 2;
}

double golden_section_search_point(std::vector<double> &point, std::vector<double> &grad, Function &f, double h = 1, double eps = 1e-6) {
	double l = -h, m = 0, r = +h;

	double fl = f.value_at(get_vector_point(point, grad, l)); 
	double fm = f.value_at(get_vector_point(point, grad, m));
	double fr = f.value_at(get_vector_point(point, grad, r));

	unsigned int step = 1;

	if (fm > fr) {
		do {
			l = m;
			m = r;
			fm = fr;
			r = 0 + h * (step *= 2);
			fr = f.value_at(get_vector_point(point, grad, r));
		} while (fm > fr);
	} else {
		do {
			r = m;
			m = l;
			fm = fl;
			l = 0 - h * (step *= 2);
			fl = f.value_at(get_vector_point(point, grad, l));
		} while (fm > fl);
	}

	//printf("Found unimodal interval: [%lf, %lf]\n", l, r);
	return golden_section_search_interval(point, grad, l, r, f, eps);
}


double eucl_norm(std::vector<double> x) {
	double val = 0;
	for (int i = 0; i < x.size(); i++) {
		val += x[i] * x[i];
	}
	return sqrt(val);
}

void print(std::vector<double> &v) {
	for (int i = 0; i < v.size(); i++) {
		printf("%lf ", v[i]);
	}
	printf("\n");
}

std::vector<double> gradient_descent(std::vector<double> x0, Function &f, bool golden = false, double eps = 1e-6) {
	std::vector<double> grad, x = x0;
	int iter;

	for (iter = 1; iter <= 1e4 && eucl_norm((grad = f.gradient_at(x))) > eps; iter++) {
		double lambda = golden ? golden_section_search_point(x, grad, f) : -1;
		for (int i = 0; i < x.size(); i++) {
			x[i] += lambda * grad[i];
		}
		// print(x);
	}

	printf("Done after %d iterations.\n", iter);
	return x;
}

int main() {
	// f1
	Function1 f1;
	std::vector<double> x1;
	x1.push_back(-1.9);
	x1.push_back(2);
	std::vector<double> result1 = gradient_descent(x1, f1, true);
	print(result1);

	// f2
	Function2 f2;
	std::vector<double> x2;
	x2.push_back(0.1);
	x2.push_back(0.3);
	std::vector<double> result2 = gradient_descent(x2, f2, true);
	print(result2);

	// f3
	std::vector<double> x3;
	x3.push_back(0);
	x3.push_back(0);
	Function3 f3;
	std::vector<double> result3 = gradient_descent(x3, f3, true);
	print(result3);

    return 0;
}
