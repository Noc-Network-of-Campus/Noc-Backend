package com.mycompany.myapp.web.controller;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.exception.ResponseMessage;
import com.mycompany.myapp.exception.StatusCode;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.NamecardService;
import com.mycompany.myapp.web.dto.NamecardRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.myapp.exception.CustomExceptions;
import com.mycompany.myapp.web.controller.base.BaseController;
import com.mycompany.myapp.web.dto.base.DefaultRes;

@Api(tags = "명함첩 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/namecard")
public class NameCardController extends BaseController {
    private final NamecardService namecardService;
    private final UserRepository userRepository;

    @ApiOperation(value = "Create Category API")
    @ApiResponse(code = 200, message = "카테고리 생성 성공")
    @PostMapping("/category")
    public ResponseEntity createCategory(@RequestBody NamecardRequestDto.CreateCategoryDto request){
        try {
            logger.info("Received request: method={}, path={}, description={}", "POST", "/api/namecard/category", "Create Category API");
            User user = userRepository.getByPhoneNumber("010-2944-0386");
            String categoryName = request.getCategory();
            namecardService.createCategory(user, categoryName);

            return new ResponseEntity( DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_CATEGORY_SUCCESS), HttpStatus.OK);
        } catch (CustomExceptions.testException e) {
            return handleApiException(e, HttpStatus.BAD_REQUEST);
        }
    }

}
