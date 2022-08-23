package com.usergems.meetingenrichment.calendar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MeetingsDTO {
    private int total;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("current_page")
    private int currentPage;
    private List<MeetingDTO> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<MeetingDTO> getData() {
        return data;
    }

    public void setData(List<MeetingDTO> data) {
        this.data = data;
    }

    public int getPages() {
        int pages = total / perPage;
        int lastPage = (total % perPage) == 0 ? 0 : 1;

        return pages + lastPage;
    }
}
