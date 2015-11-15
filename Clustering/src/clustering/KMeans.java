package clustering;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class KMeans {

	public static void getClusters(Double[][] genes, int m, int n, int k) {

		int i, j, l;
		Double dist = 0.0;
		Double[][] centroids = new Double[k][n - 2];
		int[] geneClusters = new int[m];
		int r, count = 0;
		Double minDist = Double.POSITIVE_INFINITY, sum = 0.0;
		Boolean done = false;
		Random rand = new Random();
		dist = 0.0;
		for (i = 0; i < k; i++) {
			r = rand.nextInt(m - 1 + 1) + 1;
			// System.out.println("R generated is:" + r);
			for (j = 2; j < n; j++)
				centroids[i][j - 2] = genes[r - 1][j];
		}
		System.out.println("k=" + k);
		// while(!done)
		for (int itr = 0; itr < 20; itr++) {

			// System.out.println("############## Iteration "+itr+" #######");
			count = 0;
			done = true;
			for (i = 0; i < m; i++) {
				if (geneClusters[i] <= 0) {
					minDist = Double.POSITIVE_INFINITY;

				} else {
					dist = 0.0;
					for (l = 2; l < n; l++) {
						dist += Math.pow(genes[i][l]
								- centroids[geneClusters[i] - 1][l - 2], 2);

					}
					minDist = dist;

				}

				for (j = 0; j < k; j++) {
					dist = 0.0;
					// if(j!=geneClusters[i])
					// {
					for (l = 2; l < n; l++) {
						// System.out.println("i:"+i+"j:"+j+"k:"+k);
						dist += Math.pow(genes[i][l] - centroids[j][l - 2], 2);

					}
					if (dist < minDist) {
						count++;
						done = false;
						minDist = dist;
						geneClusters[i] = j + 1;
					}
					// }
				}
				// System.out.println("geneClusters["+i+"]"+geneClusters[i]);

			}
			if (done) {
				System.out.println("Done is:" + done + " itr=" + itr);
				break;
			}

			// Recomputing centroids

			for (i = 0; i < k; i++) {
				for (j = 2; j < n; j++) {
					sum = 0.0;
					count = 0;
					for (l = 0; l < m; l++) {
						if ((geneClusters[l] - 1) == i) {
							sum += genes[l][j];
							count++;
						}

					}
					if (count != 0) {
						centroids[i][j - 2] = sum / count;
						// System.out.println("For cluster "+i+": count is:"+count);
					}
				}
			}

			if (done)
				break;

		}

		//
		// System.out.println("The clusters are:");
		// for(i=0;i<m;i++)
		// System.out.println(geneClusters[i]);
		//

		// count of each cluster
		HashMap<Integer, Integer> countGene = new HashMap<Integer, Integer>();

		for (i = 0; i < m; i++) {

			if (countGene.get(geneClusters[i]) == null)
				countGene.put(geneClusters[i], 1);
			else {
				int val = countGene.get(geneClusters[i]);
				countGene.put(geneClusters[i], ++val);
			}

		}
		System.out.println("The count is" + countGene);

		// External index

		int[][] iCluster = new int[m][m];
		int[][] iGroundTruth = new int[m][m];
		int agree = 0, total = 0;
		Double jaccard = 0.0;

		for (i = 0; i < m; i++) {
			for (j = i; j < m; j++) {
				if (geneClusters[i] == geneClusters[j]) {
					iCluster[i][j] = 1;
					iCluster[j][i] = 1;

				}

				if (genes[i][1].equals(genes[j][1])) {
					iGroundTruth[i][j] = 1;
					iGroundTruth[j][i] = 1;
				}

			}
		}

		for (i = 0; i < m; i++) {
			for (j = i; j < m; j++) {
				if (iCluster[i][j] == 1 && iGroundTruth[i][j] == 1)
					agree++;

				if (!(iCluster[i][j] == 0 && iGroundTruth[i][j] == 0))
					total++;
			}
		}

		jaccard = (double) agree / total;
		// System.out.println("Agree:" + agree + "  total:" + total);
		System.out.println("The Jaccard coeff is:" + jaccard);

		// System.out.println("The incidence matrix for ground truth:"
		// + iGroundTruth);
		// System.out.println("The incidence matrix for clusters:" + iCluster);

		/*
		 * //Print matrices for( i=0;i<m;i++) { for( j=0;j<m;j++) {
		 * System.out.print(iCluster[i][j]+"  "); } System.out.println(); }
		 * 
		 * System.out.println("##################### GROUND TRUTH #############")
		 * ; for( i=0;i<m;i++) { for( j=0;j<m;j++) {
		 * System.out.print(iGroundTruth[i][j]+"  "); } System.out.println(); }
		 */

		// Correaltion

		Double[][] distance = new Double[m][m];

		for (i = 0; i < m; i++) {
			for (j = 0; j < m; j++) {
				if (i != j) {
					for (l = 2; l < n; l++)
						dist = dist + Math.pow(genes[i][l] - genes[j][l], 2);
					distance[i][j] = Math.sqrt(dist);
					dist = 0.0;
				} else {
					distance[i][j] = Double.POSITIVE_INFINITY;
				}

			}

		}

		Double meanD = 0.0, meanC = 0.0;
		Double numerator = 0.0, denom1 = 0.0, denom2 = 0.0, denom = 0.0;
		Double correlation = 0.0;
		for (i = 0; i < m; i++) {
			for (j = 0; j < m; j++) {
				if (i != j) {
					meanD = meanD + distance[i][j];
					meanC = meanC + iCluster[i][j];
				}

			}
		}

		meanD = meanD / (m * m);
		meanC = meanC / (m * m);

		for (i = 0; i < m; i++) {
			for (j = 0; j < m; j++) {
				if (i != j) {

					numerator = numerator
							+ ((distance[i][j] - meanD) * (iCluster[i][j] - meanC));
					denom1 = denom1 + Math.pow((distance[i][j] - meanD), 2);
					denom2 = denom2 + Math.pow((iCluster[i][j] - meanC), 2);
				}

			}
		}

		denom = Math.sqrt(denom1) * Math.sqrt(denom2);
		correlation = numerator / denom;

		System.out.println("Correlation is:" + correlation);

		// PCA

		try {
			FileWriter fw = new FileWriter(new File(
					"D:\\Pycharm Workspace\\KMeans\\datasetPCA1.txt"));
			BufferedWriter bw = new BufferedWriter(fw);
			for (i = 0; i < m; i++) {
				for (j = 2; j < n; j++) {
					if (j != n - 1)
						bw.write(genes[i][j].toString() + "\t");
					else
						bw.write(genes[i][j].toString());
				}
				bw.newLine();
			}

			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Cannot open");
			e.printStackTrace();
		}

		try {
			FileWriter fw = new FileWriter(new File(
					"D:\\Pycharm Workspace\\KMeans\\datasetPCA2.txt"));
			BufferedWriter bw = new BufferedWriter(fw);
			for (i = 0; i < m; i++) {
				bw.write(String.valueOf(geneClusters[i]));
				bw.newLine();
			}

			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Cannot open");
			e.printStackTrace();
		}

	}

}
