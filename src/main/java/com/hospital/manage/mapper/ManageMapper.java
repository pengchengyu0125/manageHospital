package com.hospital.manage.mapper;

import com.hospital.manage.dto.DeptDTO;
import com.hospital.manage.dto.DoctorDTO;
import com.hospital.manage.dto.HospitalDTO;
import com.hospital.manage.dto.UserDTO;
import com.hospital.manage.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ManageMapper {

    //查找科室分类
    @Select("select * from t_mg_dept_class_type order by sort")
    List<DeptClass> deptClasses();

    //查找科室
    @Select("select a.*,b.dept_class_name from t_mg_hospital_dept AS a LEFT JOIN t_mg_dept_class_type AS b ON a.dept_class_type=b.dept_class_code order by a.sort")
    List<DeptDTO> deptList();

    //查找医生
    @Select("select a.*,b.dept_name from t_mg_doctor as a left join t_mg_hospital_dept as b on a.dept_code=b.dept_code where a.dept_code=#{deptCode}")
    List<Doctor> doctorList(@Param(value = "deptCode") String deptCode);

    //查找所有医生id
    @Select("select * from t_mg_doctor")
    List<DoctorDTO> doctorDTOS();

    //模糊搜索医生
    @Select("<script> select * from t_mg_doctor" +
            "<if  test= \"(doctorCode != null and doctorCode != '') || (doctorName != null and doctorName != '') || (titleCode != null and titleCode != '')\"> where </if>" +
            "<if  test= \"doctorName != null and doctorName != ''\"> doctor_name regexp #{doctorName} </if>" +
            "<if  test= \"doctorName != null and doctorName != '' and doctorCode != null and doctorCode != ''\"> and </if>" +
            "<if  test= \"doctorCode != null and doctorCode != ''\"> doctor_code regexp #{doctorCode} </if>" +
            "<if  test= \"((doctorName != null and doctorName != '') || (doctorCode != null and doctorCode != '')) and titleCode != null and titleCode != ''\"> and </if>" +
            "<if  test= \"titleCode != null and titleCode != ''\"> title_code=#{titleCode} </if>" +
            " order by dept_code</script>")
    List<Doctor> searchList(@Param(value = "doctorCode") String doctorCode, @Param(value = "doctorName") String doctorName, @Param(value = "titleCode") String titleCode);

    //模糊搜索重载
    @Select("<script> select a.*,b.dept_name from t_mg_doctor as a left join t_mg_hospital_dept as b on a.dept_code=b.dept_code" +
            "<if  test= \"(deptCode != null and deptCode != '') || (doctorCode != null and doctorCode != '') || (doctorName != null and doctorName != '') || (titleCode != null and titleCode != '')\"> where </if>" +
            "<if  test= \"deptCode != null and deptCode != ''\"> a.dept_code=#{deptCode} </if>" +
            "<if  test= \"deptCode != null and deptCode != '' and doctorName != null and doctorName != ''\"> and </if>" +
            "<if  test= \"doctorName != null and doctorName != ''\"> doctor_name regexp #{doctorName} </if>" +
            "<if  test= \"((deptCode != null and deptCode != '') || (doctorName != null and doctorName != '')) and doctorCode != null and doctorCode != ''\"> and </if>" +
            "<if  test= \"doctorCode != null and doctorCode != ''\"> doctor_code regexp #{doctorCode} </if>" +
            "<if  test= \"((deptCode != null and deptCode != '') || (doctorName != null and doctorName != '') || (doctorCode != null and doctorCode != '')) and titleCode != null and titleCode != ''\"> and </if>" +
            "<if  test= \"titleCode != null and titleCode != ''\"> title_code=#{titleCode} </if>" +
            "</script>")
    List<Doctor> reloadList(@Param(value = "doctorCode") String doctorCode, @Param(value = "doctorName") String doctorName, @Param(value = "titleCode") String titleCode, @Param(value = "deptCode") String deptCode);

    //获取科室
    @Select("select * from t_mg_hospital_dept where dept_class_type=#{deptClassType}")
    List<Department> departments(@Param(value = "deptClassType") String deptClassType);

    //模糊搜索科室
    @Select("select * from t_mg_hospital_dept where dept_code regexp #{deptCode} and dept_name regexp #{deptName}")
    List<Department> searchDeptList(@Param(value = "deptCode") String deptCode, @Param(value = "deptName") String deptName);

    @Select("select * from t_mg_hospital_dept where dept_name regexp #{deptName}")
    List<Department> searchDeptList1(@Param(value = "deptName") String deptName);

    @Select("select * from t_mg_hospital_dept where dept_code regexp #{deptCode}")
    List<Department> searchDeptList2(@Param(value = "deptCode") String deptCode);

    //重载模糊查询科室表格
    @Select("select * from t_mg_hospital_dept where dept_code regexp #{deptCode} and dept_name regexp #{deptName} and dept_class_type=#{deptClassType}")
    List<Department> reloadDept(@Param(value = "deptCode") String deptCode, @Param(value = "deptName") String deptName, @Param(value = "deptClassType") String deptClassType);

    @Select("select * from t_mg_hospital_dept where dept_name regexp #{deptName} and dept_class_type=#{deptClassType}")
    List<Department> reloadDept1(@Param(value = "deptName") String deptName, @Param(value = "deptClassType") String deptClassType);

    @Select("select * from t_mg_hospital_dept where dept_code regexp #{deptCode} and dept_class_type=#{deptClassType}")
    List<Department> reloadDept2(@Param(value = "deptCode") String deptCode, @Param(value = "deptClassType") String deptClassType);

    //获取当前医院的所有职称
    @Select("select title_name from title where hospital_code=#{hospitalCode}")
    List<String> getTitle(@Param(value = "hospitalCode") String hospitalCode);

    //获取所有医院
    @Select("select * from database_source")
    List<HospitalDTO> getHospitals();

    //获取用户信息
    @Select("select a.*,b.hospital_name from user as a left join database_source as b on a.hospital_code = b.hospital_code where a.access!='1'")
    List<UserDTO> getUsers();

    //获取数据库信息
    @Select("select * from database_source")
    List<DataSource> getDataSource();

    //获取上传服务地址
    @Select("select upload_url from database_source where hospital_code=#{hospitalCode}")
    String getUploadUrl(@Param(value = "hospitalCode") String hospitalCode);

    //获取该医院的功能菜单
    @Select("select a.* from function AS a LEFT JOIN relation AS b ON a.function_id=b.function_id where b.hospital_code=#{datasourceId}")
    List<Function> getFunctions(@Param(value = "datasourceId") String datasourceId);
}
