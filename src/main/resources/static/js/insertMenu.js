// 파일 선택(input type="file") 요소에 change 이벤트 추가
document.getElementById("menuImageFile").addEventListener("change", function() {

    // 사용자가 파일을 선택했는지 확인
    // this.files.length > 0  → 파일이 1개 이상 선택된 경우
    // 파일이 선택되었다면 파일 이름을 가져오고, 선택되지 않았다면 안내 문구 표시
    const fileName = this.files.length > 0 
        ? this.files[0].name 
        : "選択されたファイルはありません";  // 선택된 파일이 없을 때 표시할 문구

    // 선택된 파일명을 화면에 표시
    // 예: <span id="file-name"></span> 안에 파일명을 넣음
    document.getElementById("file-name").textContent = fileName;
});