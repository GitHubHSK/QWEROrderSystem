// HTML 문서의 모든 요소가 로드된 후 실행되는 이벤트
document.addEventListener("DOMContentLoaded", function() {

    // 삭제 버튼(.btn-delete)을 모두 선택해서 리스트로 가져옴
    const deleteButtons = document.querySelectorAll(".btn-delete");

    // 각 삭제 버튼에 클릭 이벤트를 등록
    deleteButtons.forEach(btn => {

        btn.addEventListener("click", function(event) {
            event.preventDefault();   // a 태그의 기본 기능(페이지 이동)을 막음

            // 클릭한 버튼에 저장된 menuNo 값을 가져옴
            // <a data-id="3" ...> 이런 식의 data-id 값을 읽어옴
            const menuNo = this.getAttribute("data-id");

            // 사용자에게 삭제 여부 확인
            if (confirm("本当に削除しますか？")) {
                
                // 확인 버튼을 누르면 해당 메뉴 삭제 URL로 이동
                window.location.href = `/admin/menu/delete/${menuNo}`;
            }
        });
    });
});
