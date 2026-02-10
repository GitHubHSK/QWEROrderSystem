/** 
 * 테이블 번호 가져오기 함수
 * 1) URL ?tableNo=3 처럼 들어온 경우 → URL 값이 우선
 * 2) 서버(Thymeleaf)에서 JS 변수 TABLE_NO를 주입한 경우 → 그 값 사용
 * 3) 둘 다 없으면 0 반환 (0이면 주문 불가 처리)
 */
function getTableNo() {
	// 현재 URL에서 tableNo 파라미터 추출
    const qs = new URLSearchParams(location.search).get("tableNo");
	
	// URL에 tableNo가 있고 숫자라면 → URL 값 사용
    if (qs && !isNaN(qs)) {
        return parseInt(qs, 10);
    }
	
	// Thymeleaf에서 제공한 TABLE_NO 변수 사용
    if (typeof TABLE_NO !== "undefined" && TABLE_NO != null && TABLE_NO > 0) {
        return parseInt(TABLE_NO, 10);
    }
    
	// 둘 다 없으면 기본값 0
    return 0;
}


// 결정된 tableNo 저장
const tableNo = getTableNo();

// 테이블 번호별로 장바구니를 구분하기 위한 key
const CART_KEY = `cart_${tableNo}`;

// 전체 메뉴 데이터를 저장할 변수
let fullMenuData = [];

/** 
 * 페이지 로딩 완료 시 실행되는 jQuery 함수
 */
$(document).ready(function () {


	/** 
	 * 1. 메뉴 목록을 서버에서 AJAX로 불러옴(JSON) 
	 * 성공 시 fullMenuData에 저장 후 화면 렌더링
	 */
    $.ajax({
        url: "/menu/list/data",
        type: "GET",
        success: function (data) {
            fullMenuData = data;
            renderMenuList(data);
        },
        error: function () {
            alert("メニュー情報の読み込みに失敗しました。");
        }
    });

	/** 
	 * 2. 메뉴 목록을 화면에 출력하는 함수 (메뉴 리스트를 반복하며 HTML 요소 생성)
	 */
    function renderMenuList(menuList) {
        $("#menu-container").empty();
        menuList.forEach(menu => {
            $("#menu-container").append(`
                <div class="menu-card" data-id="${menu.menuNo}">
                    <img src="${menu.imageUrl}" alt="${menu.menuName}">
                    <h3 class="menu-name">${menu.menuName}</h3>
                    <p class="menu-price">${menu.price}円</p>

                    <div class="quantity-box">
                        <button class="btn-qty minus">-</button>
                        <input type="text" class="qty-input" value="1" readonly>
                        <button class="btn-qty plus">+</button>
                    </div>

                    <button class="btn-add-cart">カートに入れる</button>
                </div>
            `);
        });
    }

	/** 
	 * 3. 카테고리 필터 버튼(버튼 클릭 시 active 상태 변경 후 메뉴 필터링)
	 */
    $(document).on("click", ".btn-filter", function () {
        $(".btn-filter").removeClass("active");
        $(this).addClass("active");

        const filter = $(this).data("filter");
        if (filter === "all") {
            renderMenuList(fullMenuData);
        } else {
            const filtered = fullMenuData.filter(menu => menu.categoryName === filter);
            renderMenuList(filtered);
        }
    });

	/** 
	 * 4. 수량 조절 버튼 (+ / -)
	 * 최소 1(자유롭게 증가 가능)
	 */
    $(document).on("click", ".btn-qty", function () {
        const input = $(this).siblings(".qty-input");
        let current = parseInt(input.val());
        if ($(this).hasClass("plus")) {
            input.val(current + 1);
        } else if ($(this).hasClass("minus") && current > 1) {
            input.val(current - 1);
        }
    });

	/** 
	 * 5. '담기' 버튼 클릭 → 로컬스토리지에 저장
	 * 테이블별 장바구니 분리됨
	 */
    $(document).on("click", ".btn-add-cart", function () {
        if (!tableNo || tableNo <= 0) {
            alert("テーブル番号が無効です。QRコードを再スキャンしてください。");
            return;
        }

        const menuCard = $(this).closest(".menu-card");
        const menuNo = menuCard.data("id");
        const menuName = menuCard.find("h3").text();
        const price = parseInt(menuCard.find("p").text().replace(/[^0-9]/g, ""));
        const quantity = parseInt(menuCard.find(".qty-input").val());

        let cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];
		
		// 이미 담은 메뉴면 수량만 증가
        const existingItem = cart.find(item => item.menuNo === menuNo);

        if (existingItem) {
            existingItem.quantity += quantity;
        } else {
            cart.push({ menuNo, menuName, price, quantity });
        }

		// 로컬스토리지에 저장
        localStorage.setItem(CART_KEY, JSON.stringify(cart));
		
        alert(`${menuName} ${quantity}品追加しました！`);
    });

	/** 
	 * 6. 장바구니 열기(로컬스토리지에서 읽어서 모달에 출력)
	 */
    $(document).on("click", "#btn-cart", function () {
        const cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];
        renderCart(cart);
        $("#cart-modal").removeClass("hidden");
    });

	/** 
	 * 7. 장바구니 닫기 + 메뉴 목록 초기화
	 */
    $(document).on("click", "#btn-close-cart", function () {
        $("#cart-modal").addClass("hidden");
		
        $(".btn-filter").removeClass("active");
        $(".btn-filter[data-filter='all']").addClass("active");
		
        renderMenuList(fullMenuData);
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

	/** 
	 * 8. 주문 버튼 클릭 → 서버로 주문 전송
	 * AJAX POST 요청으로 OrderRequestDTO 형태로 보냄
	 */
	$(document).on("click", "#btn-order", function () {
			
		// 테이블 번호 유효성 검사
	    if (!tableNo || tableNo <= 0 || Number.isNaN(tableNo)) {
			alert("テーブル番号を確認できませんでした。QRコードをもう一度スキャンしてください。");
			return;
		}

		const cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];
		if (cart.length === 0) {
			alert("ご注文リストは空です。");
			return;
		}

		// 알레르기 정보 세션 값(JS 변수) → 배열로 변환
		let allergyList = [];

		if (Array.isArray(USER_ALLERGY)) {
			allergyList = USER_ALLERGY;

		} else if (typeof USER_ALLERGY === "string" && USER_ALLERGY.trim() !== "") {

			allergyList = USER_ALLERGY.split(/[,/]/) // , 또는 / 기준 분리
			.map(a => a.trim()) // 공백 제거
			.filter(a => a !== ""); // 빈 값 제거
		}

		// 서버 DTO 형태에 맞추어 주문 정보 구성
		const orderData = {
			tableNo: parseInt(tableNo, 10),
			peopleCount:
			(Number.isFinite(parseInt(MEMBER_COUNT, 10)) && parseInt(MEMBER_COUNT, 10) > 0)
			? parseInt(MEMBER_COUNT, 10)
			: 1,
			allergy: allergyList, // DTO List<String> 매칭
			orderItems: cart.map(item => ({
				menuNo: item.menuNo,
				quantity: item.quantity,
				menuName: item.menuName
			}))
		};

		// 서버로 AJAX POST 요청
		$.ajax({
			url: "/order",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(orderData),
			success: function () {
				alert("ご注文が完了しました！");
				localStorage.removeItem(CART_KEY);
				$("#cart-modal").addClass("hidden");
				renderMenuList(fullMenuData);
			},
			error: function () {
				alert("ご注文の処理中にエラーが発生しました。");
			}
		});
	});
});

/** 
* 장바구니 화면 렌더링
* 테이블 형태로 목록 출력
*/
function renderCart(cart) {
    let total = 0;
    $("#cart-body").empty();

    cart.forEach((item, index) => {
        $("#cart-body").append(`
            <tr>
                <td>${item.menuName}</td>
                <td>
                    <button onclick="changeQty(${index}, -1)">-</button>
                    ${item.quantity}
                    <button onclick="changeQty(${index}, 1)">+</button>
                </td>
                <td>${item.price * item.quantity}円</td>
                <td><button onclick="removeItem(${index})">X</button></td>
            </tr>
        `);
        total += item.price * item.quantity;
    });

    $("#cart-total").text(`合計金額: ${total}円`);
}

/** 
 * 장바구니에서 항목 삭제
 */
function removeItem(index) {
    let cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];
    cart.splice(index, 1);
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    $("#btn-cart").trigger("click");
}

/** 
 * 장바구니 수량 변경
 */
function changeQty(index, diff) {
    let cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];
    if (!cart[index]) return;
    cart[index].quantity += diff;
    if (cart[index].quantity <= 0) return;
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    $("#btn-cart").trigger("click");
}