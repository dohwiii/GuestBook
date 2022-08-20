package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<EN, DTO> { //파라미터를 entity -> dto 변환해야함

    private List<DTO> dtoList; //dto 객체들을 보관함
    
    private int totalPage;

    private int page; //현재 페이지

    private int size; //목록 사이즈

    private int start, end;

    private boolean prev, next;

    private List<Integer> pageList; //페이지 번호 목록

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {

        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();

        makePageList(result.getPageable());

    }
    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd; //전체 페이지가 50인데 현재 페이지의 끝이 20이면 다음 페이지로 이동하는게 가능

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
    
}
