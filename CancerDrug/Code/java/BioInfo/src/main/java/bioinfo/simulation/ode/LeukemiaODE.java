// Auto-generated
package bioinfo.simulation.ode;

import java.util.HashMap;
import java.util.Map;

public class LeukemiaODE extends CellSimulationBase {
	public static final int GENE_COUNT = 81;

	public static final String[] index2name = new String[] {
		"C/EBPβ", 	// 0
		"NGAL", 	// 1
		"C/EBPα", 	// 2
		"Caspase 8", 	// 3
		"Ras", 	// 4
		"Stat5", 	// 5
		"PTEN", 	// 6
		"OPN", 	// 7
		"FGF2", 	// 8
		"Pu.1", 	// 9
		"p53", 	// 10
		"NF-κB", 	// 11
		"Sox2", 	// 12
		"FGF7", 	// 13
		"SHH", 	// 14
		"IFN-γ", 	// 15
		"Integrin", 	// 16
		"Hey2", 	// 17
		"Myc", 	// 18
		"Caspase 3", 	// 19
		"Sox9", 	// 20
		"Sox7", 	// 21
		"Sox4", 	// 22
		"Gata4/6", 	// 23
		"IL-1", 	// 24
		"SStat3", 	// 25
		"Notch", 	// 26
		"NOG", 	// 27
		"PKA", 	// 28
		"PRunx2", 	// 29
		"IL-3", 	// 30
		"MAPK", 	// 31
		"IL-8", 	// 32
		"HSPG2", 	// 33
		"p21", 	// 34
		"IL-10", 	// 35
		"PPARγ", 	// 36
		"IL-6", 	// 37
		"TAkt", 	// 38
		"p27", 	// 39
		"PHoxa3", 	// 40
		"Bcl-2", 	// 41
		"pRb", 	// 42
		"G-CSFR", 	// 43
		"Hoxa9", 	// 44
		"PRDM14", 	// 45
		"XIAP", 	// 46
		"EGFR", 	// 47
		"Hoxa5", 	// 48
		"Cytochrome C", 	// 49
		"E2F", 	// 50
		"PGC-1", 	// 51
		"TGF-β", 	// 52
		"Hes1", 	// 53
		"Cyclin E", 	// 54
		"Cyclin D", 	// 55
		"Wnt", 	// 56
		"Hoxa10", 	// 57
		"Runx1", 	// 58
		"Bach1", 	// 59
		"Gata1/2", 	// 60
		"AP2", 	// 61
		"HIF", 	// 62
		"BMP", 	// 63
		"Cdx2", 	// 64
		"Bad", 	// 65
		"EGF", 	// 66
		"VEGF", 	// 67
		"Bim", 	// 68
		"NR2F2", 	// 69
		"iκB", 	// 70
		"c-Jun", 	// 71
		"Bcl-xL", 	// 72
		"Cdk6", 	// 73
		"TNF-α", 	// 74
		"RARs", 	// 75
		"Cdk2", 	// 76
		"Bax", 	// 77
		"Fas", 	// 78
		"Stat1", 	// 79
		"FGFR2" 	// 80
	};

	public static final Map<String, Integer> name2index;
	static {
		name2index = new HashMap<String, Integer>();
		name2index.put("C/EBPβ", 0);
		name2index.put("NGAL", 1);
		name2index.put("C/EBPα", 2);
		name2index.put("Caspase 8", 3);
		name2index.put("Ras", 4);
		name2index.put("Stat5", 5);
		name2index.put("PTEN", 6);
		name2index.put("OPN", 7);
		name2index.put("FGF2", 8);
		name2index.put("Pu.1", 9);
		name2index.put("p53", 10);
		name2index.put("NF-κB", 11);
		name2index.put("Sox2", 12);
		name2index.put("FGF7", 13);
		name2index.put("SHH", 14);
		name2index.put("IFN-γ", 15);
		name2index.put("Integrin", 16);
		name2index.put("Hey2", 17);
		name2index.put("Myc", 18);
		name2index.put("Caspase 3", 19);
		name2index.put("Sox9", 20);
		name2index.put("Sox7", 21);
		name2index.put("Sox4", 22);
		name2index.put("Gata4/6", 23);
		name2index.put("IL-1", 24);
		name2index.put("SStat3", 25);
		name2index.put("Notch", 26);
		name2index.put("NOG", 27);
		name2index.put("PKA", 28);
		name2index.put("PRunx2", 29);
		name2index.put("IL-3", 30);
		name2index.put("MAPK", 31);
		name2index.put("IL-8", 32);
		name2index.put("HSPG2", 33);
		name2index.put("p21", 34);
		name2index.put("IL-10", 35);
		name2index.put("PPARγ", 36);
		name2index.put("IL-6", 37);
		name2index.put("TAkt", 38);
		name2index.put("p27", 39);
		name2index.put("PHoxa3", 40);
		name2index.put("Bcl-2", 41);
		name2index.put("pRb", 42);
		name2index.put("G-CSFR", 43);
		name2index.put("Hoxa9", 44);
		name2index.put("PRDM14", 45);
		name2index.put("XIAP", 46);
		name2index.put("EGFR", 47);
		name2index.put("Hoxa5", 48);
		name2index.put("Cytochrome C", 49);
		name2index.put("E2F", 50);
		name2index.put("PGC-1", 51);
		name2index.put("TGF-β", 52);
		name2index.put("Hes1", 53);
		name2index.put("Cyclin E", 54);
		name2index.put("Cyclin D", 55);
		name2index.put("Wnt", 56);
		name2index.put("Hoxa10", 57);
		name2index.put("Runx1", 58);
		name2index.put("Bach1", 59);
		name2index.put("Gata1/2", 60);
		name2index.put("AP2", 61);
		name2index.put("HIF", 62);
		name2index.put("BMP", 63);
		name2index.put("Cdx2", 64);
		name2index.put("Bad", 65);
		name2index.put("EGF", 66);
		name2index.put("VEGF", 67);
		name2index.put("Bim", 68);
		name2index.put("NR2F2", 69);
		name2index.put("iκB", 70);
		name2index.put("c-Jun", 71);
		name2index.put("Bcl-xL", 72);
		name2index.put("Cdk6", 73);
		name2index.put("TNF-α", 74);
		name2index.put("RARs", 75);
		name2index.put("Cdk2", 76);
		name2index.put("Bax", 77);
		name2index.put("Fas", 78);
		name2index.put("Stat1", 79);
		name2index.put("FGFR2", 80);
	}

	@Override
	public final int getDimension() {
		return GENE_COUNT;
	}

	@Override
	public final void computeDerivatives(double t, double[] y, double[] yDot) {
		double activators;
		double inhibitors;

		// C/EBPβ
		if(isOverrideDerivative(t, 0)) {
			yDot[0] = overrideDerivative(t, 0);
		} else { 
			// activators : AP2, IL-1, MAPK
			activators = Math.pow(applyNoise(y[61], t, 61), n) + Math.pow(applyNoise(y[24], t, 24), n) + Math.pow(applyNoise(y[31], t, 31), n);
			// inhibitors : PHoxa3, TAkt, TGF-β
			inhibitors = Math.pow(applyNoise(y[40], t, 40), n) + Math.pow(applyNoise(y[38], t, 38), n) + Math.pow(applyNoise(y[52], t, 52), n);
			yDot[0] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[0];
		}

		// NGAL
		if(isOverrideDerivative(t, 1)) {
			yDot[1] = overrideDerivative(t, 1);
		} else { 
			// activators : IFN-γ, Runx1, TNF-α
			activators = Math.pow(applyNoise(y[15], t, 15), n) + Math.pow(applyNoise(y[58], t, 58), n) + Math.pow(applyNoise(y[74], t, 74), n);
			inhibitors = 0;
			yDot[1] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[1];
		}

		// C/EBPα
		if(isOverrideDerivative(t, 2)) {
			yDot[2] = overrideDerivative(t, 2);
		} else { 
			// activators : C/EBPβ, NOG, Runx1
			activators = Math.pow(applyNoise(y[0], t, 0), n) + Math.pow(applyNoise(y[27], t, 27), n) + Math.pow(applyNoise(y[58], t, 58), n);
			// inhibitors : HIF, IL-1, NR2F2, Notch, SHH, Stat5
			inhibitors = Math.pow(applyNoise(y[62], t, 62), n) + Math.pow(applyNoise(y[24], t, 24), n) + Math.pow(applyNoise(y[69], t, 69), n) + Math.pow(applyNoise(y[26], t, 26), n) + Math.pow(applyNoise(y[14], t, 14), n) + Math.pow(applyNoise(y[5], t, 5), n);
			yDot[2] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[2];
		}

		// Caspase 8
		if(isOverrideDerivative(t, 3)) {
			yDot[3] = overrideDerivative(t, 3);
		} else { 
			// activators : Fas, TNF-α
			activators = Math.pow(applyNoise(y[78], t, 78), n) + Math.pow(applyNoise(y[74], t, 74), n);
			inhibitors = 0;
			yDot[3] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[3];
		}

		// Ras
		if(isOverrideDerivative(t, 4)) {
			yDot[4] = overrideDerivative(t, 4);
		} else { 
			// activators : EGFR, FGFR2, IL-6, VEGF
			activators = Math.pow(applyNoise(y[47], t, 47), n) + Math.pow(applyNoise(y[80], t, 80), n) + Math.pow(applyNoise(y[37], t, 37), n) + Math.pow(applyNoise(y[67], t, 67), n);
			// inhibitors : p53
			inhibitors = Math.pow(applyNoise(y[10], t, 10), n);
			yDot[4] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[4];
		}

		// Stat5
		if(isOverrideDerivative(t, 5)) {
			yDot[5] = overrideDerivative(t, 5);
		} else { 
			// activators : FGFR2, G-CSFR, IL-3, VEGF
			activators = Math.pow(applyNoise(y[80], t, 80), n) + Math.pow(applyNoise(y[43], t, 43), n) + Math.pow(applyNoise(y[30], t, 30), n) + Math.pow(applyNoise(y[67], t, 67), n);
			// inhibitors : PRunx2, Runx1
			inhibitors = Math.pow(applyNoise(y[29], t, 29), n) + Math.pow(applyNoise(y[58], t, 58), n);
			yDot[5] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[5];
		}

		// PTEN
		if(isOverrideDerivative(t, 6)) {
			yDot[6] = overrideDerivative(t, 6);
		} else { 
			// activators : AP2, PPARγ, p53
			activators = Math.pow(applyNoise(y[61], t, 61), n) + Math.pow(applyNoise(y[36], t, 36), n) + Math.pow(applyNoise(y[10], t, 10), n);
			// inhibitors : NF-κB, SStat3
			inhibitors = Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[25], t, 25), n);
			yDot[6] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[6];
		}

		// OPN
		if(isOverrideDerivative(t, 7)) {
			yDot[7] = overrideDerivative(t, 7);
		} else { 
			// activators : PRunx2, Runx1, TGF-β
			activators = Math.pow(applyNoise(y[29], t, 29), n) + Math.pow(applyNoise(y[58], t, 58), n) + Math.pow(applyNoise(y[52], t, 52), n);
			// inhibitors : PGC-1
			inhibitors = Math.pow(applyNoise(y[51], t, 51), n);
			yDot[7] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[7];
		}

		// FGF2
		if(isOverrideDerivative(t, 8)) {
			yDot[8] = overrideDerivative(t, 8);
		} else { 
			// activators : HSPG2, Hoxa10
			activators = Math.pow(applyNoise(y[33], t, 33), n) + Math.pow(applyNoise(y[57], t, 57), n);
			inhibitors = 0;
			yDot[8] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[8];
		}

		// Pu.1
		if(isOverrideDerivative(t, 9)) {
			yDot[9] = overrideDerivative(t, 9);
		} else { 
			// activators : C/EBPα, Runx1
			activators = Math.pow(applyNoise(y[2], t, 2), n) + Math.pow(applyNoise(y[58], t, 58), n);
			// inhibitors : Gata1/2, Sox4
			inhibitors = Math.pow(applyNoise(y[60], t, 60), n) + Math.pow(applyNoise(y[22], t, 22), n);
			yDot[9] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[9];
		}

		// p53
		if(isOverrideDerivative(t, 10)) {
			yDot[10] = overrideDerivative(t, 10);
		} else { 
			// activators : Cdk6, IFN-γ, Myc, PPARγ, PTEN, RARs
			activators = Math.pow(applyNoise(y[73], t, 73), n) + Math.pow(applyNoise(y[15], t, 15), n) + Math.pow(applyNoise(y[18], t, 18), n) + Math.pow(applyNoise(y[36], t, 36), n) + Math.pow(applyNoise(y[6], t, 6), n) + Math.pow(applyNoise(y[75], t, 75), n);
			// inhibitors : Bach1, Sox4, TAkt
			inhibitors = Math.pow(applyNoise(y[59], t, 59), n) + Math.pow(applyNoise(y[22], t, 22), n) + Math.pow(applyNoise(y[38], t, 38), n);
			yDot[10] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[10];
		}

		// NF-κB
		if(isOverrideDerivative(t, 11)) {
			yDot[11] = overrideDerivative(t, 11);
		} else { 
			// activators : C/EBPβ, IFN-γ, IL-1, TNF-α
			activators = Math.pow(applyNoise(y[0], t, 0), n) + Math.pow(applyNoise(y[15], t, 15), n) + Math.pow(applyNoise(y[24], t, 24), n) + Math.pow(applyNoise(y[74], t, 74), n);
			// inhibitors : Cdx2, PPARγ, Sox2, iκB
			inhibitors = Math.pow(applyNoise(y[64], t, 64), n) + Math.pow(applyNoise(y[36], t, 36), n) + Math.pow(applyNoise(y[12], t, 12), n) + Math.pow(applyNoise(y[70], t, 70), n);
			yDot[11] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[11];
		}

		// Sox2
		if(isOverrideDerivative(t, 12)) {
			yDot[12] = overrideDerivative(t, 12);
		} else { 
			// activators : AP2, FGFR2, SStat3, Wnt
			activators = Math.pow(applyNoise(y[61], t, 61), n) + Math.pow(applyNoise(y[80], t, 80), n) + Math.pow(applyNoise(y[25], t, 25), n) + Math.pow(applyNoise(y[56], t, 56), n);
			// inhibitors : p21
			inhibitors = Math.pow(applyNoise(y[34], t, 34), n);
			yDot[12] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[12];
		}

		// FGF7
		if(isOverrideDerivative(t, 13)) {
			yDot[13] = overrideDerivative(t, 13);
		} else { 
			// activators : HSPG2, IL-1
			activators = Math.pow(applyNoise(y[33], t, 33), n) + Math.pow(applyNoise(y[24], t, 24), n);
			inhibitors = 0;
			yDot[13] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[13];
		}

		// SHH
		if(isOverrideDerivative(t, 14)) {
			yDot[14] = overrideDerivative(t, 14);
		} else { 
			// activators : Sox2, TGF-β
			activators = Math.pow(applyNoise(y[12], t, 12), n) + Math.pow(applyNoise(y[52], t, 52), n);
			// inhibitors : BMP, Cdx2, Hes1, p53
			inhibitors = Math.pow(applyNoise(y[63], t, 63), n) + Math.pow(applyNoise(y[64], t, 64), n) + Math.pow(applyNoise(y[53], t, 53), n) + Math.pow(applyNoise(y[10], t, 10), n);
			yDot[14] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[14];
		}

		// IFN-γ
		if(isOverrideDerivative(t, 15)) {
			yDot[15] = overrideDerivative(t, 15);
		} else { 
			// activators : NF-κB, OPN, Runx1, Stat1
			activators = Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[7], t, 7), n) + Math.pow(applyNoise(y[58], t, 58), n) + Math.pow(applyNoise(y[79], t, 79), n);
			inhibitors = 0;
			yDot[15] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[15];
		}

		// Integrin
		if(isOverrideDerivative(t, 16)) {
			yDot[16] = overrideDerivative(t, 16);
		} else { 
			// activators : EGF, MAPK, TNF-α, VEGF
			activators = Math.pow(applyNoise(y[66], t, 66), n) + Math.pow(applyNoise(y[31], t, 31), n) + Math.pow(applyNoise(y[74], t, 74), n) + Math.pow(applyNoise(y[67], t, 67), n);
			// inhibitors : Myc
			inhibitors = Math.pow(applyNoise(y[18], t, 18), n);
			yDot[16] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[16];
		}

		// Hey2
		if(isOverrideDerivative(t, 17)) {
			yDot[17] = overrideDerivative(t, 17);
		} else { 
			// activators : Notch
			activators = Math.pow(applyNoise(y[26], t, 26), n);
			inhibitors = 0;
			yDot[17] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[17];
		}

		// Myc
		if(isOverrideDerivative(t, 18)) {
			yDot[18] = overrideDerivative(t, 18);
		} else { 
			// activators : E2F, MAPK, Notch, SHH, Wnt, pRb
			activators = Math.pow(applyNoise(y[50], t, 50), n) + Math.pow(applyNoise(y[31], t, 31), n) + Math.pow(applyNoise(y[26], t, 26), n) + Math.pow(applyNoise(y[14], t, 14), n) + Math.pow(applyNoise(y[56], t, 56), n) + Math.pow(applyNoise(y[42], t, 42), n);
			// inhibitors : AP2, TGF-β, p53
			inhibitors = Math.pow(applyNoise(y[61], t, 61), n) + Math.pow(applyNoise(y[52], t, 52), n) + Math.pow(applyNoise(y[10], t, 10), n);
			yDot[18] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[18];
		}

		// Caspase 3
		if(isOverrideDerivative(t, 19)) {
			yDot[19] = overrideDerivative(t, 19);
		} else { 
			// activators : Bad, Caspase 8, Cytochrome C, Fas, TNF-α
			activators = Math.pow(applyNoise(y[65], t, 65), n) + Math.pow(applyNoise(y[3], t, 3), n) + Math.pow(applyNoise(y[49], t, 49), n) + Math.pow(applyNoise(y[78], t, 78), n) + Math.pow(applyNoise(y[74], t, 74), n);
			// inhibitors : NF-κB, XIAP
			inhibitors = Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[46], t, 46), n);
			yDot[19] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[19];
		}

		// Sox9
		if(isOverrideDerivative(t, 20)) {
			yDot[20] = overrideDerivative(t, 20);
		} else { 
			// activators : AP2, BMP, PKA, SHH, TGF-β, Wnt
			activators = Math.pow(applyNoise(y[61], t, 61), n) + Math.pow(applyNoise(y[63], t, 63), n) + Math.pow(applyNoise(y[28], t, 28), n) + Math.pow(applyNoise(y[14], t, 14), n) + Math.pow(applyNoise(y[52], t, 52), n) + Math.pow(applyNoise(y[56], t, 56), n);
			// inhibitors : IL-1, NF-κB, RARs, c-Jun
			inhibitors = Math.pow(applyNoise(y[24], t, 24), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[75], t, 75), n) + Math.pow(applyNoise(y[71], t, 71), n);
			yDot[20] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[20];
		}

		// Sox7
		if(isOverrideDerivative(t, 21)) {
			yDot[21] = overrideDerivative(t, 21);
		} else { 
			// activators : BMP, c-Jun
			activators = Math.pow(applyNoise(y[63], t, 63), n) + Math.pow(applyNoise(y[71], t, 71), n);
			// inhibitors : PRDM14
			inhibitors = Math.pow(applyNoise(y[45], t, 45), n);
			yDot[21] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[21];
		}

		// Sox4
		if(isOverrideDerivative(t, 22)) {
			yDot[22] = overrideDerivative(t, 22);
		} else { 
			// activators : Sox7, Stat5, TGF-β, Wnt
			activators = Math.pow(applyNoise(y[21], t, 21), n) + Math.pow(applyNoise(y[5], t, 5), n) + Math.pow(applyNoise(y[52], t, 52), n) + Math.pow(applyNoise(y[56], t, 56), n);
			// inhibitors : C/EBPα
			inhibitors = Math.pow(applyNoise(y[2], t, 2), n);
			yDot[22] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[22];
		}

		// Gata4/6
		if(isOverrideDerivative(t, 23)) {
			yDot[23] = overrideDerivative(t, 23);
		} else { 
			// activators : PKA, Sox7, Wnt
			activators = Math.pow(applyNoise(y[28], t, 28), n) + Math.pow(applyNoise(y[21], t, 21), n) + Math.pow(applyNoise(y[56], t, 56), n);
			// inhibitors : Hey2, PRDM14, Sox2, c-Jun
			inhibitors = Math.pow(applyNoise(y[17], t, 17), n) + Math.pow(applyNoise(y[45], t, 45), n) + Math.pow(applyNoise(y[12], t, 12), n) + Math.pow(applyNoise(y[71], t, 71), n);
			yDot[23] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[23];
		}

		// IL-1
		if(isOverrideDerivative(t, 24)) {
			yDot[24] = overrideDerivative(t, 24);
		} else { 
			// activators : C/EBPβ, NF-κB, OPN, Stat1
			activators = Math.pow(applyNoise(y[0], t, 0), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[7], t, 7), n) + Math.pow(applyNoise(y[79], t, 79), n);
			// inhibitors : AP2, C/EBPα, TAkt
			inhibitors = Math.pow(applyNoise(y[61], t, 61), n) + Math.pow(applyNoise(y[2], t, 2), n) + Math.pow(applyNoise(y[38], t, 38), n);
			yDot[24] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[24];
		}

		// SStat3
		if(isOverrideDerivative(t, 25)) {
			yDot[25] = overrideDerivative(t, 25);
		} else { 
			// activators : FGFR2, G-CSFR, IL-6, IL-8, OPN, VEGF
			activators = Math.pow(applyNoise(y[80], t, 80), n) + Math.pow(applyNoise(y[43], t, 43), n) + Math.pow(applyNoise(y[37], t, 37), n) + Math.pow(applyNoise(y[32], t, 32), n) + Math.pow(applyNoise(y[7], t, 7), n) + Math.pow(applyNoise(y[67], t, 67), n);
			// inhibitors : Gata1/2, PPARγ, PTEN, RARs
			inhibitors = Math.pow(applyNoise(y[60], t, 60), n) + Math.pow(applyNoise(y[36], t, 36), n) + Math.pow(applyNoise(y[6], t, 6), n) + Math.pow(applyNoise(y[75], t, 75), n);
			yDot[25] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[25];
		}

		// Notch
		if(isOverrideDerivative(t, 26)) {
			yDot[26] = overrideDerivative(t, 26);
		} else { 
			// activators : Cdx2, FGFR2, NF-κB, SStat3
			activators = Math.pow(applyNoise(y[64], t, 64), n) + Math.pow(applyNoise(y[80], t, 80), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[25], t, 25), n);
			// inhibitors : BMP, C/EBPα, Hoxa5, NR2F2
			inhibitors = Math.pow(applyNoise(y[63], t, 63), n) + Math.pow(applyNoise(y[2], t, 2), n) + Math.pow(applyNoise(y[48], t, 48), n) + Math.pow(applyNoise(y[69], t, 69), n);
			yDot[26] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[26];
		}

		// NOG
		if(isOverrideDerivative(t, 27)) {
			yDot[27] = overrideDerivative(t, 27);
		} else { 
			// activators : SHH, Sox9, Wnt
			activators = Math.pow(applyNoise(y[14], t, 14), n) + Math.pow(applyNoise(y[20], t, 20), n) + Math.pow(applyNoise(y[56], t, 56), n);
			// inhibitors : FGFR2
			inhibitors = Math.pow(applyNoise(y[80], t, 80), n);
			yDot[27] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[27];
		}

		// PKA
		if(isOverrideDerivative(t, 28)) {
			yDot[28] = overrideDerivative(t, 28);
		} else { 
			// activators : EGFR, TAkt, TGF-β
			activators = Math.pow(applyNoise(y[47], t, 47), n) + Math.pow(applyNoise(y[38], t, 38), n) + Math.pow(applyNoise(y[52], t, 52), n);
			// inhibitors : p53
			inhibitors = Math.pow(applyNoise(y[10], t, 10), n);
			yDot[28] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[28];
		}

		// PRunx2
		if(isOverrideDerivative(t, 29)) {
			yDot[29] = overrideDerivative(t, 29);
		} else { 
			// activators : BMP, Hoxa10, Runx1, TGF-β
			activators = Math.pow(applyNoise(y[63], t, 63), n) + Math.pow(applyNoise(y[57], t, 57), n) + Math.pow(applyNoise(y[58], t, 58), n) + Math.pow(applyNoise(y[52], t, 52), n);
			// inhibitors : C/EBPβ, NR2F2, Notch, Stat5
			inhibitors = Math.pow(applyNoise(y[0], t, 0), n) + Math.pow(applyNoise(y[69], t, 69), n) + Math.pow(applyNoise(y[26], t, 26), n) + Math.pow(applyNoise(y[5], t, 5), n);
			yDot[29] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[29];
		}

		// IL-3
		if(isOverrideDerivative(t, 30)) {
			yDot[30] = overrideDerivative(t, 30);
		} else { 
			// activators : C/EBPα, Runx1
			activators = Math.pow(applyNoise(y[2], t, 2), n) + Math.pow(applyNoise(y[58], t, 58), n);
			inhibitors = 0;
			yDot[30] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[30];
		}

		// MAPK
		if(isOverrideDerivative(t, 31)) {
			yDot[31] = overrideDerivative(t, 31);
		} else { 
			// activators : EGFR, FGFR2, IFN-γ, Integrin, NF-κB, NR2F2, PPARγ, Ras
			activators = Math.pow(applyNoise(y[47], t, 47), n) + Math.pow(applyNoise(y[80], t, 80), n) + Math.pow(applyNoise(y[15], t, 15), n) + Math.pow(applyNoise(y[16], t, 16), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[69], t, 69), n) + Math.pow(applyNoise(y[36], t, 36), n) + Math.pow(applyNoise(y[4], t, 4), n);
			// inhibitors : PTEN, TGF-β
			inhibitors = Math.pow(applyNoise(y[6], t, 6), n) + Math.pow(applyNoise(y[52], t, 52), n);
			yDot[31] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[31];
		}

		// IL-8
		if(isOverrideDerivative(t, 32)) {
			yDot[32] = overrideDerivative(t, 32);
		} else { 
			// activators : NF-κB, Stat1, TGF-β, c-Jun
			activators = Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[79], t, 79), n) + Math.pow(applyNoise(y[52], t, 52), n) + Math.pow(applyNoise(y[71], t, 71), n);
			// inhibitors : SStat3
			inhibitors = Math.pow(applyNoise(y[25], t, 25), n);
			yDot[32] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[32];
		}

		// HSPG2
		if(isOverrideDerivative(t, 33)) {
			yDot[33] = overrideDerivative(t, 33);
		} else { 
			// activators : PRunx2, TGF-β
			activators = Math.pow(applyNoise(y[29], t, 29), n) + Math.pow(applyNoise(y[52], t, 52), n);
			inhibitors = 0;
			yDot[33] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[33];
		}

		// p21
		if(isOverrideDerivative(t, 34)) {
			yDot[34] = overrideDerivative(t, 34);
		} else { 
			// activators : AP2, C/EBPα, Gata1/2, TGF-β, TNF-α, p53
			activators = Math.pow(applyNoise(y[61], t, 61), n) + Math.pow(applyNoise(y[2], t, 2), n) + Math.pow(applyNoise(y[60], t, 60), n) + Math.pow(applyNoise(y[52], t, 52), n) + Math.pow(applyNoise(y[74], t, 74), n) + Math.pow(applyNoise(y[10], t, 10), n);
			// inhibitors : Hes1, Myc, TAkt
			inhibitors = Math.pow(applyNoise(y[53], t, 53), n) + Math.pow(applyNoise(y[18], t, 18), n) + Math.pow(applyNoise(y[38], t, 38), n);
			yDot[34] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[34];
		}

		// IL-10
		if(isOverrideDerivative(t, 35)) {
			yDot[35] = overrideDerivative(t, 35);
		} else { 
			// activators : Fas, TNF-α
			activators = Math.pow(applyNoise(y[78], t, 78), n) + Math.pow(applyNoise(y[74], t, 74), n);
			// inhibitors : IL-10
			inhibitors = Math.pow(applyNoise(y[35], t, 35), n);
			yDot[35] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[35];
		}

		// PPARγ
		if(isOverrideDerivative(t, 36)) {
			yDot[36] = overrideDerivative(t, 36);
		} else { 
			// activators : C/EBPα, C/EBPβ
			activators = Math.pow(applyNoise(y[2], t, 2), n) + Math.pow(applyNoise(y[0], t, 0), n);
			// inhibitors : NR2F2, RARs, TGF-β, TNF-α
			inhibitors = Math.pow(applyNoise(y[69], t, 69), n) + Math.pow(applyNoise(y[75], t, 75), n) + Math.pow(applyNoise(y[52], t, 52), n) + Math.pow(applyNoise(y[74], t, 74), n);
			yDot[36] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[36];
		}

		// IL-6
		if(isOverrideDerivative(t, 37)) {
			yDot[37] = overrideDerivative(t, 37);
		} else { 
			// activators : C/EBPβ, NGAL, PHoxa3
			activators = Math.pow(applyNoise(y[0], t, 0), n) + Math.pow(applyNoise(y[1], t, 1), n) + Math.pow(applyNoise(y[40], t, 40), n);
			// inhibitors : RARs, Sox2
			inhibitors = Math.pow(applyNoise(y[75], t, 75), n) + Math.pow(applyNoise(y[12], t, 12), n);
			yDot[37] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[37];
		}

		// TAkt
		if(isOverrideDerivative(t, 38)) {
			yDot[38] = overrideDerivative(t, 38);
		} else { 
			// activators : EGF, EGFR, FGFR2, NF-κB, Notch, PKA, Ras, c-Jun
			activators = Math.pow(applyNoise(y[66], t, 66), n) + Math.pow(applyNoise(y[47], t, 47), n) + Math.pow(applyNoise(y[80], t, 80), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[26], t, 26), n) + Math.pow(applyNoise(y[28], t, 28), n) + Math.pow(applyNoise(y[4], t, 4), n) + Math.pow(applyNoise(y[71], t, 71), n);
			// inhibitors : PTEN
			inhibitors = Math.pow(applyNoise(y[6], t, 6), n);
			yDot[38] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[38];
		}

		// p27
		if(isOverrideDerivative(t, 39)) {
			yDot[39] = overrideDerivative(t, 39);
		} else { 
			// activators : Gata1/2, PTEN, Stat1, TGF-β
			activators = Math.pow(applyNoise(y[60], t, 60), n) + Math.pow(applyNoise(y[6], t, 6), n) + Math.pow(applyNoise(y[79], t, 79), n) + Math.pow(applyNoise(y[52], t, 52), n);
			// inhibitors : Hes1, Myc, TAkt
			inhibitors = Math.pow(applyNoise(y[53], t, 53), n) + Math.pow(applyNoise(y[18], t, 18), n) + Math.pow(applyNoise(y[38], t, 38), n);
			yDot[39] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[39];
		}

		// PHoxa3
		if(isOverrideDerivative(t, 40)) {
			yDot[40] = overrideDerivative(t, 40);
		} else { 
			// activators : RARs, Wnt, c-Jun
			activators = Math.pow(applyNoise(y[75], t, 75), n) + Math.pow(applyNoise(y[56], t, 56), n) + Math.pow(applyNoise(y[71], t, 71), n);
			// inhibitors : SStat3
			inhibitors = Math.pow(applyNoise(y[25], t, 25), n);
			yDot[40] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[40];
		}

		// Bcl-2
		if(isOverrideDerivative(t, 41)) {
			yDot[41] = overrideDerivative(t, 41);
		} else { 
			// activators : Integrin, NF-κB, VEGF
			activators = Math.pow(applyNoise(y[16], t, 16), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[67], t, 67), n);
			// inhibitors : Caspase 3, TGF-β, p53
			inhibitors = Math.pow(applyNoise(y[19], t, 19), n) + Math.pow(applyNoise(y[52], t, 52), n) + Math.pow(applyNoise(y[10], t, 10), n);
			yDot[41] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[41];
		}

		// pRb
		if(isOverrideDerivative(t, 42)) {
			yDot[42] = overrideDerivative(t, 42);
		} else { 
			// activators : Cdk2, Cdk6, Cyclin D, Cyclin E
			activators = Math.pow(applyNoise(y[76], t, 76), n) + Math.pow(applyNoise(y[73], t, 73), n) + Math.pow(applyNoise(y[55], t, 55), n) + Math.pow(applyNoise(y[54], t, 54), n);
			inhibitors = 0;
			yDot[42] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[42];
		}

		// G-CSFR
		if(isOverrideDerivative(t, 43)) {
			yDot[43] = overrideDerivative(t, 43);
		} else { 
			// activators : C/EBPα, NF-κB, Pu.1
			activators = Math.pow(applyNoise(y[2], t, 2), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[9], t, 9), n);
			inhibitors = 0;
			yDot[43] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[43];
		}

		// Hoxa9
		if(isOverrideDerivative(t, 44)) {
			yDot[44] = overrideDerivative(t, 44);
		} else { 
			// activators : C/EBPα, Notch, Pu.1
			activators = Math.pow(applyNoise(y[2], t, 2), n) + Math.pow(applyNoise(y[26], t, 26), n) + Math.pow(applyNoise(y[9], t, 9), n);
			// inhibitors : NF-κB
			inhibitors = Math.pow(applyNoise(y[11], t, 11), n);
			yDot[44] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[44];
		}

		// PRDM14
		if(isOverrideDerivative(t, 45)) {
			yDot[45] = overrideDerivative(t, 45);
		} else { 
			// activators : SStat3
			activators = Math.pow(applyNoise(y[25], t, 25), n);
			inhibitors = 0;
			yDot[45] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[45];
		}

		// XIAP
		if(isOverrideDerivative(t, 46)) {
			yDot[46] = overrideDerivative(t, 46);
		} else { 
			// activators : Integrin, MAPK, NF-κB, RARs, TAkt
			activators = Math.pow(applyNoise(y[16], t, 16), n) + Math.pow(applyNoise(y[31], t, 31), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[75], t, 75), n) + Math.pow(applyNoise(y[38], t, 38), n);
			// inhibitors : Caspase 3
			inhibitors = Math.pow(applyNoise(y[19], t, 19), n);
			yDot[46] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[46];
		}

		// EGFR
		if(isOverrideDerivative(t, 47)) {
			yDot[47] = overrideDerivative(t, 47);
		} else { 
			// activators : EGF, Myc, Runx1, Sox4
			activators = Math.pow(applyNoise(y[66], t, 66), n) + Math.pow(applyNoise(y[18], t, 18), n) + Math.pow(applyNoise(y[58], t, 58), n) + Math.pow(applyNoise(y[22], t, 22), n);
			// inhibitors : PTEN
			inhibitors = Math.pow(applyNoise(y[6], t, 6), n);
			yDot[47] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[47];
		}

		// Hoxa5
		if(isOverrideDerivative(t, 48)) {
			yDot[48] = overrideDerivative(t, 48);
		} else { 
			// activators : Hoxa10, RARs
			activators = Math.pow(applyNoise(y[57], t, 57), n) + Math.pow(applyNoise(y[75], t, 75), n);
			// inhibitors : Runx1
			inhibitors = Math.pow(applyNoise(y[58], t, 58), n);
			yDot[48] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[48];
		}

		// Cytochrome C
		if(isOverrideDerivative(t, 49)) {
			yDot[49] = overrideDerivative(t, 49);
		} else { 
			// activators : Bad, Bax, Caspase 3
			activators = Math.pow(applyNoise(y[65], t, 65), n) + Math.pow(applyNoise(y[77], t, 77), n) + Math.pow(applyNoise(y[19], t, 19), n);
			// inhibitors : Bcl-2, Bcl-xL, p21
			inhibitors = Math.pow(applyNoise(y[41], t, 41), n) + Math.pow(applyNoise(y[72], t, 72), n) + Math.pow(applyNoise(y[34], t, 34), n);
			yDot[49] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[49];
		}

		// E2F
		if(isOverrideDerivative(t, 50)) {
			yDot[50] = overrideDerivative(t, 50);
		} else { 
			// activators : E2F, Myc
			activators = Math.pow(applyNoise(y[50], t, 50), n) + Math.pow(applyNoise(y[18], t, 18), n);
			// inhibitors : p21
			inhibitors = Math.pow(applyNoise(y[34], t, 34), n);
			yDot[50] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[50];
		}

		// PGC-1
		if(isOverrideDerivative(t, 51)) {
			yDot[51] = overrideDerivative(t, 51);
		} else { 
			// activators : C/EBPβ, IFN-γ
			activators = Math.pow(applyNoise(y[0], t, 0), n) + Math.pow(applyNoise(y[15], t, 15), n);
			// inhibitors : p53
			inhibitors = Math.pow(applyNoise(y[10], t, 10), n);
			yDot[51] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[51];
		}

		// TGF-β
		if(isOverrideDerivative(t, 52)) {
			yDot[52] = overrideDerivative(t, 52);
		} else { 
			// activators : Hoxa10, OPN, PHoxa3, SStat3, Sox7, TNF-α, p53
			activators = Math.pow(applyNoise(y[57], t, 57), n) + Math.pow(applyNoise(y[7], t, 7), n) + Math.pow(applyNoise(y[40], t, 40), n) + Math.pow(applyNoise(y[25], t, 25), n) + Math.pow(applyNoise(y[21], t, 21), n) + Math.pow(applyNoise(y[74], t, 74), n) + Math.pow(applyNoise(y[10], t, 10), n);
			// inhibitors : NF-κB, NR2F2, RARs, Stat1, Stat5
			inhibitors = Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[69], t, 69), n) + Math.pow(applyNoise(y[75], t, 75), n) + Math.pow(applyNoise(y[79], t, 79), n) + Math.pow(applyNoise(y[5], t, 5), n);
			yDot[52] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[52];
		}

		// Hes1
		if(isOverrideDerivative(t, 53)) {
			yDot[53] = overrideDerivative(t, 53);
		} else { 
			// activators : Notch, TGF-β
			activators = Math.pow(applyNoise(y[26], t, 26), n) + Math.pow(applyNoise(y[52], t, 52), n);
			inhibitors = 0;
			yDot[53] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[53];
		}

		// Cyclin E
		if(isOverrideDerivative(t, 54)) {
			yDot[54] = overrideDerivative(t, 54);
		} else { 
			// activators : Bach1, E2F, Myc
			activators = Math.pow(applyNoise(y[59], t, 59), n) + Math.pow(applyNoise(y[50], t, 50), n) + Math.pow(applyNoise(y[18], t, 18), n);
			// inhibitors : PTEN, p21, p27
			inhibitors = Math.pow(applyNoise(y[6], t, 6), n) + Math.pow(applyNoise(y[34], t, 34), n) + Math.pow(applyNoise(y[39], t, 39), n);
			yDot[54] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[54];
		}

		// Cyclin D
		if(isOverrideDerivative(t, 55)) {
			yDot[55] = overrideDerivative(t, 55);
		} else { 
			// activators : E2F, EGFR, Myc
			activators = Math.pow(applyNoise(y[50], t, 50), n) + Math.pow(applyNoise(y[47], t, 47), n) + Math.pow(applyNoise(y[18], t, 18), n);
			// inhibitors : p27, p53
			inhibitors = Math.pow(applyNoise(y[39], t, 39), n) + Math.pow(applyNoise(y[10], t, 10), n);
			yDot[55] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[55];
		}

		// Wnt
		if(isOverrideDerivative(t, 56)) {
			yDot[56] = overrideDerivative(t, 56);
		} else { 
			// activators : PGC-1, Sox4, TGF-β
			activators = Math.pow(applyNoise(y[51], t, 51), n) + Math.pow(applyNoise(y[22], t, 22), n) + Math.pow(applyNoise(y[52], t, 52), n);
			// inhibitors : Cdx2, Sox2, Sox7, Sox9
			inhibitors = Math.pow(applyNoise(y[64], t, 64), n) + Math.pow(applyNoise(y[12], t, 12), n) + Math.pow(applyNoise(y[21], t, 21), n) + Math.pow(applyNoise(y[20], t, 20), n);
			yDot[56] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[56];
		}

		// Hoxa10
		if(isOverrideDerivative(t, 57)) {
			yDot[57] = overrideDerivative(t, 57);
		} else { 
			// activators : BMP, Hoxa9, Stat5, Wnt
			activators = Math.pow(applyNoise(y[63], t, 63), n) + Math.pow(applyNoise(y[44], t, 44), n) + Math.pow(applyNoise(y[5], t, 5), n) + Math.pow(applyNoise(y[56], t, 56), n);
			// inhibitors : Cdx2
			inhibitors = Math.pow(applyNoise(y[64], t, 64), n);
			yDot[57] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[57];
		}

		// Runx1
		if(isOverrideDerivative(t, 58)) {
			yDot[58] = overrideDerivative(t, 58);
		} else { 
			// activators : Cdx2, Gata1/2, Notch, Sox2, c-Jun
			activators = Math.pow(applyNoise(y[64], t, 64), n) + Math.pow(applyNoise(y[60], t, 60), n) + Math.pow(applyNoise(y[26], t, 26), n) + Math.pow(applyNoise(y[12], t, 12), n) + Math.pow(applyNoise(y[71], t, 71), n);
			// inhibitors : PHoxa3, Stat5
			inhibitors = Math.pow(applyNoise(y[40], t, 40), n) + Math.pow(applyNoise(y[5], t, 5), n);
			yDot[58] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[58];
		}

		// Bach1
		if(isOverrideDerivative(t, 59)) {
			yDot[59] = overrideDerivative(t, 59);
		} else { 
			// activators : HIF, TGF-β
			activators = Math.pow(applyNoise(y[62], t, 62), n) + Math.pow(applyNoise(y[52], t, 52), n);
			inhibitors = 0;
			yDot[59] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[59];
		}

		// Gata1/2
		if(isOverrideDerivative(t, 60)) {
			yDot[60] = overrideDerivative(t, 60);
		} else { 
			// activators : Cdx2, Notch, SStat3
			activators = Math.pow(applyNoise(y[64], t, 64), n) + Math.pow(applyNoise(y[26], t, 26), n) + Math.pow(applyNoise(y[25], t, 25), n);
			// inhibitors : Hes1, Hoxa10, Pu.1, VEGF, c-Jun
			inhibitors = Math.pow(applyNoise(y[53], t, 53), n) + Math.pow(applyNoise(y[57], t, 57), n) + Math.pow(applyNoise(y[9], t, 9), n) + Math.pow(applyNoise(y[67], t, 67), n) + Math.pow(applyNoise(y[71], t, 71), n);
			yDot[60] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[60];
		}

		// AP2
		if(isOverrideDerivative(t, 61)) {
			yDot[61] = overrideDerivative(t, 61);
		} else { 
			// activators : C/EBPβ, NR2F2, RARs
			activators = Math.pow(applyNoise(y[0], t, 0), n) + Math.pow(applyNoise(y[69], t, 69), n) + Math.pow(applyNoise(y[75], t, 75), n);
			inhibitors = 0;
			yDot[61] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[61];
		}

		// HIF
		if(isOverrideDerivative(t, 62)) {
			yDot[62] = overrideDerivative(t, 62);
		} else { 
			// activators : SStat3, TAkt
			activators = Math.pow(applyNoise(y[25], t, 25), n) + Math.pow(applyNoise(y[38], t, 38), n);
			// inhibitors : p53
			inhibitors = Math.pow(applyNoise(y[10], t, 10), n);
			yDot[62] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[62];
		}

		// BMP
		if(isOverrideDerivative(t, 63)) {
			yDot[63] = overrideDerivative(t, 63);
		} else { 
			// activators : EGFR, FGFR2, Gata4/6, Runx1
			activators = Math.pow(applyNoise(y[47], t, 47), n) + Math.pow(applyNoise(y[80], t, 80), n) + Math.pow(applyNoise(y[23], t, 23), n) + Math.pow(applyNoise(y[58], t, 58), n);
			// inhibitors : NOG, NR2F2, TGF-β
			inhibitors = Math.pow(applyNoise(y[27], t, 27), n) + Math.pow(applyNoise(y[69], t, 69), n) + Math.pow(applyNoise(y[52], t, 52), n);
			yDot[63] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[63];
		}

		// Cdx2
		if(isOverrideDerivative(t, 64)) {
			yDot[64] = overrideDerivative(t, 64);
		} else { 
			// activators : BMP, Gata1/2, MAPK, Sox2, c-Jun
			activators = Math.pow(applyNoise(y[63], t, 63), n) + Math.pow(applyNoise(y[60], t, 60), n) + Math.pow(applyNoise(y[31], t, 31), n) + Math.pow(applyNoise(y[12], t, 12), n) + Math.pow(applyNoise(y[71], t, 71), n);
			// inhibitors : C/EBPβ, SStat3, Sox9, TGF-β
			inhibitors = Math.pow(applyNoise(y[0], t, 0), n) + Math.pow(applyNoise(y[25], t, 25), n) + Math.pow(applyNoise(y[20], t, 20), n) + Math.pow(applyNoise(y[52], t, 52), n);
			yDot[64] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[64];
		}

		// Bad
		if(isOverrideDerivative(t, 65)) {
			yDot[65] = overrideDerivative(t, 65);
		} else { 
			// activators : TNF-α, p53
			activators = Math.pow(applyNoise(y[74], t, 74), n) + Math.pow(applyNoise(y[10], t, 10), n);
			// inhibitors : MAPK, NF-κB, TAkt, p21
			inhibitors = Math.pow(applyNoise(y[31], t, 31), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[38], t, 38), n) + Math.pow(applyNoise(y[34], t, 34), n);
			yDot[65] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[65];
		}

		// EGF
		if(isOverrideDerivative(t, 66)) {
			yDot[66] = overrideDerivative(t, 66);
		} else { 
			// activators : HIF
			activators = Math.pow(applyNoise(y[62], t, 62), n);
			inhibitors = 0;
			yDot[66] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[66];
		}

		// VEGF
		if(isOverrideDerivative(t, 67)) {
			yDot[67] = overrideDerivative(t, 67);
		} else { 
			// activators : Gata4/6, HIF, NR2F2, PGC-1, SHH, SStat3, TGF-β
			activators = Math.pow(applyNoise(y[23], t, 23), n) + Math.pow(applyNoise(y[62], t, 62), n) + Math.pow(applyNoise(y[69], t, 69), n) + Math.pow(applyNoise(y[51], t, 51), n) + Math.pow(applyNoise(y[14], t, 14), n) + Math.pow(applyNoise(y[25], t, 25), n) + Math.pow(applyNoise(y[52], t, 52), n);
			// inhibitors : RARs, Sox9
			inhibitors = Math.pow(applyNoise(y[75], t, 75), n) + Math.pow(applyNoise(y[20], t, 20), n);
			yDot[67] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[67];
		}

		// Bim
		if(isOverrideDerivative(t, 68)) {
			yDot[68] = overrideDerivative(t, 68);
		} else { 
			activators = 0;
			// inhibitors : MAPK, TAkt
			inhibitors = Math.pow(applyNoise(y[31], t, 31), n) + Math.pow(applyNoise(y[38], t, 38), n);
			yDot[68] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[68];
		}

		// NR2F2
		if(isOverrideDerivative(t, 69)) {
			yDot[69] = overrideDerivative(t, 69);
		} else { 
			// activators : SHH, Wnt
			activators = Math.pow(applyNoise(y[14], t, 14), n) + Math.pow(applyNoise(y[56], t, 56), n);
			inhibitors = 0;
			yDot[69] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[69];
		}

		// iκB
		if(isOverrideDerivative(t, 70)) {
			yDot[70] = overrideDerivative(t, 70);
		} else { 
			// activators : NF-κB, TGF-β
			activators = Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[52], t, 52), n);
			// inhibitors : EGF, Fas, TAkt, TNF-α
			inhibitors = Math.pow(applyNoise(y[66], t, 66), n) + Math.pow(applyNoise(y[78], t, 78), n) + Math.pow(applyNoise(y[38], t, 38), n) + Math.pow(applyNoise(y[74], t, 74), n);
			yDot[70] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[70];
		}

		// c-Jun
		if(isOverrideDerivative(t, 71)) {
			yDot[71] = overrideDerivative(t, 71);
		} else { 
			// activators : EGFR, Runx1, TAkt, TNF-α
			activators = Math.pow(applyNoise(y[47], t, 47), n) + Math.pow(applyNoise(y[58], t, 58), n) + Math.pow(applyNoise(y[38], t, 38), n) + Math.pow(applyNoise(y[74], t, 74), n);
			// inhibitors : C/EBPα
			inhibitors = Math.pow(applyNoise(y[2], t, 2), n);
			yDot[71] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[71];
		}

		// Bcl-xL
		if(isOverrideDerivative(t, 72)) {
			yDot[72] = overrideDerivative(t, 72);
		} else { 
			// activators : EGFR, NF-κB, Stat5
			activators = Math.pow(applyNoise(y[47], t, 47), n) + Math.pow(applyNoise(y[11], t, 11), n) + Math.pow(applyNoise(y[5], t, 5), n);
			// inhibitors : Caspase 3
			inhibitors = Math.pow(applyNoise(y[19], t, 19), n);
			yDot[72] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[72];
		}

		// Cdk6
		if(isOverrideDerivative(t, 73)) {
			yDot[73] = overrideDerivative(t, 73);
		} else { 
			// activators : Hoxa9, MAPK, Pu.1
			activators = Math.pow(applyNoise(y[44], t, 44), n) + Math.pow(applyNoise(y[31], t, 31), n) + Math.pow(applyNoise(y[9], t, 9), n);
			// inhibitors : Gata1/2, p53
			inhibitors = Math.pow(applyNoise(y[60], t, 60), n) + Math.pow(applyNoise(y[10], t, 10), n);
			yDot[73] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[73];
		}

		// TNF-α
		if(isOverrideDerivative(t, 74)) {
			yDot[74] = overrideDerivative(t, 74);
		} else { 
			// activators : IL-1, NF-κB
			activators = Math.pow(applyNoise(y[24], t, 24), n) + Math.pow(applyNoise(y[11], t, 11), n);
			// inhibitors : IL-10
			inhibitors = Math.pow(applyNoise(y[35], t, 35), n);
			yDot[74] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[74];
		}

		// RARs
		if(isOverrideDerivative(t, 75)) {
			yDot[75] = overrideDerivative(t, 75);
		} else { 
			// activators : AP2, PPARγ
			activators = Math.pow(applyNoise(y[61], t, 61), n) + Math.pow(applyNoise(y[36], t, 36), n);
			// inhibitors : MAPK, NR2F2
			inhibitors = Math.pow(applyNoise(y[31], t, 31), n) + Math.pow(applyNoise(y[69], t, 69), n);
			yDot[75] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[75];
		}

		// Cdk2
		if(isOverrideDerivative(t, 76)) {
			yDot[76] = overrideDerivative(t, 76);
		} else { 
			// activators : MAPK
			activators = Math.pow(applyNoise(y[31], t, 31), n);
			// inhibitors : C/EBPα, Gata1/2
			inhibitors = Math.pow(applyNoise(y[2], t, 2), n) + Math.pow(applyNoise(y[60], t, 60), n);
			yDot[76] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[76];
		}

		// Bax
		if(isOverrideDerivative(t, 77)) {
			yDot[77] = overrideDerivative(t, 77);
		} else { 
			// activators : Bim, p53
			activators = Math.pow(applyNoise(y[68], t, 68), n) + Math.pow(applyNoise(y[10], t, 10), n);
			// inhibitors : Bcl-2, Sox4, TAkt
			inhibitors = Math.pow(applyNoise(y[41], t, 41), n) + Math.pow(applyNoise(y[22], t, 22), n) + Math.pow(applyNoise(y[38], t, 38), n);
			yDot[77] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[77];
		}

		// Fas
		if(isOverrideDerivative(t, 78)) {
			yDot[78] = overrideDerivative(t, 78);
		} else { 
			// activators : TNF-α
			activators = Math.pow(applyNoise(y[74], t, 74), n);
			// inhibitors : Ras
			inhibitors = Math.pow(applyNoise(y[4], t, 4), n);
			yDot[78] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[78];
		}

		// Stat1
		if(isOverrideDerivative(t, 79)) {
			yDot[79] = overrideDerivative(t, 79);
		} else { 
			// activators : IFN-γ
			activators = Math.pow(applyNoise(y[15], t, 15), n);
			// inhibitors : OPN, TGF-β
			inhibitors = Math.pow(applyNoise(y[7], t, 7), n) + Math.pow(applyNoise(y[52], t, 52), n);
			yDot[79] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[79];
		}

		// FGFR2
		if(isOverrideDerivative(t, 80)) {
			yDot[80] = overrideDerivative(t, 80);
		} else { 
			// activators : FGF2, FGF7, Hoxa10, PRunx2
			activators = Math.pow(applyNoise(y[8], t, 8), n) + Math.pow(applyNoise(y[13], t, 13), n) + Math.pow(applyNoise(y[57], t, 57), n) + Math.pow(applyNoise(y[29], t, 29), n);
			// inhibitors : PRDM14
			inhibitors = Math.pow(applyNoise(y[45], t, 45), n);
			yDot[80] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - y[80];
		}
	}

}
