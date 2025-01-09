package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class PageResult<T> {
    private List<T> content;
    private long totalElements;
    private int pageNumber;
    private int pageSize;

    public PageResult(List<T> content, long totalElements, int pageNumber, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

//    // Getters and Setters
//    public List<T> getContent() {
//        return content;
//    }
//
//    public long getTotalElements() {
//        return totalElements;
//    }
//
//    public int getPageNumber() {
//        return pageNumber;
//    }
//
//    public int getPageSize() {
//        return pageSize;
//    }
}
