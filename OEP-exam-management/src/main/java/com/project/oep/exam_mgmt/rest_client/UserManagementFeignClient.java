package com.project.oep.exam_mgmt.rest_client;
//package com.project.oep.exam_mgmt.rest_client;
//
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import com.project.oep.models.Student;
//
//@FeignClient(name = "UserManagementClient", url = "${oep.user-management.api}")
//public interface UserManagementClient {
//
//	@GetMapping(value = "/student/{userName}")
//	public Student getStudentsByUserName(@PathVariable("userName") String userName);
//
//	@PutMapping(value = "/student/permission")
//	public Student updateStudentPermissionsByUserName(@RequestBody Student updatedStudent);
//
//}
