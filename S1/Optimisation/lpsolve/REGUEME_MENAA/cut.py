import subprocess
import sys
import os

# @AUTHORS : REGUEME Yohann, MENAA Mathis

def get_all_optimized_cut(objectives, barSize):
	"""
	@param objectives : size of wanted bars | barSize : default bar size
	@return list of the possible cuts
	Take a list of wanted size and return all cut of a bar with size barSize to obtain it.
	"""
	res = []
	size_possible = barSize // objectives[0] #2 à la première itération
	if len(objectives) == 1: 
		return [[size_possible]]
	for i in range(size_possible + 1): #0, 1, 2
		tmp_remains = i * objectives[0] #barSize restant
		combinations = get_all_optimized_cut(objectives[1:], barSize - tmp_remains) # 100,50 puis 50 puis return de la val
		for elem in combinations:
			elem.insert(0, i) #Ajout pour la val grande (120) et ajout pour la val moyenne (100)
			res.append(elem)
	return res

class CutToLP:

	#Filename for output
	_OUTPUT_FILENAME = "myCut.lp"

	def __init__(self, barLength, wantedLengths, amounts):
		self._barlength = barLength
		self._wantedLengths = wantedLengths
		self._amounts = amounts
		self._cuts = get_all_optimized_cut(wantedLengths, barLength)

	def objective_function(self):
		"""
		@param None
		@return str
		Return the benefits function for lp files in string format.
		"""
		tmp = "min: "
		for i in range(1, len(self._cuts)):
			tmp += 'X'+str(i) + '+'
		tmp += 'X' + str(len(self._cuts))
		return tmp + ';' + '\n'

	def constraints_equations(self):
		"""
		@param None
		@return str
		Return the constraint functions for lp files in string format.
		"""
		tmp = ""
		for i in range(len(self._amounts)):
			for j in range(len(self._cuts)):
				tmp2 = self._cuts[j][i]
				if tmp2 != 0:
					tmp += str(tmp2) + 'X' + str(j+1) + '+'
			tmp = tmp[:-1] + " >= " + str(self._amounts[i]) + ';' + '\n'
		return tmp

	def int_option(self):
		"""
		@param None
		@return str
		Return the int function for lp files in string format.
		"""
		tmp = "int "
		for i in range(1, len(self._cuts)):
			tmp += 'X' + str(i) + ', '
		return tmp + 'X' + str(len(self._cuts)) + ';'

	def write_in_file(self):
		"""
		@param None
		@return None
		Write the benefits function, the constraint functions and the options in the output file with LP format.
		"""
		with open(self._OUTPUT_FILENAME, 'w+') as f :
			f.write(self.objective_function() + self.constraints_equations() + self.int_option())

	def execute_lp(self):
		"""
		@param None
		@return None
		Print the result of lp_solve command on our file (optimum, non null variables and their values).
		"""
		p = subprocess.run(["lp_solve", self._OUTPUT_FILENAME], capture_output=True)
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


def stringToIntList(stringRepresentation):
	"""
	@param String representation of a list :  [], [12,56] [21   , 89,  8849]
	@return list of int
	Take a String representation of a list and convert it to an int list.
	"""
	return [int(number.strip()) for number in stringRepresentation[1:-1].split(',')]


if __name__ == "__main__" :
	argument_number = len(sys.argv)
	if argument_number > 3 :
		try:
			c = CutToLP(int(sys.argv[1]), stringToIntList(sys.argv[2]), stringToIntList(sys.argv[3]))
		except ValueError:
			sys.stderr.write("[ERROR] Invalid arguments\n")
			sys.stderr.write("[HELP] Lists arguments need to be non-empty representations like : [1, 2, 3]. Maybe you need to add quotation marks like \"[1,2,3]\" with your console ?\n")	
			sys.stderr.write("usage: cut.py <bar length -> INT> <wanted lengths -> LIST REPRESENTATION> <amounts -> LIST REPRESENTATION>\n")
			sys.exit(1)
		c.write_in_file()
		c.execute_lp()
	else :
		sys.stderr.write("Number of argument is incorrect.\nusage: cut.py <bar length -> INT> <wanted lengths -> LIST REPRESENTATION> <amounts -> LIST REPRESENTATION>\n")
