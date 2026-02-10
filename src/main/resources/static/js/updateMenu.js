// 파일 선택 input(#menuImageFile)에 변경(change) 이벤트 등록
document.getElementById("menuImageFile").addEventListener("change", function() {

    // 사용자가 파일을 선택했으면 파일명 가져오고, 선택한 파일이 없다면 기본 문구로 설정
    const fileName = this.files.length > 0
        ? this.files[0].name
        : "選択されたファイルはありません";

    // 파일명을 표시할 요소(#file-name)의 텍스트 변경
    document.getElementById("file-name").textContent = fileName;
});