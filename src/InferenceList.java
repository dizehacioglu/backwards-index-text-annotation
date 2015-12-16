import java.util.*;


public class InferenceList extends LinkedList<Inference> implements Comparable<InferenceList> {
	
	String key;
	
	public InferenceList(String s){
		
		this.key = s;
	}
	
	// method to:
	// print out inference's identifying information for the index
	public void display()
	{
		Inference[] infArray = this.toArray(new Inference[this.size()]);
		
		for (int i = 0; i < infArray.length; i++) {
			
			if(i == (infArray.length - 1))
			{
				System.out.println(infArray[i].docID + "." + infArray[i].num);
			}
			else
			{
				System.out.print(infArray[i].docID + "." + infArray[i].num + ", ");
			}
		}
	}
	
	// Overriding Comparable's compareTo() method to alphabetize index according to the InferenceList's key
	@Override
	public int compareTo(InferenceList otherInf) {

		return this.key.compareTo(otherInf.key);
	}
	


}
