#ifndef __LIMIT_H__
#define __LIMIT_H__

#include <vector>

class limit {
public:
	virtual bool valid(std::vector<double> &point);
};

class limit1 : public limit {
public:
	virtual bool valid(std::vector<double> &point) override;
};

class limit2 : public limit {
public:
	virtual bool valid(std::vector<double> &point) override;
};

#endif