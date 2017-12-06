#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <vector>
#include <assert.h>
#include <math.h>

#include "limit.h"
#include "functions.h"

void print(std::vector<double> &v) {
	for (int i = 0; i < v.size(); i++) {
		printf("%lf ", v[i]);
	}
	printf("\n");
}

bool valid_limits(std::vector<double> &v, std::vector<limit*> &implicit_limits) {
	for (int i = 0; i < implicit_limits.size(); i++) {
		if (!implicit_limits[i]->valid(v)) return false;
	}
	return true;
}

double eucl_norm_diff(std::vector<double> &x1, std::vector<double> &x2) {
	assert(x1.size() == x2.size());
	double val = 0;
	for (int i = 0; i < x1.size(); i++) {
		val += pow(x1[i] - x2[i], 2);
	}
	return sqrt(val);
}

std::vector<double> box(std::vector<double> point, std::vector<double> xd, std::vector<double> xg, std::vector<limit*> implicit_limits, Function &f, double alpha = 1.3, double eps = 1e-6) {
	assert(xd.size() == xg.size() && xd.size() == point.size());

	int n = point.size();
	for (int i = 0; i < n; i++) {
		assert(point[i] >= xd[i] && point[i] <= xg[i]);
	}

	assert(valid_limits(point, implicit_limits));

	std::vector<double> xc = point;
	std::vector< std::vector<double> > X;

	for (int i = 0; i < 2 * n; i++) {
		// generate new valid point
		std::vector<double> xj = xd;
		for (int i = 0; i < n; i++) {
			double R = (double)rand() / (double)RAND_MAX;
			xj[i] += R * (xg[i] - xd[i]);
		}

		while (!valid_limits(xj, implicit_limits)) {
			for (int i = 0; i < n; i++) {
				xj[i] = (xj[i] + xc[i]) / 2.0;
			}
		}
		X.push_back(xj);

		// calculate new xc
		xc = point;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < X.size(); j++) {
				xc[i] += X[j][i];
			}
			xc[i] /= (1 + X.size()); // X.size() + starting point
		}
	}

	int iter;
	for (iter = 0; iter < 1e3; iter++) {
		int h = 0, h2 = 0;
		double fh = f.value_at(X[0]), fh2 = fh;

		// get h, h2
		for (int i = 1; i < X.size(); i++) {
			double val = f.value_at(X[i]);
			if (val > fh) {
				fh2 = fh;
				h2 = h;
				fh = val;
				h = i;
			} else if (val > fh2) {
				fh2 = val;
				h2 = i;
			}
		}

		// xc without h
		xc = std::vector<double>(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < X.size(); j++) {
				if (j != h) {
					xc[i] += X[j][i];
				}
			}
			xc[i] /= X.size() - 1;
		}

		// reflexion
		std::vector<double> xr(n);
		for (int i = 0; i < n; i++) {
			xr[i] = (1 + alpha) * xc[i] - alpha * X[h][i];
		}

		for (int i = 0; i < n; i++) {
			xr[i] = std::max(xr[i], xd[i]);
			xr[i] = std::min(xr[i], xg[i]);
		}

		while (!valid_limits(xr, implicit_limits)) {
			for (int i = 0; i < n; i++) {
				xr[i] = (xr[i] + xc[i]) / 2.0;
			}
		}

		if (f.value_at(xr) > fh) {
			for (int i = 0; i < n; i++) {
				xr[i] = (xr[i] + xc[i]) / 2.0;
			}
		}

		X[h] = xr;

		// stop cond
		bool done = true;
		for (int i = 0; i < X.size(); i++) {
			if (eucl_norm_diff(xc, X[i]) > eps) {
				done = false;
				break;
			}
		}
		if (done) break;
	}

	printf("Done after %d iterations\n", iter);
	return xc;
}

int main(void) {
	srand(time(NULL));

	// f1
	Function1 f1;
	limit1 l1;
	limit2 l2;
	std::vector<limit*> implicit_limits;
	implicit_limits.push_back(&l1);
	implicit_limits.push_back(&l2);

	std::vector<double> x1;
	x1.push_back(-1.9);
	x1.push_back(2);

	std::vector<double> xd;
	xd.push_back(-100);
	xd.push_back(-100);

	std::vector<double> xg;
	xg.push_back(100);
	xg.push_back(100);

    std::vector<double> sol = box(x1, xd, xg, implicit_limits, f1);
    print(sol);

	// f2
	Function2 f2;
	std::vector<double> x2;
	x2.push_back(0.1);
	x2.push_back(0.3);

    sol = box(x2, xd, xg, implicit_limits, f2);
    print(sol);

	return 0;
}