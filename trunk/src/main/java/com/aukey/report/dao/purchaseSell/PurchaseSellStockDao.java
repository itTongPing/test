package com.aukey.report.dao.purchaseSell;

import com.aukey.report.domain.purchaseSell.PurchaseSellStockVO;
import com.aukey.report.dto.purchaseSell.PurchaseSellStockParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;

public interface PurchaseSellStockDao {

	TableData<PurchaseSellStockVO> queryPage(PageParam pageParam,
			PurchaseSellStockParam param);

	int count(PurchaseSellStockParam param);

}
