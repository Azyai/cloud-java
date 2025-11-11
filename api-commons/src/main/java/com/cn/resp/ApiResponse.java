package com.cn.resp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.entity.dto.PageInfo;
import com.cn.entity.dto.PageWrapper;

import java.util.List;

public class ApiResponse<T> extends ResultData<T>{

    public static <E> ResultData<PageWrapper<E>> page(IPage<E> page) {
        return page(page,true);
    }

    public static <E> ResultData<PageWrapper<E>> page(IPage<E> page, Boolean isMulti) {
        PageWrapper<E> paper = new PageWrapper<>();

        paper.setTbody(page.getRecords());
        paper.setIsMulti(isMulti);
        paper.setPageInfo(new PageInfo(page.getCurrent(), page.getSize(), page.getTotal()));

        return success(paper);
    }

    public static <E> ResultData<PageWrapper<E>> pageForJustRecords(List<E> list, Boolean isMulti) {
        PageWrapper<E> paper = new PageWrapper<>();

        paper.setTbody(list);
        paper.setIsMulti(isMulti);

        return success(paper);
    }

    public static <E> ResultData<PageWrapper<E>> pageForList(List<E> list, Long page, long size, Boolean isMulti) {
        if(null == page || page <= 0) {
            page = 1L;
        }
        PageWrapper<E> paper = new PageWrapper<>();
        int totalSize = list.size();
        int fromIndex = (int)((page - 1) * size);
        int toIndex = Math.min(fromIndex + (int)size, totalSize);
        paper.setTbody(list.subList(fromIndex, toIndex));
        paper.setIsMulti(isMulti);
        paper.setPageInfo(new PageInfo(page, size, (long)list.size()));
        return success(paper);
    }

    public static <T> ResultData<T> forbidden(){
        return ResultData.fail("403");
    }

    public static <T> ResultData<T> paramError(){
        return ResultData.fail("400");
    }

    public static <T> ResultData<T> paramError(String message){
        return ResultData.fail("400", message);
    }
}
