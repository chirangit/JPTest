package uk.jp.trade.report.service.model;

import java.util.Set;
import java.util.TreeSet;

/**
 * Collection of TradeInspecton
 *
 */
public class TradeInstructions {
	
	private Set<TradeInstruction> outAndInInstructions = new TreeSet<TradeInstruction>();
    
	public Set<TradeInstruction> getOutAndInInstructions() {
		return outAndInInstructions;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Please find Trade Report below...\n");
		for (TradeInstruction tradeInstruction : getOutAndInInstructions()) {
			builder.append(tradeInstruction.toString());
			builder.append("\n");
		}
		return builder.toString();
	}

	
}
