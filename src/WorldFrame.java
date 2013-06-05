/**
 * A Frame to hold the choice entry and start button.
 * The evolutionary processes take place in the EvolutionAction inner class.
 * The code for opening networks from file is closely based on the "Streams and Files" chapter in "Core Java 2"
 * by Horstmann & Cornell
 * @author Rob Impey
 * @date 20-iii-03
 */

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WorldFrame extends JFrame
{
    // Class constants
    private static final int WIDTH = 450;
    private static final int HEIGHT = 400;

    // GUI stuff
    private JLabel popSizeLabel, genNumLabel, hiddenLayersLabel, neuronsInHiddenLabel, epochsLabel, learningRateLabel, trainSizeLabel, testSizeLabel, validSizeLabel, selectRateLabel, mutateRateLabel;
    private JTextField popSizeIn, genNumIn, hiddenLayersIn, neuronsInHiddenIn, epochsIn, learningRateIn, trainSizeIn, testSizeIn, validSizeIn, selectRateIn, mutateRateIn;
    private JButton startButton;

    /**
     * Creates a new WorldFrame
     */
    public WorldFrame()
    {
	setTitle("Evolving ANNs with CGP");

	setSize(WIDTH, HEIGHT);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Add file menu
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	
	JMenuItem openItem = new JMenuItem("Open");
	fileMenu.add(openItem);
	openItem.addActionListener(new OpenAction());

	JMenuItem exitItem = new JMenuItem("Exit");
	fileMenu.add(exitItem);
	exitItem.addActionListener(new ActionListener()
	    {
		public void actionPerformed(ActionEvent e)
		{
		    System.exit(0);
		}
	    });

	menuBar.add(fileMenu);
	setJMenuBar(menuBar);

	// Set up labels
	popSizeLabel = new JLabel(" Population Size: ");
	genNumLabel = new JLabel(" Number of generations: ");

	hiddenLayersLabel = new JLabel(" Hidden Layers: ");
	neuronsInHiddenLabel = new JLabel(" Neurons in a Hidden Layer: ");

	epochsLabel = new JLabel(" Epochs: ");
	learningRateLabel = new JLabel(" Learning rate: ");

	trainSizeLabel = new JLabel(" Size of training set: ");
	testSizeLabel = new JLabel(" Size of test set: ");
	validSizeLabel = new JLabel(" Size of the validation set: ");

	selectRateLabel = new JLabel(" Selection Rate: ");
	mutateRateLabel = new JLabel(" Mutation Rate: ");
	
	// Set up text fields
	popSizeIn = new JTextField();
	popSizeIn.setText("" + 100);
	popSizeIn.setMaximumSize(new Dimension(40, 22));
        popSizeIn.setMinimumSize(new Dimension(40, 22));
        popSizeIn.setPreferredSize(new Dimension(40, 22));

	genNumIn = new JTextField();
	genNumIn.setText("" + 100);
	genNumIn.setMaximumSize(new Dimension(40, 22));
        genNumIn.setMinimumSize(new Dimension(40, 22));
        genNumIn.setPreferredSize(new Dimension(40, 22));

	hiddenLayersIn = new JTextField();
	hiddenLayersIn.setText("" + 3);
	hiddenLayersIn.setMaximumSize(new Dimension(40, 22));
        hiddenLayersIn.setMinimumSize(new Dimension(40, 22));
        hiddenLayersIn.setPreferredSize(new Dimension(40, 22));

	neuronsInHiddenIn = new JTextField();
	neuronsInHiddenIn.setText("" + 6);
	neuronsInHiddenIn.setMaximumSize(new Dimension(40, 22));
        neuronsInHiddenIn.setMinimumSize(new Dimension(40, 22));
        neuronsInHiddenIn.setPreferredSize(new Dimension(40, 22));

	epochsIn = new JTextField();
	epochsIn.setText("" + 100);
	epochsIn.setMaximumSize(new Dimension(40, 22));
        epochsIn.setMinimumSize(new Dimension(40, 22));
        epochsIn.setPreferredSize(new Dimension(40, 22));

	learningRateIn = new JTextField();
	learningRateIn.setText("" + 0.05);
	learningRateIn.setMaximumSize(new Dimension(40, 22));
        learningRateIn.setMinimumSize(new Dimension(40, 22));
        learningRateIn.setPreferredSize(new Dimension(40, 22));

	trainSizeIn = new JTextField();
	trainSizeIn.setText("" + 100);
	trainSizeIn.setMaximumSize(new Dimension(40, 22));
        trainSizeIn.setMinimumSize(new Dimension(40, 22));
        trainSizeIn.setPreferredSize(new Dimension(40, 22));

	testSizeIn = new JTextField();
	testSizeIn.setText("" + 100);
	testSizeIn.setMaximumSize(new Dimension(40, 22));
        testSizeIn.setMinimumSize(new Dimension(40, 22));
        testSizeIn.setPreferredSize(new Dimension(40, 22));

	validSizeIn = new JTextField();
	validSizeIn.setText("" + 100);
	validSizeIn.setMaximumSize(new Dimension(40, 22));
        validSizeIn.setMinimumSize(new Dimension(40, 22));
        validSizeIn.setPreferredSize(new Dimension(40, 22));

	selectRateIn = new JTextField();
	selectRateIn.setText("" + 50);
	selectRateIn.setMaximumSize(new Dimension(40, 22));
        selectRateIn.setMinimumSize(new Dimension(40, 22));
        selectRateIn.setPreferredSize(new Dimension(40, 22));

	mutateRateIn = new JTextField();
	mutateRateIn.setText("" + 5);
	mutateRateIn.setMaximumSize(new Dimension(40, 22));
        mutateRateIn.setMinimumSize(new Dimension(40, 22));
        mutateRateIn.setPreferredSize(new Dimension(40, 22));

	Container contentPane = getContentPane();
	
	// Add labels, text fields and the button
	JPanel topPanel = new JPanel(new GridLayout(3, 2));

	JPanel evCompPanel = new JPanel();
	JPanel networkShapePanel = new JPanel();
	JPanel networkParametersPanel = new JPanel();
	JPanel dataPanel = new JPanel();
	JPanel selMutPanel = new JPanel();

	JPanel popSizePanel = new JPanel();
	popSizePanel.add(popSizeLabel);
	popSizePanel.add(popSizeIn);			      
	evCompPanel.add(popSizePanel);

	JPanel genNumPanel = new JPanel();
	genNumPanel.add(genNumLabel);
	genNumPanel.add(genNumIn);
	evCompPanel.add(genNumPanel);

	JPanel hiddenLayersPanel = new JPanel();
	hiddenLayersPanel.add(hiddenLayersLabel);
	hiddenLayersPanel.add(hiddenLayersIn);			      
	networkShapePanel.add(hiddenLayersPanel);

	JPanel neuronsInHiddenPanel = new JPanel();
	neuronsInHiddenPanel.add(neuronsInHiddenLabel);
	neuronsInHiddenPanel.add(neuronsInHiddenIn);
	networkShapePanel.add(neuronsInHiddenPanel);

	JPanel epochsPanel = new JPanel();
	epochsPanel.add(epochsLabel);
	epochsPanel.add(epochsIn);
	networkParametersPanel.add(epochsPanel);

	JPanel learningRatePanel = new JPanel();
	learningRatePanel.add(learningRateLabel);
	learningRatePanel.add(learningRateIn);
	networkParametersPanel.add(learningRatePanel);

	JPanel trainSizePanel = new JPanel();
	trainSizePanel.add(trainSizeLabel);
	trainSizePanel.add(trainSizeIn);
	dataPanel.add(trainSizePanel);

	JPanel testSizePanel = new JPanel();
	testSizePanel.add(testSizeLabel);
	testSizePanel.add(testSizeIn);
	dataPanel.add(testSizePanel);

	JPanel validSizePanel = new JPanel();
	validSizePanel.add(validSizeLabel);
	validSizePanel.add(validSizeIn);
	dataPanel.add(validSizePanel);

	JPanel selectRatePanel = new JPanel();
	selectRatePanel.add(selectRateLabel);
	selectRatePanel.add(selectRateIn);
	selMutPanel.add(selectRatePanel);

	JPanel mutateRatePanel = new JPanel();
	mutateRatePanel.add(mutateRateLabel);
	mutateRatePanel.add(mutateRateIn);
	selMutPanel.add(mutateRatePanel);

	// Add these panels to the top panel
	topPanel.add(evCompPanel);
	topPanel.add(networkShapePanel);
	topPanel.add(networkParametersPanel);
	topPanel.add(dataPanel);
	topPanel.add(selMutPanel);

	// Create the button
	startButton = new JButton("Start");

	// Create its action listener and associate it with the button
	EvolutionAction eA = new EvolutionAction();
	startButton.addActionListener(eA);

	JPanel startButtonPanel = new JPanel();
	startButtonPanel.add(startButton);
	topPanel.add(startButtonPanel);

	contentPane.add(topPanel);
    }

    /**
     * Where the evolution occurs.
     */
    private class EvolutionAction implements ActionListener
    {
	// Parameters for the evolutionary run
	private int popSize, genNum, hiddenLayers, neuronsInHidden, epochs, trainSize, testSize, validSize, selectRate, mutateRate;
	private double learningRate;
	
	/**
	 * Evolution with the parameters set in the boxes
	 */
	public void actionPerformed(ActionEvent _e)
	{
	    // Start a stop watch
	    long startTime = System.currentTimeMillis();

	    // Read the variables
	    popSize = Integer.parseInt(popSizeIn.getText());
	    genNum = Integer.parseInt(genNumIn.getText());
	    hiddenLayers = Integer.parseInt(hiddenLayersIn.getText());
	    neuronsInHidden = Integer.parseInt(neuronsInHiddenIn.getText());
	    epochs = Integer.parseInt(epochsIn.getText());
	    learningRate = Double.parseDouble(learningRateIn.getText());
	    trainSize = Integer.parseInt(trainSizeIn.getText());
	    testSize = Integer.parseInt(testSizeIn.getText());
	    validSize = Integer.parseInt(validSizeIn.getText());
	    selectRate = Integer.parseInt(selectRateIn.getText());
	    mutateRate = Integer.parseInt(mutateRateIn.getText());
	    
	    // Check for valid inputs
	    if ((popSize < 1)
		|| (genNum < 1)
		|| (hiddenLayers < 2)
		|| (neuronsInHidden < 1)
		|| (epochs < 1)
		|| (trainSize < 1)
		|| (trainSize > 200)
		|| (testSize < 1)
		|| (testSize > 200)
		|| (validSize < 1)
		|| (validSize > 200)
		|| (selectRate < 1)
		|| (selectRate > 100)
		|| (mutateRate < 1)
		|| (mutateRate > 100))
		System.out.println("Check inputs!");
	    else
		{
		    
		    ObjectOutputStream out; // For saving objects
		    
		    // Set up the data sets
		    DataSet dS = new SecondYearData();
		    Tuple[] train, test, valid;
		    
		    // START OF EVOLUTION
		    
		    Genome[] parentGenomes, childrenGenomes;
		    Network[] candidateNets;
		    NetworkPerformance[] candidatePerformances;
		    Network[] bestNets = new Network[genNum]; // An array of the best networks from each generation
		    double[] bestSSEs = new double[genNum];// An array of the SSEs of the best network in each generation
		    
		    // GENERATE THE INITIAL POPULATION OF NETWORKS
		    
		    // Create initial genomes
		    parentGenomes = new Genome[popSize];
		    for (int i = 0; i < popSize; i++)
			{
			    parentGenomes[i] = new Genome(3, neuronsInHidden, hiddenLayers, 1);
			}
		    
		    // Set up the arrays of best and children genomes
		    // We select the percentage specified by the user
		    childrenGenomes = new Genome[popSize];
		    
		    // Set up the array of networks
		    candidateNets = new Network[popSize];
		    
		    // Set up the array to store the training performance of the networks
		    candidatePerformances = new NetworkPerformance[popSize];
		    
		    // Variables used for sorting
		    int tempIndex;
		    Network tempNet;
		    
		    // variables for the best in every twentieth generation
		    Network bIG;
		    NetworkFrame bIGD;
		    GraphFrame bIGG;
		    
		    // Variables used to select genomes in the good candidates array at random
		    int p1, p2;
		    
		    for (int generation = 0; generation < genNum; generation++) // Loop of generations
			{
			    // Create the networks from the current parent genomes
			    for (int i = 0; i < popSize; i++)
				{
				    candidateNets[i] = new Network(parentGenomes[i], generation, learningRate);
				}
			    
			    // Train the networks
			    // Note that the training and test sets are different random selections
			    // for each network
			    for(int net = 0; net < popSize; net++)
				{
				    train = dS.getSample(trainSize);
				    test = dS.getSample(testSize);
				    candidatePerformances[net] = candidateNets[net].train(train, test, epochs);
				} 
			    
			    // Select the networks with the best fitness
			    // The best fitness is the lowest SSE for an array of Tuples
			    // Select the percentage chosen by the user
			    
			    // First, find the SSE of the networks for a validation set of Tuples
			    for (int net = 0; net < popSize; net++)
				{
				    valid = dS.getSample(validSize);
				    candidateNets[net].cacheSSE(valid);
				}
			    
			    // Sort the candidate networks according to the cached SSEs 
			    // Selection sorting is used because the arrays are in random order to start with
			    for (int i = 0; i < candidateNets.length; i++)
				{
				    tempIndex = i;
				    for (int j = i + 1; j < candidateNets.length; j++)
					{
					    if (candidateNets[j].getCachedSSE() 
						< candidateNets[tempIndex].getCachedSSE())
						tempIndex = j;
					}
				    tempNet = candidateNets[i];
				    candidateNets[i] = candidateNets[tempIndex];
				    candidateNets[tempIndex] = tempNet;
				}

			    // FOR TESTING
			    if (candidateNets[0].getCachedSSE() > candidateNets[1].getCachedSSE())
				System.out.println("You have a sorting error!");
			    
			    // Store the best network in this generation
			    bestNets[generation] = candidateNets[0];
			    
			    // Every 20 generations and the final generation, display the best network and save it
			    if ((generation  % 20 == 0) || (generation + 1 == genNum))
				{
				    bIG = candidateNets[0];
				    bIGD = bIG.getDiagram();
				    bIGD.show();
				    
				    try
					{
					    out = new ObjectOutputStream(new FileOutputStream("nets/" 
											      + popSize + "P"
											      + genNum + "G"
											      + hiddenLayers + "HL"
											      + neuronsInHidden + "NIH"
											      + epochs + "E"
											      + learningRate + "LR"
											      + trainSize + "TR"
											      + testSize + "TE"
											      + selectRate + "S"
											      + mutateRate + "M"
											      + generation + "G"
											      + ".net"));
					    out.writeObject(bIG);
					    out.close(); 
					}
				    catch(Exception e)
					{
					}				    
				}
			    	    
			    // Apply crossover to create children
			    // Parents within the best selectRate percentage combine at random
			    // NB selectRate will be less than 100
			    for (int i = 0; i < popSize; i++)
				{
				    p1 = (int)(Math.random() * ((popSize * selectRate) / 100.0));
				    p2 = (int)(Math.random() * ((popSize * selectRate) / 100.0));
				    
				    childrenGenomes[i] = candidateNets[p1].getGenome()
					.uniformCross(candidateNets[p2].getGenome());
				}
			    
			    // Mutate the children's genomes
			    for (int i = 0; i < childrenGenomes.length; i++)
				{
				    childrenGenomes[i].mutate(mutateRate);
				}

			    // The children become the parents of the next generation
			    for (int i = 0; i < popSize; i++)
				{
				    parentGenomes[i] = childrenGenomes[i];
				}
			}
		    
		    // Show the change in performance of the best network in each generation
		    for (int i = 0; i < bestNets.length; i++)
			{
			    bestSSEs[i] = bestNets[i].getCachedSSE();
			}

		    GraphFrame ePGF = new GraphFrame(("Evolution of "
						      + hiddenLayers + " by "
						      + neuronsInHidden + " Networks"), 
						     "Generation", "SSE", bestSSEs, new Color(27, 137, 91));    

		    ePGF.show();
		    
		    // Save the graph of the evolutionary progress
		    EvolutionPerformance eP = new EvolutionPerformance(("Evolution of " + hiddenLayers + " by " 
									+ neuronsInHidden + " Networks"), bestSSEs);
		    try
			{
			    out = new ObjectOutputStream(new FileOutputStream("nets/" 
									      + popSize + "P"
									      + genNum + "G"
									      + hiddenLayers + "HL"
									      + neuronsInHidden + "NIH"
									      + epochs + "E"
									      + learningRate + "LR"
									      + trainSize + "TR"
									      + testSize + "TE"
									      + selectRate + "S"
									      + mutateRate + "M"
									      + ".evo"));
			    out.writeObject(eP);
			    out.close(); 
			}
		    catch(Exception e)
			{
			}

		    // Print a table of the SSEs over generations 
		    // and the number of active links in the best network of each generation
		    // The print out is Latex friendly
		    System.out.println();
		    System.out.println("\\begin{table}");
		    System.out.println("\\begin{center}");
		    System.out.println("\\begin{tabular}{|c|c|c|}");
		    System.out.println("\\hline");
		    System.out.println("Generation & SSE & \\% Active Links \\\\");
		    System.out.println("\\hline");
		    for (int i = 0; i < bestNets.length; i++)
			{
			    System.out.println("" + i + " & " 
					       + bestNets[i].getCachedSSE() + " & " 
					       + (int)(bestNets[i].getPercentActWeights() * 100)
					       + " \\\\");
			}
		    System.out.println("\\hline");
		    System.out.println("\\end{tabular}");
		    System.out.println("\\end{center}");
		    System.out.println("\\caption{Evolution of " + hiddenLayers + " by " + neuronsInHidden + " networks}");
		    System.out.println("\\end{table}");
		    System.out.println();

		    // Set up a fully connected network, train it, show the performance and save it
		    Genome fullGenome = new Genome(3, neuronsInHidden, hiddenLayers, 1);
		    for (int i = 0; i < fullGenome.getBinStringLength(); i++)
			{
			    fullGenome.setBit(i, true);
			}
		    
		    Network fullNet = new Network(fullGenome, -1, learningRate);
		    train = dS.getSample(trainSize);
		    test = dS.getSample(testSize);
		    NetworkPerformance fullPerformance = fullNet.train(train, test, epochs);
		    
		    NetworkFrame fullFrame = fullNet.getDiagram(("Fully connected " 
								 + hiddenLayers + " by "
								 + neuronsInHidden + " Network"));
		    fullFrame.show();
		    
		    GraphFrame fullGraph = fullPerformance.getGraph(("Fully connected " 
								     + hiddenLayers + " by "
								     + neuronsInHidden + " Network"));
		    fullGraph.show();
		    
		    try
			{
			    // Save fully connected network
			    out = new ObjectOutputStream(new FileOutputStream("nets/" 
									      + hiddenLayers + "HL"
									      + neuronsInHidden + "NIH"
									      + epochs + "E"
									      + learningRate + "LR"
									      + trainSize + "TR"
									      + testSize + "TE"
									      + "full"
									      + ".net"));
			    out.writeObject(fullNet);
			    out.close();
			    
			    // Save its performance
			    out = new ObjectOutputStream(new FileOutputStream("nets/" 
									      + hiddenLayers + "HL"
									      + neuronsInHidden + "NIH"
									      + epochs + "E"
									      + learningRate + "LR"
									      + trainSize + "TR"
									      + testSize + "TE"
									      + "full"
									      + ".per"));
			    out.writeObject(fullPerformance);
			    out.close();
			}
		    catch (Exception e)
			{
			}
		}
	    // Stop the watch
	    long endTime = System.currentTimeMillis();
	    long calcTime = endTime - startTime;

	    System.out.println("Completed in " +calcTime + " milliseconds.");
	}
    }

    /**
     * For opening old networks.
     */
    private class OpenAction implements ActionListener
    {
	public void actionPerformed(ActionEvent _e)
	{
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File("nets/"));
	    int r = chooser.showOpenDialog(WorldFrame.this);
	    if (r == JFileChooser.APPROVE_OPTION)
		{
		    openNetwork(chooser.getSelectedFile().getPath());
		}
	}
    }

    /**
     * Loads a network or graph and shows it
     * @param _p The path of the network
     */
    public static void openNetwork(String _p)
    {
	try
	    {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(_p));

		if (_p.endsWith(".evo"))
		    {
			EvolutionPerformance eP = (EvolutionPerformance)in.readObject();
			GraphFrame ePG = eP.getGraph();
			ePG.show();
		    }

		if (_p.endsWith(".net"))
		    {
			Network n = (Network)in.readObject();
			NetworkFrame nF = n.getDiagram();
			nF.show();
		    }
		
		if (_p.endsWith(".per"))
		    {
			NetworkPerformance nP = (NetworkPerformance)in.readObject();
			GraphFrame gF = nP.getGraph();
			gF.show();
		    }
		
		in.close();
	    }
	catch (Exception e)
	    {
		System.out.println(e);
	    }
    }
}
