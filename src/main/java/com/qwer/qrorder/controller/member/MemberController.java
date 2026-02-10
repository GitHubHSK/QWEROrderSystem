package com.qwer.qrorder.controller.member;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qwer.qrorder.controller.order.OrderController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    // 주문 처리 로직을 활용하기 위해 OrderController 주입
    private final OrderController orderController;


    /**
     * QR 코드를 처음 찍었을 때 호출되는 화면
     * 테이블 번호를 URL에서 받아서 모델에 담고
     * 인원수 입력 화면(countMember.html)을 보여줌
     */
    @GetMapping("/countMember/{tableNo}")
    public String countMember(@PathVariable int tableNo, Model model) {

        // 뷰에서 바로 사용할 수 있도록 테이블 번호 저장
        model.addAttribute("tableNo", tableNo);

        // templates/member/countMember.html 반환
        return "member/countMember";
    }


    /**
     * 인원수와 알레르기 정보를 입력받아 세션에 저장한 후 메뉴 화면으로 이동
     * member : 선택된 인원수
     * allergy : 사용자가 선택한 알레르기 리스트 (없을 수 있기 때문에 optional)
     * tableNo : 현재 테이블 번호
     * 세션에 저장하여 이후 주문 페이지에서도 사용 가능하게 만듦
     */
    @PostMapping("/count")
    public String saveMemberInfo(@RequestParam int member,
                                 @RequestParam(required = false) List<String> allergy,
                                 @RequestParam int tableNo,
                                 HttpSession session) {

        // 인원수, 테이블번호, 알레르기 정보를 세션에 저장
        session.setAttribute("memberCount", member);
        session.setAttribute("tableNo", tableNo);
        session.setAttribute("allergy", allergy);

        // 서버 콘솔 로그로 알레르기 값 확인
        System.out.println(allergy);

        // 저장 후 메뉴 리스트 페이지로 이동
        return "redirect:/menu/list";
    }
}