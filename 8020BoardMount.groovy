// code here
CSG makePCBMount (
    double screwThreadRadius,
    double standoffHeight,
    double standoffRadius,
    double width,
    double height,
    double baseThickness,
    double chamfer
    )
{

	CSG standoffBase = new Cylinder(standoffRadius, baseThickness).toCSG();
	ArrayList<CSG> standoffs = new ArrayList<CSG>();
	standoffs.add(standoffBase.movex(width/2).movey(height/2));
	standoffs.add(standoffBase.movex(width/2).movey(-height/2));
	standoffs.add(standoffBase.movex(-width/2).movey(-height/2));
	standoffs.add(standoffBase.movex(-width/2).movey(height/2));

	CSG basePlate = CSG.hullAll(standoffs);

	// Add standoffs to baseplate
	CSG standoff = new Cylinder(standoffRadius, 10).toCSG();
	standoff = standoff.difference(new Cylinder(screwThreadRadius, 10).toCSG());

     basePlate = basePlate.union(standoff.movex(width/2).movey(height/2));
     basePlate = basePlate.union(standoff.movex(width/2).movey(-height/2));
     basePlate = basePlate.union(standoff.movex(-width/2).movey(-height/2));
     basePlate = basePlate.union(standoff.movex(-width/2).movey(height/2));

	CSG standoffChamferBase = new Cylinder(standoffRadius+chamfer, baseThickness+chamfer).toCSG();
	ArrayList<CSG> standoffChamferBaseList = new ArrayList<CSG>();
	standoffChamferBaseList.add(standoffChamferBase.movex(width/2).movey(height/2));
	standoffChamferBaseList.add(standoffChamferBase.movex(width/2).movey(-height/2));
	standoffChamferBaseList.add(standoffChamferBase.movex(-width/2).movey(-height/2));
	standoffChamferBaseList.add(standoffChamferBase.movex(-width/2).movey(height/2));

	for (int i = 0; i < standoffChamferBaseList.size(); i++)
	{
		basePlate = basePlate.union(basePlate.intersect(standoffChamferBaseList[i]).hull());
	}

	CSG screwHole = new Cylinder(screwThreadRadius, standoffHeight+baseThickness).toCSG();

     basePlate = basePlate.difference(screwHole.movex(width/2).movey(height/2));
     basePlate = basePlate.difference(screwHole.movex(width/2).movey(-height/2));
     basePlate = basePlate.difference(screwHole.movex(-width/2).movey(-height/2));
     basePlate = basePlate.difference(screwHole.movex(-width/2).movey(height/2));

	// mounting holes
	CSG mountHole = new Cylinder((5.5)/2, baseThickness).toCSG();
     basePlate = basePlate.difference(mountHole.movex(width/3));
     basePlate = basePlate.difference(mountHole.movex(-width/3));
     
	return basePlate;
}

CSG makePCBMountFromParams(){
	screwThreadD   = new LengthParameter("Diameter of the thread of the screw", 2.8, [120.0, 0.5]);
	standoffHeight = new LengthParameter("Height of standoffs ", 10,[100.0, 0]);
	standoffD   	= new LengthParameter("Standoff Diameter", 6,[140.0,3.0]);
	width 	     = new LengthParameter("width between mounting holes", 30,[150.0,10.0]);
	height 	     = new LengthParameter("height between mounting holes", 30,[150.0,10.0]);
	baseThickness 	= new LengthParameter("Thickness of baseplate", 3,[150.0,1.0]);
	chamfer 	     = new LengthParameter("Chanfer betwwen standoffs and baseplate", 3,[150.0, 0.0]);

	CSG mount = makePCBMount(
				screwThreadRadius = screwThreadD.getMM()/2.0,
				standoffHeight    = standoffHeight.getMM(),
				standoffRadius    = standoffD.getMM()/2.0,
				width             = width.getMM(),
				height            = height.getMM(),
				baseThickness     = baseThickness.getMM(),
				chamfer           = chamfer.getMM()
				);

	return mount.setRegenerate({ makePCBMountFromParams()})
}

//CSGatabase.clear()//set up the database to force only the default values in			
return makePCBMountFromParams();