import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;

public class Inference{

	// variables
	String docID;
	String num;
	String significance;
	NodeList categoryOfInferences;
	List<String> categoryInf;
	NodeList domains;
	List<String> domain;
	
	// constructor
	public Inference(){}
	
	// method to:
	// copy Inferences from a NodeList into an ArrayList
	public void NLtoArray()
	{
		ArrayList<String> PEcatInf = sortCat(categoryOfInferences);
		
		if(PEcatInf != null)
		{
			categoryInf = new ArrayList<String>();
			for(int i = 0; i < PEcatInf.size(); i++)
			{
				categoryInf.add(PEcatInf.get(i));
			}
		}
		if(domains != null)
		{
			domain = new ArrayList<String>();
			for(int i = 0; i < domains.getLength(); i++)
			{
				domain.add(domains.item(i).getTextContent());
			}
		}
	}
	
	// method to:
	// deal with multiple attributes in multiple Category of Inference tags
	// by going through each <categoryInf> tag and concatenating each of its child tags
	// then adding them, unedited, into an ArrayList
	public ArrayList<String> sortCat(NodeList nl)
	{
		String infName = "";
		ArrayList<String> PEcatInf = new ArrayList<String>();

		for(int j = 0; j < nl.getLength(); j++)
		{
			int k = 0;
			Node currentNode = nl.item(j).getChildNodes().item(0);
			while(currentNode != null)
			{
				infName += idInf(nl.item(j).getChildNodes().item(k).getTextContent()).trim() + " ";
				
				currentNode = currentNode.getNextSibling();
				k++;
			}
			PEcatInf.add(infName);
			infName = "";
		}
		return PEcatInf;
	}
	
	// method to:
	// take only the important part of the String in <categoryInf>'s child tags
	public String idInf(String s)
	{
		
		String word = "";
		int firstIndex = 0;
		int lastIndex = 0;
		
		for(int i = 0; i < s.length(); i++)
		{
			if(Character.isLetter(s.charAt(i)))
			{
				firstIndex = i;
				break;
			}
		}
		for(int i = 0; i < s.length(); i++)
		{
			String c = Character.toString(s.charAt(i));
			if((c.equals("=")))
			{
				lastIndex = i - 1;
				break;
			}
			else
			{
				lastIndex = s.length();
			}
		}

		word = s.substring(firstIndex, lastIndex);
		
		return word;
	}
}