// 각각의 HTML 요소를 가져와 변수에 저장
const plusBtn = document.getElementById("plus");        // + 버튼
const minusBtn = document.getElementById("minus");      // - 버튼
const memberCount = document.getElementById("memberCount"); // 표시용 숫자 영역
const memberInput = document.getElementById("member");       // 실제 form으로 전달되는 hidden input

// 인원수 기본값 설정 (최초 1명)
let count = 1;

// + 버튼 클릭 시 실행되는 이벤트 등록
plusBtn.addEventListener("click", () => {
    // 최대 인원수를 10명으로 제한
    if (count < 10) count++;

    // 화면에 표시되는 숫자 업데이트
    memberCount.textContent = count;

    // form으로 전달될 숫자(hidden input)도 함께 업데이트
    memberInput.value = count;
});

// - 버튼 클릭 시 실행되는 이벤트 등록
minusBtn.addEventListener("click", () => {
    // 최소 인원수는 1명으로 제한
    if (count > 1) count--;

    // 화면 표시 숫자 업데이트
    memberCount.textContent = count;

    // hidden input 값 업데이트
    memberInput.value = count;
});
