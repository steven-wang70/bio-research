%matplotlib inline
import numpy
from ecell4 import *

k_1s = 2
k_2s = 2
k_3s = 15
k_1d = 1
k_2d = 1
k_3d = 1
k_21 = 1
k_31 = 1
k_32 = 1
k_13 = 100

with reaction_rules():
    ~G1 > G1 | k_1s / (1 + k_13 * G3) - k_1d * G1
    ~G2 > G2 | k_2s * k_21 * G1 / (1 + k_21 * G1) - k_2d * G2
    ~G3 > G3 | k_3s * k_31 * G1 * k_32 * G2 / (1 + k_31 * G1) * (1 + k_32 * G2) - k_3d * G3

y = run_simulation(
    numpy.linspace(0, 6, 1000), {'G1': 0, 'G2': 0, 'G3': 0}, solver='ode')