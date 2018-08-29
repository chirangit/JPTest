package uk.jp.trade.report.service.impl;

import java.util.Calendar;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.jp.trade.report.service.TradeReportService;
import uk.jp.trade.report.service.exception.InvalidTradeException;
import uk.jp.trade.report.service.model.TradeInstruction;
import uk.jp.trade.report.service.model.TradeInstructions;
import uk.jp.trade.report.service.util.CurrencyType;
import uk.jp.trade.report.service.util.TradeReportConstant;

public class TradeReportServiceImpl implements TradeReportService {
    public static final Logger LOGGER = Logger.getLogger(TradeReportServiceImpl.class);

	
	public TradeInstruction processTrade(TradeInstruction instruction) throws InvalidTradeException {
		validateTradeInstruction(instruction);
		processSettlemetDate(instruction);
		calculateTradeAmount(instruction);
		return instruction;
	}

	private void calculateTradeAmount(TradeInstruction instruction) throws InvalidTradeException {
		        if (instruction.getPricePerUnit() == null || instruction.getUnits() == null
		            || instruction.getAgreedFX() == null) {
		            LOGGER.warn("Trade amount is null for the Entity " + instruction.getEntity());
		            LOGGER.warn("Can not able to caculate Trade Amount,  Price Pr Unit or Units or Agreed Fx are not valid  ");
		            instruction.setTradeAmount(null);
		            return;
		        }
		        double tradeAmount = instruction.getPricePerUnit() * instruction.getUnits() * instruction.getAgreedFX();
		        instruction.setTradeAmount(tradeAmount);
		        LOGGER.debug("Calculate Trade Amount for the instruction Entity " + instruction.getEntity() + " Trade Amount + " + tradeAmount);
	}

	private void processSettlemetDate(TradeInstruction instruction) {
		LOGGER.debug("Before processing Settlemet Date  : Currency : " + instruction.getCurrency() 
		+ " Settlement Date " + instruction.getSettlementDate().getTime());
		int dayOfWeek, noOfDays = 0;
        //Work week starts Sunday and ends Thursday for AED and SAR
		 if (instruction.getCurrency().equalsIgnoreCase(CurrencyType.AED.name())
		            || instruction.getCurrency().equalsIgnoreCase(CurrencyType.SAR.name())) {
		             dayOfWeek = instruction.getSettlementDate().get(Calendar.DAY_OF_WEEK);
		         
		          /*if the settlementDate is Friday or Saturday then settlement date should be changed to
		            the next working day which is Sunday.
		            */
		            if (Calendar.FRIDAY == dayOfWeek) {
		            	noOfDays = TradeReportConstant.DAY_2;
		            } else if (Calendar.SATURDAY == dayOfWeek) {
		            	noOfDays = TradeReportConstant.DAY_1;
		            }

		            //Work week starts Monday and ends Friday for AED and SAR
		        } else {
		             dayOfWeek = instruction.getSettlementDate().get(Calendar.DAY_OF_WEEK);
		            /*if the settlementDate is SATURDAY or SUNDAY then settlement date should be changed to
		            the next working day which is Sunday.
		            */
		            if (Calendar.SATURDAY == dayOfWeek) {
		            	noOfDays = TradeReportConstant.DAY_2;
		            } else if (Calendar.SUNDAY == dayOfWeek) {
		            	noOfDays = TradeReportConstant.DAY_1;
		            }
		        }
		 
     		updateDateToNextWorkingDay(instruction.getSettlementDate(), noOfDays);

		 LOGGER.debug("After processing Settlemet Date  : Currency : " + instruction.getCurrency() 
			+ " Settlement Date " + instruction.getSettlementDate().getTime());
	}
	
	 private void updateDateToNextWorkingDay(Calendar settlementDate, Integer day) {
	        settlementDate.add(Calendar.DATE, day);
	 }

	private void validateTradeInstruction(TradeInstruction instruction) throws InvalidTradeException {
		if(instruction.getSettlementDate() == null || instruction.getCurrency() == null){
			throw new InvalidTradeException("Invalid trade Data : " + instruction);
		}
	}

	public TradeInstructions prepareTradeReport(Set<TradeInstruction> instructions) {
		TradeInstructions tradeReport = new TradeInstructions();
		 for (TradeInstruction tradeInstruction : instructions) {
			try {
				//the below method process trade data
				processTrade(tradeInstruction);
			} catch (InvalidTradeException e) {
	            LOGGER.error("Failed to process the Trade Instructions");
			}
			//adding processed data into Tree Set which takes log(n) complexity for ordering the Data.
			//For all data takes tradeReport.getOutAndInInstructions().size() * log(n)
			tradeReport.getOutAndInInstructions().add(tradeInstruction);
		}
		 LOGGER.debug("Sorting complexity n = " + tradeReport.getOutAndInInstructions().size() + ",  the formula is n * log(n)");
		return tradeReport;
	}

}
