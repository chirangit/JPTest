package uk.jp.trade.report.service;

import java.util.Set;

import uk.jp.trade.report.service.exception.InvalidTradeException;
import uk.jp.trade.report.service.model.TradeInstruction;
import uk.jp.trade.report.service.model.TradeInstructions;

public interface TradeReportService {
	/**
	 * 	Process trade does the following jobs
	 * 	Validates given instruction
	 * 	A work week starts Monday and ends Friday, unless the currency of the trade is AED or SAR, where the work week starts Sunday and ends Thursday. No other holidays to be taken into account.
		A trade can only be settled on a working day. 
		If an instructed settlement date falls on a weekend, then the settlement date should be changed to
		the next working day.
 		USD amount of a trade = Price per unit * Units * Agreed Fx
	 * @param instruction
	 * @return
	 * @throws InvalidTradeException
	 */
    public TradeInstruction processTrade(TradeInstruction instruction) throws InvalidTradeException;
    public TradeInstructions prepareTradeReport(Set<TradeInstruction> instructions);

}
