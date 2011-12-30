class Atom{
	
	private float yPos, xPos, zPos;
	private long ID;
	
	Atom (long  id, float xPos, float yPos,float zPos){

		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.ID=id;
	}
	
	float getXpos() {return xPos;}
	float getYpos() {return yPos;}
	float getZpos() {return zPos;}
	long getID() {return ID;}
}
