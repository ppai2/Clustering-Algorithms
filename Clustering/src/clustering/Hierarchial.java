package clustering;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class Hierarchial {

	public static void getClusters(Double genes[][], int m, int n) {
		Double[][] distance = new Double[m][m];
		int minDist[] = new int[m];
		int i, j, l,x=0,y=0;
		int k = 6;
		Double dist = 0.0;
		for (i = 0; i < m; i++) {
			for (j = 0; j < m; j++) {
				if (i != j) {
					for (l = 2; l < n; l++)
						dist = Math.pow(genes[i][l] - genes[j][l], 2);
					distance[i][j] = Math.sqrt(dist);
					dist = 0.0;
				} else {
					distance[i][j] = Double.POSITIVE_INFINITY;
				}

			}

		}

		for (i = 0; i < m; i++) {
			for (j = 0; j < m; j++) {

				if (distance[i][j] < distance[i][minDist[i]])
					minDist[i] = j;

			}
		}

		// printing the distance matrix
		// for (i = 0; i < m; i++) {
		// for (j = 0; j < m; j++) {
		// System.out.print(distance[i][j] + "\t");
		// }
		// System.out.println();
		// }

		// print minDist matrix
		// for (i = 0; i < m; i++)
		// System.out.println(minDist[i]);

		for (int w = 0; w < (m - k); w++) {

			 x = 0;
			for (i = 0; i < m; i++)
				if (distance[i][minDist[i]] < distance[x][minDist[x]])
					x = i;
			 y = minDist[x];

			for (j = 0; j < m; j++)
				if (distance[y][j] < distance[x][j])
				{
					distance[x][j] = distance[y][j];
					distance[j][x] = distance[y][j];
				}
			distance[x][x] = Double.POSITIVE_INFINITY;

			for (i = 0; i < m; i++)
			{
				distance[y][i] = Double.POSITIVE_INFINITY;
				distance[i][y] = Double.POSITIVE_INFINITY;
			}

			for (j = 0; j < m; j++) {
				if (minDist[j] == y)
					minDist[j] = x;
				if (distance[x][j] < distance[x][minDist[x]])
					minDist[x] = j;
			}
		}

		System.out.println("After##");
		// print minDist matrix
		for (i = 0; i < m; i++)
			System.out.println(minDist[i]);

		// count of each cluster
		HashMap<Integer, Integer> countGene = new HashMap<Integer, Integer>();

		for (i = 0; i < m; i++) {

			if (countGene.get(minDist[i]) == null)
				countGene.put(minDist[i], 1);
			else {
				int val = countGene.get(minDist[i]);
				countGene.put(minDist[i], ++val);
			}

		}
		System.out.println("The count is" + countGene);

		// External index

		// Jaccard

		int[][] iCluster = new int[m][m];
		int[][] iGroundTruth = new int[m][m];
		int agree = 0, total = 0;
		Double jaccard = 0.0;

		for (i = 0; i < m; i++) {
			for (j = i; j < m; j++) {
				if (minDist[i] == minDist[j]) {
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
		System.out.println("Agree:" + agree + "  total:" + total);
		System.out.println("the Jaccard coeff is:" + jaccard);

		for (i = 0; i < m; i++) {
			for (j = 0; j < m; j++) {
				if (i != j) {
					for (l = 2; l < n; l++)
						dist = Math.pow(genes[i][l] - genes[j][l], 2);
					distance[i][j] = Math.sqrt(dist);
					dist = 0.0;
				} else {
					distance[i][j] = Double.POSITIVE_INFINITY;
				}

			}

		}

		// Correaltion

		Double meanD = 0.0, meanC = 0.0;
		Double numerator = 0.0, denom1 = 0.0, denom2 = 0.0, denom = 0.0;
		Double correlation = 0.0;
		for (i = 0; i < m; i++) {
			for (j = 0; j < m; j++) {
				if (i != j) {
					if (distance[i][j] == Double.POSITIVE_INFINITY)
						System.out.println("Found infy");
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

		System.out.println("Correlation:" + correlation);

		
		
		//PCA
		
		
		try {
			FileWriter fw=new FileWriter(new File("D:\\Pycharm Workspace\\Hierarchical\\datasetPCA1.txt"));
			BufferedWriter bw=new BufferedWriter(fw);
			for(i=0;i<m;i++)
			{
				for(j=2;j<n;j++)
				{ 
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
			FileWriter fw=new FileWriter(new File("D:\\Pycharm Workspace\\Hierarchical\\datasetPCA2.txt"));
			BufferedWriter bw=new BufferedWriter(fw);
			for(i=0;i<m;i++)
			{
				bw.write(String.valueOf(minDist[i]));
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
