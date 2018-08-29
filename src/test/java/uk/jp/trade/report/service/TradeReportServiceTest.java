/**
 * 
 */
package uk.jp.trade.report.service;


import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.jp.trade.report.service.exception.InvalidTradeException;
import uk.jp.trade.report.service.impl.TradeReportServiceImpl;
import uk.jp.trade.report.service.model.TradeInstruction;
import uk.jp.trade.report.service.model.TradeInstructions;


public class TradeReportServiceTest {
	
    public static final Logger LOGGER = Logger.getLogger(TradeReportServiceTest.class);

	private TradeReportService service;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		service = new TradeReportServiceImpl();
	}

	private Calendar getDate(int year, int month, int day) {
        Calendar inputDate = Calendar.getInstance();
        inputDate.set(year, month, day);
        return inputDate;
	}

	/**
	 * When any trade data received null then check throws an InvalidTradeException exception 
	 * Test method for {@link uk.jp.trade.report.service.TradeReportService#processTrade(uk.jp.trade.report.service.model.Instruction)}.
	 * @throws InvalidTradeException 
	 */
    @Test(expected = InvalidTradeException.class)
	public void whenGivenTradeDataNullThenCheckThorwsException() throws InvalidTradeException {
    	TradeInstruction  invalidInstruction =
                  new TradeInstruction(1l, "foo", "B", 0.50d, "SAR", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                          Calendar.JANUARY, 1), 200l, 100.25d);
    	invalidInstruction.setSettlementDate(null);
		service.processTrade(invalidInstruction);
	}
    
    /**
	 * When a valid trade data received and the settlement date is weekend for the currency SAR 
	 * then then the settlement date should be changed to the next working day.
	 * Test method for {@link uk.jp.trade.report.service.TradeReportService#processTrade(uk.jp.trade.report.service.model.Instruction)}.
	 * @throws InvalidTradeException 
	 */
    @Test()
	public void whenGivenSettlementDateIsFriForTheCurrencySARThenSettlemntDateShouldBeSunday() throws InvalidTradeException {
    	TradeInstruction  invalidInstruction =
                  new TradeInstruction(1l, "foo", "B", 0.50d, "SAR", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                          Calendar.JANUARY, 1), 200l, 100.25d);
    	TradeInstruction processedTrade = service.processTrade(invalidInstruction);
    	
    	Assert.assertEquals(Calendar.SUNDAY , processedTrade.getSettlementDate().get(Calendar.DAY_OF_WEEK));
	}
    
    /**
  	 * When a valid trade data received and the settlement date is weekend for the currency AED 
  	 * then then the settlement date should be changed to the next working day.
  	 * Test method for {@link uk.jp.trade.report.service.TradeReportService#processTrade(uk.jp.trade.report.service.model.Instruction)}.
  	 * @throws InvalidTradeException 
  	 */
      @Test()
  	public void whenGivenSettlementDateIsFriForTheCurrencyAEDThenSettlemntDateShouldBeSunday() throws InvalidTradeException {
      	TradeInstruction  invalidInstruction =
                    new TradeInstruction(1l, "foo", "B", 0.50d, "AED", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                            Calendar.JANUARY, 1), 200l, 100.25d);
      	TradeInstruction processedTrade = service.processTrade(invalidInstruction);
      	
      	Assert.assertEquals(Calendar.SUNDAY , processedTrade.getSettlementDate().get(Calendar.DAY_OF_WEEK));
  	}
      
      /**
    	 * When a valid trade data received and the settlement date is weekend for the currency GBP or USD 
    	 * then then the settlement date should be changed to the next working day.
    	 * Test method for {@link uk.jp.trade.report.service.TradeReportService#processTrade(uk.jp.trade.report.service.model.Instruction)}.
    	 * @throws InvalidTradeException 
    	 */
        @Test()
    	public void whenGivenSettlementDateIsSatForTheOtherCurrencyThenSettlemntDateShouldBeMonday() throws InvalidTradeException {
        	Calendar settlementDate = getDate(2016, Calendar.JANUARY, 2);
        	TradeInstruction  invalidInstruction =
                      new TradeInstruction(1l, "foo", "B", 0.50d, "GBP", getDate(2016, Calendar.JANUARY, 1), settlementDate, 200l, 100.25d);
        	TradeInstruction processedTrade = service.processTrade(invalidInstruction);
        	
        	Assert.assertEquals(Calendar.MONDAY , processedTrade.getSettlementDate().get(Calendar.DAY_OF_WEEK));
        	
        	TradeInstruction  invalidInstruction1 =
                    new TradeInstruction(1l, "foo", "B", 0.50d, "USD", getDate(2016, Calendar.JANUARY, 1), settlementDate, 200l, 100.25d);
        	TradeInstruction processedTrade1 = service.processTrade(invalidInstruction1);
      	
        	Assert.assertEquals(Calendar.MONDAY , processedTrade1.getSettlementDate().get(Calendar.DAY_OF_WEEK));
    	} 
        
        
        /**
    	 * When a valid trade data received and the settlement date is weekend for the currency GBP 
    	 * then then the settlement date should be same day
    	 * Test method for {@link uk.jp.trade.report.service.TradeReportService#processTrade(uk.jp.trade.report.service.model.Instruction)}.
    	 * @throws InvalidTradeException 
    	 */
        @Test()
    	public void whenGivenSettlementDateIsWeekDayThenSettlemntDateShouldBeSameDay() throws InvalidTradeException {
        	Calendar settlementDate = getDate(2016, Calendar.JANUARY, 4);
        	TradeInstruction  invalidInstruction =
                      new TradeInstruction(1l, "foo", "B", 0.50d, "GBP", getDate(2016, Calendar.JANUARY, 1), settlementDate, 200l, 100.25d);
        	TradeInstruction processedTrade = service.processTrade(invalidInstruction);
        	
        	Assert.assertEquals(Calendar.MONDAY , processedTrade.getSettlementDate().get(Calendar.DAY_OF_WEEK));
    	} 

	/**
	 * The below methods generates report on the order of Settlement Date Desc, Trade Type Asc, and amount Highest Desc order
	 * Test method for {@link uk.jp.trade.report.service.TradeReportService#generateTradeReport(java.util.List)}.
	 * @throws InvalidTradeException 
	 */
	@Test
	public void testGenerateTradeReportOnJan1st() throws InvalidTradeException {
		Set<TradeInstruction> tradeInstructions = new LinkedHashSet<TradeInstruction>();
		
		TradeInstruction  buyOnSARRank1 =
                new TradeInstruction(1l, "foo", "B", 0.50d, "SAR", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                        Calendar.JANUARY, 1), 500l, 100.25d);
		TradeInstruction  buyOnSARRank2 =
                new TradeInstruction(2l, "bar", "B", 0.50d, "SAR", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                        Calendar.JANUARY, 1), 200l, 100.25d);
		
		TradeInstruction  sellOnSARRank1 =
                new TradeInstruction(3l, "bar", "SS", 0.50d, "SAR", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                        Calendar.JANUARY, 1), 200l, 100.25d);
		tradeInstructions.add(buyOnSARRank2);
		tradeInstructions.add(sellOnSARRank1);
		tradeInstructions.add(buyOnSARRank1);
		
		TradeInstructions tradeReport = service.prepareTradeReport(tradeInstructions);
		LOGGER.info(tradeReport.getOutAndInInstructions());
		
		Set<TradeInstruction> expectedReport = new LinkedHashSet<TradeInstruction>();
		expectedReport.add(service.processTrade(buyOnSARRank1));
		expectedReport.add(service.processTrade(buyOnSARRank2));
		expectedReport.add(service.processTrade(sellOnSARRank1));
		
		Assert.assertArrayEquals(expectedReport.toArray(), tradeReport.getOutAndInInstructions().toArray());
		
		
	}
	
	/**
	 * Test method for {@link uk.jp.trade.report.service.TradeReportService#generateTradeReport(java.util.List)}.
	 * @throws InvalidTradeException 
	 */
	@Test
	public void testGenerateTradeReportForJan1stnd4th() throws InvalidTradeException {
		Set<TradeInstruction> tradeInstructions = new LinkedHashSet<TradeInstruction>();
		
		TradeInstruction  buyOnSARRank1 =
                new TradeInstruction(1l, "foo", "B", 0.50d, "SAR", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                        Calendar.JANUARY, 1), 500l, 100.25d);
		TradeInstruction  buyOnSARRank2 =
                new TradeInstruction(2l, "bar", "B", 0.50d, "SAR", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                        Calendar.JANUARY, 1), 200l, 100.25d);
		
		TradeInstruction  sellOnSARRank1 =
                new TradeInstruction(3l, "bar", "S", 0.50d, "SAR", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                        Calendar.JANUARY, 1), 200l, 100.25d);
		
		TradeInstruction  buyOnSARRank3 =
                new TradeInstruction(4l, "bar", "S", 0.50d, "SAR", getDate(2016, Calendar.JANUARY, 1), getDate(2016,
                        Calendar.JANUARY, 4), 200l, 100.25d);
		
		tradeInstructions.add(buyOnSARRank3);
		tradeInstructions.add(buyOnSARRank2);
		tradeInstructions.add(sellOnSARRank1);
		tradeInstructions.add(buyOnSARRank1);
		
		
		TradeInstructions tradeReport = service.prepareTradeReport(tradeInstructions);
		LOGGER.info(tradeReport);
		
		Set<TradeInstruction> expectedReport = new LinkedHashSet<TradeInstruction>();
		expectedReport.add(service.processTrade(buyOnSARRank3));
		expectedReport.add(service.processTrade(buyOnSARRank1));
		expectedReport.add(service.processTrade(buyOnSARRank2));
		expectedReport.add(service.processTrade(sellOnSARRank1));
		
		
		Assert.assertArrayEquals(expectedReport.toArray(), tradeReport.getOutAndInInstructions().toArray());
		
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		service = null;
	}
}
