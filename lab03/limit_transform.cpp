#include <stdio.h>
#include <vector>
#include <assert.h>
#include <math.h>

#include "functions.h"

void print(std::vector<double> v) {
	for (int i = 0; i < v.size(); i++) {
		printf("%lf ", v[i]);
	}
	printf("\n");
}

bool hooke_jeeves_end(std::vector<double> &dx, std::vector<double> &eps) {
	for (int i = 0; i < dx.size(); i++) {
		if (dx[i] > eps[i]) return false;
	}
	return true;
}

std::vector<double> explore(std::vector<double> &xp, std::vector<double> &dx, Function &f, std::vector<Function*> &g, std::vector<Function*> &h, double r) {
	std::vector<double> x = xp;
	for (int i = 0; i < x.size(); i++) {
		double p = f.limit_value_at(x, r, g, h);
		x[i] += dx[i];
		double n = f.limit_value_at(x, r, g, h);
		if (n > p) {
			x[i] -= 2 * dx[i];
			n = f.limit_value_at(x, r, g, h);
			if (n > p) {
				x[i] += dx[i];
			}
		}
	}
	return x;
}

std::vector<double> hooke_jeeves_solver(std::vector<double> &x0, std::vector<double> dx, std::vector<double> eps, Function &f, std::vector<Function*> &g, std::vector<Function*> &h, double r) {
	std::vector<double> xp = x0, xb = x0, xn;

	while (!hooke_jeeves_end(dx, eps)) {
		xn = explore(xp, dx, f, g, h, r);
		double val_xn = f.limit_value_at(xn, r, g, h);
		double val_xb = f.limit_value_at(xb, r, g, h);

		if (val_xn < val_xb) {
			for (int i = 0; i < xp.size(); i++) {
				xp[i] = 2 * xn[i] - xb[i];
			}
			xb = xn;
		} else {
			for (int i = 0; i < dx.size(); i++) dx[i] /= 2;
			xp = xb;
		} 
	}

	return xb;
}

std::vector<double> hooke_jeeves(std::vector<double> &x0, Function &f, std::vector<Function*> &g, std::vector<Function*> &h, double r, double epsval = 1e-6) {
	std::vector<double> dx(x0.size());
	std::vector<double> eps(x0.size());
	
	for (int i = 0; i < x0.size(); i++) {
		dx[i] = 0.5;
		eps[i] = epsval;
	}
	
	return hooke_jeeves_solver(x0, dx, eps, f, g, h, r);
}

bool stop_condition(std::vector<double> &x1, std::vector<double> &x2, double eps = 1e-6) {
	assert(x1.size() == x2.size());
	
	for (int i = 0; i < x1.size(); i++) {
		if (abs(x1[i] - x2[i]) >= eps) return false;
	}
	
	return true;
}

bool valid_initial_point(std::vector<double> point, std::vector<Function*> &g) {
	for (int i = 0; i < g.size(); i++) {
		if (g[i]->value_at(point) < 0) {
			return false; 
		}
	}
	return true;
}

std::vector<double> limit_transform(std::vector<double> point, Function &f, std::vector<Function*> &g, std::vector<Function*> &h, double t = 1.0, double eps = 1e-6) {
	std::vector<double> x = point, xn(point.size());

	// find valid inner point
	if (!valid_initial_point(point, g)) {
		GLimitFunction glf;
		x = hooke_jeeves(x, glf, g, h, 1.0 / t, eps);
		printf("Found new point:\n");
		print(x);
	}
	
	int iter;
	for (iter = 1; iter <= 100; iter++, t *= 10) {
		xn = hooke_jeeves(x, f, g, h, 1.0 / t, eps);
		if (stop_condition(x, xn)) break;
		x = xn;
	}

	printf("Done after %d iterations.\n", iter);
	return x;
}

int main() {
	// Task 4.
	LimitFunction1 l1;
	LimitFunction2 l2;
	std::vector<Function*> g;
	g.push_back(&l1);
	g.push_back(&l2);
	std::vector<Function*> h;

	// f1	
	std::vector<double> x1;
	x1.push_back(-1.9);
	x1.push_back(2);
	Function1 f1;
	printf("Starting point: [%lf %lf]\n", x1[0], x1[1]);
	std::vector<double> sol1 = limit_transform(x1, f1, g, h);
	print(sol1);
	x1[0] = 1.5; x1[1] = 3;
	printf("Starting point: [%lf %lf]\n", x1[0], x1[1]);
	sol1 = limit_transform(x1, f1, g, h);
	print(sol1);
	printf("\n");
	

	// f2
	std::vector<double> x2;
	x2.push_back(0.1);
	x2.push_back(0.3);
	Function2 f2;
	printf("Starting point: [%lf %lf]\n", x2[0], x2[1]);
	std::vector<double> sol2 = limit_transform(x2, f2, g, h);
	print(sol2);
	printf("\n");

	// Task 5.
	LimitFunction3 l3; 
	LimitFunction4 l4;
	std::vector<Function*> g4;
	g4.push_back(&l3);
	g4.push_back(&l4);

	LimitFunction5 l5;
	std::vector<Function*> h4;
	h4.push_back(&l5);

	std::vector<double> x4;

	x4.push_back(5);
	x4.push_back(5);

	Function4 f4;
	printf("Starting point: [%lf %lf]\n", x4[0], x4[1]);
	std::vector<double> sol4 = limit_transform(x4, f4, g4, h4);
	print(sol4);

	return 0;
} 
