package com.bridgelabz.AddressBook;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AddressBookIO {
	// Create directory to store all resources
	public Path createDirectory() {
		Path pathForResources = Paths.get("C:\\Users\\DIRECTOR HOME\\eclipseworkspace\\AddressBook\\Resources");
		if (Files.notExists(pathForResources)) {
			try {
				Files.createDirectory(pathForResources);
			} catch (IOException e) {
			}
		}
		return pathForResources;
	}

	// Write to file
	public void writeDataToFile(List<Contacts> contactList, String addressBookName) {
		Path pathForResources = createDirectory();
		StringBuffer contactBuffer = new StringBuffer();
		contactList.forEach(contact -> {
			String contactDataString = contact.toString().concat("\n");
			contactBuffer.append(contactDataString);
		});
		try {
			Files.write(Paths.get(pathForResources + "/" + addressBookName + ".txt"),
					contactBuffer.toString().getBytes());
		} catch (IOException e) {
		}
	}

	// Count entries in file
	public int countEntriesForFile(String addressBookName) {
		Path pathForResources = createDirectory();
		int entries = 0;
		try {
			entries = (int) Files.lines(new File(pathForResources + "/" + addressBookName + ".txt").toPath()).count();
		} catch (IOException e) {
		}
		return entries;
	}

	// Read data from File
	public ArrayList<Contacts> readDataFromFile(String addressBookName) {
		Path pathForResources = createDirectory();
		ArrayList<Contacts> contactList = new ArrayList<>();
		try {
			Files.lines(Paths.get(pathForResources + "/" + addressBookName + ".txt")).forEach(line -> {
				line = line.trim();
				line = line.replace(":", "");
				String[] arr = line.split(" ");
				contactList.add(new Contacts(arr[1], arr[3], arr[5], arr[7], arr[9], arr[11], arr[13], arr[15]));
			});
		} catch (IOException e) {

		}
		return contactList;
	}

	// Write to CSV File
	public void writeDataToCSV(List<Contacts> contactList, String addressBookName)
			throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		Path pathForResources = createDirectory();
		try (Writer writer = Files.newBufferedWriter(Paths.get(pathForResources + "/" + addressBookName + ".csv"));) {
			StatefulBeanToCsv<Contacts> beanToCsv = new StatefulBeanToCsvBuilder<Contacts>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			beanToCsv.write(contactList);
		} catch (IOException e) {
		}
	}

	// Read from CSV
	public ArrayList<Contacts> readDataFromCSV(String addressBookName) throws IOException {
		Path pathForResources = createDirectory();
		ArrayList<Contacts> contactList = new ArrayList<>();
		try (Reader reader = Files.newBufferedReader(Paths.get(pathForResources + "/" + addressBookName + ".csv"));) {
			CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
			String[] nextRecord;
			while ((nextRecord = csvReader.readNext()) != null) {
				contactList.add(new Contacts(nextRecord[0], nextRecord[1], nextRecord[2], nextRecord[3], nextRecord[4],
						nextRecord[5], nextRecord[6], nextRecord[7]));
			}
			return contactList;
		}
	}
}
