/************************************************************************************** 
 * The purpose of this program is to calculate a receipt for a customer who would like
 * to buy up to 25 boats at one time. It will calculate a Markup amount, Subtotal, Tax,
 * Total and Grand Total.
 * 
 * 
 * 	This program will prompt the user to enter the following: 
 *
 * 		Boat Type - must be B P S or C - if not, default is S
 * 		Accessory Type - must be 1 2 or 3 - if not, default is 1
 * 		Quantity - must be 1 - 25 - if not, default is 1
 * 		Boat Cost - must be 2,500 to 150,000 - if not, default is 25,000
 * 		Prep Cost - must be 100 to 9,999.99 - if not, default is 5,0000
 * 
 * 
 * 			Author:  		Matt Pringle  
 * 			Date written:	12-06-2018
 **************************************************************************************/
import java.text.*;
import java.util.*;

public class BoatSales {
	
	// declare global variables
	static String iString;
	static int iInt;
	static String iBoatType, oBoatName, 
		oAccessory, oBoatCost, oAccessCost, oPrepCost, oMarkupAmt, oGrandTotal, 
		oSubtotal, oTax, oTotal, oGrandtotal, oGrandTax, oGrandSubtotal;
	static int iAccessType, iBoatQuantity, cBoatCtr;
	static char iContinue = 'Y';
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
		
	System.out.print("Enter boat type(B, P, S or C): ");
	iBoatType = myScanner.next().toUpperCase();
	
	switch (iBoatType) {
		case "B": 							// Bass Boat logic
			oBoatName = "Bass boat";
			cPct = .33;
			break;
		case "P": 							// Pontoon Boat logic
			oBoatName = "Pontoon boat";
			cPct = .25;
			break;
		case "S": 							// Ski Boat logic
			oBoatName = "Ski boat";
			cPct = .425;
			break;	
		case "C": 							// Canoe Boat logic
			oBoatName = "Canoe";
			cPct = .2;
			break;
		default:
			System.out.println("Boat Type must be B, P, S or C, defaulted to S");
			iBoatType = "S";
			oBoatName = "Ski";
			cPct = .425;
			break;
		}
	
//******************************************************************************
	//  getting Accessory Type and validating it
//******************************************************************************
	System.out.print("Enter Accessory Type: ");		
	iString = myScanner.next();
	iAccessType = Integer.parseInt(iString);
	
	switch (iAccessType) {
		case 1: 							// Bass Boat logic
			oAccessory = "Electronics";
			cAccessCost = 5415.30;
			break;
		case 2: 							// Pontoon Boat logic
			oAccessory = "Ski Package";
			cAccessCost = 3980;
			break;
		case 3: 							// Ski Boat logic
			oAccessory = "Fishing Package";
			cAccessCost = 345.45;
			break;	
		default:
			System.out.println("Accessory Type must be 1 - 3, defaulted to 1");
			oAccessory = "Electronics";
			cAccessCost = 5415.30;
			iAccessType = 1;
			break;
		}
	
// ****************************************************************************
	//  getting and validating quantity
//*****************************************************************************
		try {
			System.out.print("Enter Quantity: ");		
			iString = myScanner.next();
			iBoatQuantity = Integer.parseInt(iString);
			if (iBoatQuantity < 1 || iBoatQuantity > 25) {
				System.out.println("Quantity must be 1 - 25, defaulted to 1");
				iBoatQuantity = 1;
				}
			}
	
		catch (Exception e) {
			System.out.println("Quantity invalid - defaulted to 1");
			iBoatQuantity = 1;
			}
		
// ***************************************************************************
	//  getting and validating boat cost	
//****************************************************************************
		try {
			System.out.print("Enter the cost of the boat: ");	
			iString = myScanner.next();
			iBoatCost = Double.parseDouble(iString);
			if (iBoatCost < 2500.00 || iBoatCost > 150000.00) {
				System.out.println("Boat cost must be $2,500 to $150,000, defaulted to $25,000");
				iBoatCost = 25000;
				}
			}
	
		catch (Exception e) {
			System.out.println("Boat cost invalid - defaulted to $25,000");
			iBoatCost = 25000;
			}
	
// ***************************************************************************
	//  getting and validating prep cost	
//****************************************************************************
		try {
			System.out.print("Enter the prep cost: ");	
			iString = myScanner.next();
			iPrepCost = Double.parseDouble(iString);
			if (iPrepCost < 100.00 || iPrepCost > 9999.99) {
				System.out.println("Prep cost must be $100 to $9,999.99, defaulted to $5,000");
				iPrepCost = 5000;
				}
			}
	
		catch (Exception e) {
			System.out.println("Prep cost invalid - defaulted to 5,000.00");
			iPrepCost = 5000;
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
	
//                 .format("%5s %10s %5s %4d %5.2f %n");		
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