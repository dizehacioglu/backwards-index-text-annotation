// read in XML file
//provide backwards index for "significance," "category of inference," and "domain" elements
import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class backindex{
	
	static List<InferenceList> sigIndex = new ArrayList<InferenceList>();
	static List<InferenceList> catIndex = new ArrayList<InferenceList>();
	static List<InferenceList> domIndex = new ArrayList<InferenceList>();
	
	public static void analyzeXML(String f) throws ParserConfigurationException, SAXException, IOException
	{
		// read in XML file
		File xmlFile = new File(f + ".xml");
	
		// create a DOM XML Parser, then parse the given XML file
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xmlFile);
		
		// normalize the XML file
		doc.getDocumentElement().normalize();
		
		// get the docID
		String docID = doc.getDocumentElement().getElementsByTagName("docID").item(0).getTextContent();
		
		// create a list of nodes, each node representing an instance of the <annotation> tag
		NodeList nL = doc.getElementsByTagName("annotation");
		
		if(nL != null){
		
			// traverse through the list of nodes
			for(int i = 0; i < nL.getLength(); i++)
			{
				// access each node as an element
				Element e = (Element) nL.item(i);
				
				// make sure that the inference/annotation has not been deleted
				if(!e.getElementsByTagName("inference").item(0).getTextContent().equals(" Deleted. Renumber")){
					
					// create new inference and assign appropriate values to each of its variables
					Inference inf = new Inference();
					
					inf.docID = docID;
					inf.num = e.getElementsByTagName("infNumber").item(0).getTextContent().replaceAll("\\s+", "");
					inf.significance = e.getElementsByTagName("significance").item(0).getTextContent();
					inf.categoryOfInferences = e.getElementsByTagName("categoryInf");
					inf.domains = e.getElementsByTagName("domain");
					//inf.domain.add(e.getElementsByTagName("domain").item(0).getTextContent());

					sigIndex = createIndex(sigIndex, inf);
					catIndex = createIndex(catIndex, inf);
					domIndex = createIndex(domIndex, inf);
				}
			}
		}
		else
		{
			System.out.println("There are no annotations or inferences in this text.");
		}
	}
	
	public static ArrayList<InferenceList> createIndex(List<InferenceList> sigIndex2, Inference inf)
	{
		inf.NLtoArray();
		
		if(sigIndex2 == sigIndex)
		{
			if(sigIndex2.size() == 0)
			{
				sigIndex2.add(new InferenceList(inf.significance));
				sigIndex2.get(0).add(inf);
			}
			else
			{
				for(int j = 0; j < sigIndex2.size(); j++)
				{
					if((normalizeText(sigIndex2.get(j).key)).contains(normalizeText(inf.significance)))
					{
						sigIndex2.get(j).add(inf);
						break;
					}
					else if(!(normalizeText(sigIndex2.get(j).key)).contains(normalizeText(inf.significance)) && j == sigIndex2.size() - 1)
					{
						sigIndex2.add(new InferenceList(inf.significance));
						sigIndex2.get(sigIndex2.size()-1).add(inf);
						break;
					}
					
				}
			}
		}
		
		else if(sigIndex2 == domIndex)
		{
			if(sigIndex2.size() == 0)
			{
				for(int j = 0; j < inf.domain.size(); j++)
				{
					
					sigIndex2.add(new InferenceList(inf.domain.get(j)));
					sigIndex2.get(0).add(inf);
				}	
			}
			else
			{
				for(int j = 0; j < inf.domain.size(); j++)
				{
					for(int k = 0; k < sigIndex2.size(); k++)
					{
						if((normalizeText(sigIndex2.get(k).key)).contains(normalizeText(inf.domain.get(j))))
						{
							sigIndex2.get(k).add(inf);
							break;
						}
						else if(!(normalizeText(sigIndex2.get(k).key)).contains(normalizeText(inf.domain.get(j))) && k == sigIndex2.size() - 1)
						{
							sigIndex2.add(new InferenceList(inf.domain.get(j)));
							sigIndex2.get(sigIndex2.size()-1).add(inf);
							break;
						}
					}
				}
			}
		}
		
		else if(sigIndex2 == catIndex)
		{
			if(sigIndex2.size() == 0)
			{
				for(int j = 0; j < inf.categoryInf.size(); j++)
				{
					
					sigIndex2.add(new InferenceList(inf.categoryInf.get(j)));
					sigIndex2.get(0).add(inf);
				}	
			}
			else
			{
				for(int j = 0; j < inf.categoryInf.size(); j++)
				{
					for(int k = 0; k < sigIndex2.size(); k++)
					{
						if((normalizeText(sigIndex2.get(k).key)).contains(normalizeText(inf.categoryInf.get(j))))
						{
							sigIndex2.get(k).add(inf);
							break;
						}
						else if(!(normalizeText(sigIndex2.get(k).key)).contains(normalizeText(inf.categoryInf.get(j))) && k == sigIndex2.size() - 1)
						{
							sigIndex2.add(new InferenceList(inf.categoryInf.get(j)));
							sigIndex2.get(sigIndex2.size()-1).add(inf);
							break;
						}
					}
				}
			}
		}
		
		return (ArrayList<InferenceList>) sigIndex2;
	}
	
	public static String normalizeText(String s)
	{
		String newS = s.toLowerCase();
		newS = newS.replaceAll("[^a-zA-Z]","");
		
		
		return newS;
	}
	
	public static String normalizeKey(String s)
	{
		
		return s.trim().replaceAll("[^a-zA-Z\\s]", "").replaceAll("\\s+", " ");
	}
	
	
	public static void listInferences(String option)
	{
		Collections.sort(sigIndex);
		Collections.sort(catIndex);
		Collections.sort(domIndex);
		
		if(option.equals("all")){
			System.out.println("***LINGUISTIC SIGNIFICANCE INDEX***");
			for(int i = 0; i < sigIndex.size(); i++)
			{
				System.out.print(normalizeKey(sigIndex.get(i).key) + ": ");
				sigIndex.get(i).display();
				System.out.println();
			}
			
			System.out.println();
			System.out.println("***CATEGORY OF INFERENCE INDEX***");
			for(int i = 0; i < catIndex.size(); i++)
			{
				System.out.print(normalizeKey(catIndex.get(i).key) + ": ");
				catIndex.get(i).display();
				System.out.println();
			}
			
			System.out.println();
			System.out.println("***DOMAIN INDEX***");
			for(int i = 0; i < domIndex.size(); i++)
			{
				System.out.print(normalizeKey(domIndex.get(i).key) + ": ");
				domIndex.get(i).display();
				System.out.println();
			}
			
		}
		
		else if(option.equals("sig"))
		{
			System.out.println("***LINGUISTIC SIGNIFICANCE INDEX***");
			for(int i = 0; i < sigIndex.size(); i++)
			{
				System.out.print(sigIndex.get(i).key + ": ");
				sigIndex.get(i).display();
				System.out.println();
			}
		}
		
		else if(option.equals("cat"))
		{
			System.out.println();
			System.out.println("***CATEGORY OF INFERENCE INDEX***");
			for(int i = 0; i < catIndex.size(); i++)
			{
				System.out.print(catIndex.get(i).key + ": ");
				catIndex.get(i).display();
				System.out.println();
			}
		}
		
		else if(option.equals("dom"))
		{
			System.out.println();
			System.out.println("***DOMAIN INDEX***");
			for(int i = 0; i < domIndex.size(); i++)
			{
				System.out.print(domIndex.get(i).key + ": ");
				domIndex.get(i).display();
				System.out.println();
			}
		}
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException 
	{
		// ask user for files to analyze
		Scanner input = new Scanner(System.in);
		System.out.print("List files to analyze (ex. N1, N2): ");
		String listOfFiles = input.nextLine();
		
		System.out.println("Menu: ");
		System.out.println("(1) General Index");
		System.out.println("(2) Significance Index");
		System.out.println("(3) Category of Inference Index");
		System.out.println("(4) Domain Index");
		System.out.println("-------------------------");
		System.out.print("Menu option: ");
		int option = input.nextInt();
		System.out.println();
		
		// ANALYZE FILES
		
		String[] splitFiles = listOfFiles.split(", ");
		ArrayList<String> files = new ArrayList<String>();
		
		for(int i = 0; i < splitFiles.length; i++)
		{
			files.add(splitFiles[i]);
		}
		
		for(int i = 0; i < files.size(); i++)
		{
			analyzeXML(files.get(i));
		}
		
		// according to menu option, perform task
		if(option == 1){
			listInferences("all");
		}
		else if(option == 2){
			listInferences("sig");
		}
		else if(option == 3){
			listInferences("cat");
		}
		else if(option == 4){
			listInferences("dom");
		}
	}	
}