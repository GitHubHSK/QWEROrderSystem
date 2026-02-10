package com.qwer.qrorder.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qwer.qrorder.domain.Menu;
import com.qwer.qrorder.domain.OrderDetail;
import com.qwer.qrorder.domain.OrderMain;
import com.qwer.qrorder.domain.TableInfo;
import com.qwer.qrorder.dto.OrderItemDTO;
import com.qwer.qrorder.dto.OrderRequestDTO;
import com.qwer.qrorder.repository.MenuRepository;
import com.qwer.qrorder.repository.OrderMainRepository;
import com.qwer.qrorder.repository.TableInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMainRepository orderMainRepository;
    private final MenuRepository menuRepository;
    private final TableInfoRepository tableInfoRepository;

    /**
    * 注文保存処理
    * ORDER_MAIN 作成
    * ORDER_DETAIL 作成および連携
    * 合計金額計算
    * アレルギーメモ保存
    */
    @Override
    @Transactional
    public OrderMain createOrder(OrderRequestDTO orderRequestDTO) {

        // ORDER_MAIN 생성
        OrderMain orderMain = new OrderMain();

        // 테이블 번호로 TableInfo 엔티티 조회
        TableInfo table = tableInfoRepository.findById(orderRequestDTO.getTableNo())
                .orElseThrow(() -> new IllegalArgumentException("テーブル番号エラー: " + orderRequestDTO.getTableNo()));
        orderMain.setTable(table);

        orderMain.setPeopleCount(orderRequestDTO.getPeopleCount());
        orderMain.setOrderStatus("WAIT"); // 기본 상태: 대기중

        int totalPrice = 0; // 주문 총 금액 계산용 변수

        // ORDER_DETAIL 생성 (일반 for문 사용)
        for (OrderItemDTO item : orderRequestDTO.getOrderItems()) {

           // 메뉴 번호로 Menu 엔티티 조회
            Menu menu = menuRepository.findById(item.getMenuNo())
                    .orElseThrow(() -> new IllegalArgumentException("メニューIDエラー: " + item.getMenuNo()));

            OrderDetail detail = new OrderDetail();
            detail.setMenu(menu);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(menu.getPrice() * item.getQuantity()); // 단가 × 수량
            detail.setMemo(item.getMemo());
            detail.setMenuName(item.getMenuName()); // 메뉴명 저장
            
            
            // 요청 DTO에 알레르기 정보가 있는 경우
            if (orderRequestDTO.getAllergy() != null && !orderRequestDTO.getAllergy().isEmpty()) {
                detail.setMemo(String.join(", ", orderRequestDTO.getAllergy()));
            } 
            // 알레르기 정보는 없고 기존 memo가 있는 경우
            else if (item.getMemo() != null && !item.getMemo().isEmpty()) {
                detail.setMemo(item.getMemo());
            }

            // OrderMain 엔티티에 OrderDetail 추가 (양방향 연관관계 처리)
            orderMain.addDetail(detail);

            // 주문 총 금액 누적
            totalPrice += detail.getPrice();
        }

        // 총 금액 설정 후 저장
        orderMain.setTotalPrice(totalPrice);

        // Cascade 옵션에 의해 OrderDetail 자동 저장됨
        return orderMainRepository.save(orderMain);
    }

    /**
    * 注文番号による単一注文照会
    */
    @Override
    public OrderMain getOrderById(Integer orderNo) {
        return orderMainRepository.findById(orderNo)
                .orElseThrow(() -> new IllegalArgumentException("注文番号なし: " + orderNo));
    }


   /**
   * 全注文一覧照会
   */
   @Override
   public List<OrderMain> getAllOrders() {
      return orderMainRepository.findAllByOrderByCreatedDateDesc();
   }   
}