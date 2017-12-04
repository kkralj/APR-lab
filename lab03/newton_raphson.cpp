#include <stdio.h>
#include <math.h>

#include "functions.h"

double eucl_norm(std::vector<double> x) {
	double val = 0;
	for (int i = 0; i < x.size(); i++) {
		val += x[i] * x[i];
	}
	return sqrt(val);
}

std::vector<double> multiply(std::vector< std::vector<double> > H, std::vector<double> G) {
	assert(H.size() > 0 && H[0].size() == G.size());
	std::vector<double> res(G.size());

	e

	return res;
}


std::vector<double> newton_raphson(std::vector<double> point, Function &f, double eps = 1e-6) {
	int iter = 250;
	std::vector<double> x = point, grad;

	while (iter > 0 && eucl_norm((grad = f.gradient_at(x))) > eps) {
		std::vector< std::vector<double> > H = f.hessian_at(x);
		std::vector<double> G = f.gradient_at(x);

		std::vector<double> hg = get_result(H, G);
		std::vector<double> xj = x;

		assert(xj.size() == hg.size());
		for (int i = 0; i < xj.size(); i++) {
			xj[i] -= hg[i];
		}

		x = xj;
		iter--;
	}
} 

int main(void) {

	return 0;
}