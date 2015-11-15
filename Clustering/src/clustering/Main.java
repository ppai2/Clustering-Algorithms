package clustering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {

	public static void main(String args[]) {
		
		String dataDir = "C:\\Users\\Prasanna\\Google Drive\\SUNY_Buffalo\\SUNY_Buffalo\\UB_Grad_Courses\\Data Mining and Bioinformatics\\Project 2\\";
		Double[][] genes;
		String line;
		String[] val;
		Double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY;
		BufferedReader br;
		int m = 0, n = 0, i, j;
		try {
			br = new BufferedReader(new FileReader(new File(
					dataDir + File.separator + "dataset1.txt")));
			while ((line = br.readLine()) != null) {
				m++;
				if (n == 0) {
					n = line.split("\t").length;
				}
			}

			genes = new Double[m][n];
			br = new BufferedReader(new FileReader(new File(
					dataDir + File.separator + "dataset1.txt")));
			for (i = 0; i < m; i++) {
				line = br.readLine();
				val = line.split("\t");

				for (j = 0; j < n; j++)
					genes[i][j] = Double.parseDouble(val[j]);

			}
			
			FileWriter fw = new FileWriter("input_data_matrix.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			for (int idx = 0; idx < m; idx++) {
				for (int idxx = 2; idxx < n; idxx++) {
					bw.write(genes[idx][idxx].toString());
					if (idxx < n-1)
						bw.write("\t");
				}
				bw.newLine();
			}
			bw.flush();
			bw.close();

			System.out.println("m=" + m + " n=" + n);
		
			Double[] epss = new Double[]{1.0, 1.2, 1.4, 1.6, 1.8, 2.0};
			int[] minPtss = new int[]{7, 23, 39, 55};
			Double maxJeps = 0.0;
			int maxJminpts = 0;
			Double maxReps = 0.0;
			int maxRminpts = 0;
			Double maxJacc = 0.0;
			Double maxRand = 0.0;
			Double maxCeps = 0.0;
			int maxCminpts = 0;
			int maxClusters = 0;
			int itr = 0;
			//Double[] jaccardRand = DBScan.getClusters(genes, m, n, 1.6, 55);
			for (int ep = 0; ep < epss.length; ep++) {
				Double eps = epss[ep];
				for (int mp = 0; mp < minPtss.length; mp++) {
					int minPts = minPtss[mp];
					System.out.println(eps + " | " + minPts + " | " + itr);
					Double[] jaccardRand = DBScan.getClusters(genes, m, n, eps, minPts, itr);
					itr++;
					if (jaccardRand[0] > maxJacc) {
						maxJacc = jaccardRand[0];
						maxJeps = eps;
						maxJminpts = minPts;
					}
					if (jaccardRand[1] > maxRand) {
						maxRand = jaccardRand[1];
						maxReps = eps;
						maxRminpts = minPts;
					}
					if (jaccardRand[2] > maxClusters) {
						maxClusters = jaccardRand[2].intValue();
						maxCeps = eps;
						maxCminpts = minPts;
					}
				}
			}
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("Max Jaccard = " + maxJacc + ", eps: " + maxJeps + ", minPts: " + maxJminpts);
			System.out.println("Max Rand = " + maxRand + ", eps: " + maxReps + ", minPts: " + maxRminpts);
			System.out.println("Max Clusters = " + maxClusters + ", eps: " + maxCeps + ", minPts: " + maxCminpts);

			// Normalizing
			for (i = 2; i < n; i++) {
				max = Double.NEGATIVE_INFINITY;
				min = Double.POSITIVE_INFINITY;

				for (j = 0; j < m; j++) {
					if (genes[j][i] > max)
						max = genes[j][i];

					if (genes[j][i] < min)
						min = genes[j][i];
				}

				for (j = 0; j < m; j++)
					genes[j][i] = ((genes[j][i] - min) / (max - min));

			}

			//KMeans.getClusters(genes, m, n, 5);
			//Hierarchial.getClusters(genes, m, n);
			
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
