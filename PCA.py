import matplotlib.pyplot as plt
import numpy as np
from sklearn.decomposition import PCA

print "before X"
X = np.loadtxt(fname="D:\Workspace\Clustering\input_data_matrix.txt", delimiter="\t")                        # input matrix of 517x12 data
print X.shape
y = np.array(np.loadtxt(fname="2.txt"))
print y.shape
target_names = [0, 1, 2, 3, 4]

try:
    pca = PCA(n_components=2)
    print X
    X_r = pca.fit(X).transform(X)
    print X_r
except ValueError:
    print("That is not a valid number for float")

# Percentage of variance explained for each components
print('explained variance ratio (first two components): %s'
      % str(pca.explained_variance_ratio_))

plt.figure()
plt.scatter(X_r[:, 0], X_r[:, 1], c=y)
plt.legend()
plt.title('PCA')

plt.show()
