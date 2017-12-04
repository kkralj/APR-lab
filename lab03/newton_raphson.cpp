#include <stdio.h>
#include <math.h>
#include <assert.h>

#include "functions.h"

const double K = 0.5 * (sqrt(5) - 1);

void print(std::vector<double> &v) {
	for (int i = 0; i < v.size(); i++) {
		printf("%lf ", v[i]);
	}
	printf("\n");
}

double eucl_norm(std::vector<double> x) {
	double val = 0;
	for (int i = 0; i < x.size(); i++) {
		val += x[i] * x[i];
	}
	return sqrt(val);
}

void swap_rows(int current, int next, int **p, std::vector< std::vector<double> > &M) {
	int n = M.size();
    double tmp;
    for (int j = 0; j < n; j++) {
        tmp = p[current][j];
        p[current][j] = p[next][j];
        p[next][j] = tmp;
    }

    for (int j = 0; j < n; j++) {
        tmp = M[current][j];
        M[current][j] = M[next][j];
        M[next][j] = tmp;
    }
}

void decompose(std::vector< std::vector<double> > &M) {
	std::vector< std::vector<double> > result(M.size());
	int n = M.size();

	int **P = new int*[M.size()];
	for (int i = 0; i < M.size(); i++) {
    	P[i] = new int[M.size()];
    	P[i][i] = 1;
	}

    double x;
    for (int i = 0; i < n - 1; i++) {
        int pivot = i;
        for (int j = i + 1; j < n; j++) {
            if (abs(M[pivot][i]) < abs(M[j][i])) {
                pivot = j;
            }
        }
        if (pivot != i) {
            swap_rows(i, pivot, P, M);
        }

        x = M[i][i];

        assert(abs(x) >= 1e-6);

        for (int j = i + 1; j < n; j++) {
            M[j][i] /= x;
            for (int k = i + 1; k < n; k++) {
            	M[j][k] -= M[j][i] * M[i][k];
            }
        }
    }

	for (int i = 0; i < M.size(); i++) {
		delete[] P[i];
	}
	delete[] P;
}

std::vector<double> forward_substitute(std::vector< std::vector<double> > &L, std::vector<double> &G) {
    assert(L.size() == L[0].size() && L.size() == G.size());

    int n = G.size();
    std::vector<double> res = G;

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < i; j++) {
            res[i] -= L[i][j] * res[j];
        }
    }

    return res;
}

std::vector<double> backward_substitute(std::vector< std::vector<double> > &U, std::vector<double> &y) {
	assert(U.size() == U[0].size() && U.size() == y.size());

    int n = U.size();
    std::vector<double> res = y;

    for (int i = n - 1; i >= 0; i--) {
    	assert(abs(U[i][i]) >= 1e-6);
        double sum = y[i];
        for (int j = n - 1; j > i; j--) {
            sum -= U[i][j] * res[j];
        }
        res[i] = sum / U[i][i];
    }

    return res;
}

// solve Hx=G	
std::vector<double> get_result(std::vector< std::vector<double> > &H, std::vector<double> &G) {
	assert(H.size() > 0 && H[0].size() == G.size());

	decompose(H);
	std::vector<double> y = forward_substitute(H, G);
	std::vector<double> x = backward_substitute(H, y);
	return x;
}

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

std::vector<double> newton_raphson(std::vector<double> point, Function &f, bool golden = false, double eps = 1e-6) {
	int iter;
	std::vector<double> x = point, grad;

	for (iter = 1; iter <= 1e4; iter++) {
		std::vector< std::vector<double> > H = f.hessian_at(x);
		std::vector<double> G = f.gradient_at(x);

		std::vector<double> hg = get_result(H, G);
		assert(x.size() == hg.size());
		if (eucl_norm(hg) < eps) break;

		double lambda = golden ? golden_section_search_point(x, hg, f) : -1;
		for (int i = 0; i < x.size(); i++) {
			x[i] += lambda * hg[i];
		}
		//printf("lambda: %lf\n", lambda);
		//print(x);
	}

	printf("Done after %d iterations.\n", iter);
	return x;
} 

int main(void) {
	// f1
	Function1 f1;
	std::vector<double> x1;
	x1.push_back(-1.9);
	x1.push_back(2);
	std::vector<double> result1 = newton_raphson(x1, f1, true);
	print(result1);

	// f2
	Function2 f2;
	std::vector<double> x2;
	x2.push_back(0.1);
	x2.push_back(0.3);
	std::vector<double> result2 = newton_raphson(x2, f2, true);
	print(result2);

	return 0;
}