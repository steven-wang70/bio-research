#!/usr/bin/python

# Usage:
# filterGene regulationConfidence regulationDef threshold

import sys

# Read regulation def file
def loadRegulationDefs(defFile):
	defs = {}
	fo = open(defFile, 'r')
	counter = 0
	while True:
		counter += 1;
		line = fo.readline()
		if line == '':
			break;
		line = line.strip('\n')
		items = line.split(',');
		if len(items) != 5:
			print('Something wrong in the regulation at line: ' + counter)
			break;
			
		if items[4] != '3' and items[4] != '6':
			continue;
		
		defs[items[1] + ',' + items[2]] = items[4]
	
	fo.close()
	return defs
	

# Read regulation confidence file
def loadRegulationConfs(conFile, threshold):
	fo = open(conFile, 'r')
	counter = 0
	while True:
		counter += 1;
		line = fo.readline()
		if line == '':
			break;
		line = line.strip('\n')
		items = line.split(',');
		if len(items) != 3:
			print('Something wrong in the regulation at line: ' + counter)
			break;
			
		conf = float(items[2])
		if conf < threshold:
			continue;
		
		key = items[0] + ',' + items[1]
		if key in regDefs:
			print('HS,' + key + ',1,' + regDefs[key])
			
	fo.close()


defFile = sys.argv[1]
conFile = sys.argv[2]
threshold = float(sys.argv[3])
print(threshold)

regDefs = loadRegulationDefs(defFile)
loadRegulationConfs(conFile, threshold)
