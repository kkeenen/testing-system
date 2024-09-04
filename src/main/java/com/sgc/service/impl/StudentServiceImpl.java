package com.sgc.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.sgc.entity.Student;
import com.sgc.mapper.StudentMapper;
import com.sgc.service.StudentService;
import com.sgc.utils.PageResult;
import com.sgc.vo.EchartsVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import com.qcloud.cos.region.Region;
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> findAll(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<Student> students = studentMapper.findAll(pageSize, offset);
        int count = studentMapper.findCount();
        PageResult<Student> page = new PageResult<>();
        page.setTotal(count);
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        page.setData(students);
        return page;
    }

    @Override
    public List<String> findClasses() {
        List<String> list = studentMapper.findClasses();
        return list;
    }

    @Override
    public PageResult<Student> findByCondition(Integer pageNo, Integer pageSize, Student student) {
        int offset = (pageNo - 1) * pageSize;
        List<Student> students = studentMapper.findByCondition(pageSize, offset, student);
        int count = students.size();

        PageResult<Student> page = new PageResult<>();
        page.setTotal(count);
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        page.setData(students);
        return page;
    }

    @Override
    public int add(Student student) {
        int i = -1;
        try {
            i = studentMapper.add(student);
            studentMapper.addToUser(student.getStudentId());
        } catch (Exception e) {

        }
        return i;
    }

    @Override
    public int edit(Student student) {
        int i = studentMapper.edit(student);
        return i;
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        int i = studentMapper.deleteByIds(ids);
        return i;
    }

    @Override
    public List<Student> findAllStudentsByClassName(List<String> classNameList) {
        List<Student> students = studentMapper.findAllStudentsByClassName(classNameList);
        return students;
    }

    @Override
    public List<EchartsVo> getGenderData() {
        int maleCount = studentMapper.getMaleCount();
        int femaleCount = studentMapper.getFemaleCount();
        List<EchartsVo> voList = new ArrayList<>();
        voList.add(new EchartsVo("男", maleCount));
        voList.add(new EchartsVo("女", femaleCount));

        return voList;
    }

    @Override
    public List<EchartsVo> getMajorData() {
        // 获取所有专业
        List<String> majors = studentMapper.getAllMajor();

        // 封装vo
        List<EchartsVo> echartsVos = majors.stream().map(item -> {
            Integer count = studentMapper.getMajorCount(item);
            return new EchartsVo(item, count);
        }).collect(Collectors.toList());
        return echartsVos;
    }

    @Override
    public Map<String, Object> getEntryTrend() {
        // 获得连续时间
//        List<String> continuedDate = ?
        // 获得每个时间学生入学人数 -》 先查出所有学生 然后遍历
        List<Student> students = studentMapper.findAll(1000000, 0);

        // 找到最早和最晚的日期

        LocalDate earliestDateLocal = students.stream().map(Student::getEntryDate).min(Comparator.naturalOrder()).orElse(null);

        LocalDate latestDateLocal = students.stream().map(Student::getEntryDate).max(Comparator.naturalOrder()).orElse(null);

        Date earliestDate = Date.from(earliestDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date latestDate = Date.from(latestDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // 格式化日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // 生成从最早到最晚日期的列表
        Map<String, Integer> dateCountMap = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(earliestDate);

        while (!calendar.getTime().after(latestDate)) {
            String dateString = formatter.format(calendar.getTime());
            dateCountMap.put(dateString, 0);  // 初始化为0
            calendar.add(Calendar.DATE, 1);
        }

        // 统计每个日期的人数
        for (Student student : students) {
            LocalDate entryDateLocal = student.getEntryDate();
            String entryDateStr = formatter.format(Date.from(entryDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            dateCountMap.put(entryDateStr, dateCountMap.get(entryDateStr) + 1);
        }

        // 输出结果

        return Map.of("data", dateCountMap);
    }




}
