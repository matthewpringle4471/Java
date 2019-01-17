/************************************************************************************** 
 *
 * 		 	Author:  		  Matt Pringle  
 * 			Date updated:	  01-13-2019
 *          Coding language:  JAVA 
 * 
 * The purpose of this program is to create contribution reports.  The reports will
 * the political affiliation and gender of the contributor. This program will actually
 * get its main input from a file.  The input will be validated.  Error output file will be
 * created and its name is error.prt. Valid output will be sent to the screen. A 
 * summary report will be created tracking the following:  Record count, total contribution 
 * and average contribution for the following groupings:
 * 
 *			1)	Men					7)  Democratic Women  
 *			2)	Women				8)  Republican Men
 *			3)	Democrats			9)  Republican Women
 *			4)	Republicans			10) Independent Men
 *			5)	Independents		11) Independent Women
 *			6)	Democratic Men		12) Overall
 *
 *	Input file - contributors.dat
 *  Input from scanner (user) - to ask which report
 *  Output file - error.prt - this file will contain all invalid records
 *  Output to screen the main report produced from this program 
 *  
 **************************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.text.*;
import java.util.*;

public class SaveTheBarns {
	
	// declare global variables
	
//  declaring contribution accumulators  	
	static double totMenContrib = 0;
	static double demMenContrib = 0;
	static double repMenContrib = 0;
	static double indMenContrib = 0;
	static double totDemContrib = 0;
	static double totRepContrib = 0;
	static double totIndContrib = 0;
	static double totWomenContrib = 0;
	static double demWomenContrib = 0;
	static double repWomenContrib = 0;
	static double indWomenContrib = 0;
	static double totContrib = 0;
	
//  declaring contribution average variables
	static double avgTotMenContrib = 0;
	static double avgDemMenContrib = 0;
	static double avgRepMenContrib = 0;
	static double avgIndMenContrib = 0;
	static double avgTotWomenContrib = 0;
	static double avgDemWomenContrib = 0;
	static double avgRepWomenContrib = 0;
	static double avgIndWomenContrib = 0;
	static double avgDemContrib = 0;
	static double avgRepContrib = 0;
	static double avgIndContrib = 0;
	static double avgTotContrib = 0;
	
//  declaring counters 	
	static int totMenCtr = 0;
	static int demMenCtr = 0;
	static int repMenCtr = 0;
	static int indMenCtr = 0;
	static int totDemCtr = 0;
	static int totRepCtr = 0;
	static int totIndCtr = 0;
	static int totWomenCtr = 0;
	static int demWomenCtr = 0;
	static int repWomenCtr = 0;
	static int indWomenCtr = 0;	
	static int totContributorsCtr = 0;
	
	static LocalDate today = LocalDate.now();
	static LocalDate userDate;
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	
	static String iString;			// generic input string
	static char overRpt;			// over $500 report switch 
	static String iName, iAddress, iCity, iState, iZip, iParty, iGender, record, errmsg, iDate,
		ototMenContrib, oavgTotMenContrib, ototWomenContrib, oavgTotWomenContrib, ototDemContrib, oavgDemContrib,
		ototRepContrib, oavgRepContrib, ototIndContrib, oavgIndContrib, odemMenContrib, oavgDemMenContrib, 
		odemWomenContrib, oavgDemWomenContrib, orepMenContrib, oavgRepMenContrib, orepWomenContrib, oavgRepWomenContrib,
		oindMenContrib, oavgIndMenContrib, oindWomenContrib, oavgIndWomenContrib, ototContrib, oavgTotContrib;
	static double iContribution;
		
	static Scanner contribScanner;	//input device to read from .dat file
	static boolean EOF = false;
	static boolean validInput = true;
	
	static Scanner myScanner;	    // input device to get from console
	static PrintWriter pw;			// file used for error.prt output
	static NumberFormat nf;
	
// ********************************************************************
// ************  The start of the programs logic  *********************
// ********************************************************************
	
	public static void main(String[] args) {
		
		init();    
		
//  loop starts here - End of File = N, validate - if valid record do calcs if not 
//		valid write to error.prt and set validInput to true and read next
		
		while (!EOF ) {
			validate();
			if (validInput)
				calcs();
			else  {
				badreport();
				}
			readNextRecord();
			}  
			
		calcAvgs();
		
		output();
		
		pw.close();
		
		System.out.println(" ");
		if(totContributorsCtr == 0) {
			System.out.println("  Attention:  EITHER INPUT FILE WAS EMPTY OR ALL RECORDS HAD BAD DATA!!!  ");
			System.out.println(" ");
			}
		System.out.println("     *** Program has ended successfully ***");
		System.out.println("  Check error.prt file for any invalid records");
	}
//*********************************************************************************************
// initialize the input file, output file and scanner to receive input from user and read 1st rec
//*********************************************************************************************	
		
	public static void init() {
									// set scanner to input file 
		try {
			contribScanner = new Scanner(new File("contributors.dat"));
			contribScanner.useDelimiter(System.getProperty("line.separator"));
		} 
		catch (FileNotFoundException e1) {
			System.out.println(" * File error * ");
			System.exit(1);
		}

		myScanner = new Scanner(System.in);		// set scanner to console		
				//change delimiter from blank space to Enter key
				// to allow spaces in strings
		myScanner.useDelimiter(System.getProperty("line.separator"));
		
				// set formatter to use US currency format
		nf = NumberFormat.getCurrencyInstance(java.util.Locale.US);
		
				//  declaring output file
		try {
			pw = new PrintWriter(new File ("error.prt"));
		} 
		catch (FileNotFoundException e) {
			System.out.println("Output file error");
		}
		
        System.out.print("Process this report for contributions over $500 only? Y or N: ");
		overRpt = myScanner.next().toUpperCase().charAt(0);
	
		//initial reading of dat.file
		readNextRecord();
	}		
//*********************************************************************************************
// after the reading and parsing of the next record to process we must validate each field
//*********************************************************************************************

	public static void validate() {
		validInput = true;  
		
		if(iName.trim().length() < 1) {
			errmsg = "Name is missing - must be entered";			
		 	validInput = false;
		 	return;
		}

		if(iAddress.trim().length() < 1) {
			errmsg = "Address is missing - must be entered";
			validInput = false;
			return;
		}
	
		if(iCity.trim().length() < 1) {
			errmsg = "City is missing - must be entered";
			validInput = false;
			return;
		}

		if(iState.trim().length() < 1)  {		
			errmsg = "State is missing - must be abbreviation for a State";
			validInput = false;
			return;
		}	
		
		if(iZip.trim().length() < 1)   {
			errmsg = "Zip code is required";
			validInput = false;
			return;
		}	
		
		switch (iParty) {
			case "D":
			case "I": 
			case "R":
				break;
			default:
				errmsg = ("Party must be a D I or R");
				validInput = false;
				return;
		}
 	
		switch (iGender) {
			case "F": 
			case "M":
				break;
			default:
				errmsg = ("Gender must be a F or M");
				validInput = false;
				return;
		}	
// parsing the input field contribution here to use the try/catch function
		try {
			iString = record.substring(74,81);		 
			iContribution = Double.parseDouble(iString);
			if (iContribution < .01) {
				errmsg = ("Contribution amount must be between 0 - 10,000");
				validInput = false;
				return;
			} 
			}
		catch (Exception e) {
			errmsg = ("Contribution amount must be numeric between 0 - 10,000");
			validInput = false;
			}
		}
//*********************************************************************************************
// calculates record count, total contributions (per group), average contribution (per group)
//*********************************************************************************************
		
	public static void calcs() {

// if user only wants report to reflect contributions over $500 then user enters Y at beginning
// below processes only those records with contributions greater than 500 - user request
		
		if (overRpt == 'Y')
			if (iContribution > 500) {
				totContributorsCtr++;
				totContrib += iContribution;
				
				if (iGender.equals("M")) {
					totMenCtr++;
					totMenContrib += iContribution;
					if (iParty.equals("D")) {
						demMenCtr++;
						demMenContrib += iContribution;
						totDemCtr++;
						totDemContrib += iContribution;  }
					else 
						if (iParty.equals("R")) {
							repMenCtr++;
							repMenContrib += iContribution;
							totRepCtr++;
							totRepContrib += iContribution;  }
						else {
							indMenCtr++;
							indMenContrib += iContribution;
							totIndCtr++;
							totIndContrib += iContribution;
						}
				}		
				
				if (iGender.equals("F")) {
					totWomenCtr++;
					totWomenContrib += iContribution;
					if (iParty.equals("D")) {
						demWomenCtr++;
						demWomenContrib += iContribution;
						totDemCtr++;
						totDemContrib += iContribution;  }
					else 
						if (iParty.equals("R")) {
							repWomenCtr++;
							repWomenContrib += iContribution;
							totRepCtr++;
							totRepContrib += iContribution;  }
						else {
							indWomenCtr++;
							indWomenContrib += iContribution;
							totIndCtr++;
							totIndContrib += iContribution;
						}
					}
				}
		
	// below processes all valid records
		if (overRpt != 'Y') {
			totContributorsCtr++;
			totContrib += iContribution;
			
			if (iGender.equals("M")) {
				totMenCtr++;
				totMenContrib += iContribution;
				if (iParty.equals("D")) {
					demMenCtr++;
					demMenContrib += iContribution;
					totDemCtr++;
					totDemContrib += iContribution;  }
				else 
					if (iParty.equals("R")) {
						repMenCtr++;
						repMenContrib += iContribution;
						totRepCtr++;
						totRepContrib += iContribution;  }
					else {
						indMenCtr++;
						indMenContrib += iContribution;
						totIndCtr++;
						totIndContrib += iContribution;
				}
			}		
			
			
			if (iGender.equals("F")) {
				totWomenCtr++;
				totWomenContrib += iContribution;
				if (iParty.equals("D")) {
					demWomenCtr++;
					demWomenContrib += iContribution;
					totDemCtr++;
					totDemContrib += iContribution;  }
				else 
					if (iParty.equals("R")) {
						repWomenCtr++;
						repWomenContrib += iContribution;
						totRepCtr++;
						totRepContrib += iContribution;  }
					else  { 
						indWomenCtr++;
						indWomenContrib += iContribution;
						totIndCtr++;
						totIndContrib += iContribution;
					
					}
				}
			}
		}
//*********************************************************************************************
// read the next record and if last record then indicate End of File and quit
//*********************************************************************************************
	
	public static void readNextRecord() {
			
		if (contribScanner.hasNext()) {
			record = contribScanner.next();	
			iName = record.substring(0,25);			//file position 1 - 25
			iAddress = record.substring(25,50);		//file position 26 - 50
			iCity = record.substring(50,65);		//file position 51 - 65
			iState = record.substring(65,67);		//file position 66 - 67
			iZip = record.substring(67,72);			//file position 67 - 71
			iParty = record.substring(72,73);
			iGender = record.substring(73,74);
			}
		else {
			EOF=true;	//no more records so set eof to true
			}
		}
//*********************************************************************************************
// calculate averages for final output - check counters to avoid division by zero
//*********************************************************************************************
		
	public static void calcAvgs() {
		if(totMenCtr > 0) 
			avgTotMenContrib = totMenContrib / totMenCtr;
		if(demMenCtr > 0)
			avgDemMenContrib = demMenContrib / demMenCtr;
		if(repMenCtr > 0)
			avgRepMenContrib = repMenContrib / repMenCtr;
		if(indMenCtr > 0)
			avgIndMenContrib = indMenContrib / indMenCtr;
		
		if(totWomenCtr > 0)
			avgTotWomenContrib = totWomenContrib / totWomenCtr;
		if(demWomenCtr > 0)
			avgDemWomenContrib = demWomenContrib / demWomenCtr;
		if(repWomenCtr > 0)
			avgRepWomenContrib = repWomenContrib / repWomenCtr;
		if(indWomenCtr > 0)
			avgIndWomenContrib = indWomenContrib / indWomenCtr;
		
		if(totDemCtr > 0)
			avgDemContrib = totDemContrib / totDemCtr;
		if(totRepCtr > 0)
			avgRepContrib = totRepContrib / totRepCtr;
		if(totIndCtr > 0)
			avgIndContrib = totIndContrib / totIndCtr;
		if(totContributorsCtr > 0)
			avgTotContrib = totContrib / totContributorsCtr;
	}
//*********************************************************************************************
// format and display output and totals
//*********************************************************************************************
	public static void output() {

		if (overRpt == 'Y') {
			System.out.format("%-65s%10s%n", 
					" ", today.format(dtf));
			System.out.println("                                 Save the Barns ");
			System.out.println("                     All Contributions over $500 Report");
			System.out.println(" ");
		} else {
			System.out.format("%-65s%10s%n", 
					" ", today.format(dtf));
			System.out.println("                                 Save the Barns ");
			System.out.println("                              Contribution Report");
			System.out.println(" ");
		}
		
		System.out.println("                                               Total             Average   ");	
		System.out.println("Group                          Count        Contribution       Contribution");
		System.out.println("---------------------------------------------------------------------------");
		
		ototMenContrib = nf.format(totMenContrib);
		oavgTotMenContrib = nf.format(avgTotMenContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Men", " ", totMenCtr, " ", ototMenContrib, " ", oavgTotMenContrib);
		
		ototWomenContrib = nf.format(totWomenContrib);
		oavgTotWomenContrib = nf.format(avgTotWomenContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Women", " ", totWomenCtr, " ", ototWomenContrib, " ", oavgTotWomenContrib);
		
		ototDemContrib = nf.format(totDemContrib);
		oavgDemContrib = nf.format(avgDemContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Democrats", " ", totDemCtr, " ", ototDemContrib, " ", oavgDemContrib);
		
		ototRepContrib = nf.format(totRepContrib);
		oavgRepContrib = nf.format(avgRepContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Republicans", " ", totRepCtr, " ", ototRepContrib, " ", oavgRepContrib);
		
		ototIndContrib = nf.format(totIndContrib);
		oavgIndContrib = nf.format(avgIndContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Independents", " ", totIndCtr, " ", ototIndContrib, " ", oavgIndContrib);
		
		odemMenContrib = nf.format(demMenContrib);
		oavgDemMenContrib = nf.format(avgDemMenContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Democratic Men", " ", demMenCtr, " ", odemMenContrib, " ", oavgDemMenContrib);
		
		odemWomenContrib = nf.format(demWomenContrib);
		oavgDemWomenContrib = nf.format(avgDemWomenContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Democratic Women", " ", demWomenCtr, " ", odemWomenContrib, " ", oavgDemWomenContrib);
		
		orepMenContrib = nf.format(repMenContrib);
		oavgRepMenContrib = nf.format(avgRepMenContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Republican Men", " ", repMenCtr, " ", orepMenContrib, " ", oavgRepMenContrib);
		
		orepWomenContrib = nf.format(repWomenContrib);
		oavgRepWomenContrib = nf.format(avgRepWomenContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Republican Women", " ", repWomenCtr, " ", orepWomenContrib, " ", oavgRepWomenContrib);
		
		oindMenContrib = nf.format(indMenContrib);
		oavgIndMenContrib = nf.format(avgIndMenContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Independent Men", " ", indMenCtr, " ", oindMenContrib, " ", oavgIndMenContrib);
		
		oindWomenContrib = nf.format(indWomenContrib);
		oavgIndWomenContrib = nf.format(avgIndWomenContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Independent Women", " ", indWomenCtr, " ", oindWomenContrib, " ", oavgIndWomenContrib);
		
		ototContrib = nf.format(totContrib);
		oavgTotContrib = nf.format(avgTotContrib);
		System.out.format("%-20s%5s%10d%3s%18s%4s%15s%n", 
				"Overall", " ", totContributorsCtr, " ", ototContrib, " ", oavgTotContrib);
	}
//*********************************************************************************************
// write the bad record out to error.prt file with a descriptive message
//*********************************************************************************************	
	
	public static void badreport() {
		
		pw.format("%-55s%3s%81s%n", errmsg, " ", record);
		validInput = true;		
	}
}