#include <assert.h>
#include "limit.h"
#include <stdio.h>

bool limit::valid(std::vector<double> &point) {
	return false;
}


bool limit1::valid(std::vector<double> &point) {
	assert(point.size() == 2);
	return point[1] - point[0] >= 0;
}

bool limit2::valid(std::vector<double> &point) {
	assert(point.size() == 2);
	return 2 - point[0] >= 0;
}