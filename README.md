# JP Test
The project implemeted to process the trade and prepares the below report.

1 Amount is in USD settled incoming everyday
2 Amount is in USD settled outgoing everyday
3 Ranking of entities based on incoming and outgoing amount. Eg: If entity foo instructs the highest
amount for a buy instruction, then foo is rank 1 for outgoing

Please run the Junit Test TradeReportServiceTest.java to test the report.

The generated Report will be loged on the file trade_report_log.log.
# JP Test

The problem : 
Sample data represents the instructions sent by various clients to JP Morgan to execute in the international market.
1 A work week starts Monday and ends Friday, unless the currency of the trade is AED or SAR, where the work week starts Sunday and ends Thursday. No other holidays to be taken into account.
2 A trade can only be settled on a working day.
3 If an instructed settlement date falls on a weekend, then the settlement date should be changed to
the next working day.
4 USD amount of a trade = Price per unit * Units * Agreed Fx
Requirements
Create a report that shows
5 Amount in USD settled incoming everyday
6 Amount in USD settled outgoing everyday
7 Ranking of entities based on incoming and outgoing amount. Eg: If entity foo instructs the highest
amount for a buy instruction, then foo is rank 1 for outgoing
