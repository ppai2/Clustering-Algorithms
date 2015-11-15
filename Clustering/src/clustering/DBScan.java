package clustering;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;


public class DBScan {
	
	public static ArrayList<Double> visited;
	public static ArrayList<Double> noise;
	public static ArrayList<ArrayList<Double>> clusters;
	
	public static Double maxDist = Double.NEGATIVE_INFINITY;
	public static Double minDist = Double.POSITIVE_INFINITY;
	public static Double avgDist = 0.0;
	public static Double distCount = 0.0;
	
	public static Double[] getClusters(Double[][] genes, int m, int n, Double eps, int minPts, int itr) {
		
		visited = new ArrayList<>();
		noise = new ArrayList<>();
		clusters = new ArrayList<>();
		
		maxDist = Double.NEGATIVE_INFINITY;
		minDist = Double.POSITIVE_INFINITY;
		avgDist = 0.0;
		distCount = 0.0;
		
		for (int i = 0; i < m; i++) {
			Double[] gene = genes[i];
			Double geneId = gene[0];
			if (!visited.contains(geneId)) {
				visited.add(geneId);
				ArrayList<Double> neighbors = getNeighbors(gene, genes, m, n, eps);
				
				if (neighbors.size() < minPts) {
					noise.add(geneId);
				} else {
					//System.out.println("Forming new cluster....");
					ArrayList<Double> c = new ArrayList<>();
					c.add(geneId);
					c = expandCluster(c, gene, neighbors, genes, m, n, eps, minPts);
					clusters.add(c);
				}
			}
		}
		avgDist = avgDist / distCount;
//		System.out.println("minDist: " + minDist);
//		System.out.println("maxDist: " + maxDist);
//		System.out.println("avgDist: " + avgDist);
		System.out.println("----------------------------------------");
		System.out.println("eps = " + eps + ", minPts = " + minPts);
		System.out.println("Number of clusters = " + clusters.size());
		Double[] jaccardRand = getJaccardRand(genes, clusters, m, itr);
		System.out.println("Jaccard = " + jaccardRand[0]);
		System.out.println("Rand = " + jaccardRand[1]);
		Double[] output = new Double[3];
		output[0] = jaccardRand[0];
		output[1] = jaccardRand[1];
		output[2] = (double) clusters.size();
		return output;
	}
	
	public static ArrayList<Double> expandCluster(ArrayList<Double> cluster, Double[] gene, ArrayList<Double> neighbors, Double[][] genes, 
			int m, int n, Double eps, int minPts) {
		
		for (int i = 0; i < neighbors.size(); i++) {
			Double nbr = neighbors.get(i);
			Double[] neighbor = genes[nbr.intValue() - 1];
			
			if (!visited.contains(nbr)) {
				visited.add(nbr);
				ArrayList<Double> neighbors1 = getNeighbors(neighbor, genes, m, n, eps);
				if (neighbors1.size() >= minPts) {
					neighbors = joinNeighbors(neighbors, neighbors1);
				}
			}
			
			boolean inCluster = false;
			if (cluster.contains(nbr)) {
				inCluster = true;
			}
			for (ArrayList<Double> cl : clusters) {
				if (cl.contains(nbr)) {
					inCluster = true;
					break;
				}
			}
			if (!inCluster) {
				if (noise.contains(nbr)) {
					noise.remove(nbr);
				}
				cluster.add(nbr);
			}
		}
		
		return cluster;
	}
	
	public static ArrayList<Double> joinNeighbors(ArrayList<Double> n, ArrayList<Double> n1) {
		ArrayList<Double> nTemp = new ArrayList<>(n);
		for (Double pt : n1) {
			if (!nTemp.contains(pt)) {
				n.add(pt);
			}
		}
		return n;
	}
	
	public static ArrayList<Double> getNeighbors(Double[] gene, Double[][] genes, int m, int n, Double eps) {
		ArrayList<Double> neighbors = new ArrayList<>();
		
		for (int i = 0; i < m; i++) {
			Double[] gn = genes[i];
			Double dist = getDistance(gene, gn, m, n);
			if (dist <= eps) {
				neighbors.add(gn[0]);
			}
		}
		
		//System.out.println("Gene-" + gene[0] + ", Neighbors = " + neighbors.size());
		
		return neighbors;
	}
	
	public static Double getDistance(Double[] gene1, Double[] gene2, int m, int n) {
		Double dist = 0.0;
		
		for (int i = 2; i < n; i++) {
			Double diff = gene1[i] - gene2[i];
			dist += Math.pow(diff, 2);
		}
		dist = Math.sqrt(dist);
		
		if (dist < minDist) {
			minDist = dist;
		}
		if (dist > maxDist) {
			maxDist = dist;
		}
		avgDist += dist;
		distCount += 1.0;
		
		return dist;
	}
	
	public static Double[][] getP(Double[][] genes, int m) {
		Double[][] P = new Double[m][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				if (genes[i][1].equals(genes[j][1]))
					P[i][j] = 1.0;
				else
					P[i][j] = 0.0;
			}
		}
		
		return P;
	}
	
	public static Double[][] getC(Double[][] genes, ArrayList<ArrayList<Double>> cls, int m, int itr) {
		Double[][] C = new Double[m][m];
		
		Integer[][] clusterMap = new Integer[m][2];
		TreeMap<Integer, Integer> map = new TreeMap<>();
		for (int i = 0; i < cls.size(); i++) {
			ArrayList<Double> cl = cls.get(i);
			for (Double pt : cl) {
				map.put(pt.intValue(), i+1);
			}
		}
		for (int i = 0; i < m; i++) {
			clusterMap[i][0] = i+1;
			if (map.containsKey(clusterMap[i][0])) {
				clusterMap[i][1] = map.get(clusterMap[i][0]);
			} else {
				clusterMap[i][1] = 0;
			}
		}
		
		try {
			FileWriter fw = new FileWriter(itr + ".txt");
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < m; i++) {
				bw.write(clusterMap[i][1].toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				if (clusterMap[i][1] == clusterMap[j][1])
					C[i][j] = 1.0;
				else
					C[i][j] = 0.0;
			}
		}
		
		return C;
	}
	
	public static Double[] getJaccardRand(Double[][] genes, ArrayList<ArrayList<Double>> cls, int m, int itr) {
		Double[] jaccardRand = new Double[2];
		Double SD = 0.0;
		Double DS = 0.0;
		Double SS = 0.0;
		Double DD = 0.0;
		
		Double[][] P = getP(genes, m);
		Double[][] C = getC(genes, cls, m, itr);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				if (!P[i][j].equals(C[i][j]) && C[i][j].equals(1.0))
					SD += 1.0;
				else if (!P[i][j].equals(C[i][j]) && C[i][j].equals(0.0))
					DS += 1.0;
				else if (P[i][j].equals(C[i][j]) && C[i][j].equals(1.0))
					SS += 1.0;
				else if (P[i][j].equals(C[i][j]) && C[i][j].equals(0.0))
					DD += 1.0;
			}
		}
		
		Double jaccard = SS / (SD + DS + SS);
		Double rand = (DD + SS) / (SD + DS + SS + DD);
		jaccardRand[0] = jaccard;
		jaccardRand[1] = rand;
		
		return jaccardRand;
	}
	
}