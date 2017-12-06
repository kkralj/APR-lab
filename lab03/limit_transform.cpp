#include <stdio.h>
#include <vector>

#include "functions.h"

int main() {

	LimitFunction1 l1;
	LimitFunction2 l2;
	std::vector<Function*> g;
	g.push_back(&l1);
	g.push_back(&l2);

	std::vector<Function*> h;
	std::vector<double> x0(2);

	// f1
	Function1 f1;

	printf("%lf\n", f1.limit_value_at(x0, 5, g, h));

	// f2
	Function2 f2;

	return 0;
} 