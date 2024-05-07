package com.webshop.project3.schedule;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.webshop.project3.repository.BillRepo;
import com.webshop.project3.repository.UserRepo;
import com.webshop.project3.service.EmailService;

@Component
public class JobScheduler {
	
	@Autowired 
	UserRepo userRepo;
	
	@Autowired
	BillRepo billRepo;
	
	@Autowired
	EmailService emailService;
	
	public void sendAdminEmail(String eamailUser) {
		emailService.sendMail(eamailUser, "ban da co don hang moi", "abc");

	}
	
	public void sendNewPassword(String eamailUser,String newPassword) {
		emailService.sendMail(eamailUser, "mat khau moi cua ban", newPassword);
	}
	
	public void sendMailExcel(String eamailUser) {
		emailService.sendMailwithFile(eamailUser, "file thong ke bill", "abc");
	}

	@Async
	public void createFileExcel() {
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("thongKe");
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		//cot header
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		//
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		headerStyle.setFont(font);
		// thang
		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("So luong don");
		headerCell.setCellStyle(headerStyle);
		//nam
		headerCell = header.createCell(1);
		headerCell.setCellValue("Thang");
		headerCell.setCellStyle(headerStyle);
		//so luong don
		headerCell = header.createCell(2);
		headerCell.setCellValue("Nam");
		headerCell.setCellStyle(headerStyle);
		
		
		
		//bang
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		//ghi tu mang vao excel
		List<Object[]> kq = billRepo.thongKeBill();
		//
		int rowNum=2;
		for(Object[] rowData : kq) {
			Row row = sheet.createRow(rowNum);
			for(int i=0;i<rowData.length;i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(String.valueOf(rowData[i]));
				cell.setCellStyle(style);
			}
			rowNum++;
		}
		//luu lai 
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
		
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(fileLocation);
			workbook.write(outputStream);
			System.err.println("tao file excel thanh cong");
			System.err.println(fileLocation);
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
}
