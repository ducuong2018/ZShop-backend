package com.example.zshop.data;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
public class Metadata {
    private int totalPage;
    private int page;
    private int size;
    private Long totalSize;

    public Metadata(Page page) {
        setTotalPage(page.getTotalPages());
        setTotalSize(page.getTotalElements());
        setPage(page.getNumber());
        setSize(page.getSize());
    }

}
