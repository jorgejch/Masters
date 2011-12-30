import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList; 
import java.util.List;
import java.util.ListIterator;
import java.text.DecimalFormat;


public class AtomCompare {
	
	//Declaration of the lists that will hold the atom references and axis positions
	
	static List <Atom> atomList = new LinkedList <Atom> () ;
	
	static String filePath;
		
	public static void main(String[] args) {
		
		/* Handles arguments passed to the main method and calls the functions that will load the atoms and then 
		 *  the function that will compare them. */
	
		String usage="Usage: java -jar AtomCompare.jar <full pdb file path> <distance>\n<distance>: maximum distance between two atoms.";

		if (args.length < 2) {
			
			System.out.println(usage);
			
		}
		else {
			
			try {
				
				filePath = args[0];
				System.out.println("Output atoms in " + filePath + " with relative distances less than " + args[1] + " Angstons.");
				loadAtoms();
				Compare(Double.valueOf(args[1]));
		 
			}catch (NumberFormatException ex){
			
                ex.printStackTrace(System.err);

				//System.out.println("Invalid distance argument.");
				System.out.println();
				System.out.println(usage);
				return;
			}
		}
	}
		
	static void loadAtoms () { 
		
		
		/* Function that reads the pdb file and loads atoms references and x,y,z positions to lists */
				
			File pdbFile = new File(filePath);
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(pdbFile));
				String s = br.readLine();
							
				while(s!=null) {
					
					if (s.length()>6 && (s.substring(0,4).equals("ATOM") || s.substring(0,6).equals("HETATM") )) {											
						
						long ID = Integer.valueOf(s.substring(6, 11).trim());
						float xPos = Float.valueOf(s.substring(30,38).trim());
						float yPos = Float.valueOf(s.substring(38,46).trim());
						float zPos = Float.valueOf(s.substring(46,54).trim());
						atomList.add(new Atom(ID, xPos, yPos, zPos));
						
					}
						s = br.readLine();
				}
				
				br.close();
				System.out.println(filePath + " loaded");
				
			}	
			catch(FileNotFoundException ex){
				System.out.println("File '" + filePath + "' not found!");	
				System.exit(0);
			}
			catch(IOException ioex){
				ioex.printStackTrace();
				System.exit(0);
			}
			
	}
	
	static void Compare (double distance){		
		List<ClosePair> closePairsList = new LinkedList <ClosePair> ();
		long comparisons = 0;		
		long done =0;
		long atomlistSize = atomList.size();
		
		for (long x = 1; x <= atomlistSize; x++)
			comparisons = comparisons + (atomlistSize - x);

		System.out.println(comparisons + " comparisons to be made.");
		System.out.print("Completed:");
			
		int interval = Math.round(comparisons/10);
		
		for (ListIterator<Atom> iterateFirstAtom  = atomList.listIterator(); iterateFirstAtom.hasNext();iterateFirstAtom.next()) {
							
			Atom firstAtom = atomList.get(iterateFirstAtom.nextIndex());
			float x1 = firstAtom.getXpos();
			float y1 = firstAtom.getYpos();
			float z1 = firstAtom.getZpos();
			
			for (ListIterator<Atom> iterateSecondAtom =  atomList.listIterator(iterateFirstAtom.nextIndex()+1); iterateSecondAtom.hasNext();iterateSecondAtom.next()){	
				
				Atom secondAtom = atomList.get(iterateSecondAtom.nextIndex());
				float x2 =  secondAtom.getXpos();
				float y2 =  secondAtom.getYpos();
				float z2 = secondAtom.getZpos();
				double d = Math.sqrt(Math.pow((x2 - x1),2) + Math.pow((y2 - y1),2) + Math.pow((z2 - z1),2));
					
				if (d < distance)						
					closePairsList.add(new ClosePair(firstAtom, secondAtom, d));
					
				++done;
									
				if ((done % interval)==0 && done/interval !=10)
					System.out.print(" " + (done/interval)*10 + "%" );
				else if((done % interval)==0 && done/interval ==10)		
					System.out.print(" " +(done/interval)*10 + "%" + "\n");
				
			}
				
		}
			
		System.out.println("Atoms compared.");
		atomList.clear();
		printToScreen (closePairsList, distance);
	}
	
	static void printToScreen(List<ClosePair> toPrint, double distance){
		
			DecimalFormat decimalFormater = new DecimalFormat("#.###");
			
			if (toPrint.size()==0) {
				System.out.println("No distance between two atoms smaller than " + distance + "." );
				return;
			}
			
			File pdbFile = new File(filePath);
						
			for (ListIterator<ClosePair> iterateToPrint = toPrint.listIterator(); iterateToPrint.hasNext();iterateToPrint.next()){
							
				Atom firstAtom = toPrint.get(iterateToPrint.nextIndex()).getAtom1();				 				
 				Atom secondAtom = toPrint.get(iterateToPrint.nextIndex()).getAtom2();	
				String firstAtomID = String.valueOf(firstAtom.getID());
				String secondAtomID = String.valueOf(secondAtom.getID());
 				double atomsDistance = toPrint.get(iterateToPrint.nextIndex()).getDistance();
 				
 				try {
				
	 				BufferedReader br = new BufferedReader(new FileReader(pdbFile));
					
					String s = br.readLine();

					while(s!=null){
 					 					
						if (s.length()>6 && (s.substring(0,4).equals("ATOM") || s.substring(0,6).equals("HETATM")) ) {
						
							if (s.substring(6, 11).trim().equals(firstAtomID)){
							
								System.out.println(s);
							
							}else if (s.substring(6, 11).trim().equals(secondAtomID)){
							
								System.out.println(s);
								System.out.println("Distance = " + decimalFormater.format(atomsDistance) + " angstrons");
								System.out.println("-------------------------------------------------------------------------------");
							
							}
						}
						s = br.readLine();
					}
					br.close();	
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
	}		
}
