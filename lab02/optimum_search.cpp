#include <stdio.h>
#include <cmath>
#include <assert.h>
#include <stdlib.h>
#include <time.h>   

#include "functions.h"

// Golden ratio search

const double K = 0.5 * (sqrt(5) - 1);

double golden_section_search_interval(double a, double b, Function &f, double eps = 1e-6) {
	double c = b - K * (b - a);
	double d = a + K * (b - a);

	double fc = f.value_at(c);
	double fd = f.value_at(d);

	while (b - a > eps) {
		if (fc < fd) {
			b = d;
			d = c;
			c = b - K * (b - a);
			fd = fc;
			fc = f.value_at(c);
		} else {
			a = c;
			c = d;
			d = a + K * (b - a);
			fc = fd;
			fd = f.value_at(d);
		}
	}

	return (a + b) / 2;
}

double golden_section_search_point(double point, double h, Function &f,  double eps = 1e-6) {
	double l = point - h, m = point, r = point + h;

	double fl = f.value_at(l); 
	double fm = f.value_at(point);
	double fr = f.value_at(r);
	
	unsigned int step = 1;

	if (fm > fr) {
		do {
			l = m;
			m = r;
			fm = fr;
			r = point + h * (step *= 2);
			fr = f.value_at(r);
		} while (fm > fr);
	} else {
		do {
			r = m;
			m = l;
			fm = fl;
			l = point - h * (step *= 2);
			fl = f.value_at(l);
		} while (fm > fl);
	}

	// printf("Found unimodal interval: [%lf, %lf]\n", l, r);
	return golden_section_search_interval(l, r, f, eps);
}

void print_golden_ratio_search(Function &f, double result) {
	printf("Golden-ratio search done after %d calls.\n", f.get_call_count());
	printf("Optimum point: [ %lf ]\n", result);
	f.reset_counter();
}

// Coordinate axis search

double l2_norm_compare(std::vector<double> &v1, std::vector<double> &v2, double eps) {
	assert(v1.size() == v2.size());
	double total = 0;
	for (int i = 0; i < v1.size(); i++) {
		total += (v1[i] - v2[i]) * (v1[i] - v2[i]);
	}
	return sqrt(total) < eps;
}

std::vector<double> coordinate_axis_search(std::vector<double> &x0, Function &f, double eps = 1e-6) {
	std::vector<double> x = x0, xs;

	do {
		xs = x;
		for (int i = 0; i < x.size(); i++) {
			Function1D fun(x, i, f);
			x[i] = x[i] + golden_section_search_point(0, eps, fun);
		}
	} while (!l2_norm_compare(x, xs, eps));

	return x;
}

void print_coordinate_axis_search(Function &f, std::vector<double> result) {
	printf("Coordinate-axis search done after %d calls.\n", f.get_call_count());
	printf("Optimum point: [ ");
	for (int i = 0; i < result.size(); i++) {
		printf("%f ", result[i]);
	}
	printf("]\n");
	f.reset_counter();
}

// Nelder & Mead

std::pair<int, int> get_best_and_worst(std::vector<std::vector<double>> &X, Function &f) {
	assert(X.size() >= 1);
	int N = X.size();

	int pos_best = 0, pos_worst = 0;

	double val_best = f.value_at(X[0]);
	double val_worst = val_best;

	for (int i = 1; i < N; i++) {
		double val = f.value_at(X[i]);
		if (val < val_best) { val_best = val; pos_best = i; }
		if (val > val_worst) { val_worst = val;	pos_worst = i; }
	}

	return std::make_pair(pos_best, pos_worst);
}

std::vector<double> get_centroid(std::vector<std::vector<double>> &X, int h) {
	assert(X.size() > 1);

	int N = X.size(), n = X[0].size();
	std::vector<double> centroid(n, 0);

	for (int i = 0; i < N; i++) {
		if (i == h) continue;
		for (int j = 0; j < n; j++) {
			centroid[j] += X[i][j];
		}
	}

	for (int i = 0; i < n; i++) {
		centroid[i] /= (N - 1);
	}

	return centroid;
}

bool simplex_stop_condition(std::vector<std::vector<double>> &X, Function &f, double eps, int h) {
	std::vector<double> xc = get_centroid(X, h);
	double total = 0, val_xc = f.value_at(xc);

	for (int i = 0; i < X.size(); i++) {
		total += pow(f.value_at(X[i]) - val_xc, 2);
	}
	total /= X.size();

	//printf("Stop condition error: %f\n", total);
	return total < eps;
}

std::vector<double> reflexion(std::vector<double> &xc, std::vector<double> &xh, double alpha) {
	assert(xc.size() == xh.size());
	std::vector<double> xr(xc.size());
	
	for (int i = 0; i < xc.size(); i++) {
		xr[i] = (1 + alpha) * xc[i] - alpha * xh[i];
	}
	
	return xr;
}

std::vector<double> expansion(std::vector<double> &xc, std::vector<double> &xr, double gamma) {
	assert(xc.size() == xr.size());
	std::vector<double> xe(xc.size());

	for (int i = 0; i < xc.size(); i++) {
		xe[i] = (1 - gamma) * xc[i] + gamma * xr[i];
	}

	return xe;
}

std::vector<double> contraction(std::vector<double> &xc, std::vector<double> &xh, double beta) {
	assert(xc.size() == xh.size());
	std::vector<double> xk(xc.size());

	for (int i = 0; i < xc.size(); i++) {
		xk[i] = (1 - beta) * xc[i] + beta * xh[i];
	}

	return xk;
}

std::vector<double> nelder_mead_simplex(std::vector<double> &x0, double dx, Function &f, double alpha = 1, double beta = 0.5, double gamma = 2, double sigma = 0.5, double eps = 1e-6) {
	std::vector<std::vector<double>> X;

	X.push_back(x0);
	for (int i = 0; i < x0.size(); i++) {
		x0[i] += dx;
		X.push_back(x0);
		x0[i] -= dx;
	}

	std::vector<double> xc, xr, xe, xk;
	std::pair<int, int> lh = get_best_and_worst(X, f);

	while (f.get_call_count() < 15000 && !simplex_stop_condition(X, f, eps, lh.second)) {
		int l = lh.first, h = lh.second; 

		xc = get_centroid(X, h);
		xr = reflexion(xc, X[h], alpha);

		/*
		printf("Centroid point: [ ");
		for (int i = 0; i < xc.size(); i++) {
			printf("%f ", xc[i]);
		}
		printf("]\n");
		*/

		double xr_val = f.value_at(xr);
		double xl_val = f.value_at(X[l]);

		if (xr_val < xl_val) {
			xe = expansion(xc, xr, gamma);
			if (f.value_at(xe) - xl_val < eps) {
				X[h] = xe;
			} else {
				X[h] = xr;
			}
		} else {
			bool flag = true;
			for (int i = 0; i < X.size() && flag; i++) {
				if (i == h) continue;
				flag &= (xr_val - f.value_at(X[i]) > eps);
			}

			if (flag) {
				if (xr_val - f.value_at(X[h]) < eps) {
					X[h] = xr;
				}
				xk = contraction(xc, X[h], beta);
				if (f.value_at(xk) - f.value_at(X[h]) < eps) {
					X[h] = xk;
				} else {
					for (int i = 0; i < X.size(); i++) {
						if (i == l) continue;
						for (int j = 0; j < X[i].size(); j++) {
							X[i][j] = (X[i][j] + X[l][j]) * sigma;
						}
					}
				}
			} else {
				X[h] = xr;
			}
		}

		lh = get_best_and_worst(X, f);
	}

	return xc;
}

void print_nelder_mead_result(Function &f, std::vector<double> result) {
	printf("Nelder-Mead done after %d calls.\n", f.get_call_count());
	printf("Optimum point: [ ");
	for (int i = 0; i < result.size(); i++) {
		printf("%f ", result[i]);
	}
	printf("]\n");
	f.reset_counter();
}

// Hooke-Jeeves

bool hooke_jeeves_end(std::vector<double> &dx, std::vector<double> &eps) {
	for (int i = 0; i < dx.size(); i++) {
		if (dx[i] > eps[i]) return false;
	}
	return true;
}

std::vector<double> explore(std::vector<double> &xp, std::vector<double> &dx, Function &f) {
	std::vector<double> x = xp;
	for (int i = 0; i < x.size(); i++) {
		double p = f.value_at(x);
		x[i] += dx[i];
		double n = f.value_at(x);
		if (n > p) {
			x[i] -= 2 * dx[i];
			n = f.value_at(x);
			if (n > p) {
				x[i] += dx[i];
			}
		}
	}
	return x;
}

std::vector<double> hooke_jeeves_solver(std::vector<double> &x0, std::vector<double> dx, std::vector<double> eps, Function &f) {
	std::vector<double> xp = x0, xb = x0, xn;

	while (!hooke_jeeves_end(dx, eps)) {
		xn = explore(xp, dx, f);
		double val_xn = f.value_at(xn);
		double val_xb = f.value_at(xb);

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

std::vector<double> hooke_jeeves(std::vector<double> &x0, Function &f) {
	std::vector<double> dx(x0.size());
	std::vector<double> eps(x0.size());
	for (int i = 0; i < x0.size(); i++) {
		dx[i] = 0.5;
		eps[i] = 1e-6;
	}
	return hooke_jeeves_solver(x0, dx, eps, f);
}

void print_hooke_jeeves_result(Function &f, std::vector<double> result) {
	printf("Hooke-Jeeves done after %d calls.\n", f.get_call_count());
	printf("Optimum point: [ ");
	for (int i = 0; i < result.size(); i++) {
		printf("%f ", result[i]);
	}
	printf("]\n");
	f.reset_counter();
}

// Examples 

void example1() {
	Function3 f3(-3.0);
	std::vector<double> x0; x0.push_back(10);
	print_golden_ratio_search(f3, golden_section_search_point(10, 1, f3));
	print_coordinate_axis_search(f3, coordinate_axis_search(x0, f3));
	print_nelder_mead_result(f3, nelder_mead_simplex(x0, -0.99, f3));
	print_hooke_jeeves_result(f3, hooke_jeeves(x0, f3));
}

void example2() {
	printf("F1\n");
	Function1 f1;
	std::vector<double> x0(2);
	x0[0] = -1.9; x0[1] = 2;
	print_coordinate_axis_search(f1, coordinate_axis_search(x0, f1));
	print_nelder_mead_result(f1, nelder_mead_simplex(x0, -1, f1));
	print_hooke_jeeves_result(f1, hooke_jeeves(x0, f1));
	
	printf("F2\n");
	Function2 f2;
	x0[0] = 0.1; x0[1] = 0.3;
	print_coordinate_axis_search(f2, coordinate_axis_search(x0, f2));
	print_nelder_mead_result(f2, nelder_mead_simplex(x0, -1, f2));
	print_hooke_jeeves_result(f2, hooke_jeeves(x0, f2));

	printf("F3\n");
	Function3 f3;
	x0 = std::vector<double>(5, 0);
	print_coordinate_axis_search(f3, coordinate_axis_search(x0, f3));
	print_nelder_mead_result(f3, nelder_mead_simplex(x0, -1, f3));
	print_hooke_jeeves_result(f3, hooke_jeeves(x0, f3));

	printf("F4\n");
	Function4 f4;
	x0 = std::vector<double>(2);
	x0[0] = 5.1; x0[1] = 1.1;
	print_coordinate_axis_search(f4, coordinate_axis_search(x0, f4));
	print_nelder_mead_result(f4, nelder_mead_simplex(x0, -1, f4));
	print_hooke_jeeves_result(f4, hooke_jeeves(x0, f4));
}

void example3() {
	Function4 f4;

	std::vector<double> x0(2);
	x0[0] = 5; x0[1] = 5;

	print_nelder_mead_result(f4, nelder_mead_simplex(x0, -1, f4));
	print_hooke_jeeves_result(f4, hooke_jeeves(x0, f4));
}

void example4() {
	Function1 f1;
	std::vector<double> x0(2);
	x0[0] = 0.5; x0[1] = 0.5;

	for (int i = 1; i <= 20; i++) {
		print_nelder_mead_result(f1, nelder_mead_simplex(x0, i, f1));
	}

	printf("\n\n");
	x0[0] = 20; x0[1] = 20;

	for (int i = 1; i <= 20; i++) {
		print_nelder_mead_result(f1, nelder_mead_simplex(x0, i, f1));
	}
}

double fRand(double fMin, double fMax) {
	double f = (double)rand() / RAND_MAX;
    return fMin + f * (fMax - fMin);
}

void example5() {
	Function5 f5;
	std::vector<double> x0(2);

	srand(time(NULL));

	int found = 0, total = 5000;
	for (int i = 0; i < total; i++) {
		x0[0] = fRand(-50, +50), x0[1] = fRand(-50, +50);
		std::vector<double> sol = hooke_jeeves(x0, f5);
		if (sol.size() == 0) continue;
		found += (abs(f5.value_at(sol)) <= 1e-4);
	}

	printf("Found global optimums: %d, that is %f percent.\n", found, found * 100.0 / total);
}

int main(void) {
	// example1();
	example2();
	// example3();
	// example4();
	// example5();
	return 0;
}

