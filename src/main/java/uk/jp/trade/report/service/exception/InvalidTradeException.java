package uk.jp.trade.report.service.exception;

import org.apache.log4j.Logger;

public class InvalidTradeException extends Exception {

	private static final long serialVersionUID = 1L;
	
    public static final Logger LOGGER = Logger.getLogger(InvalidTradeException.class);

	
	public InvalidTradeException() {
        super();
    }

    public InvalidTradeException(String errorMsg) {
        super(errorMsg);
    	LOGGER.error(errorMsg );
    }

    public InvalidTradeException(String errorMsg, Exception ex) {
        super(errorMsg, ex);
    	LOGGER.error(errorMsg, ex);
    }

}
