public class Hierarchial {

	public static void getClusters(Double genes[][], int m, int n) {
		Double[][] distance = new Double[m][m];
		int minDist[] = new int[m];
		int i, j, l;
		Double dist = 0.0;
		for (i = 0; i < m; i++) {
			for (j = 0; j < m; j++) {
				dist = 0.0;
				if (i != j) {
					for (l = 2; l < n; l++)
						dist = Math.pow(genes[i][l] - genes[j][l], 2);
					distance[i][j] = Math.sqrt(dist);

					if (distance[i][j] < distance[i][minDist[i]])
						minDist[i] = j;
				} else {
					distance[i][j] = Double.POSITIVE_INFINITY;
				}

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
		for (i = 0; i < m; i++)
			System.out.println(minDist[i]);

		for (int s = 0; s < 380; s++) {

			int i1 = 0;
			for (i = 0; i < m; i++)
				if (distance[i][minDist[i]] < distance[i1][minDist[i1]])
					i1 = i;
			int i2 = minDist[i1];

			for (j = 0; j < m; j++)
				if (distance[i2][j] < distance[i1][j])
					distance[i1][j] = distance[j][i1] = distance[i2][j];
			distance[i1][i1] = Double.POSITIVE_INFINITY;

			for (i = 0; i < m; i++)
				distance[i2][i] = distance[i][i2] = Double.POSITIVE_INFINITY;

			for (j = 0; j < m; j++) {
				if (minDist[j] == i2)
					minDist[j] = i1;
				if (distance[i1][j] < distance[i1][minDist[i1]])
					minDist[i1] = j;
			}
		}

		System.out.println("After##");
		// print minDist matrix
		for (i = 0; i < m; i++)
			System.out.println(minDist[i]);

	}

}
