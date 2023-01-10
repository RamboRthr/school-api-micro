package com.service.apigateway;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalLookupService;

@RestController
@RequestMapping("/gateway")
public class Gateway {
    /*
      redirectTo
       1 = CRUD /student
       2 = CRUD /professor
       3 = CRUD /Classrom
       ...
       n = AUTH
       n+1 = Sign up / Reset
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity redirectTo(@RequestParam("redirectTo") Integer service,
                                     @RequestParam(value = "subRedirect", required = false) Integer sub,
                                     @RequestParam(value = "token", required = false) String token,
                                     @RequestParam("method") HttpMethod method,
                                     @RequestHeader HttpHeaders headers,
                                     @RequestBody String body) {
        switch (service){
            case 1:
                if (sub == null){
                    return RestClient.request("http://crud-service:8081/student",
                            method, headers, body);
                }
                else {
                    switch (sub){
                        case 1:
                            return RestClient.request("http://crud-service:8081/student/byId",
                                    method, headers, body);
                        case 2:
                            return RestClient.request("http://crud-service:8081/student/studentByName",
                                    method, headers, body);
                    }
                }
            case 2:
                return RestClient.request("http://crud-service:8081/professor", method,headers,body);
            case 3:
                return RestClient.request("http://crud-service:8081/classroom", method,headers,body);

            case 4:
                return RestClient.request("http://localhost:8082/api/auth/signup", method,headers,body);

            case 5:
                return RestClient.request("http://localhost:8082/api/auth/signin", method,headers,body);

            case 6:
                if (sub == 1){
                    return RestClient.request("http://reset-password:8083/forgotMyPassword/request", method,headers,body);
                } else if (sub == 2) {
                    return RestClient.request("http://reset-password:8083/forgotMyPassword/reset", method,headers,body);
                }
                else {
                    return new ResponseEntity("Especifique o serviço", HttpStatus.BAD_REQUEST);
                }

            default:
                return new ResponseEntity("Especifique o serviço", HttpStatus.BAD_REQUEST);
        }
    }
}
