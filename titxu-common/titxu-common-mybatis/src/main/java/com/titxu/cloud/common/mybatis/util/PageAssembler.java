package com.titxu.cloud.common.mybatis.util;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Assembler class for the page.
 **/
public class PageAssembler {
    public static Page toPage(IPage iPage) {
        return new Page(iPage.getRecords(), iPage.getTotal(), iPage.getSize(), iPage.getCurrent());
    }
}
