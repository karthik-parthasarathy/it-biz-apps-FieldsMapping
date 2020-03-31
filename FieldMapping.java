import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldMapping {

	private static String line = "";
	private static String name_lead = "lead";
	private static String name_accounts = "accounts";
	private static String name_contacts = "contacts";
	private static String name_deal = "deal";
	private static String sales_prefix = "sales_account_cf_";
	private static String prefix = "cf_";
	private static String alias_sale_accnt_value = "sacf.";
	private static String alias_lead_value = "lcf.";
	private static String alias_contact_value = "ccf.";
	private static String alias_deal_value = "dcf.";
	private static String default_sacc_check = ", sacc.";
	private static String default_sacc_replace = "sales_account_";
	private static String database_name = "freshworks_freshsales.";

	// Contacts
	private static String contacts_query = "/Users/kaparthasarathy/Desktop/OpenPrice/contacts_query.txt";
	private static String contacts_custom_address = "/Users/kaparthasarathy/Desktop/OpenPrice/contacts_fieldsmapping.txt";
	private static String contacts_excel_address = "/Users/kaparthasarathy/Desktop/OpenPrice/contacts_excel.txt";
	private static String contacts_fromQuery = "From \n" + database_name + "account_settings acc \n" + ", "
			+ database_name + "contact_custom_fields ccf \n" + ", " + database_name + "contacts\n" + ", "
			+ database_name + "sales_accounts sacc\n" + ", " + database_name + "sales_account_custom_fields sacf\n"
			+ "Where contacts.account_id=acc.id \n" + "AND ccf.account_id=contacts.account_id\n"
			+ "And sacc.account_id=contacts.account_id\n" + "and " + "sacf.account_id=contacts.account_id";

	// SalesAccount/account
	private static String sale_accnt_query = "/Users/kaparthasarathy/Desktop/OpenPrice/saleaccounts_query.txt";
	private static String sale_accnt_custom_address = "/Users/kaparthasarathy/Desktop/OpenPrice/saleaccounts_cf_fieldsmapping.txt";
	private static String sale_accnt_excel_address = "/Users/kaparthasarathy/Desktop/OpenPrice/saleaccounts_excel.txt";
	private static String accnt_excel_address = "/Users/kaparthasarathy/Desktop/OpenPrice/accounts_excel.txt";
	private static String sale_accnt_fromQuery = "From \n" + database_name + "account_settings acc \n" + ","
			+ database_name + "sales_accounts sacc\n" + "," + database_name + "sales_account_custom_fields sacf\n"
			+ "Where sacc.account_id=acc.id \n" + "And sacf.account_id=sacc.account_id\n";

	// lead
	private static String lead_custom_address = "/Users/kaparthasarathy/Desktop/OpenPrice/leads_fieldmapping.txt";
	private static String lead_excel_address = "/Users/kaparthasarathy/Desktop/OpenPrice/Leads_excel.txt";
	private static String lead_query = "/Users/kaparthasarathy/Desktop/OpenPrice/lead_query.txt";
	private static String lead_fromQuery = "From \n" + database_name + "account_settings acc \n" + "," + database_name
			+ "lead_custom_fields lcf\n" + "," + database_name + "leads\n" + "Where acc.id=leads.account_id\n"
			+ "And lcf.account_id=leads.account_id";

	// deal
	private static String deal_custom_address = "/Users/kaparthasarathy/Desktop/OpenPrice/Deal_mapping.txt";
	private static String deal_excel_address = "/Users/kaparthasarathy/Desktop/OpenPrice/deal_excel.txt";
	private static String deal_query = "/Users/kaparthasarathy/Desktop/OpenPrice/Deal_Query.txt";
	private static String deal_fromQuery = "From \n" + database_name + "account_settings acc \n" + "," + database_name
			+ "deal_custom_fields dcf\n" + "," + database_name + "deals\n" + "," + database_name
			+ "sales_accounts sacc\n" + "," + database_name + "sales_account_custom_fields sacf\n"
			+ "Where acc.id=deals.account_id\n" + "and dcf.account_id=deals.account_id\n"
			+ "And sacc.account_id=deals.account_id" + " and sacf.account_id=deals.account_id";

	public static void main(String args[])

	{
		deal();
		contacts();
		lead();
		accounts();

	}

	public static void accounts() {
		// Field Mapping from text to map,text to list
		Map<String, String> account_map = customFieldExtract(sale_accnt_custom_address);
		// renaming custom fields with excel
		account_map = field_mapping(account_map, accnt_excel_address, prefix);
		account_map.put("alias", alias_sale_accnt_value);
		// final output
		List<Map<String, String>> mapLst = Arrays.asList(account_map);
		query_output(mapLst, sale_accnt_fromQuery, sale_accnt_query, name_accounts);
	}

	public static Map<String, String> sales_accounts() {
		// Field Mapping from text to map,text to list
		Map<String, String> saleacc_map = customFieldExtract(sale_accnt_custom_address);
		// renaming custom fields with excel
		saleacc_map = field_mapping(saleacc_map, sale_accnt_excel_address, sales_prefix);
		saleacc_map.put("alias", alias_sale_accnt_value);
		return saleacc_map;
	}

	public static void lead() {

		// Field Mapping from text to map
		Map<String, String> leadMap = customFieldExtract(lead_custom_address);
		// renaming custom fields with excel
		leadMap = field_mapping(leadMap, lead_excel_address, prefix);
		// final output
		leadMap.put("alias", alias_lead_value);
		List<Map<String, String>> mapLst = Arrays.asList(leadMap);
		query_output(mapLst, lead_fromQuery, lead_query, name_lead);
	}

	public static void contacts() {

		// Field Mapping from text to map,text to list
		Map<String, String> contactMap = customFieldExtract(contacts_custom_address);
		// renaming custom fields with excel
		contactMap = field_mapping(contactMap, contacts_excel_address, prefix);
		contactMap.put("alias", alias_contact_value);
		Map<String, String> salesAccMap = sales_accounts();
		// final output
		List<Map<String, String>> mapLst = Arrays.asList(contactMap, salesAccMap);
		query_output(mapLst, contacts_fromQuery, contacts_query, name_contacts);
	}

	public static void deal() {

		// Field Mapping from text to map
		Map<String, String> dealMap = customFieldExtract(deal_custom_address);
		// renaming custom fields with excel
		dealMap = field_mapping(dealMap, deal_excel_address, prefix);
		dealMap.put("alias", alias_deal_value);
		Map<String, String> salesAccMap = sales_accounts();
		// final output
		List<Map<String, String>> mapLst = Arrays.asList(dealMap, salesAccMap);
		query_output(mapLst, deal_fromQuery, deal_query, name_deal);
	}

	public static void query_output(List<Map<String, String>> mapLst, String fromQuery, String query, String name) {
		FileReader readerDeal;
		try {
			readerDeal = new FileReader(query);

			BufferedReader bufferedDealReader = new BufferedReader(readerDeal);
			FileWriter writer = new FileWriter("MyFile_" + name + ".txt", true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			String dealFrom = fromQuery;
			while ((line = bufferedDealReader.readLine()) != null) {
				// default
				String value = "";
				String temp = "";
				int dotIndex = line.indexOf(".");
				if (line.trim().startsWith(default_sacc_check)) {
					System.out.println("sacc check");
					value = line.substring(dotIndex + 1, line.length()).trim();
					temp = line.trim() + " as " + '"' + default_sacc_replace + value + '"';
				} else if (line.trim().length() > 0) {
					value = line.substring(dotIndex + 1, line.length()).trim();
					temp = line.trim() + " as " + '"' + value + '"';
				}
				bufferedWriter.newLine();
				bufferedWriter.write(temp);
			}
			// custom_fields
			for (Map<String, String> mp : mapLst) {
				for (Map.Entry<String, String> entry : mp.entrySet()) {

					String customFields = "," + mp.get("alias") + entry.getKey() + " As " + '"' + entry.getValue()
							+ '"';
					bufferedWriter.newLine();
					String exceptionValue = "," + mp.get("alias") + "alias As " + '"' + entry.getValue() + '"'; // used
																												// to
																												// display
																												// alias
																												// ,so
																												// this
																												// can
																												// avoided
																												// from
																												// query
					if (!customFields.trim().equalsIgnoreCase(exceptionValue)) {
						bufferedWriter.write(customFields.trim());
					}
				}

			}

			bufferedWriter.newLine();
			bufferedWriter.write(fromQuery);

			bufferedDealReader.close();
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Map<String, String> field_mapping(Map<String, String> dealMap, String address, String prefix) {
		FileReader readerExcelDeal;
		try {
			readerExcelDeal = new FileReader(address);

			BufferedReader bufferedExcelDealReader = new BufferedReader(readerExcelDeal);
			List<String> dealLst = new ArrayList<>();
			while ((line = bufferedExcelDealReader.readLine()) != null) {

				boolean chk = line.startsWith(prefix);
				String temp = "";
				if (chk && line.trim().length() > 0) { // cf
					temp = line.substring(prefix.length()).replace("_", "").trim();

					for (Map.Entry<String, String> entry : dealMap.entrySet()) {
						if (entry.getValue().equalsIgnoreCase(temp.trim())) {
							dealMap.put(entry.getKey(), line);
						} else {
							String temp_1 = entry.getValue().replace("?", "").replace("/", " ").replace("_", "")
									.replace(" ", "").replace("(", "").replace(")", "").trim();

							if (temp.equalsIgnoreCase(temp_1)) {
								dealMap.put(entry.getKey(), line);
							}

						}

					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dealMap;

	}

	public static Map<String, String> customFieldExtract(String address) {
		FileReader readerDealMapping;
		Map<String, String> contactMap = new HashMap<String, String>();

		try {
			readerDealMapping = new FileReader(address);
			BufferedReader bufferedDealMapReader = new BufferedReader(readerDealMapping);

			List<String> contactDefLst = new ArrayList<String>();
			// List<Map> dealCustomLst=new ArrayList<Map>();

			// Field Mapping
			while ((line = bufferedDealMapReader.readLine()) != null) {

				int count = 0;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == ',')
						count++;
				}
				String[] strLine = { "", "" };
				if (count == 1) {
					strLine = line.split(",");
				} else {
					int commaPnt = line.lastIndexOf(",");
					strLine[0] = line.substring(0, commaPnt);
					strLine[1] = line.substring(commaPnt + 1, line.length());
					System.out.println(strLine[0] + "--->" + strLine[1]);
				}
				// System.out.println(strLine[1]);
				if (strLine[1].trim().equals("default")) {
//					contactMap.put("strLine[1]", value)
//					contactDefLst.add(strLine[0].trim());
				} else {
					contactMap.put(strLine[1].trim(), strLine[0].trim());
				}
			}

			bufferedDealMapReader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return contactMap;
	}

}
