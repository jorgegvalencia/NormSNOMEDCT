package test;

import db.DBManager;
import main.CTManager;

//import db.*;
//import model.*;
//import main.*;

public class Test {

	public static void main(String[] args) {
		CTManager ctm = new CTManager();
		ctm.buildClinicalTrial("NCT00378313").print();
		DBManager db;
		try {
			db = new DBManager();
			db.getClinicalTrial("NCT02061917");
			db.saveClinicalTrial(ctm.buildClinicalTrial("NCT02061917"));
			db.getClinicalTrial("NCT02061917").print();
			db.saveClinicalTrial(ctm.buildClinicalTrial("NCT02061917"));
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

	}
}
