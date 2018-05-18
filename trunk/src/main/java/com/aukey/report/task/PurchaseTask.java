package com.aukey.report.task;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aukey.report.dao.impl.PurchaseReportDaoImpl;

@Component
public class PurchaseTask {
	@Autowired
	private PurchaseReportDaoImpl purchaseReportDaoImpl;

	private final Log log = LogFactory.getLog(getClass());

	@Scheduled(cron = "0 */2 * * * *")
	public void insert() throws SQLException {

		long startTime = System.currentTimeMillis();
		purchaseReportDaoImpl.sync();
		long endTime = System.currentTimeMillis();
		log.info("计算开票数量及状态耗时" + (endTime - startTime) / 1000 + "秒");
	}
}
