/**
 * A main method to test the Network class.
 * <code>train<train</code> is tested in particular.
 * @author Rob Impey
 */

public class TestNetwork
{
    public static void main(String[] args)
    {
	// set up training, test and validation sets
	DataSet ds = new SecondYearData();
	Tuple[] tr, te, v;

	System.out.println("Data sets built");

	// set up the fully connected genomes of various sizes
	Genome[][] genomes = new Genome[4][28];

	for (int i = 0; i < 4; i++)
	    {
		for (int j = 0; j < 28; j++)
		    {
			genomes[i][j] = new Genome(3, (j + 3), (i + 2), 1);
			
			for (int k = 0; k < genomes[i][j].getBinStringLength(); k++)
			    {
				genomes[i][j].setBit(k, true);
			    }
		    }
	    }

	System.out.println("Genomes built");

	// Set up the networks
	Network[][] networks = new Network[4][28];

	for (int i = 0; i < 4; i++)
	    {
		for (int j = 0; j < 28; j++)
		    {
			networks[i][j] = new Network(genomes[i][j], 0, 0.01);
		    }
	    }

	System.out.println("Networks built\n");

	// Train each network 
	NetworkPerformance[][] perfs = new NetworkPerformance[4][28];
	
	for (int i = 0; i < 4; i++)
	    {
		for (int j = 0; j < 28; j++)
		    {
			tr = ds.getSample(100);
			te = ds.getSample(100);
			perfs[i][j] = networks[i][j].train(tr, te, 200);
			System.out.println("" + (i + 2) + " by " + (j + 3) + " network trained");
		    }
	    }

	// Show the graphs
	/* Or not, there is little point in drawing 122 graphs
	GraphFrame[][] graphs = new GraphFrame[4][28];
	
	for (int i = 0; i < 4; i++)
	    {
		for (int j = 0; j < 28; j++)
		    {
			graphs[i][j] = perfs[i][j].getGraph("" + (i + 2) + " by " + (j + 3));
			graphs[i][j].show();
		    }
	    }
	*/

	// Print off the SSE of each network for the validation sets
	int iSSE; // used for rounding the long doubles
	double dSSE;
	double[][] sSSEs = new double[4][28];

	for (int i = 0; i < 4; i++)
	    {
		for (int j = 0; j < 28; j++)
		    {
			v = ds.getSample(100);
			dSSE = networks[i][j].getSSE(v);
			dSSE *= 10.0;
			iSSE = (int)dSSE;
			dSSE = (double)iSSE;
			dSSE /= 10.0;
			sSSEs[i][j] = dSSE;
		    }
	    }

	// Print out in latex
	System.out.println();

	for (int i = 0; i < 28; i++)
	    {
		System.out.print("" + (i + 3));
		
		for (int j = 0; j < 4; j++)
		    {
			System.out.print(" & " + sSSEs[j][i]);
		    }
		if (i != 27)
		    System.out.println("\\\\\n\\cline{1-1}");
		else
		    System.out.println("\\\\");
	    }
    }
}
