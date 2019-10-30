%matplotlib inline
import numpy
from ecell4 import *

k_d_ERM = 0
k_s_ERM = 1
k_x_ERM_E2ER = 2
k_x_ERM_ERM = 3
k_x_ERM_GFR = 4
k_d_ERP = 5
k_s_ERP = 6
k_x_ERP_ERM = 7
k_x_ERP_GFR = 8
k_d_GFR = 9
k_s_GFR = 10
k_x_GFR_E2ER = 11
k_x_GFR_ERM = 12
k_x_GFR_GFR = 13
k_count = 14

init_ERM = 0
init_ERP = 1
init_GFR = 2
init_E2ER = 3
init_count = 4

def start_simulation(factors, initials, length, steps):
	with reaction_rules():
		~ERM > ERM | factors[k_s_ERM]*(1/(1+factors[k_x_ERM_E2ER]*E2ER))*(factors[k_x_ERM_ERM]*ERM/(1+factors[k_x_ERM_ERM]*ERM))*(factors[k_x_ERM_GFR]*GFR/(1+factors[k_x_ERM_GFR]*GFR))-factors[k_d_ERM]*ERM
		~ERP > ERP | factors[k_s_ERP]*(factors[k_x_ERP_ERM]*ERM/(1+factors[k_x_ERP_ERM]*ERM))*(factors[k_x_ERP_GFR]*GFR/(1+factors[k_x_ERP_GFR]*GFR))-factors[k_d_ERP]*ERP
		~GFR > GFR | factors[k_s_GFR]*(1/(1+factors[k_x_GFR_E2ER]*E2ER))*(factors[k_x_GFR_ERM]*ERM/(1+factors[k_x_GFR_ERM]*ERM))*(factors[k_x_GFR_GFR]*GFR/(1+factors[k_x_GFR_GFR]*GFR))-factors[k_d_GFR]*GFR
	
	obs1 = FixedIntervalNumberObserver(length * 1.0 / steps, ('ERM','ERP','GFR','E2ER'))
	y = run_simulation(numpy.linspace(0, length, steps), {'ERM':initials[init_ERM],'ERP':initials[init_ERP],'GFR':initials[init_GFR],'E2ER':initials[init_E2ER]}, solver='ode', return_type=None, observers=(obs1))
	return obs1
