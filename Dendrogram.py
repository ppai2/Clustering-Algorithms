from matplotlib import pyplot as plt
from scipy.cluster.hierarchy import dendrogram, linkage
import numpy as np
X = np.loadtxt(fname="D:\Workspace\Clustering\input_data_matrix.txt", delimiter="\t")
print X.shape
Z = linkage(X, 'ward')
print Z.shape
plt.figure(figsize=(25, 10))
plt.title('Hierarchical Clustering Dendogram')
plt.xlabel('index')
plt.ylabel('distance')
dendrogram(
    Z,
    truncate_mode='lastp',
    p = 16,
    show_leaf_counts=False,
    leaf_rotation=90.,  # rotates the x axis labels
    leaf_font_size=8.,  # font size for the x axis labels
    show_contracted=True
)
plt.show()