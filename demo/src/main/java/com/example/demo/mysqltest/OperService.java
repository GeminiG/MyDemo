package com.example.demo.mysqltest;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OperService {

    void insert(List<String> alarmSignalList);

    void insertC(List<String> alarmSignalList);

    void delete(List<String> alarmSignalList);

    void deleteC(List<String> alarmSignalList);

    void update(List<String> alarmSignalList);
}
