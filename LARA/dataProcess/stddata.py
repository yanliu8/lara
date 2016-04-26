import csv
import numpy as np
def stddata(file):
	filename = '../Data/Results/' + file
	data = []
	with open(filename, 'r') as csvfile:
		csvreader = csv.reader(csvfile, delimiter='	')
		for row in csvreader:
			while '' in row:
				row.remove('')
			data.append(row)
	data = np.array(data)
	ID = data[:,0]
	aspectRating = data[:,6:10].astype(np.float)
	for col in range(aspectRating.shape[1]):
		aspectRating[:,col] *= (2 / np.std(aspectRating[:,col]))
		aspectRating[:,col] += (3 - np.mean(aspectRating[:,col]))
		
		for row in range(aspectRating.shape[0]):
			if aspectRating[row, col] > 5:
				aspectRating[row, col] = 5
			elif aspectRating[row, col] < 0:
				aspectRating[row, col] = 0
			else:
				aspectRating[row, col] = round(aspectRating[row, col], 1)
	with open('../Data/aspectRating/output_' + file , 'w') as outputfile:
		writer = csv.writer(outputfile, delimiter = '	')
		for i in range(aspectRating.shape[0]):
			writer.writerow(np.append([ID[i]],aspectRating[i]))

if __name__ == "__main__":
	for i in range(53):
		stddata('rest_prediction' + str(i) + '.dat')


