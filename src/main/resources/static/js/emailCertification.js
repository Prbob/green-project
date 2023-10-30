// 이메일 보냄
    function emailSend() {
        let clientEmail = document.getElementById('emailText').value;
        console.log('입력한 메일' + clientEmail);

        $.ajax({
            type: "POST",
            url: "/Checkmail",
            data: {u_mail: clientEmail},
            success: function (data) {
                alert("인증번호가 발송되었습니다.");
                // 이메일 인증 버튼을 비활성화
                document.getElementById('emailCheck').disabled = true;
                // 이메일 입력 상자를 읽기 전용으로 변경
                /*document.getElementById('emailText').readOnly = true;*/

                console.log('성공', data);
            },
            error: function (e) {
                alert("인증번호 발송에 실패하였습니다.");
                console.log('실패', e);
            }
        });
    }

// 이메일 인증
    function emailCertification() {

        let clientEmail = document.getElementById('emailText').value;
        let inputCode = document.getElementById('certificationNumber').value;
        console.log('이메일' + clientEmail);
        console.log('인증코드' + inputCode);

        $.ajax({
            type: "POST",
            url: "/certification.do",
            data: {u_mail: clientEmail, inputCode: inputCode},
            success: function (data) {
                console.log(data);
                if (data == true) {
                    alert('인증완료');
                    // 부모 창(즉, joinMemberForm.html를 열었던 창)에 접근하여 이메일 주소 전달
                    window.opener.setEmail(clientEmail);

                    window.close();
                } else {
                    alert('인증번호가 일치하지 않습니다.');
                }
            },
            error: function (e) {
                alert('전송오류');
                console.log('에러' + e);
            }
        });
    }



//이메일 받아오기
    function setEmail(email) {
        // joinMemberForm.html 페이지의 이메일 필드에 이메일 주소 설정
        document.getElementById("email").value = email;
    }

// 이메일 인증 창 띄우기
    function openNewWindow() {
        var newWindow = window.open("/member/emailCertificationForm", "_blank", "width=600,height=400");
    }
