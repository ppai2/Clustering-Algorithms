import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String args[]) {
		Double[][] genes;
		String line;
		String[] val;
		Double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY;
		BufferedReader br;
		int m = 0, n = 0, i, j;
		try {

			br = new BufferedReader(new FileReader(new File(
					"C:\\Users\\Prasanna\\Google Drive\\SUNY_Buffalo\\SUNY_Buffalo\\UB_Grad_Courses\\Data Mining and Bioinformatics\\Project 2\\cho.txt")));
			while ((line = br.readLine()) != null) {
				m++;
				if (n == 0) {
					n = line.split("\t").length;
				}
			}

			genes = new Double[m][n];
			br = new BufferedReader(new FileReader(new File(
					"C:\\Users\\Prasanna\\Google Drive\\SUNY_Buffalo\\SUNY_Buffalo\\UB_Grad_Courses\\Data Mining and Bioinformatics\\Project 2\\cho.txt")));
			for (i = 0; i < m; i++) {
				line = br.readLine();
				val = line.split("\t");

				for (j = 0; j < n; j++)
					genes[i][j] = Double.parseDouble(val[j]);

			}

			System.out.println("m=" + m + " n=" + n);

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

			// KMeans.getClusters(genes, m, n, 5);
			Hierarchial.getClusters(genes, m, n);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
