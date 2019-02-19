/************************************************************************************** 
 *
 * 		 	Author:  		  Matt Pringle  
 * 			Date updated:	  01-29-2019
 *          Coding language:  JAVA 
 * 
 * The purpose of this program is to calculate amount of pop sales for a local fundraiser.
 * All records read will be validated before being processed.  Invalid records will be dumped
 * to an error report (JAVPOPERB.PRT) and processing bypassed.  Valid records will be 
 * processed and written to a sales report (JAVPOPSLB.PRT).
 * 
 * 	Validation will check the following:   
 *  	P-LNAME	PIC 	X(15).		NON BLANK
 *		P-FNAME	PIC 	X(15).		NON BLANK
 *		P-ADDRESS		PIC X(15).	NON BLANK
 *		P-CITY			PIC X(10).	NON BLANK
 *		P-STATE			PIC XX.		IA, IL, MI, MO, NE, WI
 *		P-ZIP			PIC 9(9).	NUMERIC
 *		P-POP-TYPE		PIC 99.		1-6, NUMERIC
 *		P-#CASES		PIC 99.		NUMERIC, Minimum of 1
 *		P-TEAM			PIC X		A – E
 *
 *	Input file 		- popsales.dat
 *  Error Output file - JAVPOPERB.PRT - this file will contain all invalid records
 *  Good Output file - JAVPOPSLB.PRT  
 *  
 **************************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.text.*;
import java.util.*;

public class PopSalesArray {
	
	// declare global variables
	
	static LocalDate today = LocalDate.now();
	static LocalDate userDate;
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	
	static String iString;			// generic input string 
	static String iLname, iFname, iAddress, iCity, iState, record, errmsg, iDate, oTeamTotals;
	static int iCases, totalCases, iPopType, iZip, i, index, 
		bottlePerCase = 24, pgCtr = 1, badRecCtr;
	static char iTeam;
	static double cDeposit, tPopSale, caseCost = 18.71;
	static Scanner salesScanner;	//input device to read from .dat file
	static boolean EOF = false;
	static boolean validInput = true;
	
	static Scanner myScanner;	    // input device to get from console
	static PrintWriter pwB;			// file used for invalid output
	static PrintWriter pwG;			// file used for valid output
	static NumberFormat nf;
	
//----------  State Array  	
	static String[] arrState = {"IA", "NE", "WI", "MI", "MO", "IL"};
	
//----------  Pop name Array	
	static String[] arrPopName = {	"Coke",			"Diet Coke",		"Mello Yello", 
									"Cherry Coke", 	"Diet Cherry Coke",	"Sprite"};
	
//----------  Pop type Array	
	static int[] arrPopType = {01, 02, 03, 04, 05, 06};
	
//----------  Error Message Array	
	static String[] arrErrMsg = {	"Last name is missing - must be entered",
									"First name is missing - must be entered",
									"Address is missing - must be entered",
									"City is missing - must be entered",
									"State must be IA, IL, MI, MO, NE, WI",
									"Zip must be greater than zero",
									"Zip needs to be numeric",
									"Pop type must be numeric = 1 - 6",
									"Pop type must be 1 - 6",
									"Cases must be numeric",
									"Cases must be greater than zero",
									"Team must be A, B, C, D, E"};
//-----------  Deposit Array
	static double[] arrDepAmount = {.05, .05, .05, .10, .00, .00};
	
//-----------  Team name Array	
	static char[] arrTeam = {'A', 'B', 'C', 'D', 'E'};
	
//-----------  Team Totals Array	
	static double[] arrTeamTotals = new double [arrTeam.length];
	
//-----------  Pop Totals Array
	static int [] arrPopTotals = new int [arrPopType.length];
	
// ********************************************************************
// ************  The start of the programs logic  *********************
// ********************************************************************
	
	public static void main(String[] args) {
		
		init();    
		
//  loop starts here - End of File = N, validate - if valid record do calcs if not 
//		valid write the badreport and set validInput to true and read next
		
		while (!EOF ) {
			validate();
			if (validInput) {
				calcs();
				detailLines();
			}
			else  {
				badreport();
				}
			readNextRecord();
			}  
	
		totals();
		
		pwG.format("%n");
		pwG.format("       *** Program has ended successfully ***");
		pwG.format("%n");
		pwG.format("  Check JAVPOPERB.PRT file for any invalid records");
		
		if (badRecCtr == 0) {
			pwB.format("%n");
			pwB.format(" All input records were valid - No Errors");
		}
			
		pwB.close();
		pwG.close();
	}
//*********************************************************************************************
// initialize the input file, output file and scanner to receive input from user and read 1st rec
//*********************************************************************************************	
		
	public static void init() {
			 
		try {
			salesScanner = new Scanner(new File("badpopsales.dat"));
			salesScanner.useDelimiter(System.getProperty("line.separator"));
		} 
		catch (FileNotFoundException e1) {
			System.out.println(" * File error * ");
			System.exit(1);
		}

				// set formatter to use US currency format
		nf = NumberFormat.getCurrencyInstance(java.util.Locale.US);
		
				//  declaring output files
		try {
			pwG = new PrintWriter(new File ("JAVPOPSLB.PRT"));
		} 
		catch (FileNotFoundException e) {
			System.out.println("Output file error on valid data report");
		}
		
		try {
			pwB = new PrintWriter(new File ("JAVPOPERB.PRT"));
		} 
		catch (FileNotFoundException e) {
			System.out.println("Output file error on invalid data report");
		}
		
		for(i=0; i < arrPopType.length; i++)  {	
			arrPopTotals[i] = 0;
			}
		
		for(i=0; i < arrTeam.length; i++)  {	
			arrTeamTotals[i] = 0.0;
			}
		
		columnHeadings();
					
		//initial reading of dat.file
		readNextRecord();
	}		
//*********************************************************************************************
// after the reading and parsing of the next record to process we must validate each field
//*********************************************************************************************

	public static void validate() {
		validInput = true;  		
		if(iLname.trim().length() < 1) {
			errmsg = arrErrMsg[0];			
		 	validInput = false;
		 	return;
		}

		if(iFname.trim().length() < 1) {
			errmsg = arrErrMsg[1];			
		 	validInput = false;
		 	return;
		}
		
		if(iAddress.trim().length() < 1) {
			errmsg = arrErrMsg[2];
			validInput = false;
			return;
		}
	
		if(iCity.trim().length() < 1) {
			errmsg = arrErrMsg[3];
			validInput = false;
			return;
		}
		
		index = -1;
		for(i=0; i < arrState.length; i++)  {			
			if(iState.equals(arrState[i]))
				index = i;
		}
		
		if (index == -1) {
			errmsg = arrErrMsg[4];
			validInput = false;
			return;
		}
		
		try {
			iString = record.substring(57,66); 	
			iZip = Integer.parseInt(iString);
			if (iZip == 0) {
				errmsg = arrErrMsg[5];
				validInput = false;
				return;
				} 
		}
		catch (Exception e) {
			errmsg = arrErrMsg[6];
			validInput = false;
			return;
		}
		
		try {
			iString = record.substring(66,68);		 
			iPopType = Integer.parseInt(iString);
			if (iPopType == 0) {
				errmsg = arrErrMsg[8];
				validInput = false;
				return;
				} 
			else 
				if (iPopType > 6) {
					errmsg = arrErrMsg[8];
					validInput = false;
					return;
				}
					
		}
		catch (Exception e) {
			errmsg = arrErrMsg[7];
			validInput = false;
			return;
		}
		
		try {
			iString = record.substring(68,70); 		 
			iCases = Integer.parseInt(iString);
			if (iCases == 0) {
				errmsg = arrErrMsg[10];
				validInput = false;
				return;
				} 
		}
		catch (Exception e) {
			errmsg = arrErrMsg[9];
			validInput = false;
			return;
		}
		
		index = -1;
		
		for(i=0; i<arrTeam.length; i++)  {			
			if(iTeam == arrTeam[i])
				index = i;
		}
		
		if (index == -1) {
			errmsg = arrErrMsg[11];
			validInput = false;
			return;
		}
	}
//*********************************************************************************************
// calculates 
//*********************************************************************************************
		
	public static void calcs() {
//  loop to calculate deposit amount based on state		
		for(i=0; i < arrState.length; i++)  {	
			if (iState.equals(arrState[i]))   {
				cDeposit = ((iCases * bottlePerCase) * arrDepAmount[i]);				
				}
			}
		tPopSale = (iCases * caseCost) + cDeposit;
		totalCases += iCases;
//  loop to add cases to appropriate pop type total		
		for(i=0; i < arrPopType.length; i++)  {	
			if (iPopType == arrPopType[i])
				arrPopTotals[i] += iCases;
			}
//  loop to add cases to appropriate team name total		
		for(i=0; i < arrTeam.length; i++)  {	
			if (iTeam == arrTeam[i])
				arrTeamTotals[i] += tPopSale;
			}
		}
//*********************************************************************************************
// column headings  (for both good report and bad report)
//*********************************************************************************************

	public static void columnHeadings() {
		pwG.format("%6s%10s%36s%28s%44s%6s%3s%n",	"DATE: ", today.format(dtf), " ", 
				"ALBIA SOCCER CLUB FUNDRAISER", " ", "PAGE: ", pgCtr);
		pwG.format("%60s%12s%n",	" ", "SALES REPORT");
		pwG.format("%n");
		
		pwG.format("%3s%9s%8s%10s%7s%4s%8s%7s%7s%3s%8s%12s%8s%5s%11s%7s%11s%n", 
				" ", "LAST NAME", " ", "FIRST NAME", " ", "CITY", " ", "STATE  ", "ZIP CODE", 
				" ", "POP TYPE", " ", "QUANITY", " ", "DEPOSIT AMT", " ", "TOTAL SALES");
		pwG.format("%n");
	
		
		pwB.format("%6s%10s%36s%28s%44s%6s%3s%n",	"DATE: ", today.format(dtf), " ",
				"ALBIA SOCCER CLUB FUNDRAISER", " ", "PAGE: ", pgCtr);
		pwB.format("%60s%12s%n",	" ", "ERROR REPORT");
		pwB.format("%n");
		
		pwB.format("%12s%64s%17s%n", "ERROR RECORD", " ", "ERROR DESCRIPTION");
		pwB.format("%n");
	}
//*********************************************************************************************
// read the next record and if last record then indicate End of File and quit
//*********************************************************************************************
	
	public static void readNextRecord() {
			
		if (salesScanner.hasNext()) {
			record = salesScanner.next();	
			iLname = record.substring(0,15);			//file position 1 - 15
			iFname = record.substring(15,30);			//file position 16 - 30
			iAddress = record.substring(30,45);		//file position 31 - 45
			iCity = record.substring(45,55);		//file position 46 - 54
			iState = record.substring(55,57);		//file position 55 - 57
			iString = record.substring(70,71);
			iTeam = iString.trim().toUpperCase().charAt(0);
			}
		else {
			EOF=true;	//no more records so set eof to true
			}
		}
//*********************************************************************************************
// formats and print the detail line
//*********************************************************************************************
	
	public static void detailLines() {			
		
		pwG.format("%3s%15s%2s%15s%2s%10s%3s%2s%3s%10s%2s%-16s%8s%2s%11s%7.2f%9s%9.2f%n", 
				" ", iLname, " ", iFname, " ", iCity, " ", iState, " ", iZip, " ", 
				arrPopName[iPopType-1],	" ", iCases, " ", cDeposit, " ", 
				(iCases * caseCost + cDeposit));
		}
//*********************************************************************************************
// format and display output and grand totals
//*********************************************************************************************
	public static void totals() {
		pgCtr++;
		pwG.format("%n");
		pwG.format("%6s%10s%36s%28s%44s%6s%3s%n",	"DATE: ", today.format(dtf), " ", 
				"ALBIA SOCCER CLUB FUNDRAISER", " ", "PAGE: ", pgCtr);
		pwG.format("%60s%12s%n",	" ", "SALES REPORT");
		pwG.format("%n");
		
		pwG.format("%3s%9s%8s%10s%7s%4s%8s%7s%7s%3s%8s%12s%8s%5s%11s%7s%11s%n", 
				" ", "LAST NAME", " ", "FIRST NAME", " ", "CITY", " ", "STATE  ", "ZIP CODE", 
				" ", "POP TYPE", " ", "QUANITY", " ", "DEPOSIT AMT", " ", "TOTAL SALES");
		
		pwG.format("%n");
		pwG.format("%3s%-16s%n", " ", "GRAND TOTALS      ");
		pwG.format("%n");
		for(int i = 0; i < 3; i++) {
			pwG.format("%3s%-16s%12s%5d%9s", " ", arrPopName[i], " ", arrPopTotals[i], " ");
		}
		pwG.format("%n");
		pwG.format("%n");
		for(int i = 3; i < 6; i++) {
			pwG.format("%3s%-16s%12s%5d%9s", " ", arrPopName[i], " ", arrPopTotals[i], " ");
		}
				
		pwG.format("%n");	
		
		pwG.format("%n");
		pwG.format("%3s%-20s%n", " ", "TEAM TOTALS        ");
		pwG.format("%n");
		for(int i = 0; i < arrTeamTotals.length; i++) {
			oTeamTotals = nf.format(arrTeamTotals[i]);
			pwG.format("%10s%1s%16s%11s%n", " ", arrTeam[i], " ", oTeamTotals);
		}		
	}
//*********************************************************************************************
// write the bad record out to JAVPOPERB.PRT file with a descriptive message
//*********************************************************************************************	
	
	public static void badreport() {
		
		badRecCtr++;
		pwB.format("%71s%5s%-60s%n", record, " ", errmsg);
		pwB.format("%n");
		validInput = true;		
	}
}