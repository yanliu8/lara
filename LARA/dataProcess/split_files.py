jsonfile = open('joined.json','r')
linecount = 0
filecount = 0
outputfile = open('resturant'+str(filecount)+'.json', 'w')
for line in jsonfile:
	outputfile.write(line)
	linecount += 1
	if linecount == 1000:
		filecount += 1
		outputfile.close()
		outputfile = open('resturant'+str(filecount)+'.json', 'w')
		linecount = 0
outputfile.close()
		
