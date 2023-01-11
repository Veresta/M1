#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
import math
import itertools
import tests.RowRules
import tests.SubgridRules
import subprocess

"""Sudoku"""

def var(i, j, k):
	"""Return the literal Xijk.
	>>> var(1, 2, 3)
	(1, 1, 2, 3)
	"""
	return (1, i, j, k)

def neg(quadruplet):
	"""Return the negation of the literal.
	>>> neg((1, 1, 2, 3))
	(-1, 1, 2, 3)
	"""
	return (-quadruplet[0],) + quadruplet[1:]

def initial_configuration():
	"""Return the initial configuration of the example in td6.pdf
	>>> cnf = initial_configuration()
	>>> [(1, 1, 4, 4)] in cnf
	True
	>>> [(1, 2, 1, 2)] in cnf
	True
	>>> [(1, 2, 3, 1)] in cnf
	False
	"""
	return [[var(2, 1, 2)], [var(1, 4, 4)], [var(3, 2, 1)], [var(4, 3, 1)]]

def at_least_one(L):
	"""Return a cnf that represents the constraint: at least one of the
	literals in the list L is true.
	
	>>> lst = [var(1, 1, 1), var(2, 2, 2), var(3, 3, 3)]
	>>> cnf = at_least_one(lst)
	>>> len(cnf)
	1
	>>> clause = cnf[0]
	>>> len(clause)
	3
	>>> clause.sort()
	>>> clause == [var(1, 1, 1), var(2, 2, 2), var(3, 3, 3)]
	True
	"""
	return [L]

def at_most_one(L):
	"""Return a cnf that represents the constraint: at most one of the
	literals in the list L is true
	
	>>> lst = [var(1, 1, 1), var(2, 2, 2), var(3, 3, 3)]
	>>> cnf = at_most_one(lst)
	>>> len(cnf)
	3
	>>> cnf[0].sort()
	>>> cnf[1].sort()
	>>> cnf[2].sort()
	>>> cnf.sort()
	>>> cnf == [[neg(var(1,1,1)), neg(var(2,2,2))], \
	[neg(var(1,1,1)), neg(var(3,3,3))], \
	[neg(var(2,2,2)), neg(var(3,3,3))]]
	True
	"""
	return [[neg(x), neg(y)] for x, y in itertools.combinations(L, 2)]


def assignment_rules(N):
	"""Return a list of clauses describing the rules for the assignment (i, j) -> k.
	>>> cnf = assignment_rules(4)
	>>> len(cnf)
	112
	>>> for clause in cnf[:8]: print(clause)
	[(1, 1, 1, 1), (1, 1, 1, 2), (1, 1, 1, 3), (1, 1, 1, 4)]
	[(-1, 1, 1, 1), (-1, 1, 1, 2)]
	[(-1, 1, 1, 1), (-1, 1, 1, 3)]
	[(-1, 1, 1, 1), (-1, 1, 1, 4)]
	[(-1, 1, 1, 2), (-1, 1, 1, 3)]
	[(-1, 1, 1, 2), (-1, 1, 1, 4)]
	[(-1, 1, 1, 3), (-1, 1, 1, 4)]
	[(1, 1, 2, 1), (1, 1, 2, 2), (1, 1, 2, 3), (1, 1, 2, 4)]
	"""
	cnf = []
	for i in range(1, N+1):
		for j in range(1, N+1):
			tmp = [var(i, j, n) for n in range(1, N+1)]
			cnf += at_least_one(tmp) + at_most_one(tmp)
	return cnf

def row_rules(N):
	"""Return a list of clauses describing the rules for the rows.
	>>> tests.RowRules.clauses == row_rules(4)
	True
	"""
	row = []
	for i in range(1, N+1):
		for j in range(1, N+1):
			tmp = [var(i, n, j) for n in range(1, N+1)]
			row += at_least_one(tmp) + at_most_one(tmp)
	return row

def column_rules(N):
	"""Return a list of clauses describing the rules for the columns.
	"""
	column = []
	for i in range(1, N+1):
		for j in range(1, N+1):
			tmp = [var(n, i, j) for n in range(1, N+1)]
			column += at_least_one(tmp) + at_most_one(tmp)
	return column

def get_block(m, n, k, subgridsize):
	"""Return the list of clauses representing the subgrids of size : subgridsize."""
	return [var(m + i, n + j, k) for i in range(subgridsize) for j in range(subgridsize)]

def subgrid_rules(N):
	"""Return a list of clauses describing the rules for the subgrids.
	SQUAREROOT N x SQUAREROOT N and in each cell SQUAREROOT N x SQUAREROOT N
	>>> len(tests.SubgridRules.clauses) == len(subgrid_rules(4))
	True
	>>> tests.SubgridRules.clauses == subgrid_rules(4)
	True
	"""
	cnf = []
	step = int(math.sqrt(N))
	for m in range(1,N+1, step):
		for n in range(1,N+1, step):
			for k in range(1,N+1):
				block = get_block(m, n, k, step)
				for clause in at_least_one(block):
					cnf.append(clause)
				for clause in at_most_one(block):
					cnf.append(clause)
	return cnf

def generate_rules(N):
	"""Return a list of clauses describing the rules of the game.
	"""
	cnf = []    
	cnf.extend(assignment_rules(N))
	cnf.extend(row_rules(N))
	cnf.extend(column_rules(N))
	cnf.extend(subgrid_rules(N))
	return cnf

def literal_to_integer(quadruplet, N):
	"""Return the external representation of the literal l.
	>>> literal_to_integer(var(1,2,3), 4)
	7
	>>> literal_to_integer(neg(var(3,2,1)), 4)
	-37
	"""
	return quadruplet[0] * (N**2 * (quadruplet[1] - 1) + N * (quadruplet[2] - 1) + quadruplet[3])

def read_init_config(filename):
	"""
	@param str:filename
	@return int[], int
	Take a file name and return an int list which represents the initial state of the grid and his size. 
	"""
	with open(filename, "r") as f:
		data = f.readlines()
		size = data[0]
		config = []
		for ligne in range(1, len(data)):
			res = str(data[ligne]).split()
			for case in range(len(res)):
				tmp = int(res[case])
				if tmp != 0:
					config.append([var(ligne, case+1, tmp)])
	return config, size

class SudokuSolver:

	_OUTPUT_FILENAME = "sudoku.cnf"
	_SOLUTION_FILENAME = "sudoku.out"
	
	def __init__(self, cnf, gridSize):
		self._gridSize = gridSize
		self._cnf = cnf
		self._rules = generate_rules(gridSize)
		self._numberOfClauses = len(self._rules)
		self._maxvalue = 0								#Need to call rules_to_integers function to update the value

	def rules_to_integers(self):
		"""
		@param None
		@return int[]
		Transform all rules to integers with the 'literal_to_integer' function, update maxvalue and return rules modified. 
		"""
		tmp = []
		maxvalue = 0
		for rule in self._rules:
			tmp2 = [literal_to_integer(clause, self._gridSize) for clause in rule]
			maxvalue = max(maxvalue, max([abs(i) for i in tmp2]))
			tmp.append(tmp2)
		self._maxvalue = maxvalue
		return tmp

	def header(self):
		"""
		@param None
		@return str
		Return a string which represent the header of a cnf file. 
		"""
		tmp = "p cnf " + str(self._maxvalue) + ' ' + str(self._numberOfClauses) + '\n'
		return tmp + "\n".join([str(literal_to_integer(v[0], self._gridSize)) + " 0" for v in self._cnf]) + '\n'

	def body(self):
		"""
		@param None
		@return str
		Return a string which represent the body of a cnf file.
		"""
		tmp = ""
		rules = self.rules_to_integers()						#maxvalue updated
		for rule in rules :
			tmp2 = ""
			for clause in rule:
				tmp2 += str(clause) + ' '
			tmp += tmp2 + '0\n'
		return tmp

	def write_in_file(self):
		"""
		@param None
		@return None
		Write the result of the header and body methods in a cnf file.
		"""
		with open(self._OUTPUT_FILENAME, 'w') as f:
			f.write(self.header() + self.body())

	def execute_minisat(self):
		"""
		@param None
		@return None
		Execute minisat program and create a file with the result in the output file.
		"""
		p = subprocess.run(["minisat", self._OUTPUT_FILENAME], capture_output=True)

	def get_solutions(self):
		"""
		@param None
		@return str
		Read the solution file and return a string which print the sudoku solution.
		"""
		tmp = ""
		with open(self._SOLUTION_FILENAME, 'r') as f:
			lines = f.readlines()
			if lines[0].strip() != "SAT":
				return "UNSATISFACTORY"
			i = 0
			results = lines[1].split()
			for _ in range(1, self._gridSize + 1):
				for _ in range(1, self._gridSize + 1):
					for value in range(1, self._gridSize + 1):
						if int(results[i]) > 0:
							tmp += str(value)
						i += 1
					tmp += ' '
				tmp += '\n'
			return tmp

def main():
	import doctest
	doctest.testmod()

if __name__ == "__main__":
	main()
	if len(sys.argv) == 2:
		config, size = read_init_config(sys.argv[1])
		A = SudokuSolver(config, int(size))
		A.write_in_file()
		A.execute_minisat()
		print(A.get_solutions())
	else:
		sys.stderr.write("Number of arguments is incorrect.\nusage: sudoku.py <input file>\n")