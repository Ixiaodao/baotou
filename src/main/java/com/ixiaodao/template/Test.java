package com.ixiaodao.template;


import com.ixiaodao.search.image.model.CoordBean;
import com.ixiaodao.search.image.utils.FindImgUtils;
import com.ixiaodao.search.image.utils.ToolsUtils;
import com.sun.jna.platform.win32.User32;

/**
 * 1
 * @author jinwenbiao
 * @since 2025/5/5 17:49
 */
public class Test {
    //
    public static void main(String[] args) {
        while (true) {
            long l1 = System.currentTimeMillis();
            CoordBean coordBean = FindImgUtils.searchImg(0, 540, 106, 100, BaoTou.read, 31);
            long l2 = System.currentTimeMillis();
            System.out.println(l2 - l1);
            ToolsUtils.sleep(100);
        }

    }
}
