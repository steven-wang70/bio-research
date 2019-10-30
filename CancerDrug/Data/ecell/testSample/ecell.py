%matplotlib inline
import numpy
from ecell4 import *

k_d_G3 = 1
k_s_G3 = 15
k_x_G3_G1 = 1
k_x_G3_G2 = 1
k_d_G2 = 2
k_s_G2 = 1
k_x_G2_G1 = 1
k_d_G1 = 1
k_s_G1 = 2
k_x_G1_G3 = 100

with reaction_rules():
	~G3 > G3 | k_s_G3*(k_x_G3_G1*G1/(1+k_x_G3_G1*G1))*(k_x_G3_G2*G2/(1+k_x_G3_G2*G2))-k_d_G3*G3
	~G2 > G2 | k_s_G2*(k_x_G2_G1*G1/(1+k_x_G2_G1*G1))-k_d_G2*G2
	~G1 > G1 | k_s_G1*(k_x_G1_G3*G3/(1+k_x_G1_G3*G3))-k_d_G1*G1

y = run_simulation(numpy.linspace(0, 10, 1000), {'G3':0,'G2':0,'G1':0}, solver='ode')
