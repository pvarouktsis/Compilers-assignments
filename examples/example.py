#! /usr/bin/env python
# miniPython example

def fib(n): # write Fibonacci Series up to n
	a = 0
	b = 1
	while a < n:
		print a
		a = b
		b = a + b
                      
def funcwithdef(name,university="aueb"):
       print name, " studies in ", university
