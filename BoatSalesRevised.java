/************************************************************************************** 
 * The purpose of this program is to calculate a receipt for a customer who would like
 * to buy up to 25 boats at one time. It will calculate a Markup amount, Subtotal, Tax,
 * Total and Grand Total.
 * 
 * 
 * 	This program will prompt the user to enter the following: 
 *
 * 		Boat Type - must be B P S or C
 * 		Accessory Type - must be 1 2 or 3
 * 		Quantity - must be 1 - 25
 * 		Boat Cost - must be 2,500 to 150,000
 * 		Prep Cost - must be 100 to 9,999.99
 * 
 * 
 * 			Author:  		Matt Pringle  
 * 			Date updated:	12-17-2018
 **************************************************************************************/
import java.text.*;
import java.util.*;

public class BoatSalesRevised {
	
	// declare global variables
	static String iString;
	static int iInt;
	static String iBoatType, oBoatName, 
		oAccessory, oBoatCost, oAccessCost, oPrepCost, oMarkupAmt, oGrandTotal, 
		oSubtotal, oTax, oTotal, oGrandtotal, oGrandTax, oGrandSubtotal;
	static int iAccessType, iBoatQuantity, cBoatCtr;
	static char iContinue = 'Y';
	static char BTValid = 'N';
	static char ATValid = 'N';
	static char QTYValid = 'N';
	static char BCValid = 'N';
	static char PCValid = 'N';
	static double iBoatCost, iPrepCost, cPct, cAccessCost, cMarkupAmt, cSubtotal, cTax, 
		cTotal, cGrandSubtotal, cGrandTax, cGrandTotal, wMarkupAmt, wTax;
	static Scanner myScanner;
	static NumberFormat nf;
	
// ********************************************************************
// ************  The start of the programs logic  *********************
// ********************************************************************
	
	public static void main(String[] args) {
		
		// call the following methods
		init();
		while (iContinue == 'Y') {
		BTValid = 'N';					// set switches back to INVALID for input to be entered (again)
		ATValid = 'N';
		QTYValid = 'N';
		BCValid = 'N';
		PCValid = 'N';
		input();
		calcs();
		output();
		System.out.print("Calculate another boat receipt? Y or N: ");
		iContinue = myScanner.next().toUpperCase().charAt(0);
		}
		
		totals();
// a final message of advertisement and leaving the customer wanting to come back		
		System.out.println("**************************************************************");
		System.out.println("****  We here at Boats Inc. hope you consider buying *********");
		System.out.println("****     your next exciting watercraft from us!!     *********");
		System.out.println("****  Looking forward to seeing you in the Spring!   *********");
		System.out.println("*********      We are here to serve you!!      ***************");
		System.out.println("**************************************************************");
	}	
		
	public static void init() {
		// set scanner to the Console
		myScanner = new Scanner(System.in);
		
		//change delimiter from blank space to Enter key
		// to allow spaces in strings
		myScanner.useDelimiter(System.getProperty("line.separator"));
		
		// set formatter to use US currency format
		nf = NumberFormat.getCurrencyInstance(java.util.Locale.US);
	}
	
	public static void input() {
// ******************************************************************************
	//  getting a boat type from user - and validating
//******************************************************************************
		while (BTValid == 'N') {	
			System.out.print("Enter boat type(B, P, S or C): ");
			iBoatType = myScanner.next().toUpperCase();
			switch (iBoatType) {
				case "B": 							// Bass Boat logic
					oBoatName = "Bass boat";
					cPct = .33;
					BTValid = 'Y';
					break;
				case "P": 							// Pontoon Boat logic
					oBoatName = "Pontoon boat";
					cPct = .25;
					BTValid = 'Y';
					break;
				case "S": 							// Ski Boat logic
					oBoatName = "Ski boat";
					cPct = .425;
					BTValid = 'Y';
					break;	
				case "C": 							// Canoe Boat logic
					oBoatName = "Canoe";
					cPct = .2;
					BTValid = 'Y';
					break;
				default:
					System.out.println("Boat Type must be B, P, S or C");
					BTValid = 'N';
					break;
				}
		}
	
//******************************************************************************
	//  getting Accessory Type and validating it
//******************************************************************************
		while (ATValid == 'N') {
			try {
				System.out.print("Enter Accessory Type: ");		
				iString = myScanner.next();
				iAccessType = Integer.parseInt(iString);
				if ((iAccessType > 0) & (iAccessType < 4)) {
					ATValid = 'Y';
					}
				else  {
					System.out.println("Accessory Type must be 1-3");
					ATValid = 'N';
					}
				}
		
			catch (Exception e) {
				System.out.println("Accessory Type must be a 1 2 or 3");
				ATValid = 'N';
				}
			
			switch (iAccessType) {
				case 1:
					oAccessory = "Electronics";
					cAccessCost = 5415.30;
					break;
				case 2: 
					oAccessory = "Ski Package";
					cAccessCost = 3980;
					break;
				case 3:
					oAccessory = "Fishing Packaaage";
					cAccessCost = 345.45;
					break;
				}
		}
			
// ****************************************************************************
	//  getting and validating quantity
//*****************************************************************************
		while (QTYValid == 'N') {
			try {
				System.out.print("Enter Quantity: ");		
				iString = myScanner.next();
				iBoatQuantity = Integer.parseInt(iString);
				if ((iBoatQuantity > 0) & (iBoatQuantity < 26)) {
					QTYValid = 'Y';
					}
				else  {
					System.out.println("Quantity must be 1 - 25");
					QTYValid = 'N';
					}
				}
		
			catch (Exception e) {
				System.out.println("Quantity must be 1-25");
				QTYValid = 'N';
				}
		}
		
// ***************************************************************************
	//  getting and validating boat cost	
//****************************************************************************
		while (BCValid == 'N') {
			try {
				System.out.print("Enter the cost of the boat: ");	
				iString = myScanner.next();
				iBoatCost = Double.parseDouble(iString);
				if ((iBoatCost > 2499.99) & (iBoatCost < 150000.01)) {
					BCValid = 'Y';
					}
				else {
					System.out.println("Boat cost must be $2,500 to $150,000");
					BCValid = 'N';
					}
				}
		
			catch (Exception e) {
				System.out.println("Boat cost must be $2,500 - $150,000");
				BCValid = 'N';
				}
		}
	
// ***************************************************************************
	//  getting and validating prep cost	
//****************************************************************************
		while (PCValid == 'N') {	
			try {
				System.out.print("Enter the prep cost: ");	
				iString = myScanner.next();
				iPrepCost = Double.parseDouble(iString);
				if ((iPrepCost > 99.99) & (iPrepCost < 10000.01)) {
					PCValid = 'Y';
					}
				else {
					System.out.println("Prep cost must be $100 to $10,000");
					PCValid = 'N';
					}
				}
		
			catch (Exception e) {
				System.out.println("Prep cost must be $100 - $10,000");
				PCValid = 'N';
				}
		}
	}
		
	public static void calcs() {
//*********************************************************************************************
// calculate = adds cost of both toys to equal subtotal, calculate 6% tax and adds it to Total
//*********************************************************************************************

		cMarkupAmt = cPct * iBoatCost;	
		
//  rounding Markup Amount to two decimal places		
		wMarkupAmt = cMarkupAmt * 100.0;	
		wMarkupAmt = Math.round(wMarkupAmt);
		wMarkupAmt = wMarkupAmt/100.0;
		cMarkupAmt = wMarkupAmt;
		
		cSubtotal = (iBoatCost + cAccessCost + iPrepCost + cMarkupAmt) * iBoatQuantity;
		cTax = cSubtotal * .06;

//  rounding Tax Amount to two decimal places
		wTax = cTax * 100.0;
		wTax = Math.round(wTax);
		wTax = wTax / 100.0;
		cTax = wTax;
		
		cTotal = cSubtotal + cTax;
		cBoatCtr += iBoatQuantity;
		cGrandSubtotal += cSubtotal;
		cGrandTax += cTax;
		cGrandTotal += cTotal; 
	}
			
	public static void output() {
//*********************************************************************************************
// format and display output and totals
//*********************************************************************************************
		System.out.println("------------------    Results Below   ------------------------");
		
		System.out.format("%-23s%2s%15s%n", 
				"Name of boat", " ", oBoatName);

		System.out.format("%-23s%2s%15s%n", 
				"Accessory Pkg", " ", oAccessory);

		System.out.format("%-23s%2s%15s%n", 
				"Boat quantity", " ", iBoatQuantity);

		oBoatCost = nf.format(iBoatCost);
		System.out.format("%-23s%2s%15s%n", 
				"Boat cost", " ", oBoatCost);
		
		oAccessCost = nf.format(cAccessCost);
		System.out.format("%-23s%2s%15s%n", 
				"Accessory cost", " ", oAccessCost);
		
		oPrepCost = nf.format(iPrepCost);
		System.out.format("%-23s%2s%15s%n", 
				"Prep cost", " ", oPrepCost);
		
		oMarkupAmt = nf.format(cMarkupAmt);
		System.out.format("%-23s%2s%15s%n", 
				"Markup amount", " ", oMarkupAmt);
		
		System.out.println("********************  Totals Below  **************************");
		
		oSubtotal = nf.format(cSubtotal);
		System.out.format("%-23s%2s%15s%n", 
				"Subtotal", " ", oSubtotal);
		
		oTax = nf.format(cTax);
		System.out.format("%-23s%2s%15s%n", 
				"6% Tax", " ", oTax);
		
		oTotal = nf.format(cTotal);
		System.out.format("%-23s%2s%15s%n", 
				"Total cost", " ", oTotal);

	}
			
	public static void totals() {
//*********************************************************************************************
// format and display grand totals 
//*********************************************************************************************	
		System.out.println("******************** Grand Totals Below  *********************");
		System.out.format("%-23s%2s%15s%n", 
				"Total number of boats", " ", cBoatCtr);

		oGrandSubtotal = nf.format(cGrandSubtotal);
		System.out.format("%-23s%2s%15s%n", 
				"Grand subtotal", " ", oGrandSubtotal);
		
		oGrandTax = nf.format(cGrandTax);
		System.out.format("%-23s%2s%15s%n", 
				"Grand total tax", " ", oGrandTax);
			
		oGrandTotal = nf.format(cGrandTotal);
		System.out.format("%-23s%2s%15s%n", 
				"Grand total", " ", oGrandTotal);
		
	}
}