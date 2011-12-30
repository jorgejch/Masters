class ClosePair{

	private Atom Atom1, Atom2;
	private double distance;
	
	ClosePair (Atom Atom1, Atom Atom2, double distance){
		this.Atom1 = Atom1;
		this.Atom2 = Atom2;
		this.distance = distance;
	}	
	
	Atom getAtom1() {return Atom1;}
	Atom getAtom2() {return Atom2;}
	double getDistance() {return distance;}
}
