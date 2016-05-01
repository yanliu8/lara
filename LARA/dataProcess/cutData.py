import json
import csv
dic = {}
for i in range(53):
	with open('../Data/aspectRating/output_rest_prediction' + str(i) + '.dat') as aspectRating:
		csvreader = csv.reader(aspectRating, delimiter = "	")
		for row in csvreader:
			dic[row[0]] = row[1:]
print("done loading rating")
output = open("../Data/finaloutput.json", "w")
count = 0
with open("joined.json") as jsonfile:
	for item in jsonfile:
		config = json.loads(item)
		if (config['business_id'] in dic) :	
	    		del config["categories"]
	    		del config["stars"]
	    		for review in config['reviews']:
	    			del review["business_id"]
	    			del review["user_id"]
	    			del review["votes"]
	    			del review["review_id"]
	    			del review["date"]
	    		config["food"] = dic[config["business_id"]][0]
	    		config["service"] = dic[config["business_id"]][1]
	    		config["environment"] = dic[config["business_id"]][2]
	    		config["value"] = dic[config["business_id"]][3]
	    		json.dump(config, output)
	    		output.write('\n')
	    		count += 1
	    		if count % 1000 == 0:
	    			print("done writing" + str(count) + " resturants")
output.close()



		
