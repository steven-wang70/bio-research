import mygene
import sys

# query the service
def createDict(names):
	results = mg.querymany(names, scopes='symbol', fields='entrezgene', species='human')
	dict = {}
	for item in results:
		if 'notfound' not in item:
			dict[item['query']] = item['_id']
	return dict

# Collect the input request	
names = [];
for line in sys.stdin:
	line = line.strip('\n')
	names.append(line)

# Create name keys
keys = set()
for item in names:
	if item not in keys:
		keys.add(item)
		
mg = mygene.MyGeneInfo()
dict = createDict(list(keys))

for item in names:
	if item in dict:
		print('ENT_' + dict[item])
	else:
		print(item + "***notfound")