import os
import sys
import subprocess

class ProblemToLP :

	def __init__(self, input_filename, output_filename, int_option=False) :
		self._input_filename = input_filename
		self._output_filename = output_filename
		self._final_content = ""
		self._option = int_option
		self.read_file()

	def read_file(self):
		"""
		@param None
		@return None
		Take the input file and init our class fields with the right values. 
		"""
		with open(self._input_filename, 'r') as f :
			datas = f.readlines()
			self._tmp2source_number = int(datas[0].split(" ")[0])
			self._product_number = int(datas[0].split(" ")[1])
			self._constraints = datas[1].strip().split(" ")
			self._products = [line.strip().split(" ") for line in datas[2:]]

	def objective_function(self):
		"""
		@param None
		@return str
		Return the benefits function in LP format described in the input file in string format.
		"""
		tmp = "max: "
		for i in range(self._product_number - 1):
			tmp += self._products[i][1 + self._tmp2source_number] + self._products[i][0] + '+'
		tmp += self._products[-1][1 + self._tmp2source_number] + self._products[-1][0] + ';'
		return tmp + '\n'

	def constraints_equations(self):
		"""
		@param None
		@return str
		Return the constraint functions in LP format described in the input file in string format.
		"""
		tmp = ""
		for i in range(self._tmp2source_number):
			current_constraint_eq = ''
			for j in range(self._product_number - 1):
				current_constraint_eq += self._products[j][i + 1] + self._products[j][0] + '+'
			linebreak = '\n' if i !=  self._tmp2source_number - 1 else '' 
			current_constraint_eq += self._products[-1][i + 1] + self._products[-1][0] + "<= " + self._constraints[i] + ';' + linebreak
			tmp += current_constraint_eq
		return tmp

	def int_option(self):
		"""
		@param None
		@return str
		Return the int function in LP format if the option is ptmp2ent.
		"""
		tmp = "int "
		for i in range(self._product_number - 1):
			tmp += self._products[i][0] + ', '
		return tmp + self._products[-1][0] + ';'

	def write_in_file(self):
		"""
		@param None
		@return None
		Write the benefits function, the constraint functions and the options (if ptmp2ents) in the output file with LP format.
		"""
		with open(self._output_filename, 'w+') as f :
			f.write(self.objective_function() + self.constraints_equations())
			if self._option :
				f.write('\n' + self.int_option())

	def execute_lp(self):
		"""
		@param None
		@return None
		Print the result of lp_solve command on our file (optimum, non null variables and their values).
		"""
		p = subprocess.run(["lp_solve", self._output_filename], capture_output=True)
		result = "opt = "
		tmp2 = []
		tmp = str(p.stdout).split("\\n")
		for string in tmp :
			for letter in string :
				if letter.isdigit() :
					tmp2.append(string)
					break
		result += tmp2[0].split(' ')[-1]
		for i in range(1, len(tmp2)):
			result += '\n' + " = ".join(tmp2[i].split())
		print(result)
		



if __name__ == "__main__" :
	argument_number = len(sys.argv)
	if argument_number > 2 :
		option = sys.argv[3] if argument_number > 3 and sys.argv[3] == "-int" else False
		p = ProblemToLP(sys.argv[1], sys.argv[2], option)
		p.write_in_file()
		p.execute_lp()
	else :
		sys.stderr.write("Number of arguments is incorrect.\nusage: generic.py <input file> <output file> [options]\n")
	
