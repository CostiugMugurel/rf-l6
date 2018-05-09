package rf6;

public class Neighbor implements Comparable<Neighbor>{
	double grade; // nota 
	double distance; // distanta pana la punctul cautat
	String family; // insufficient, sufficient, bla bla
	
	public int compareTo(Neighbor compareNeighbor) {
		
		//ascending order
		return Double.compare(this.distance, compareNeighbor.distance);
					
	}	
		  

}
