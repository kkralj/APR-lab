all: gradient_descent box limit_transform

gradient_descent: gradient_descent.cpp
	g++ gradient_descent.cpp function.cpp functions.cpp -o gradient_descent

newton_raphson: newton_raphson.cpp
	g++ newton_raphson.cpp function.cpp functions.cpp -o newton_raphson

box: box.cpp limit.cpp
	g++ box.cpp function.cpp functions.cpp limit.cpp -o box

limit_transform: limit_transform.cpp
	g++ limit_transform.cpp function.cpp functions.cpp -o limit_transform