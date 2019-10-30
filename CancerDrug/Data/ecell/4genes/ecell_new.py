%matplotlib inline
import numpy
from ecell4 import *
import time

# Auto-generation start

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

def start_simulation(factors, initials, period, steps):
	with reaction_rules():
		~ERM > ERM | factors[k_s_ERM]*(1/(1+factors[k_x_ERM_E2ER]*E2ER))*(factors[k_x_ERM_ERM]*ERM/(1+factors[k_x_ERM_ERM]*ERM))*(factors[k_x_ERM_GFR]*GFR/(1+factors[k_x_ERM_GFR]*GFR))-factors[k_d_ERM]*ERM
		~ERP > ERP | factors[k_s_ERP]*(factors[k_x_ERP_ERM]*ERM/(1+factors[k_x_ERP_ERM]*ERM))*(factors[k_x_ERP_GFR]*GFR/(1+factors[k_x_ERP_GFR]*GFR))-factors[k_d_ERP]*ERP
		~GFR > GFR | factors[k_s_GFR]*(1/(1+factors[k_x_GFR_E2ER]*E2ER))*(factors[k_x_GFR_ERM]*ERM/(1+factors[k_x_GFR_ERM]*ERM))*(factors[k_x_GFR_GFR]*GFR/(1+factors[k_x_GFR_GFR]*GFR))-factors[k_d_GFR]*GFR
	
	obs1 = FixedIntervalNumberObserver(period * 1.0 / steps, ('ERM','ERP','GFR','E2ER'))
	y = run_simulation(numpy.linspace(0, period, steps), {'ERM':initials[init_ERM],'ERP':initials[init_ERP],'GFR':initials[init_GFR],'E2ER':initials[init_E2ER]}, solver='ode', return_type=None, observers=(obs1))
	return obs1

# Auto-generation end

#Standard part
class ValuerBase:
	def getValue(self, variable, step):
		return 0
		

# Evaluate the current simulation with expected value
def evaluateByValue(data, expectedValuer):
	ret = 0.0
	counter = 0
	for i in range(len(data)):
		for j in range(1, len(data[i])): # index 0 is the time value
			diff = data[i][j] - valuer.getValue(i, j)
			ret += diff * diff
			counter += 1
	
	return ret / counter

def refineOneParameter(expectedValuer, bestRatingInfo, changeRatio, initials, period, steps, factorStopUpperLimit, factorStopLowerLimit):
	newBestRatingInfo = dict(bestRatingInfo)
	
	for i in range(k_count):
		if (bestRatingInfo["factors"][i] > factorStopUpperLimit or bestRatingInfo["factors"][i] < factorStopLowerLimit):
			continue;
		newFactors = bestRatingInfo["factors"][:]
		newFactors[i] = newFactors[i] * (1 + changeRatio)
		obs = start_simulation(newFactors, initials, period, steps)
		rating = evaluateByValue(obs.data(), expectedValuer)
		if (newBestRatingInfo["bestRating"] == -1 or rating < newBestRatingInfo["bestRating"]):
			newBestRatingInfo["bestRating"] = rating;
			newBestRatingInfo["factors"] = newFactors;
			newBestRatingInfo["bestRatingIndex"] = i;
			newBestRatingInfo["changed"] = True;
			
		newFactors = bestRatingInfo["factors"][:]
		newFactors[i] = newFactors[i] / (1 + changeRatio)
		obs = start_simulation(newFactors, initials, period, steps)
		rating = evaluateByValue(obs.data(), expectedValuer)
		if (rating < newBestRatingInfo["bestRating"]):
			newBestRatingInfo["bestRating"] = rating;
			newBestRatingInfo["factors"] = newFactors;
			newBestRatingInfo["bestRatingIndex"] = -i;
			newBestRatingInfo["changed"] = True
	
	return newBestRatingInfo

def parameterScan(expectedValuer, initials, ratioStopLimit, factorStopUpperLimit, factorStopLowerLimit, period, steps):
	# Initialize
	factors = [None] * k_count

	import random
	for i in range(k_count):
		factors[i] = 0.1 + 10 * random.random()
		
	changeRatio = 3.0

	tryCount = 0;
	clock = time.clock();
	
	bestRatingInfo = {"factors": factors, "bestRating": -1, "bestRatingIndex": k_count, "changed": False}
	while changeRatio > ratioStopLimit:
		tryCount = tryCount + 1
		bestRatingInfo["changed"] = False
		newBestRatingInfo = refineOneParameter(expectedValuer, bestRatingInfo, changeRatio, initials, period, steps, factorStopUpperLimit, factorStopLowerLimit)
		if (newBestRatingInfo["changed"] == False):
			changeRatio /= 2.0
		elif (newBestRatingInfo["bestRatingIndex"] + bestRatingInfo["bestRatingIndex"] == 0):
			changeRatio /= 2.0
		else:
			bestRatingInfo = newBestRatingInfo
	
	print(tryCount)
	clock = time.clock() - clock
	print(clock)
	
	return bestRatingInfo["factors"]

# End of standard part

class ValuerBase1(ValuerBase):
	def getValue(self, variable, step):
		return 1

sampleCount = 200
ratioStopLimit = 0.01
factorStopUpperLimit = 10000
factorStopLowerLimit = 0.0001

period = 5
steps = 100

initials = [None] * init_count
for i in range(init_count):
	initials[i] = 1;

parameters = [];
valuer = ValuerBase1()

print("Start...")
# First run parameter scanning
for i in range(sampleCount):
	factors = parameterScan(valuer, initials, ratioStopLimit, factorStopUpperLimit, factorStopLowerLimit, period, steps)
	parameters.append({"factors": factors})
	print(i)

# Evaluate the current simulation with last half change
def evaluateByChange(data):
	ret = 0.0
	counter = 0
	sampleLength = len(data)
	firstSampleIndex = sampleLength / 2
	secondSampleIndex = sampleLength - 1
	for i in range(1, len(data[0])):
		diff = (data[secondSampleIndex][i] - data[firstSampleIndex][i]) / (secondSampleIndex - firstSampleIndex)
		ret += diff * diff
		counter += 1
	
	return ret / counter

# Then evaluate every parameter scanning result by value and change rate
for i in range(len(parameters)):
	obs = start_simulation(parameters[i]["factors"], initials, period * 10, steps * 10)
	parameters[i]["obs"] = obs;
	parameters[i]["rating"] = evaluateByValue(obs.data(), valuer)
	parameters[i]["rating2"] = evaluateByChange(obs.data())
	
def getKey(item):
	return item["rating"]
	
parameters = sorted(parameters, key=getKey)

# Plot the distribution map
rating1s = []
rating2s = []
for i in range(sampleCount):
	rating1s.append(parameters[i]["rating"])
	rating2s.append(parameters[i]["rating2"])
	
import matplotlib.pyplot as plt
plt.plot(rating1s, rating2s, 'ro')
plt.xscale('log')
plt.yscale('log')
plt.xlabel('Rating 1')
plt.ylabel('Rating 2')

plt.axis([0, 6, 0, 20])
plt.show()

# Remove those converged too far from the epxected value

# Resort the new value by rating2 which is changes rate

for i in range(sampleCount):
	viz.plot_number_observer(parameters[i]["obs"])
	print(parameters[i]["rating"])
	print(parameters[i]["rating2"])
	print(parameters[i]["factors"])

