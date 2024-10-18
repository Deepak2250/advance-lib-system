package com.ShelfSpace.ShelfSpace.model;

import java.util.List;

import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDto;

import lombok.Data;

@Data
public class StudentResponse {
    private List<StudentDetailsDto> studentDetails;
    private boolean hasMore;
}
