package bioinfo.markov;

public class testMarkov {

	public static void main(String[] args) {
		testSerialization();
	}
	
	/*
		[NodeIDs] // Optional
		ID=1;Name=Node1
		ID=2;Name=Node2
		ID=3;Name=Node3
		ID=4;Name=Node4
		
		[InputPoints] // Optional
		ID=11;State=0
		ID=12;State=1
		
		[Gates]
		ID=1;InputNode=3,4;Logic=0111
		ID=2;InputNode=1,2;Logic=0111
		ID=3;InputNode=1,2;Logic=0001
		ID=4;InputNode=3,4;Logic=0100
		
		[OutputGates] // Optional
		ID=1
		ID=2
		ID=3
		ID=4
		
		[InitialStates] // Optional
		ID=1;InitialState=0

	 */
	private static void testSerialization() {
		try {
			MarkovNetwork mkn;
			mkn = Serializer.deserialize(System.in);
			System.out.println(Serializer.serialize(mkn));
		} catch (SerializationException e) {
			e.printStackTrace();
		} catch (MarkovException e) {
			e.printStackTrace();
		}
	}
	
	private static void overallTest() {
		MarkovNetwork mkn = new MarkovNetwork();
		try {
			Gate g1 = new Gate(mkn, 1, new int[]{3,4});
			g1.setTransitionVector(new boolean[]{false, true, true, true});
			mkn.addGate(g1, true);
			
			Gate g2 = new Gate(mkn, 2, new int[]{1,2});
			g2.setTransitionVector(new boolean[]{false, true, true, true});
			mkn.addGate(g2, true);
			
			Gate g3 = new Gate(mkn, 3, new int[]{1,2});
			g3.setTransitionVector(new boolean[]{false, false, false, true});
			mkn.addGate(g3, true);
			
			Gate g4 = new Gate(mkn, 4, new int[]{3,4});
			g4.setTransitionVector(new boolean[]{false, true, false, false});
			mkn.addGate(g4, true);
			
			System.out.println(Serializer.serialize(mkn));
			
			System.out.println("Test state transition");
			testWithState(mkn, new boolean[] {false, false, false, false});
			testWithState(mkn, new boolean[] {false, false, false, true});
			testWithState(mkn, new boolean[] {false, false, true, false});
			testWithState(mkn, new boolean[] {false, false, true, true});
			testWithState(mkn, new boolean[] {false, true, false, false});
			testWithState(mkn, new boolean[] {false, true, false, true});
			testWithState(mkn, new boolean[] {false, true, true, false});
			testWithState(mkn, new boolean[] {false, true, true, true});
			
			testWithState(mkn, new boolean[] {true, false, false, false});
			testWithState(mkn, new boolean[] {true, false, false, true});
			testWithState(mkn, new boolean[] {true, false, true, false});
			testWithState(mkn, new boolean[] {true, false, true, true});
			testWithState(mkn, new boolean[] {true, true, false, false});
			testWithState(mkn, new boolean[] {true, true, false, true});
			testWithState(mkn, new boolean[] {true, true, true, false});
			testWithState(mkn, new boolean[] {true, true, true, true});
			
			System.out.println("Test attractor");
			testAttractor(mkn, new boolean[] {false, true, false, true});
		} catch (MarkovException e) {
			
		}

		return;
	}
	
	private static void testWithState(MarkovNetwork mkn, boolean[] states) throws MarkovException {
		mkn.setInitialStates(states);
		mkn.moveNext();
		System.out.println(mkn.getOutputStates());
	}
	
	private static void testAttractor(MarkovNetwork mkn, boolean[] states) throws MarkovException {
		mkn.setInitialStates(states);
		
		System.out.println(mkn.getOutputStates());
		mkn.moveNext();
		System.out.println(mkn.getOutputStates());
		mkn.moveNext();
		System.out.println(mkn.getOutputStates());
		mkn.moveNext();
		System.out.println(mkn.getOutputStates());
		mkn.moveNext();
		System.out.println(mkn.getOutputStates());
		mkn.moveNext();
		System.out.println(mkn.getOutputStates());
		mkn.moveNext();
		System.out.println(mkn.getOutputStates());
		mkn.moveNext();
		System.out.println(mkn.getOutputStates());
		mkn.moveNext();
		System.out.println(mkn.getOutputStates());
	}

}
